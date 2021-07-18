import { Component, OnInit, Input } from '@angular/core';
import { PostModel } from '../post.model';
import { LikePayload } from "./like.payload";
import { throwError } from 'rxjs';
import {LikeService} from "./like.service";
import {AuthService} from "../../auth/auth.service";
import {PostService} from "../post.service";


@Component({
  selector: 'app-like',
  templateUrl: './like.component.html',
  styleUrls: ['./like.component.css']
})
export class LikeComponent implements OnInit {

  @Input() post: PostModel;
  likePayload : LikePayload;

  constructor(private likeService: LikeService,
              private authService: AuthService, private postService: PostService) {
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

  }

  like() {
    this.likePayload.postId = this.post.postId;
    this.likeService.like(this.likePayload).subscribe(() => {
      this.updateLikesNum();
      }, error => {
      throwError(error);
    });
  }

  private updateLikesNum() {
    this.postService.getPost(this.post.postId).subscribe(post => {
      this.post = post;
    });
  }
}
