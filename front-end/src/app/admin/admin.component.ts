import { ConnectResponse } from './../connect-button/Connect';
import { LikePayload } from './../post/like/like.payload';
import { LikeResponse } from './../post/like/LikeResponse';
import { CommentResponse } from './../post/comment';
import { JobPostResponse } from './../jobs/Jobs';
import { PersonalInfoPayload } from './../personal-info/Personal-info';
import { PostModel } from './../post/post.model';
import { stringify } from '@angular/compiler/src/util';
import { throwError } from 'rxjs';
import { AdminService } from './admin.service';
import { UserResponse } from './../user';
import { Router } from '@angular/router';
import { AuthService } from './../auth/auth.service';
import { Component, OnInit } from '@angular/core';
import {MatCheckboxDefaultOptions, MatCheckboxModule, MAT_CHECKBOX_DEFAULT_OPTIONS_FACTORY} from '@angular/material/checkbox';
import {ThemePalette} from '@angular/material/core';
import { FormBuilder, FormGroup, FormControl, CheckboxControlValueAccessor } from '@angular/forms';
import * as JsonToXML from "js2xmlparser";


export class DetailedUser {
  userId: number;
  username: string;
  email:string;
  phone:string;
  first_name:string;
  last_name:string;
  company_name:string;
  //
  personalInfo: PersonalInfoPayload;
  posts: PostModel[];   // // posts created by this user
  jobPosts: JobPostResponse[]; // job posts created by this user
  comments: CommentResponse[]; // all comments created by this user
  likes: LikePayload[];
  connectedWith: string[];  // list of friend usernames
}


@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {


  selectedList : string[] = [];  

  allComplete: boolean = false;

  isLoaded = false;
  usersList : UserResponse[] = [];
  select_all_is_activated = false;

  constructor(private _authService : AuthService, private _router: Router, private _adminService : AdminService) { }
  
  ngOnInit(): void {
    this._adminService.getAllUsers().subscribe( users => {
      var uList = users;
      this.usersList = uList.filter( (usr) => usr.username !== "admin");
      this.isLoaded = true;
    }, err => throwError(err));



  }

  exportAsJSON() {
    
    this.selectedList = []; // re init
    for (let u of this.usersList) {
      var checkbox = <HTMLInputElement> document.getElementById(u.username)
      var isChecked = checkbox.checked;
      if (isChecked) {
        this.selectedList.push(u.username)
    
      }
    }

    if (this.selectedList.length == 0) return;

    this._adminService.getAllDetailedUsers(this.selectedList).subscribe( detailed_users => {
      var output = JSON.stringify(detailed_users);
      // console.log(output)
      this.saveAFile(output);
    })

    // this.saveAFile("tpt akoma");
  }

  selectAll() {
    if (this.select_all_is_activated) { // if already select all has been triggered, then uncheck all
      for (let u of this.usersList) {
        var checkbox = <HTMLInputElement> document.getElementById(u.username);
        checkbox.checked = false;
      }
      this.select_all_is_activated = false;
    }
    else {
      for (let u of this.usersList) {
        var checkbox = <HTMLInputElement> document.getElementById(u.username);
        checkbox.checked = true;
      }
      this.select_all_is_activated = true;
    }

  }

  exportAsXML() {
    this.selectedList = []; // re init
    for (let u of this.usersList) {
      var checkbox = <HTMLInputElement> document.getElementById(u.username)
      var isChecked = checkbox.checked;
      if (isChecked) {
        this.selectedList.push(u.username)
      }
    }
    if (this.selectedList.length == 0) return;

    this._adminService.getAllDetailedUsers(this.selectedList).subscribe( detailed_users => {
      var obj = JSON.parse(JSON.stringify(detailed_users))
      var output = JsonToXML.parse("USER", obj);
      // console.log(output);
      this.saveAFile(output);
    })
  }

  saveAFile(contents : string): void {
    const dlink: HTMLAnchorElement = document.createElement('a');
    dlink.download = 'myfile.txt'; // the file name
    const myFileContent: string = contents;
    dlink.href = 'data:text/plain;charset=utf-16,' + myFileContent;
    dlink.click(); // this will trigger the dialog window
    dlink.remove();
  }

  logout() {

    this._authService.logout();
    this._router.navigate(['/login'])
  }




}
