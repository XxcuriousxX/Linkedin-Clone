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
  keywords : string[] = [];
  skills: string[] = []
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

  addKeyword(keyword) {
    if (!keyword) return;
    this.keywords.push(keyword.value);
  }

  removeKeyword(i) {
    this.keywords.splice(i, 1);
  }

  addSkill(skill) {
    if (!skill) return;
    this.skills.push(skill.value);
  }

  removeSkill(i) {
    this.skills.splice(i, 1);
  }

  createJobPost() {
    this.jobPayload.authorUsername = this._authService.getUserName();
    this.jobPayload.details = this.jobForm.value.details;
    this.jobPayload.keywords = "";
    for (let i = 0; i < this.keywords.length; i++) {
      if (i == 0) this.jobPayload.keywords = this.keywords[i];
      else this.jobPayload.keywords += "," + this.keywords[i];
    }
    this.jobPayload.requiredSkills = "";
    for (let i = 0; i < this.skills.length; i++) {
      if (i == 0) this.jobPayload.requiredSkills = this.skills[i];
      else this.jobPayload.requiredSkills += "," + this.skills[i];
    }
    this.jobPayload.employmentType = this.jobForm.value.employmentType;
    this.jobPayload.location = this.jobForm.value.location;
    this.jobPayload.title = this.jobForm.value.title;
    this._jobsService.createJobPost(this.jobPayload).subscribe( info => {
      this.keywords = [];
      this.skills = [];
      this.ngOnInit();

      this.jobForm.reset();
    }, error => {
      throwError(error);
    });
  }

}
