import {Component, HostListener, Inject, OnInit} from '@angular/core';
import {throwError} from "rxjs";
import {JobPostResponse} from "../Jobs";
import {JobsService} from "../jobs.service";
import {AuthService} from "../../auth/auth.service";
import {MyJobResponse} from "./myjob";
import {DOCUMENT} from "@angular/common";
import {MatDialogModule} from "@angular/material/dialog";
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA
} from "@angular/material/dialog";
import {User} from "../../user";
import {DialogComponent} from "./dialog.component";


@Component({
  selector: 'app-myjobs',
  templateUrl: './myjobs.component.html',
  styleUrls: ['./myjobs.component.css']
})


export class MyjobsComponent implements OnInit {
  isempty: boolean = false;
  myjobs: MyJobResponse[] = [];



  constructor(private _jobsService: JobsService, private _authService: AuthService,public dialog: MatDialog) { }

  ngOnInit(): void {
    this.isempty = false;
    this.getScreenSize();
    this.getmyjobs();
  }


  screenHeight: number = -1;
  screenWidth: number = -1;

  @HostListener('window:resize', ['$event'])
  getScreenSize(event?) {
    this.screenHeight = window.innerHeight;
    this.screenWidth = window.innerWidth;
  }

  getmyjobs() {
    this._jobsService.getmyjobs().subscribe(data => {
      this.myjobs = data;
      if (this.myjobs.length == 0) {
        this.isempty = true;
      }
      for (let e of this.myjobs) {
        for (let k of e.user_list) {
        }
      }
    }, err => throwError(err));
  }


  openDialog(user_list : User[]) {
    this.dialog.open(DialogComponent, {
      data: { users: user_list },
      backdropClass: 'backdropBackground',
      panelClass: 'dialog-container-custom'
    });
  }

  deleteJob(jobpostid: number) {
    this._jobsService.deleteJob(jobpostid).subscribe(data => {
      this.ngOnInit();
    }, err => throwError(err));

    }


}

