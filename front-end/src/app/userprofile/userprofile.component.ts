import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserProfileResponse } from './UserProfile';
import { UserprofileService } from './userprofile.service';
import {ActivatedRoute} from "@angular/router";
import { throwError } from "rxjs";
import { PublicButton } from '../personal-info/public-buttons/PublicButton';
import {PersonalInfoService} from "../personal-info/personal-info.service";

@Component({
  selector: 'app-userprofile',
  templateUrl: './userprofile.component.html',
  styleUrls: ['./userprofile.component.css']
})
export class UserprofileComponent implements OnInit {

  buttonInfo: PublicButton = new PublicButton();
  usersList : User[] = []
  username: string = "";
  constructor(private _activatedRoute: ActivatedRoute, private _userProfileService: UserprofileService, private _personalinfoService: PersonalInfoService) { }
  user_profile_info : UserProfileResponse = new UserProfileResponse();



  ngOnInit(): void {
    // /user/username
    this.username = this._activatedRoute.snapshot.params.username;
    // this.username = this._activatedRoute.params.pipe(pluck('username'));
    this.getUserProfile();

    this._userProfileService.getButtonState(this.username).subscribe(info => {
      this.buttonInfo = info;
      console.log(this.buttonInfo.abilities);
    });


    this._userProfileService.getAllConnected(this.username).subscribe( L => {
      this.usersList = L;
    });



  }

  getUserProfile() {
    this._userProfileService.getUserProfile(this.username).subscribe( res => {
      this.user_profile_info = res;
    }, err => throwError(err));
  }

}
