import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {SearchService} from "../search/search.service";
import { Router, ActivatedRoute } from '@angular/router';
import { Output, EventEmitter, Input } from '@angular/core';
import { HostListener } from "@angular/core";
@Component({
  selector: 'app-navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrls: ['./navigation-bar.component.css']
})
export class NavigationBarComponent implements OnInit {

  query : string = "";

  searchForm:FormGroup = new FormGroup({
    query_input: new FormControl('', Validators.required)
  });


  screenHeight: number = -1;
  screenWidth: number = -1;
  is_active: Boolean = false;


  constructor(private _searchService: SearchService, private _router: Router) { }

  ngOnInit(): void {
    this.getScreenSize();

  }


  @HostListener('window:resize', ['$event'])
  getScreenSize(event?) {
        this.screenHeight = window.innerHeight;
        this.screenWidth = window.innerWidth;
  }

  search() {
    this.query = this.searchForm.value.query_input ; // pass value to parent (which is my-network)
    // navigate to '/my-network/?query=input of user
    this._router.navigate(['/mynetwork/'], { queryParams: { query: this.searchForm.value.query_input }});//, { queryParams: {input: this.searchForm.value.query_input}});
    this.searchForm.reset();
  }

  active() {
    this.is_active = true;
  }
}
