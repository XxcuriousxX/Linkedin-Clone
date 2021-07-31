import { HttpClient } from '@angular/common/http';
import { JobPostResponse } from './Jobs';
import { Observable } from 'rxjs';
import { AuthService } from './../auth/auth.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class JobsService {


  constructor(private http: HttpClient, private _authService: AuthService) { }

  getSuggestions(): Observable<JobPostResponse[]> {
    return this.http.get<JobPostResponse[]>("http://localhost:8080/api/jobs/suggestions/" + this._authService.getUserName());
  }
  
}
