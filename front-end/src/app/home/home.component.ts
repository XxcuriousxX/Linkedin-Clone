import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css', './../app.component.css', '../login/login.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private _authService: AuthService, private _router: Router) {  }

  ngOnInit(): void {

  }

  logout() {
    
    this._authService.logout();
    this._router.navigate(['/login'])
  }

}
