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
      return this.http.get("http://localhost:8080/users/" + this._authService.getUserName());
    }
}
