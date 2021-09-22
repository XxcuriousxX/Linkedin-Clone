import { SettingsService } from './settings.service';
import { throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth/auth.service';
import { User } from '../user';
import { UserService } from '../user.service';
import {ChangeProfileImageRequestPayload, SettingsRequestPayload} from './settings-request.payload';
import { Router, ActivatedRoute } from '@angular/router';
import {DialogComponent} from "../jobs/myjobs/dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {ImageDialogComponent} from "./image.dialog.component";
@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  // @ts-ignore
  settingsForm: FormGroup;
  settingsRequestPayload: SettingsRequestPayload;

  img_sz_exceed : boolean = false;
  img_inv_format : boolean = false;

  changeprofilepayload : ChangeProfileImageRequestPayload;
  file:File;

  changesSuccessMessage: string ;
  userInfo: User = new User();
  email_exists : boolean = false;

  constructor(private _settingsService : SettingsService, private httpClient: HttpClient,private formBuilder: FormBuilder,private _userService: UserService,public _authservice: AuthService,public dialog: MatDialog) {
    this.settingsForm = new FormGroup({
      email: new FormControl('',Validators.required),
      password: new FormControl('', Validators.required)
    });
    this.settingsRequestPayload = {
      email: '',
      password: '',
      username: ''
    };
    this.changesSuccessMessage = "";
  }



  ngOnInit(): void {
    this.email_exists = false;
    this._userService.getUserInfo().subscribe(info => {
      this.userInfo = info;
    });


  }

  changeEmail() {
    var mailfield = <HTMLInputElement> document.getElementById("new_email");
    this.settingsRequestPayload.email = mailfield.value;
    this.settingsRequestPayload.username = this._authservice.getUserName();
    this._settingsService.changeEmail(this.settingsRequestPayload).subscribe(res => {
      this.ngOnInit();
    }, err => { this.email_exists = true; throwError(err); });
  }

  changePassword() {
    var passfield = <HTMLInputElement> document.getElementById("new_pass");
    this.settingsRequestPayload.password = passfield.value;
    this.settingsRequestPayload.username = this._authservice.getUserName();
    this._settingsService.changePassword(this.settingsRequestPayload).subscribe(res => {

      this.ngOnInit();
    });
  }

  submit_changes(){
    this.settingsRequestPayload.username = this._authservice.getUserName();
    this.settingsRequestPayload.email = this.settingsForm.value.email;
    this.settingsRequestPayload.password = this.settingsForm.value.password;

    this._userService.submitchanges(this.settingsRequestPayload).subscribe(data => {

      this.ngOnInit();
      this.settingsForm.reset();

    }, error => {

      this.email_exists = true;
      this.settingsForm.reset();

      throwError(error);

    });
  }

  changeProfileImage(){
    const formData: FormData = new FormData();
    formData.append('username', this._authservice.getUserName());
    formData.append('profileImage', this.file);

    if (!this.img_inv_format && !this.img_sz_exceed){
      this._userService.changeProfileImage(formData).subscribe(data => {
        this.ngOnInit();
      }, error => {
        throwError(error);
      });
    }


  }


  onChange(event) {
    this.img_inv_format = false;
    this.img_sz_exceed = false;
    this.file = event.target.files[0];
    let file_format : string = event.target.files[0].name.substr( event.target.files[0].name.lastIndexOf('.') + 1);
    //log / access file size in Mb
    if (event.target.files[0].size/1024/1024 > 4) {
      this.img_sz_exceed = true;
    }
    if (file_format !="jpg" && file_format != "png"){
      this.img_inv_format = true;
    }

  }

  openDialog(img_sz : boolean, img_type : boolean) {
    this.dialog.open(ImageDialogComponent, {
      data: { img_sz: img_sz, img_type: img_type },
      backdropClass: 'backdropBackground',
      panelClass: 'dialog-container-custom'
    });
  }

}
