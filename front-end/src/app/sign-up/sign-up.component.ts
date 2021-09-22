import { AuthService } from '../auth/auth.service';
import { Component, OnInit } from '@angular/core';
import {User } from './../user';
import {FormBuilder, Validators} from '@angular/forms';
import {FormGroup} from "@angular/forms";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";


@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})


export class SignUpComponent implements OnInit {
  resp : any;
  file : File;
  errors: String = "";


  signUpForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(4)]],
      first_name: ['', [Validators.required, Validators.minLength(3)]],
      last_name: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(4)]],
      email: ['', [Validators.required, Validators.minLength(4)]],
      phone: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(10)]],
      company_name: ['', [Validators.required, Validators.minLength(2)]]
      });


  constructor(private _authService: AuthService, private formBuilder: FormBuilder, private router: Router,private http: HttpClient) { }

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



      const formData: FormData = new FormData();
      formData.append('user', this.signUpForm.value);
      formData.append('profile_image', this.file);



      this._authService.signup(this.signUpForm.value).subscribe(
        res => {
          this.resp = "SUCCESS";
          this.router.navigate(['/login'], { queryParams: {registered: 'true'} });
        },
        err => {
          this.resp = "FAIL";
        }
      );


    }


}
