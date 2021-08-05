import { Router } from '@angular/router';
import { AuthService } from './../auth/auth.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  constructor(private _authService : AuthService, private _router: Router) { }

  ngOnInit(): void {
  }

  logout() {

    this._authService.logout();
    this._router.navigate(['/login'])
  }

}
