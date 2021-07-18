import { AuthService } from '../auth/auth.service';
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
      company_name: ''
      });


  constructor(private _authService: AuthService, private formBuilder: FormBuilder, private router: Router) { }

  ngOnInit(): void {
      if (this._authService.isLoggedIn())
        this.router.navigate(['/home'])
             
      this.signUpForm = this.formBuilder.group({
      username: '',
      first_name: '',
      last_name: '',
      password: '',
      email: '',
      phone: '',
      company_name: ''
      });
  }

    async onSubmit() {
      console.log('eisai gia ton poutso intellij ', this.signUpForm.value);

      // post
      // await this._authService.getValFromObservable(this.signUpForm.value).then((res:any) => this.resp = "SUCCESS",
      // (err:any) => this.resp = "FAIL");

      this._authService.signup(this.signUpForm.value).subscribe(
        res => {
          this.resp = "SUCCESS";
          this.router.navigate(['/login'], { queryParams: {registered: 'true'} });
        },
        err => {
          this.resp = "FAIL";
        }
      );


      // console.log("Our resp from post: ", this.resp);
      // if (this.resp === 'SUCCESS')
      //   this.router.navigate(['home'])
    }


}
