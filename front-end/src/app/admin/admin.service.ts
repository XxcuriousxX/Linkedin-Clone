import { DetailedUser } from './admin.component';
import { Observable } from 'rxjs';
import { UserResponse } from './../user';
import { AuthService } from './../auth/auth.service';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient, private _authService : AuthService) { }

  getAllUsers() : Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>("http://localhost:8080/api/users/get_all_users");
  }

  getAllDetailedUsers(usernames: string[]) : Observable<DetailedUser[]> {
    return this.http.post<DetailedUser[]>("http://localhost:8080/api/users/get_all_detailed_users", usernames);
  }
}
