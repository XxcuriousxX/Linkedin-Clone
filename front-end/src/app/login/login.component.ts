import { LoginRequestPayload } from './login-request.payload';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { AuthService } from '../auth/auth.service';
import { Router, ActivatedRoute } from '@angular/router';
import { throwError } from 'rxjs';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})



export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  loginRequestPayload: LoginRequestPayload;
  registerSuccessMessage: string ;
  isError: boolean;

  constructor(private _authService: AuthService, private activatedRoute: ActivatedRoute,
    private router: Router) {
      this.loginForm = new FormGroup({
        username: new FormControl('', Validators.required),
        password: new FormControl('', Validators.required)
      });
      this.loginRequestPayload = {
        username: '',
        password: ''
      };
      this.registerSuccessMessage = "";
      this.isError = false;
  }

  ngOnInit(): void {
      const isAuthenticated = this._authService.isLoggedIn();
      if (isAuthenticated) this.router.navigate(['/home'])
  }

  login() {
    this.loginRequestPayload.username = this.loginForm.value.username;
    this.loginRequestPayload.password = this.loginForm.value.password;

    this._authService.login(this.loginRequestPayload).subscribe(data => {
      this.isError = false;
      this.router.navigateByUrl('/home');
      // this.toastr.success('Login Successful');
    }, error => {
      this.isError = true;
      throwError(error);
    });
  }

}
