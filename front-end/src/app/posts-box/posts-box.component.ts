import { AuthService } from './../auth/auth.service';
import { PostService } from './../post/post.service';
import { Component, OnInit, Input, ViewEncapsulation } from '@angular/core';
import {PostModel } from '../post/post.model';
import { Router } from '@angular/router';
import { stringify } from '@angular/compiler/src/util';

@Component({
  selector: 'app-posts-box',
  templateUrl: './posts-box.component.html',
  styleUrls: ['./posts-box.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class PostsBoxComponent implements OnInit {


  posts: PostModel[] = [];



  constructor(private router: Router, private _postService: PostService, private _authService: AuthService) { }

  ngOnInit(): void {

    this._postService.getPostsFromConnectedUsers(this._authService.getUserName()).subscribe(posts => {
      this.posts = posts;
      // this.posts.sort((a, b) => (a.createdDateLong < b.createdDateLong) ? 1 : -1);
    });

  }

  goToPost(id: number): void {
    this.router.navigateByUrl('/view-post/' + id);
  }

}
