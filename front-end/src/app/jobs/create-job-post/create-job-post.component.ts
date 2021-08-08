import { Component, OnInit } from '@angular/core';
import {JobsService} from "../jobs.service";
import {AuthService} from "../../auth/auth.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {throwError} from "rxjs";
import {JobPostModel} from "../Jobs";

@Component({
  selector: 'app-create-job-post',
  templateUrl: './create-job-post.component.html',
  styleUrls: ['./create-job-post.component.css']
})
export class CreateJobPostComponent implements OnInit {

  jobForm: FormGroup;
  jobPayload: JobPostModel = new JobPostModel();
  constructor(private _jobsService: JobsService, private _authService: AuthService) {

    this.jobForm = new FormGroup({
      details: new FormControl('', Validators.required),
      requiredSkills : new FormControl('', Validators.required),
      keywords: new FormControl('', Validators.required),
      title: new FormControl('', Validators.required),
      location: new FormControl('', Validators.required),
      employmentType: new FormControl('', Validators.required)
    });

  }

  ngOnInit(): void {
  }



  createJobPost() {
    this.jobPayload.authorUsername = this._authService.getUserName();
    this.jobPayload.details = this.jobForm.value.details;
    this.jobPayload.requiredSkills = this.jobForm.value.requiredSkills;
    this.jobPayload.keywords = this.jobForm.value.keywords;
    this.jobPayload.employmentType = this.jobForm.value.employmentType;
    this.jobPayload.location = this.jobForm.value.location;
    this.jobPayload.title = this.jobForm.value.title;
    this._jobsService.createJobPost(this.jobPayload).subscribe( info => {
      this.ngOnInit();
    }, error => {
      throwError(error);
    });
  }

}
