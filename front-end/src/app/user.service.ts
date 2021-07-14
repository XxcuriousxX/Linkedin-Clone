import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { User } from './user';


@Injectable({
  providedIn: 'root'
})
export class UserService {
  private usr : User = new User();
  private _userfind_byemail = "http://localhost:8080/user/find/email/";

  constructor(private http : HttpClient) { }

  getUserByEmail(email: string) {
    return this.http.get<User>(this._userfind_byemail + email);
  }

  search_now() {
    this.getUserByEmail("@").subscribe((data: User) => this.usr = {
      first_name: data.first_name,
      last_name: data.last_name,
      password: data.password,
      email: data.email,
      phone: data.phone,
      company_name: data.company_name
    });

    console.log("This is user")
    console.log("email isss= ", this.usr.email);
  }
}


