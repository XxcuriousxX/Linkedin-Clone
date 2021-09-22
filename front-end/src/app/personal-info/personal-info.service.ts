import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../auth/auth.service";
import {MessagePayload} from "../messages/Message";
import {Observable} from "rxjs";
import {PersonalInfoPayload} from "./Personal-info";
import {PublicButtonPayload} from "./public-buttons/PublicButton";

@Injectable({
  providedIn: 'root'
})
export class PersonalInfoService {

  constructor(private http: HttpClient, private _authService: AuthService) { }

  changePersonalInfo(p_info_payload: PersonalInfoPayload): Observable<any> {
    return this.http.post<any>('http://localhost:8080/api/users/changepersonalinfo/', p_info_payload);
  }

  getButtonState(): Observable<any>{
    return this.http.get("http://localhost:8080/api/users/buttonstate/" + this._authService.getUserName());
  }

  changeButtonState(button_payload: PublicButtonPayload) :Observable<any>{
    return this.http.post<any>("http://localhost:8080/api/users/changebuttonstate/", button_payload);
  }

  getPersonalInfo() : Observable<any> {
    return this.http.get("http://localhost:8080/api/users/get_personal_info/" + this._authService.getUserName());
  }



}
