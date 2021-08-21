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
      username: ['', [Validators.required, Validators.minLength(4)]],
      first_name: ['', [Validators.required, Validators.minLength(3)]],
      last_name: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(4)]],
      email: ['', [Validators.required, Validators.minLength(4)]],
      phone: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(10)]],
      company_name: ''
      });


  constructor(private _authService: AuthService, private formBuilder: FormBuilder, private router: Router) { }

  ngOnInit(): void {
      if (this._authService.isLoggedIn())
        this.router.navigate(['/home'])

      this.signUpForm = this.formBuilder.group({
        username: ['', [Validators.required, Validators.minLength(4)]],
        first_name: ['', [Validators.required, Validators.minLength(3)]],
        last_name: ['', [Validators.required, Validators.minLength(3)]],
        password: ['', [Validators.required, Validators.minLength(4)]],
        email: ['', [Validators.required, Validators.minLength(4), Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]],
        phone: ['', [Validators.required, Validators.minLength(10), Validators.pattern("^[0-9]+$")]],
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
