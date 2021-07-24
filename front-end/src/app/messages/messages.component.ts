import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import {AuthService} from '../auth/auth.service';
import {MessagesService} from './messages.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MessagePayload, MessageResponse} from './Message';

import { throwError } from 'rxjs';


@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit {

  conversation: MessageResponse[] =  [];
  payload : MessagePayload = new MessagePayload();
  receiverUsername: string = "";
  messageForm: FormGroup = new FormGroup({
    message: new FormControl('')
  });

  
  constructor(private _messagesService: MessagesService, private _authService: AuthService, private route: ActivatedRoute) { }
  ngOnInit(): void {
    // this.getConversation();
    // console.log("PRINTING CONV\n");
    
    this.route.queryParams.subscribe( params => {
      this.receiverUsername = params.conversation_name;
      this.getConversation();
    });
    // for (let data of this.conversation) {
    //   console.log(data.message);
    // }
  }


  setReceiverUsername(receiver: string) {
    
    this.receiverUsername = receiver;
    console.log("this val: " + this.receiverUsername);
  }

  
  
  
  
  sendMessage() {

    console.log("\nOur name is " , this._authService.getUserName());
    this.payload.sender_username = this._authService.getUserName();
    this.payload.receiver_username = this.receiverUsername;
    this.payload.message = this.messageForm.value.message;
    this._messagesService.sendMessage(this.payload).subscribe(data => {
      // this.toastr.success('Login Successful');
      // window.location.reload();
      this.ngOnInit();
    }, error => {
      throwError(error);
    });
  }


  getConversation() {
    this.payload.sender_username = this._authService.getUserName();
    this.payload.receiver_username = this.receiverUsername;
    this._messagesService.getConversation(this.payload).subscribe(  res => {
      this.conversation = res;
      console.log("GET CONV : " + res);
    }, error => { throwError(error); });
  }

}
