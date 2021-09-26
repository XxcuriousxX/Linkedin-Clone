import { MessagesService } from './../messages.service';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import {UserService} from "../../user.service";
import {User} from "../../user";
import {MatButtonModule} from '@angular/material/button';
import { Output, EventEmitter } from '@angular/core';
@Component({
  selector: 'app-conversations',
  templateUrl: './conversations.component.html',
  styleUrls: ['./conversations.component.css']
})
export class ConversationsComponent implements OnInit {


  selectedUserConv : string = "";
  usersList : User[] = []
  constructor(private _userService: UserService, private _router: Router, private _messagesService: MessagesService) { }
  toggle = -1;


  ngOnInit(): void {
    this.get_conversation_names();
  }


  selectUser(value: string) {
    this.selectedUserConv = value;
    this._router.navigate(['/messages/'], { queryParams: { conversation_name: this.selectedUserConv }});
  }


  change_button_state(value: string,index:number){
    this.selectUser(value);
    this.toggle = index;
  }

  get_conversation_names() {
    this._userService.getAllConnectedMessages().subscribe( L => {
    // this._messagesService.getConversationNames().subscribe( L => {
      this.usersList = L;
    });

  }

}
