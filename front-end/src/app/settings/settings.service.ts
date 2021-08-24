import { Observable } from 'rxjs';
import { SettingsRequestPayload } from './settings-request.payload';
import { AuthService } from './../auth/auth.service';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {

  constructor(private http: HttpClient, private _authService : AuthService) { }

  changeEmail(settingsRequestPayload: SettingsRequestPayload) : Observable<any> {
    return this.http.post("http://localhost:8080/api/users/change_email", settingsRequestPayload);
  }

  changePassword(settingsRequestPayload: SettingsRequestPayload) : Observable<any> {
    return this.http.post("http://localhost:8080/api/users/change_password", settingsRequestPayload);
  }
}
