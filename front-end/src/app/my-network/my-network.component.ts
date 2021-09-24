import { AuthService } from './../auth/auth.service';
import { Component, OnInit } from '@angular/core';
import {UserService} from "../user.service";
import {User, UserResponse} from "../user";
import { Observable, BehaviorSubject, throwError } from 'rxjs';
import {ActivatedRoute} from "@angular/router";
import {SearchService} from "../search/search.service";
import {MatGridListModule} from '@angular/material/grid-list';
import { filter } from 'rxjs/operators';
import {MatButtonModule} from '@angular/material/button';
import { HostListener } from "@angular/core";
@Component({
  selector: 'app-my-network',
  templateUrl: './my-network.component.html',
  styleUrls: ['./my-network.component.css']
})
export class MyNetworkComponent implements OnInit {
  query_p: string = "";
  usersList: User[] = [];
  search_result: UserResponse[] = [];
  query_param: string = "";
  is_query: boolean = false;
  isLoaded: boolean = false;
  constructor(private _userService: UserService, private route: ActivatedRoute, private _searchService : SearchService,
                                            private _authService: AuthService) { }

  ngOnInit(): void {
    this.getScreenSize();
    this.isLoaded = false;

    this.route.queryParams
      .subscribe(params => {

          this.query_param = params.query;

          if (this.query_param != undefined)
            this.is_query = true;
          else this.is_query = false;

          this.query_p = this.query_param;

          if (this.query_p == "" || this.query_p == undefined) return;
          this._searchService.executeQuery(this.query_p).subscribe(res => {
              // filter myself from the query result
              this.search_result = res;
              let my_self = this.search_result.find(usr => usr.username == this._authService.getUserName())
              this.search_result = this.search_result.filter(usr => usr !== my_self);

              //get each user's profile image and save it in usersList array
              for (let user of this.search_result){

                this._userService.retrieveProfileImageByUsername(user.username).subscribe(data => {
                  user.image = 'data:image/jpeg;base64,' + data.image;
                }, error => {
                  throwError(error);
                });

              }

              this.isLoaded = true;
            },
            error => {
              throwError(error);
            });

        }
      );



    this._userService.getAllConnected().subscribe(res => {
      this.usersList = res;
      

        //get each user's profile image and save it in usersList array
        for (let user of this.usersList){

          this._userService.retrieveProfileImageByUsername(user.username).subscribe(data => {
            user.image = 'data:image/jpeg;base64,' + data.image;
          }, error => {
            // throwError(error);
          });

        }
        this.isLoaded = true;

    },
      err => {
        throwError(err);
      });



  }

  screenHeight: number = -1;
  screenWidth: number = -1;

  @HostListener('window:resize', ['$event'])
  getScreenSize(event?) {
        this.screenHeight = window.innerHeight;
        this.screenWidth = window.innerWidth;
  }




}
