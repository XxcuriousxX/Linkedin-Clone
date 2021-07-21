import { Router } from '@angular/router';
import { Component, OnInit, Input } from '@angular/core';
import { PostModel } from '../post.model';
import { LikePayload } from "./like.payload";
import { throwError } from 'rxjs';
import {LikeService} from "./like.service";
import {AuthService} from "../../auth/auth.service";
import {PostService} from "../post.service";
import {MatButtonModule} from '@angular/material/button';
import { ThrowStmt } from '@angular/compiler';


@Component({
  selector: 'app-like',
  templateUrl: './like.component.html',
  styleUrls: ['./like.component.css']
})
export class LikeComponent implements OnInit {

  @Input() post: PostModel;
  likePayload : LikePayload;
  liked_by_current_user: boolean;
  constructor(private likeService: LikeService,
              private authService: AuthService, private postService: PostService, private router: Router) {
     this.liked_by_current_user = false;

     this.likePayload = {
       postId : -1
     }
     this.post = {
       postId: -1,
       description: "",
       username: "",
       likeCount: 0,
       duration: "",
       commentCount:  0
     }
  }

  ngOnInit(): void {
    this.liked_by_current_user = false;
    this.updateLikesNum();
    this.has_liked();
  }

  like() {
    this.likePayload.postId = this.post.postId;
    this.likeService.like(this.likePayload).subscribe(response => {
        if (response.has_liked) {
        	this.updateLikesNum();
        	this.liked_by_current_user = true;
          // this.ngOnInit(); pio sosto
        	window.location.reload();
        }
        else {
        	this.liked_by_current_user = false;
          // this.ngOnInit();
        	window.location.reload();
        }
      }, error => {
        throwError(error);
      });
  }




  has_liked() {
    this.likePayload.postId = this.post.postId;
    this.likeService.has_liked(this.likePayload).subscribe(response => {
        if (response.has_liked) {
			this.liked_by_current_user = true;
		}
		else {
			this.liked_by_current_user = false;
		}
    }, err => {
		throwError(err);
	});
  }

  private updateLikesNum() {
    this.postService.getPost(this.post.postId).subscribe(post => {
      this.post = post;
    });
  }
}
