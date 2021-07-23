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

  @Output() selectedUserConv = new EventEmitter<string>(); // username
  usersList : User[] = []
  // @Output selected_receiver_username = new EventEmitter<string>();
  constructor(private _userService: UserService) { }


  ngOnInit(): void {
    this.get_conversation_names();
  }


  selectUser(value: string) {
    this.selectedUserConv.emit(value);
    
  }

  get_conversation_names() {
    this._userService.getAllConnected().subscribe( L => {
      this.usersList = L;
      console.log(L);
    });

  }

}
