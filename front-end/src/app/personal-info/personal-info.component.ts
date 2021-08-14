import {Component, HostListener, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {UserService} from "../user.service";
import {AuthService} from "../auth/auth.service";
import {SettingsRequestPayload} from "../settings/settings-request.payload";
import {PersonalInfoPayload} from "./Personal-info";
import {PersonalInfoService} from "./personal-info.service";
import {throwError} from "rxjs";

@Component({
  selector: 'app-personal-info',
  templateUrl: './personal-info.component.html',
  styleUrls: ['./personal-info.component.css']
})
export class PersonalInfoComponent implements OnInit {

  screenHeight: number = -1;
  screenWidth: number = -1;
  infoForm: FormGroup;
  p_info_payload: PersonalInfoPayload = new PersonalInfoPayload();

  constructor(private httpClient: HttpClient,private formBuilder: FormBuilder,private _userService: UserService,public _authservice: AuthService,
              private _personalinfoService: PersonalInfoService) {
    this.infoForm = new FormGroup({
      work: new FormControl('',Validators.required),
      studies: new FormControl('', Validators.required),
      abilities: new FormControl('',Validators.required)
    });
  }

  ngOnInit(): void {
    this.getScreenSize();
  }

  @HostListener('window:resize', ['$event'])
  getScreenSize(event?) {
    this.screenHeight = window.innerHeight;
    this.screenWidth = window.innerWidth;
    console.log(this.screenHeight, this.screenWidth);
  }

  changePersonalinfo(){
    this.p_info_payload.username = this._authservice.getUserName();
    this.p_info_payload.work_experience = this.infoForm.value.work ;
    this.p_info_payload.abilities = this.infoForm.value.abilities ;
    this.p_info_payload.studies =   this.infoForm.value.studies;

    this._personalinfoService.changePersonalInfo(this.p_info_payload).subscribe( data => {
          console.log("OK");
          this.infoForm.reset();
      },
      error => { throwError(error); });
  }


}
