import { Component, OnInit } from '@angular/core';
import {UserService} from "../user.service";
import {User} from "../user";
import { Observable, BehaviorSubject, throwError } from 'rxjs';

@Component({
  selector: 'app-my-network',
  templateUrl: './my-network.component.html',
  styleUrls: ['./my-network.component.css']
})
export class MyNetworkComponent implements OnInit {

  usersList: User[] = [];
  constructor(private _userService: UserService) { }

  ngOnInit(): void {
    this._userService.getAllConnected().subscribe((res) => {
      this.usersList = res;
      console.log("SUCCESS");
    },
      err => {
        console.log("ERR");
        throwError(err);
      });
  }
}
