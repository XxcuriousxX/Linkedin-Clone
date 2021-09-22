import {Component, Input, OnInit} from '@angular/core';
import {PostModel} from "../../post/post.model";
import {LikePayload} from "../../post/like/like.payload";
import {LikeService} from "../../post/like/like.service";
import {AuthService} from "../../auth/auth.service";
import {PostService} from "../../post/post.service";
import {Router} from "@angular/router";
import {throwError} from "rxjs";
import {JobPostModel} from "../Jobs";
import {JobrequestPayload} from "./jobrequest.payload";
import {JobsService} from "../jobs.service";
import {JobrequestService} from "./jobrequest.service";
import {FullJobPostModel} from "../full-job-post-view/full-job-post";

@Component({
  selector: 'app-jobrequest',
  templateUrl: './jobrequest.component.html',
  styleUrls: ['./jobrequest.component.css']
})
export class JobrequestComponent implements OnInit {

  isLoaded: boolean = false;
  update_likes_num_computed = false;
  has_liked_computed = false;

  @Input() post: FullJobPostModel;
  likePayload : JobrequestPayload;
  liked_by_current_user: boolean;
  constructor(private likeService: JobrequestService,
              private authService: AuthService, private postService: JobsService, private router: Router) {
    this.liked_by_current_user = false;

    this.likePayload = {
      postId : -1
    }
    this.post = {
      jobPostId :-1,
      requestedcount:0,
      title:"",
      location:"",
      employmentType:"",
      details : "",
      requiredSkills : "", // "skill1,skill2,...,skillN"
      keywords :"",   // "keyword1,keyword2,...,keywordN,
    }
  }

  ngOnInit(): void {
    this.likePayload.postId = this.post.jobPostId;
    this.update_likes_num_computed = false;
    this.has_liked_computed = false;
    this.isLoaded = false;
    this.liked_by_current_user = false;
    this.updateLikesNum_and_has_liked();
  }

  like() {
    this.likePayload.postId = this.post.jobPostId;
    this.likeService.like(this.likePayload).subscribe(response => {
      if (response.has_liked) {
        this.updateLikesNum();
        this.liked_by_current_user = true;
        this.ngOnInit();

      }
      else {
        this.liked_by_current_user = false;
        this.ngOnInit();
      }
    }, error => {
      throwError(error);
    });
  }




  updateLikesNum_and_has_liked() {
    this.likePayload.postId = this.post.jobPostId;
    this.likeService.has_liked(this.likePayload).subscribe(response => {
      if (response.has_liked) {
        this.liked_by_current_user = true;
      }
      else {
        this.liked_by_current_user = false;
      }

      this.postService.getJobPostByIdRequest(this.post.jobPostId).subscribe(post => {
        this.post = post;
        this.isLoaded = true;
      });
    }, err => {
      throwError(err);
    });
  }

  updateLikesNum() {
    this.likePayload.postId = this.post.jobPostId;
    this.postService.getJobPostByIdRequest(this.post.jobPostId).subscribe(post => {
      this.post = post;
    });
  }
}
