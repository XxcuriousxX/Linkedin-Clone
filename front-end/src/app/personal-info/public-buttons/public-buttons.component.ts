import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-public-buttons',
  templateUrl: './public-buttons.component.html',
  styleUrls: ['./public-buttons.component.css']
})
export class PublicButtonsComponent implements OnInit {
  selected: boolean = false;

  constructor() { }

  ngOnInit(): void {
  }

}
