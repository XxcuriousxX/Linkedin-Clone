import { Component, OnInit } from '@angular/core';
import {PostModel} from "../../post/post.model";
import {PostService} from "../../post/post.service";
import {ActivatedRoute} from "@angular/router";
import {CommentService} from "../../post/comment.service";
import {AuthService} from "../../auth/auth.service";
import {JobsService} from "../jobs.service";
import {FullJobPostModel} from "./full-job-post";
import {throwError} from "rxjs";

@Component({
  selector: 'app-full-job-post-view',
  templateUrl: './full-job-post-view.component.html',
  styleUrls: ['./full-job-post-view.component.css']
})



export class FullJobPostViewComponent implements OnInit {
  skills:string[] = [];
  isLoaded = false
  jobpostId: number = -1;
  jobpost : FullJobPostModel = {
    jobPostId :-1,
    requestedcount:0,
    title:"",
    location:"",
    employmentType:"",
    details : "",
    requiredSkills : "", // "skill1,skill2,...,skillN"
    keywords : ""   // "keyword1,keyword2,...,keywordN"
  };

  constructor(private _jobService: JobsService, private _activatedRoute: ActivatedRoute,
    private _authService: AuthService) {
    this.jobpostId = this._activatedRoute.snapshot.params.id;
  }



  ngOnInit(): void {

    this.isLoaded = false;
    this.jobpostId = this._activatedRoute.snapshot.params.id;
    this.jobpost.jobPostId=this.jobpostId;
    this.getJobPostById();
  }


  getJobPostById() {
    this._jobService.getJobPostById(this.jobpostId).subscribe(res => {
      this.jobpost = res;
      this.jobpost.jobPostId=this.jobpostId;
      this.skills = this.jobpost.requiredSkills.split(",");
      this.isLoaded = true;

    }, err => throwError(err))
  }

}
