import { UserService } from './../user.service';
import { ActivatedRoute } from '@angular/router';
import { AfterViewChecked, Component, ElementRef, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import {AuthService} from '../auth/auth.service';
import {MessagesService} from './messages.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MessagePayload, MessageResponse} from './Message';

import {interval, Observable, Subscription, throwError, timer, Subject} from 'rxjs';
import { ThisReceiver } from '@angular/compiler';
import { switchMap, tap, share, retry, takeUntil } from 'rxjs/operators';
// import { Observable, timer, Subscription, Subject } from 'rxjs';
import { startWith } from 'rxjs/operators';


@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit {

  timeInterval: Subscription;

  send_message: boolean = false;
  mylistener : any;
  containsmessage : boolean = false;
  conversation: MessageResponse[] =  [];
  payload : MessagePayload = new MessagePayload();
  receiverUsername: string = "";
  messageForm: FormGroup = new FormGroup({
    message: new FormControl('')
  });

  currentUser: string = this._authService.getUserName();

  private stopPolling = new Subject();
  conv$ : Observable<MessageResponse[]>;
  conversation$: Observable<MessageResponse[]>;

  constructor(private _messagesService: MessagesService, private _authService: AuthService, private route: ActivatedRoute
                          , private _userService: UserService) {



    this.conversation$ = timer(1, 3000).pipe(
      switchMap(() => {
        this.payload.sender_username = this._authService.getUserName();
        this.payload.receiver_username = this.receiverUsername;
        return this._messagesService.getConversation(this.payload);
      }),
      retry(),
      share(),
      takeUntil(this.stopPolling)
      );
   }



  ngOnInit(): void {


    this.route.queryParams.subscribe( params => {
      this.receiverUsername = params.conversation_name;
      if (params.conversation_name !== undefined) // if conversation has been selected
        this.conv$ = this.getConv();


    });


  }





  getConv() : Observable<MessageResponse[]> {
    return this.conversation$;
  }


  loadMoreMessages() : MessageResponse[] {
    if (this.receiverUsername != ""){
      this._messagesService.loadMoreMessages(this.conversation[this.conversation.length-1]).subscribe( extra_messages => {
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

    this.payload.sender_username = this._authService.getUserName();
    this.payload.receiver_username = this.receiverUsername;
    this.payload.message = this.messageForm.value.message;
    this._messagesService.sendMessage(this.payload).subscribe(data => {
      this.send_message = true;
      this.messageForm.reset();
    }, error => {
      throwError(error);
    });
  }


  getConversation(){
    this.payload.sender_username = this._authService.getUserName();
    this.payload.receiver_username = this.receiverUsername;
    this._messagesService.getConversation(this.payload).subscribe(  res => {
      this.conversation = res;
      for (let x=0; x < this.conversation.length; x++){
        this.conversation[x].timeCreated = this.conversation[x].timeCreated;
        this.conversation[x].stringTimeCreated = this.split_date(this.conversation[x].timeCreated);

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

  ngOnDestroy() {
    this.stopPolling.next();
  }

}
