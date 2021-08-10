import { AuthService } from './../auth/auth.service';
import { HostListener } from '@angular/core';
import { throwError } from 'rxjs';
import { JobPostResponse, JobPostModel } from './Jobs';
import { JobsService } from './jobs.service';
import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-jobs',
  templateUrl: './jobs.component.html',
  styleUrls: ['./jobs.component.css']
})
export class JobsComponent implements OnInit {

  jobForm: FormGroup;
  jobPayload: JobPostModel = new JobPostModel();
  skills: string[] = [];
  suggestionsList: JobPostResponse[] = [];
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
    this.getScreenSize();
    this.getSuggestions();
  }

  screenHeight: number = -1;
  screenWidth: number = -1;

  @HostListener('window:resize', ['$event'])
  getScreenSize(event?) {
        this.screenHeight = window.innerHeight;
        this.screenWidth = window.innerWidth;
        console.log(this.screenHeight, this.screenWidth);
  }


  getSuggestions() {
    this._jobsService.getSuggestions().subscribe( suggestions => {
        this.suggestionsList = suggestions;
        for(let each of this.suggestionsList){
          each.skills = each.requiredSkills.split(",");
        }
        console.log(this.suggestionsList);
    }, err => throwError(err));
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
