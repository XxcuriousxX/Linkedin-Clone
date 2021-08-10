import { Observable } from 'rxjs';
import { ActionResponse } from './ActionResponse';
import { HttpClient } from '@angular/common/http';
import { AuthService } from './../auth/auth.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NotificationsService {

  constructor(private _authService: AuthService, private http: HttpClient) { }

  getAllNotificationsForUser(): Observable<ActionResponse[]> {
    return this.http.get<ActionResponse[]>("https://localhost:8443/api/users/get_notifications/" + this._authService.getUserName());
  }
}
