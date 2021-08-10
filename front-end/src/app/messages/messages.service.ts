import { User } from './../user';
import { Observable } from 'rxjs';
import { AuthService } from './../auth/auth.service';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConnectPayload, ConnectResponse } from '../connect-button/Connect';
import {MessagePayload, MessageResponse} from './Message';
@Injectable({
  providedIn: 'root'
})
export class MessagesService {

    constructor(private http: HttpClient, private _authService: AuthService) { }


    sendMessage(messagePayload: MessagePayload): Observable<any> {
        return this.http.post<any>('https://localhost:8443/api/messages/send_message/', messagePayload);
    }

    getConversation(messagePayload: MessagePayload): Observable<MessageResponse[]> { // dont send message. only send and receiver usernames
        return this.http.post<MessageResponse[]>('https://localhost:8443/api/messages/get_conversation/', messagePayload, { responseType: 'json' } );
    }

    loadMoreMessages(messageresp : MessageResponse): Observable<MessageResponse[]> {
        return this.http.post<MessageResponse[]>('https://localhost:8443/api/messages/more_messages/', messageresp);
    }
}