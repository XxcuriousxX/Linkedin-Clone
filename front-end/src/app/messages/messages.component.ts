import { UserService } from './../user.service';
import { ActivatedRoute } from '@angular/router';
import { AfterViewChecked, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import {AuthService} from '../auth/auth.service';
import {MessagesService} from './messages.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MessagePayload, MessageResponse} from './Message';

import { throwError } from 'rxjs';
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
      this.messageForm.reset();
    }, error => {
      throwError(error);
    });
  }


  getConversation() {
    this.payload.sender_username = this._authService.getUserName();
    this.payload.receiver_username = this.receiverUsername;
    this._messagesService.getConversation(this.payload).subscribe(  res => {
      this.conversation = res;
      for (let x=0; x < this.conversation.length; x++){
        // this.conversation[x].instantTimeCreated = this.conversation[x].timeCreated;
        this.conversation[x].timeCreated = this.split_date(this.conversation[x].timeCreated);
    }
    }, error => { throwError(error); });
  }


  split_date(_date:string):string{

    let time = _date.substring(11,16);
    let date  = _date.substring(0,10);
    date = date.split('-').reverse().join('');
    date = date.substring(0,2) + '/' + date.substring(2,4) + '/' + date.substring(6,8);
    return time + "      " + date;
  }

}
