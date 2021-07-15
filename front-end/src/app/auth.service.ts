
import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from "rxjs";
import { HttpHeaders } from '@angular/common/http';
import {HttpParams} from "@angular/common/http";
import {catchError} from "rxjs/operators";
import { UserService } from "./user.service"
import { User } from "./user";
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json'
    // Authorization: 'my-auth-token'
  })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private _checkexist = "http://localhost:8080/user/find/";
  private _registerUrl = "http://localhost:8080/user/add";
  private _loginUrl = "http://localhost:8080/"
  constructor(private http: HttpClient, private usrService : UserService) { }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, body was: `, error.error);
    }
    // Return an observable with a user-facing error message.
    return throwError(
      'Something bad happened; please try again later.');
  }


  registerUser(user: User){
    return this.http.post<User>(this._registerUrl, user).subscribe(res => console.log(res), err => console.log(err));

  }

  loginUser(user: User) {
    return this.http.post<User>(this._loginUrl, user);
  }
}
