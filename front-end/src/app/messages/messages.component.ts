import { UserService } from './../user.service';
import { ActivatedRoute } from '@angular/router';
import { AfterViewChecked, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import {AuthService} from '../auth/auth.service';
import {MessagesService} from './messages.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MessagePayload, MessageResponse} from './Message';

import { throwError } from 'rxjs';
import { ScrollToBottomDirective } from '../scroll-to-bottom.directive'
import { ThisReceiver } from '@angular/compiler';


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

  currentUser: string = this._authService.getUserName();
  // @ViewChild(ScrollToBottomDirective) scroll: ScrollToBottomDirective;
  @ViewChild('container') private myScrollContainer: ElementRef;

  
  constructor(private _messagesService: MessagesService, private _authService: AuthService, private route: ActivatedRoute
                          , private _userService: UserService) {
   }
  ngOnInit(): void {
    // this.getConversation();
    // console.log("PRINTING CONV\n");
    
    this.route.queryParams.subscribe( params => {
      this.receiverUsername = params.conversation_name;
      if (params.conversation_name !== undefined) // if conversation has been selected
        this.getConversation();
        this.scrollToBottom();


    });

  }

  ngAfterViewChecked() {
    this.scrollToBottom();
  }


  scrollToBottom(): void {
    try {
      this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;
      // this.container.nativeElement.scrollTop = this.container.nativeElement.scrollHeight;
    } catch(err) { }
  }

  
  
  sendMessage() {

    console.log("\nOur name is " , this._authService.getUserName());
    this.payload.sender_username = this._authService.getUserName();
    this.payload.receiver_username = this.receiverUsername;
    this.payload.message = this.messageForm.value.message;
    this._messagesService.sendMessage(this.payload).subscribe(data => {

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
    }, error => { throwError(error); });
  }



}
