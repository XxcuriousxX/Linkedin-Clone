
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private _checkexist = "http://localhost:8080/user/find/";
  private _registerUrl = "http://localhost:8080/user/add";
  private _loginUrl = "http://localhost:8080/"
  constructor(private http: HttpClient) { }

  registerUser(user: any) {
    //
    // if ( this.http.post<any>(this._registerUrl+user.email, user.email) ){
    //
    // }
    //
      let obj = this.http.get<any>(this._checkexist+"user.email", {observe: 'response'});
      return obj;
     // return this.http.post<any>(this._registerUrl, user);
      // return this.http.post<any>(this._registerUrl, user).catch((err) => {
      //   console.error('kala pame');//handle your error here
      // });//.pipe(catchError(this.errorHandler));

  }

  loginUser(user: User) {
    return this.http.post<User>(this._loginUrl, user);
  }
}
