
import { Injectable, Output, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../user';
import { LocalStorageService } from 'ngx-webstorage';
import { Observable, throwError } from "rxjs";
import { map, tap } from 'rxjs/operators';
import { LoginRequestPayload } from '../login/login-request.payload';


export interface LoginResponse {
  authenticationToken: string;
  refreshToken: string;
  expiresAt: Date;
  username: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  @Output() loggedIn: EventEmitter<boolean> = new EventEmitter();
  @Output() username: EventEmitter<string> = new EventEmitter();

  private uname : string = "";
  refreshTokenPayload = {
    refreshToken: this.getRefreshToken(),
    username: this.getUserName()
  }

  private _checkexist = "https://localhost:8443/user/find/";
  private _registerUrl = "https://localhost:8443/api/auth/signup";
  private _loginUrl = "https://localhost:8443/";



  constructor(private httpClient: HttpClient, private localStorage: LocalStorageService) { }



  // // obsolete function
  // async getValFromObservable(user: any) {
  //   return await this.signup(user).toPromise()
  // }

  signup(user: any) {
    return this.httpClient.post<any>(this._registerUrl, user, {responseType:'text' as 'json'})
  }

  login(loginRequestPayload: LoginRequestPayload): Observable<boolean> {
    // get auth token, username, refreshtoken and expiration time from backend
    return this.httpClient.post<LoginResponse>('https://localhost:8443/api/auth/login',
      loginRequestPayload).pipe(map(data => {
      this.localStorage.store('authenticationToken', data.authenticationToken);
      console.log("Auth token = ", data.authenticationToken);

      this.localStorage.store('username', data.username);
      console.log("Auth token = ", data.username);

      this.uname = data.username

      this.localStorage.store('refreshToken', data.refreshToken);
      this.localStorage.store('expiresAt', data.expiresAt);

      this.loggedIn.emit(true);
      this.username.emit(data.username);
      return true;
    }));
  }

  getJwtToken() {
    return this.localStorage.retrieve('authenticationToken');
  }

  refreshToken() {
    return this.httpClient.post<LoginResponse>('https://localhost:8443/api/auth/refresh/token',
      this.refreshTokenPayload)
      .pipe(tap(response => {
        this.localStorage.clear('authenticationToken');
        this.localStorage.clear('expiresAt');

        this.localStorage.store('authenticationToken',
          response.authenticationToken);
        this.localStorage.store('expiresAt', response.expiresAt);
      }));
  }

  logout() {
    this.httpClient.post('https://localhost:8443/api/auth/logout', this.refreshTokenPayload,
      { responseType: 'text' })
      .subscribe(data => {
        console.log(data);
      }, error => {
        throwError(error);
      })
    this.localStorage.clear('authenticationToken');
    this.localStorage.clear('username');
    this.localStorage.clear('refreshToken');
    this.localStorage.clear('expiresAt');
    console.log("Cleared everything");
  }


  getUserName() {
    return this.localStorage.retrieve('username');
  }
  getRefreshToken() {
    return this.localStorage.retrieve('refreshToken');
  }

  isLoggedIn(): boolean {
    return this.getJwtToken() != null;
  }
}
