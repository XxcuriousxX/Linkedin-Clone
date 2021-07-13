import { Component, OnInit } from '@angular/core';
import {AppComponent} from "../app.component";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})



export class LoginComponent implements OnInit {

  ac : AppComponent;
  constructor( temp : AppComponent) {
    this.ac = temp;
  }

  ngOnInit(): void {

  }

}
