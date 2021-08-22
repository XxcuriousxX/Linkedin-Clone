import {Component, Input, OnInit} from '@angular/core';
import { User } from '../user';
import { UserProfileResponse } from './UserProfile';
import { UserprofileService } from './userprofile.service';
import {ActivatedRoute} from "@angular/router";
import {Observable, throwError} from "rxjs";
import { PublicButton } from '../personal-info/public-buttons/PublicButton';
import {PersonalInfoService} from "../personal-info/personal-info.service";
import {AuthService} from "../auth/auth.service";
import {UserService} from "../user.service";
import {ConnectService} from "../connect-button/connect.service";
import {ConnectPayload, ConnectResponse} from "../connect-button/Connect";

@Component({
  selector: 'app-userprofile',
  templateUrl: './userprofile.component.html',
  styleUrls: ['./userprofile.component.css']
})
export class UserprofileComponent implements OnInit {
  image : string = null;
  canrender: boolean = false;
  image_render: boolean = false;
  user : User = new User();
  buttonInfo: PublicButton = new PublicButton();
  usersList : User[] = []

  username: string = "";
  constructor(private _connectService: ConnectService,private _activatedRoute: ActivatedRoute, private _userProfileService: UserprofileService, private _personalinfoService: PersonalInfoService
              ,private _authService: AuthService,private route: ActivatedRoute,private _userService: UserService) { }
  user_profile_info : UserProfileResponse = new UserProfileResponse();
  connection_status: number = 0;

  connectPayload: ConnectPayload = new ConnectPayload();

  ngOnInit(): void {
    this.canrender = false;
    // /user/username
    this.username = this._activatedRoute.snapshot.params.username;
    // this.username = this._activatedRoute.params.pipe(pluck('username'));
    this.getUserProfile();


    // ----------------------------------------------------------------------------------------
    //button needed
    this.connectPayload.sender_username = this._authService.getUserName();
    this.connectPayload.receiver_username = this.username;

    this._userProfileService.getUserInfo(this.username).subscribe(info => {
      this.user = info;
      this.canrender = true;
    });

    // ----------------------------------------------------------------------------------------

    this.getButtonState();
    this.getAllConnected();
    this.areConnected();
    this.getUserProfileImage();





  }


  areConnected(){
    this._connectService.areConnected(this.connectPayload).subscribe( info => {
      if (info.status)
        this.connection_status = 3
    });
  }


  getButtonState(){
    this._userProfileService.getButtonState(this.username).subscribe(info => {
      this.buttonInfo = info;
    });
  }

  getAllConnected(){
    this._userProfileService.getAllConnected(this.username).subscribe( L => {
      this.usersList = L;
    });
  }

  getUserProfile() {

    this._userProfileService.getUserProfile(this.username).subscribe( res => {
      this.user_profile_info = res;
    }, err => throwError(err));
  }


  getUserProfileImage(){

    this._userService.retrieveProfileImageByUsername(this.username).subscribe(data => {
      this.image = 'data:image/jpeg;base64,' + data.image;
      this.image_render = true;
    }, error => {
      throwError(error);
    });

  }

}
