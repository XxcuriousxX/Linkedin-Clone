import { PostsBoxComponent } from './../posts-box/posts-box.component';
import { PostModel } from './../post/post.model';
import { Router } from '@angular/router';
import { Component, OnInit, HostListener } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Observable, throwError} from "rxjs";
import { CreatePostPayload } from "../post/create-post.payload";
import { PostService } from "../post/post.service";
import {UserService} from "../user.service";
import { finalize } from 'rxjs/internal/operators/finalize';
import { AngularFireStorage, AngularFireUploadTask} from '@angular/fire/storage';
import { AngularFirestore } from '@angular/fire/firestore';
import { LoadingDialogComponent } from './loading.dialog.component';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';


interface file_info {
  filename: string;
  type?: string;
}


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css', './../app.component.css', '../login/login.component.css']
})
export class HomeComponent implements OnInit {
  postForm: FormGroup;
  postPayload: CreatePostPayload;
  isError: boolean = false;
  posts: Array<PostModel> = [];



  percentage = 0;
  uploadProgress:Observable<number>;
  uploadTask: any;
  task: AngularFireUploadTask;
  file_info : file_info = {filename: null};

  //for profile images
  image: string = null;
  is_render: boolean = false;

  private dialogRef: MatDialogRef<LoadingDialogComponent>

  constructor(public _authService: AuthService, private _router: Router, private _postService: PostService, private _userService: UserService,
    private fire: AngularFireStorage,public dialog: MatDialog) {

    this.postForm = new FormGroup({
      description: new FormControl('', Validators.required),
    });
    this.postPayload = {
        description: "",
        file_path: ""
    };

  }


  ngOnInit(): void {
    this.is_render = false;
    this.getUserProfileImage();
  }

  //retrieve profile image of current logged in user
  getUserProfileImage(){
    this._userService.retrieveProfileImage().subscribe(data => {
      this.image = 'data:image/jpeg;base64,' + data.image;
      this.is_render = true;
    }, error => {
      throwError(error);
    });

  }

  createPost() {
    this.postPayload.description = this.postForm.value.description;
    // check if user provided a file or not
    if(this.file_info.filename == null)
      this.no_file_create_post();
    else if (this.file_info.filename != undefined){
      this.upload_file_create_post().subscribe(
        percentage => {
          this.percentage = Math.round(percentage ? percentage : 0);
          if (this.dialogRef && this.dialogRef.componentInstance) {
            this.dialogRef.componentInstance.data = {percent: this.percentage};
          }
          if (this.percentage == 100)
            this.dialogRef.close();
        },
        error => {
        }
      );
      this.openDialog(this.percentage);
    }




  }

  logout() {

    this._authService.logout();
    this._router.navigate(['/login'])
  }

    //we define the filename and filetype of the user attached file
    upload($event,filetype : string){
      this.file_info = { filename : $event.target.files[0], type : filetype };
    }

    //in case user has attached a file we upload it in firebase and then create our post
    upload_file_create_post() :  Observable<number>{
      let firebase_file_path : string = null;

      //based on file type upload file in different firebase folder
      //in order to access it later
      switch (this.file_info.type){
        case "image":
          firebase_file_path = "post_images/files"+Math.random()+this.file_info.filename;
          break;
        case "video":
          firebase_file_path = "post_video/files"+Math.random()+this.file_info.filename;
          break;
        case "audio":
          firebase_file_path = "post_audio/files"+Math.random()+this.file_info.filename;
          break;
      }



      const storageRef = this.fire.ref(firebase_file_path);
      const uploadTask = this.fire.upload(firebase_file_path,this.file_info.filename);

      uploadTask.snapshotChanges().pipe(
        finalize(()=>{
          storageRef.getDownloadURL().subscribe(downloadURL => {
            this.postPayload.file_path = downloadURL;

            this._postService.createPost(this.postPayload).subscribe(data => {
              this.isError = false;

              this._router.navigate(['/home']);
              window.location.reload();

            }, error => {
              this.isError = true;
              throwError(error);
            });
          },error =>{
            this.isError = true;
            throwError(error);
          });

        })
      ).subscribe();


      return uploadTask.percentageChanges();
    }

  //in case user has not attached a file we only create a post with file_path == null
    no_file_create_post(){
      this.postPayload.file_path = null;
      this._postService.createPost(this.postPayload).subscribe(data => {
        this.isError = false;

        this._router.navigate(['/home']);
        window.location.reload();

      }, error => {
        this.isError = true;
        throwError(error);
      });
    }


    //disable exit on click (force user to wait for the upload)
    openDialog(percent : number) {
      this.dialogRef = this.dialog.open(LoadingDialogComponent,{
        data: { percent: this.percentage },
        backdropClass: 'backdropBackground',
        panelClass: 'dialog-container-custom',
        disableClose: true
      });
    }
}
