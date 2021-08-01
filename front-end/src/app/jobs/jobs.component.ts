import { AuthService } from './../auth/auth.service';
import { HostListener } from '@angular/core';
import { throwError } from 'rxjs';
import { JobPostResponse } from './Jobs';
import { JobsService } from './jobs.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-jobs',
  templateUrl: './jobs.component.html',
  styleUrls: ['./jobs.component.css']
})
export class JobsComponent implements OnInit {
  suggestionsList: JobPostResponse[] = [];
  constructor(private _jobsService: JobsService, private _authService: AuthService) { }

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
        console.log(this.suggestionsList);
    }, err => throwError(err));
  }

}
