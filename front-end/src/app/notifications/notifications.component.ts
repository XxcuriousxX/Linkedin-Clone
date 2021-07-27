import { ActionResponse } from './ActionResponse';
import { NotificationsService } from './notifications.service';
import { User } from './../user';
import { throwError } from 'rxjs';
import { AuthService } from './../auth/auth.service';
import { ConnectService } from './../connect-button/connect.service';
import { Component, OnInit } from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import { ConnectButtonComponent } from '../connect-button/connect-button.component';
@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {


  connectionRequests: User[] = [];
  notificationList: ActionResponse[] = [];
  constructor(private _connectService: ConnectService, private _authService: AuthService
                , private _notificationsService: NotificationsService) { }
  
  ngOnInit(): void {
    this.getAllPendingRequestsSentToUs();
    this.getAllNotificationsForUser();
  }


  getAllPendingRequestsSentToUs() {
    this._connectService.getAllPendingRequestsSentToUser(this._authService.getUserName()).subscribe( res => {
      this.connectionRequests = res;
    }, err => throwError(err));
  }

  getAllNotificationsForUser() {
    this._notificationsService.getAllNotificationsForUser().subscribe( res => {
      this.notificationList = res;
    }, err => throwError(err));
  }


}
