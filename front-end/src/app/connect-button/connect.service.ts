import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConnectPayload, ConnectResponse } from './Connect';

@Injectable({
  providedIn: 'root'
})
export class ConnectService {

  constructor(private http: HttpClient) { }


  // direction does not matter  { user1, user2 } is the same as { user2, user1 }
  areConnected(connectPayload: ConnectPayload): Observable<ConnectResponse> { // send: {sender, receiver}
    return this.http.post<ConnectResponse>('http://localhost:8080/api/users/are_connected', connectPayload);
  }

  makeConnectionRequest(connectPayload: ConnectPayload): Observable<any> { // dont expect response
      return this.http.post<any>('http://localhost:8080/api/users/connect_request', connectPayload);
  }

  // Remember: sender of the request, has sent friendRequest = {sender_username, receiver_username}
  // Receiver user will also have to send friendRequest = { sender_username, receiver_username}
  acceptConnectionRequest(connectPayload: ConnectPayload): Observable<ConnectResponse> {
    return this.http.post<ConnectResponse>('http://localhost:8080/api/users/accept_connection_request', connectPayload);
  }

  rejectConnectionRequest(connectPayload: ConnectPayload): Observable<ConnectResponse> {
      return this.http.post<ConnectResponse>('http://localhost:8080/api/users/reject_connection_request', connectPayload);
  }

  isRequestPending(connectPayload: ConnectPayload): Observable<ConnectResponse> {
      return this.http.post<ConnectResponse>('http://localhost:8080/api/users/pending_request', connectPayload);
  }

  removeConnection(connectPayload: ConnectPayload): Observable<any> { // no return
      return this.http.post<any>('http://localhost:8080/api/users/remove_connection', connectPayload);
  }

}
