import {Component, Inject, NgModule, OnInit} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import { CommonModule } from "@angular/common";
import { MatProgressBarModule } from '@angular/material/progress-bar';

@Component({
  selector: 'loading-dialog-elements-example-dialog',
  templateUrl: './loading-dialog-elements-example-dialog.html',
  styleUrls: ['./loading-dialog-elements-example-dialog.css']
})
export class LoadingDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<LoadingDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }


}