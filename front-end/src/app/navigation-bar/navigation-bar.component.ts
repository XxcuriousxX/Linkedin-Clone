import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {SearchService} from "../search/search.service";
import { Router, ActivatedRoute } from '@angular/router';
import { Output, EventEmitter, Input } from '@angular/core';
@Component({
  selector: 'app-navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrls: ['./navigation-bar.component.css']
})
export class NavigationBarComponent implements OnInit {
  //@Output() query = new EventEmitter<string>();
  query : string = "";
  // @Output() has_done_query = new EventEmitter<string>();
  searchForm: FormGroup = new FormGroup({
    query_input: new FormControl('', Validators.required)
  });

  constructor(private _searchService: SearchService, private _router: Router) { }

  ngOnInit(): void {
  }

  search() {
    this.query = this.searchForm.value.query_input ; // pass value to parent (which is my-network)
    // navigate to '/my-network/?query=input of user
    console.log("INSERTED QUERY: " + this.query);
    this._router.navigate(['/mynetwork/'], { queryParams: { query: this.searchForm.value.query_input }});//, { queryParams: {input: this.searchForm.value.query_input}});
  }
}
