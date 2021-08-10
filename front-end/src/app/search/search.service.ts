import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {AuthService} from '../auth/auth.service';
import {Observable} from "rxjs";
import {User, UserResponse} from '../user';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  constructor(private _authService: AuthService, private http : HttpClient) { }

  executeQuery(query: string) : Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>("http://localhost:8080/api/users/search/" + query);
  }


}
