import { User } from './../user';
import { Observable } from 'rxjs';
import { AuthService } from './../auth/auth.service';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConnectPayload, ConnectResponse } from './Connect';

@Injectable({
  providedIn: 'root'
})
export class ConnectService {

  constructor(private http: HttpClient, private _authService: AuthService) { }


  // direction does not matter  { user1, user2 } is the same as { user2, user1 }
  areConnected(connectPayload: ConnectPayload): Observable<ConnectResponse> { // send: {sender, receiver}
    return this.http.post<ConnectResponse>('https://localhost:8443/api/users/are_connected', connectPayload);
  }

  makeConnectionRequest(connectPayload: ConnectPayload): Observable<any> { // dont expect response
      return this.http.post<any>('https://localhost:8443/api/users/connect_request', connectPayload);
  }

  // Remember: sender of the request, has sent friendRequest = {sender_username, receiver_username}
  // Receiver user will also have to send friendRequest = { sender_username, receiver_username}
  acceptConnectionRequest(connectPayload: ConnectPayload): Observable<ConnectResponse> {
    return this.http.post<ConnectResponse>('https://localhost:8443/api/users/accept_connection_request', connectPayload);
  }

  rejectConnectionRequest(connectPayload: ConnectPayload): Observable<ConnectResponse> {
      return this.http.post<ConnectResponse>('https://localhost:8443/api/users/reject_connection_request', connectPayload);
  }

  isRequestPending(connectPayload: ConnectPayload): Observable<ConnectResponse> {
      return this.http.post<ConnectResponse>('http://localhost:8080/api/users/pending_request', connectPayload);
  }

  removeConnection(connectPayload: ConnectPayload): Observable<any> { // no return
      return this.http.post<any>('http://localhost:8080/api/users/remove_connection', connectPayload);
  }

  
  getAllPendingRequestsSentToUser(username: string): Observable<User[]> {
      return this.http.get<User[]>('http://localhost:8080/api/users/get_all_pending_requests_sent_to_user/' + username);
  }

}
