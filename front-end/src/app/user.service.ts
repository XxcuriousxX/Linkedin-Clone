import { Observable } from 'rxjs';
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from './user';
import { EmailValidator } from '@angular/forms';
import { waitForAsync } from '@angular/core/testing';
import { take } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  
  private usr : User = { 
    first_name : "",
    last_name : "",
    password : "",
    email : "",
    phone : "",
    company_name : ""
  };
  response: string = "";
  private _userfind_byemail_url = "http://localhost:8080/user/find/email/";

  constructor(private http : HttpClient) { }

  getUserByEmail(email: string) {
    return this.http.get<User>(this._userfind_byemail_url + email);
  }

  getUserByEmailResponse(email: string) : Observable<HttpResponse<User>> {
    return this.http.get<User>(this._userfind_byemail_url + email,
                                          { observe: 'response'});
      
  }

  showUserByEmailResponse(email: string) {
    this.getUserByEmailResponse(email).subscribe(resp => {
      const keys = resp.headers.keys();
      this.usr = { ...resp.body! };
    });
    console.log("Priting body:")
    console.log(this.usr)
  }

  search_now(email : string) {
    // this.getUserByEmail("1").subscribe((data: User) => this.usr = {
    //   first_name: data.first_name,
    //   last_name: data.last_name,
    //   password: data.password,
    //   email: data.email,
    //   phone: data.phone,
    //   company_name: data.company_name
    // });

    // this.showUserByEmailResponse(email);

    
    console.log("This is user")
    console.log("email isss= ", this.usr.email);
  }


  doReg_obs(email: string) {
    return this.http.get(this._userfind_byemail_url + email, {responseType:'text' as 'json'});
  }

  async getValFromObs(email: string) {
    return await this.doReg_obs(email).toPromise()
  
  }


  


}


