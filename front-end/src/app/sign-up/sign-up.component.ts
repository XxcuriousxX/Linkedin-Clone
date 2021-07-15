import { AuthService } from './../auth.service';
import { Component, OnInit } from '@angular/core';
import {User } from './../user';
import {FormBuilder, Validators} from '@angular/forms';
import {FormGroup} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";


@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})


export class SignUpComponent implements OnInit {
  resp : any;

  errors: String = "";


  signUpForm = this.formBuilder.group({
      username: '',
      first_name: '',
      last_name: '',
      password: '',
      email: '',
      phone: '',
      company_name:''
      });


  constructor(private _auth: AuthService, private formBuilder: FormBuilder, private router: Router) { }

  ngOnInit(): void {
  }

    async onSubmit() {
      console.log('eisai gia ton poutso intellij ', this.signUpForm.value);
      // this._auth.registerUser(<JSON>this.signUpForm.value).subscribe(res =>  console.log(res), err => console.log(err));

        // this._auth.registerUser(<JSON>this.signUpForm.value).subscribe(res =>
        //   console.log("In success: ", this.signUpForm.value.email),
        //     err => {
        //   if (err instanceof HttpErrorResponse) {
        //     console.log(err);
        //     // window.alert(err.error);
        //     if (err.error === "username exists") {
        //       this.errors=err.error;
        //       console.log("NAIIIIIIIIIIIIIII");
        //     }
        //   }
        // });
      await this._auth.getValFromObservable(this.signUpForm.value).then((res:any) => this.resp = "SUCCESS",
      (err:any) => this.resp = "FAIL");


      console.log("Our resp from post: ", this.resp);
      if (this.resp === 'SUCCESS')
        this.router.navigate(['home'])
    }


}
