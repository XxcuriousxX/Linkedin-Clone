import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {FormBuilder} from "@angular/forms";
import {UserService} from "../../user.service";
import {AuthService} from "../../auth/auth.service";
import {PersonalInfoService} from "../personal-info.service";
import {PublicButton, PublicButtonPayload} from "./PublicButton";
import {throwError} from "rxjs";

@Component({
  selector: 'app-public-buttons',
  templateUrl: './public-buttons.component.html',
  styleUrls: ['./public-buttons.component.css']
})
export class PublicButtonsComponent implements OnInit {
  selected: boolean = false;

  abilities:boolean;
  work:boolean;
  studies:boolean;
  company:boolean;
  phone:boolean;

  buttonInfo: PublicButton = new PublicButton();

  button_payload: PublicButtonPayload = new PublicButtonPayload();



  constructor(private httpClient: HttpClient,private formBuilder: FormBuilder,private _userService: UserService
              ,public _authservice: AuthService,private _personalinfoService: PersonalInfoService) { }

  ngOnInit(): void {

    this._personalinfoService.getButtonState().subscribe(info => {

      this.buttonInfo = info;


    });

  }


  changeButtonInfo(){
    this.button_payload.username = this._authservice.getUserName();
    this.button_payload.company = this.buttonInfo.company;
    this.button_payload.phone = this.buttonInfo.phone;
    this.button_payload.studies = this.buttonInfo.studies;
    this.button_payload.abilities = this.buttonInfo.abilities;
    this.button_payload.work_exp = this.buttonInfo.work_exp;


    this._personalinfoService.changeButtonState(this.button_payload).subscribe( data => {

      },
      error => { throwError(error); });
  }


}
