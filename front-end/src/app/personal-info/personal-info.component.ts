import {Component, HostListener, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {UserService} from "../user.service";
import {AuthService} from "../auth/auth.service";
import {SettingsRequestPayload} from "../settings/settings-request.payload";
import {PersonalInfoPayload, PersonalInfoResponse} from "./Personal-info";
import {PersonalInfoService} from "./personal-info.service";
import {throwError} from "rxjs";

@Component({
  selector: 'app-personal-info',
  templateUrl: './personal-info.component.html',
  styleUrls: ['./personal-info.component.css']
})
export class PersonalInfoComponent implements OnInit {

  abilities : string[] = [];
  pr : PersonalInfoResponse = new PersonalInfoResponse();
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
    this._personalinfoService.getPersonalInfo().subscribe(res => {
      this.pr = res;
      this.infoForm.value.work = this.pr.work_desc;
      var work_elem = <HTMLInputElement> document.getElementById("work_elem") // pass the value for the view
      work_elem.value = this.pr.work_desc;

      this.infoForm.value.studies = this.pr.stud_desc;
      var stud_elem = <HTMLInputElement> document.getElementById("stud_elem") // pass the value for the view
      stud_elem.value = this.pr.stud_desc;

      if (this.pr.abilities_desc == "")
        this.abilities = [];
      else
        this.abilities = this.pr.abilities_desc.split(",");
    }, err => throwError(err));
    this.getScreenSize();
  }

  @HostListener('window:resize', ['$event'])
  getScreenSize(event?) {
    this.screenHeight = window.innerHeight;
    this.screenWidth = window.innerWidth;
  }

  changePersonalinfo(){ // the form is not used anymrore
    this.p_info_payload.username = this._authservice.getUserName();

    // this.p_info_payload.work_experience = this.infoForm.value.work ;
    var work_elem = <HTMLInputElement> document.getElementById("work_elem");
    this.p_info_payload.work_experience = work_elem.value;


    var stud_elem = <HTMLInputElement> document.getElementById("stud_elem") // pass the value for the view
    this.p_info_payload.studies = stud_elem.value;


    var abilities_str = "";
    var i = 0;
    for (let a of this.abilities) { // convert from list to "ability1,ability2,..." format
      if (i > 0) abilities_str += ","
      abilities_str += a;
      i++;
    }
    this.p_info_payload.abilities = abilities_str;
    this._personalinfoService.changePersonalInfo(this.p_info_payload).subscribe( data => {

      },
      error => { throwError(error); });
  }


  addAbility(ability) {
    if (!ability) return;
    this.abilities.push(ability.value);
  }

  removeAbility(i) {
    this.abilities.splice(i, 1);
  }

}
