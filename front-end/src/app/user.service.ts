import { UserResponse } from './user';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {AuthService} from "./auth/auth.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {

    constructor(private _authService: AuthService, private http : HttpClient) { }


    getAllConnected() : Observable<any>{
      return this.http.get("http://localhost:8080/api/users/" + this._authService.getUserName());
    }

    getUserById(uid: number) : Observable<UserResponse> {
      return this.http.get<UserResponse>("http://localhost:8080/api/users/get_user_by_id/" + uid);

    }
}
