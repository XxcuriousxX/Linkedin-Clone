import { UserService } from './../user.service';
import { ActivatedRoute } from '@angular/router';
import { AfterViewChecked, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import {AuthService} from '../auth/auth.service';
import {MessagesService} from './messages.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MessagePayload, MessageResponse} from './Message';

import {interval, Observable, Subscription, throwError, timer} from 'rxjs';
import { ThisReceiver } from '@angular/compiler';
import {switchMap} from "rxjs-compat/operator/switchMap";
import {retry} from "rxjs-compat/operator/retry";
import {share} from "rxjs-compat/operator/share";
import { startWith } from 'rxjs/operators';


@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit, AfterViewChecked {

  timeInterval: Subscription;

  mylistener : any;
  containsmessage : boolean = false;
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


    });


    // interval(5000) // run every 5 seconds
    //   .pipe(
    //     startWith(0),
    //     switchMap(() => { this.loadMoreMessages() })
    //   )
    //   .subscribe(
    //
    //   );


    this.mylistener = setInterval( () => {
      this.loadMoreMessages()
      console.log("TIMEE")
      }, 10000);

  }



  ngAfterViewChecked() {
      this.scrollToBottom();
      this.containsmessage = false;
  }



  scrollToBottom(): void {
    try {
      this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;
      // this.container.nativeElement.scrollTop = this.container.nativeElement.scrollHeight;
    } catch(err) { }
  }

  loadMoreMessages() : MessageResponse[] {
    if (this.receiverUsername != ""){
      this._messagesService.loadMoreMessages(this.conversation[this.conversation.length-1]).subscribe( extra_messages => {
        console.log("ena dio tria apo ngafter   " + extra_messages.length);
        if (extra_messages.length != 0){
          this.containsmessage = true;
          for (let message of extra_messages) {
            this.conversation.push(message);
          }
        }
        return this.conversation;
      });
    }
    return this.conversation;

  }


  sendMessage() {

    console.log("\nOur name is " , this._authService.getUserName());
    this.payload.sender_username = this._authService.getUserName();
    this.payload.receiver_username = this.receiverUsername;
    this.payload.message = this.messageForm.value.message;
    this._messagesService.sendMessage(this.payload).subscribe(data => {
      // this.loadMoreMessages();
      this.ngOnInit();
      this.messageForm.reset();
      this.containsmessage = true;
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
        this.conversation[x].timeCreated = this.conversation[x].timeCreated;
        this.conversation[x].stringTimeCreated = this.split_date(this.conversation[x].timeCreated);

      }
      this.scrollToBottom();
    }, error => { throwError(error); });
  }


  split_date(_date:string):string{

    let time = _date.substring(11,16);
    let date  = _date.substring(0,10);
    date = date.split('-').reverse().join('');
    date = date.substring(0,2) + '/' + date.substring(2,4) + '/' + date.substring(6,8);
    return time + "      " + date;
  }

  ngOnDestroy() {
      clearInterval(this.mylistener)
  }

}
