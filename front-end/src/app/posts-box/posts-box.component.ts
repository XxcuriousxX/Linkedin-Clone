import { AuthService } from './../auth/auth.service';
import { PostService } from './../post/post.service';
import { Component, OnInit, Input, ViewEncapsulation } from '@angular/core';
import {PostModel } from '../post/post.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-posts-box',
  templateUrl: './posts-box.component.html',
  styleUrls: ['./posts-box.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class PostsBoxComponent implements OnInit {


  @Input() posts: PostModel[] = [];


  constructor(private router: Router, private _postService: PostService, private _authService: AuthService) { }

  ngOnInit(): void {
    this._postService.getAllPostsByUser(this._authService.getUserName()).subscribe(post => {
      this.posts = post;
    });

  }

  goToPost(id: number): void {
    this.router.navigateByUrl('/view-post/' + id);
  }

}
