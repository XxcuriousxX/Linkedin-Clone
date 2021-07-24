import { AuthService } from './../auth/auth.service';
import { Component, OnInit } from '@angular/core';
import {UserService} from "../user.service";
import {User, userResponse} from "../user";
import { Observable, BehaviorSubject, throwError } from 'rxjs';
import {ActivatedRoute} from "@angular/router";
import {SearchService} from "../search/search.service";
import {MatGridListModule} from '@angular/material/grid-list';
import { filter } from 'rxjs/operators';
import {MatButtonModule} from '@angular/material/button';

@Component({
  selector: 'app-my-network',
  templateUrl: './my-network.component.html',
  styleUrls: ['./my-network.component.css']
})
export class MyNetworkComponent implements OnInit {
  query_p: string = "";
  usersList: User[] = [];
  search_result: userResponse[] = [];
  query_param: string = "";
  is_query: boolean = false;
  constructor(private _userService: UserService, private route: ActivatedRoute, private _searchService : SearchService,
                                            private _authService: AuthService) { }

  ngOnInit(): void {
    this.route.queryParams
      //.filter(params => params.query)
      .subscribe(params => {
          console.log(params); // { query: "popular" }

          this.query_param = params.query;

          if (this.query_param != undefined)
            this.is_query = true;
          else this.is_query = false;

          this.query_p = this.query_param;

          this._searchService.executeQuery(this.query_p).subscribe(res => {
              // filter myself from the query result
              this.search_result = res;
              let my_self = this.search_result.find(usr => usr.username == this._authService.getUserName())
              this.search_result = this.search_result.filter(usr => usr !== my_self);

              console.log("kalooo" + res);
            },
            error => {
              console.log("gtp query");
              throwError(error);
            });

        }
      );



    this._userService.getAllConnected().subscribe(res => {
      this.usersList = res;
      console.log("SUCCESS");
    },
      err => {
        console.log("ERR");
        throwError(err);
      });

  }

}
