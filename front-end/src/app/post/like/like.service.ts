import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LikePayload } from './like.payload';
import { Observable } from 'rxjs';
import { LikeResponse } from './LikeResponse';
@Injectable({
  providedIn: 'root'
})
export class LikeService {

  constructor(private http: HttpClient) { }

  like(likePayload: LikePayload): Observable<LikeResponse> {
    return this.http.post<LikeResponse>('https://localhost:8443/api/likes/', likePayload);
  }

  has_liked(likePayload: LikePayload): Observable<LikeResponse> {
    return this.http.get<LikeResponse>('https://localhost:8443/api/likes/has_liked/' + likePayload.postId);
  }



}
