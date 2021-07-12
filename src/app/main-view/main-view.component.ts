import { Component, OnInit } from '@angular/core';
import {Routes} from "@angular/router";
import {LoginComponent} from "../login/login.component";
import {HomeComponent} from "../home/home.component";
import {MessagesComponent} from "../messages/messages.component";
import {MyNetworkComponent} from "../my-network/my-network.component";
import {NotFoundComponent} from "../not-found/not-found.component";

@Component({
  selector: 'app-main-view',
  templateUrl: './main-view.component.html',
  styleUrls: ['./main-view.component.css']
})



export class MainViewComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
