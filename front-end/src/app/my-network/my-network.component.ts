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
  constructor(private _userService: UserService, private route: ActivatedRoute, private _searchService : SearchService) { }

  ngOnInit(): void {
    this.route.queryParams
      //.filter(params => params.query)
      .subscribe(params => {
          console.log(params); // { order: "popular" }

          this.query_param = params.query;

          if (this.query_param != undefined)
            this.is_query = true;
          else this.is_query = false;

          this.query_p = this.query_param;

          this._searchService.executeQuery(this.query_p).subscribe(res => {
              this.search_result = res;

              console.log("kalooo" + res);
            },
            error => {
              console.log("gtp query");
              throwError(error);
            });

        }
      );



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
