import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PostModel } from './post.model';
import { CreatePostPayload } from './create-post.payload';
import {AuthService} from "../auth/auth.service";

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private http: HttpClient, private _authService: AuthService) { }

  getAllPosts(): Observable<Array<PostModel>> {
    // return this.http.get<Array<PostModel>>('http://localhost:8080/api/posts/');
    return this.http.get<Array<PostModel>>('http://localhost:8080/api/posts/suggestions/' + this._authService.getUserName());
  }

  createPost(postPayload: CreatePostPayload): Observable<any> {
    return this.http.post('http://localhost:8080/api/posts/', postPayload);
  }

  getPost(id: number): Observable<PostModel> {
    return this.http.get<PostModel>('http://localhost:8080/api/posts/' + id);
  }

  getAllPostsByUser(name: string): Observable<PostModel[]> {
    return this.http.get<PostModel[]>('http://localhost:8080/api/posts/by-user/' + name);
  }

  getPostsFromConnectedUsers(username: string): Observable<PostModel[]> {
    // return this.http.get<PostModel[]>('http://localhost:8080/api/posts/get_all_posts_from_connections/' + username);
    return this.http.get<PostModel[]>('http://localhost:8080/api/posts/suggestions/' + this._authService.getUserName());
  }

  getMorePostSuggestions(alreadySuggested : PostModel[]) {
    return this.http.post<PostModel[]>('http://localhost:8080/api/posts/more_suggestions/', alreadySuggested);
  }
}
