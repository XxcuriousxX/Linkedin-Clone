
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {AuthService} from "../auth/auth.service";
import {Observable} from "rxjs";
import {UserProfileResponse} from './UserProfile'
import {User} from "../user";

@Injectable({
    providedIn: 'root'
  })

export class UserprofileService{

    constructor(private _authService: AuthService, private http : HttpClient) { }

    getUserProfile(username: string): Observable<UserProfileResponse> {
        return this.http.get<UserProfileResponse>("http://localhost:8080/api/users/userProfile/" + username);
    }

    getButtonState(username: string): Observable<any>{
        return this.http.get("http://localhost:8080/api/users/buttonstate/" + username);
    }

    getAllConnected(username: string) : Observable<any>{
      return this.http.get("http://localhost:8080/api/users/" + username);
    }

    getUserInfo(username: string): Observable<User>{
      return this.http.get<User>("http://localhost:8080/api/users/userInfo/" + username);
    }


}
