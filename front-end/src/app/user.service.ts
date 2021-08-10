import { UserResponse } from './user';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {AuthService} from "./auth/auth.service";
import {Observable} from "rxjs";
import {SettingsRequestPayload} from "./settings/settings-request.payload";

@Injectable({
  providedIn: 'root'
})
export class UserService {

    constructor(private _authService: AuthService, private http : HttpClient) { }


    getAllConnected() : Observable<any>{
      return this.http.get("https://localhost:8443/api/users/" + this._authService.getUserName());
    }

    getUserById(uid: number) : Observable<UserResponse> {
      return this.http.get<UserResponse>("https://localhost:8443/api/users/get_user_by_id/" + uid);

    }

    getUserInfo(): Observable<any>{
      return this.http.get("https://localhost:8443/api/users/userInfo/" + this._authService.getUserName());
    }

    submitchanges(settingsRequestPayload: SettingsRequestPayload): Observable<string>{
      return this.http.post<string>('https://localhost:8443/api/users/changeinfo/', settingsRequestPayload);
    }
}
