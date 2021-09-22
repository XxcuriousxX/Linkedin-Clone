import { throwError } from 'rxjs';
import { AuthService } from './../auth/auth.service';
import { User } from './../user';
import { ConnectService } from './connect.service';
import { ConnectPayload, ConnectResponse } from './Connect';
import { Component, OnInit } from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import { Input } from '@angular/core';
import { TOUCH_BUFFER_MS } from '@angular/cdk/a11y/input-modality/input-modality-detector';

@Component({
  selector: 'app-connect-button',
  templateUrl: './connect-button.component.html',
  styleUrls: ['./connect-button.component.css']
})
export class ConnectButtonComponent implements OnInit {

  @Input() receiver_user: User;
  // 0: not connected and not request sent or received, 1: request has been sent, 2: request has been received, 3: already connected
  connection_status: number = 0;
  connectPayload: ConnectPayload = new ConnectPayload();

  constructor(private _connectService: ConnectService, private _authService: AuthService) { }

  ngOnInit(): void {
	this.connection_status = 0; // init to not connected
    // If connected, then set are_connected = true, else set it as false
    this.connectPayload.sender_username = this._authService.getUserName();
    this.connectPayload.receiver_username = this.receiver_user.username;
    this._connectService.areConnected(this.connectPayload).subscribe(con_response => {
      if (con_response.status)
        this.connection_status = 3;

        // request has been sent
      this._connectService.isRequestPending(this.connectPayload).subscribe(con => {
        if (con.status)
          this.connection_status = 1;

        // check if a request has been received, and is awaiting aproval
        // just reverse receiver and sender
        this.connectPayload.sender_username = this.receiver_user.username;
        this.connectPayload.receiver_username = this._authService.getUserName();
        this._connectService.isRequestPending(this.connectPayload).subscribe(c => {
        	if (c.status)
            	this.connection_status = 2;
        }, 	err => { throwError(err) });
      }, err => { throwError(err) });
    }, err => { throwError(err) });

  }


  sendConnectionRequest() {
    this.connectPayload.sender_username = this._authService.getUserName();
    this.connectPayload.receiver_username = this.receiver_user.username;
    this._connectService.makeConnectionRequest(this.connectPayload).subscribe( res => {
		  this.connection_status = 1;
      this.ngOnInit();


	}, err => { throwError(err) });
  }

  remove_or_cancel_connection() {
	  this.connectPayload.sender_username = this._authService.getUserName();
	  this.connectPayload.receiver_username = this.receiver_user.username;
	  this._connectService.removeConnection(this.connectPayload).subscribe( res => {
		  this.connection_status = 0;
      this.ngOnInit();

	  }, err => { throwError(err) });
  }

  accept_connection() {
	  this.connectPayload.sender_username = this.receiver_user.username;
	  this.connectPayload.receiver_username = this._authService.getUserName();
	  this._connectService.acceptConnectionRequest(this.connectPayload).subscribe( res => {
		  this.connection_status = 3;
      this.ngOnInit();


	  })
  }

}
