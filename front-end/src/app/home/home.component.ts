import { PostsBoxComponent } from './../posts-box/posts-box.component';
import { PostModel } from './../post/post.model';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {throwError} from "rxjs";
import { CreatePostPayload } from "../post/create-post.payload";
import { PostService } from "../post/post.service";
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

  constructor(public _authService: AuthService, private _router: Router, private _postService: PostService) {
    this.postForm = new FormGroup({
      // username: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required),
      //like_count: new FormControl('', Validators.required),
      //comment_count: new FormControl('', Validators.required)
    });
    this.postPayload = {
        description: ""
    };
  }

  ngOnInit(): void {

  }

  createPost() {
    this.postPayload.description = this.postForm.value.description;

    this._postService.createPost(this.postPayload).subscribe(data => {
      this.isError = false;
      this._router.navigate(['/home']);
      // this.toastr.success('Login Successful');
    }, error => {
      this.isError = true;
      throwError(error);
    });
  }

  logout() {

    this._authService.logout();
    this._router.navigate(['/login'])
  }

}
