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
  isLoaded: boolean = false;
  update_likes_num_computed = false;
  has_liked_computed = false;

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
       commentCount:  0,
       createdDateLong : 0
     }
  }

  ngOnInit(): void {
    this.update_likes_num_computed = false;
    this.has_liked_computed = false;
    this.isLoaded = false;
    this.liked_by_current_user = false;
    // this.updateLikesNum();
    this.updateLikesNum_and_has_liked();
  }

  like() {
    this.likePayload.postId = this.post.postId;
    this.likeService.like(this.likePayload).subscribe(response => {
        if (response.has_liked) {
        	this.updateLikesNum();
        	this.liked_by_current_user = true;
          this.ngOnInit();   // pio sosto
        	// window.location.reload();
        }
        else {
        	this.liked_by_current_user = false;
          this.ngOnInit();
        	// window.location.reload();
        }
      }, error => {
        throwError(error);
      });
  }




  updateLikesNum_and_has_liked() {
    this.likePayload.postId = this.post.postId;
    this.likeService.has_liked(this.likePayload).subscribe(response => {
        if (response.has_liked) {
			    this.liked_by_current_user = true;
		    }
		    else {
			    this.liked_by_current_user = false;
		    }
        this.postService.getPost(this.post.postId).subscribe(post => {
        this.post = post;
        this.isLoaded = true;
    });
    }, err => {
		throwError(err);
	});
  }

   updateLikesNum() {
    this.postService.getPost(this.post.postId).subscribe(post => {
      this.post = post;
    });
  }
}
