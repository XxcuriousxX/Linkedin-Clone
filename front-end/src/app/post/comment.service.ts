import { Observable } from 'rxjs';
import { AuthService } from './../auth/auth.service';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CommentResponse, CommentRequest } from './comment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http: HttpClient, private _authService: AuthService) { }

  addComment(commentRequest: CommentRequest): Observable<CommentResponse> {
    return this.http.post<CommentResponse>("https://localhost:8443/api/posts/add_comment/" , commentRequest);
  }

  getAllCommentsByPost(postId: number): Observable<CommentResponse[]>  {
    return this.http.get<CommentResponse[]>("https://localhost:8443/api/posts/get_all_comments_by_post/" + postId);
  }
}
