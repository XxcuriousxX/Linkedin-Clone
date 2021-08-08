import { HttpClient } from '@angular/common/http';
import { JobPostResponse, JobPostModel } from './Jobs';
import { Observable } from 'rxjs';
import { AuthService } from './../auth/auth.service';
import { Injectable } from '@angular/core';
import {PostModel} from "../post/post.model";
import {FullJobPostModel} from "./full-job-post-view/full-job-post";

@Injectable({
  providedIn: 'root'
})
export class JobsService {


  constructor(private http: HttpClient, private _authService: AuthService) { }

  getSuggestions(): Observable<JobPostResponse[]> {
    return this.http.get<JobPostResponse[]>("http://localhost:8080/api/jobs/suggestions/" + this._authService.getUserName());
  }

  createJobPost(jobPostModel: JobPostModel): Observable<any> {
    return this.http.post<any>("http://localhost:8080/api/jobs/create/", jobPostModel)
  }

  getJobPostById(id: number): Observable<FullJobPostModel> {
    return this.http.get<FullJobPostModel>('http://localhost:8080/api/jobpost/' + id);
  }

}
