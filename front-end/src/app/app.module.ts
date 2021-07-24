import { ConnectService } from './connect-button/connect.service';
import { TokenInterceptor } from './token-interceptor';
import { AuthService } from './auth/auth.service';

import { HomeComponent } from './home/home.component';
import { MyNetworkComponent } from './my-network/my-network.component';
import { NavigationBarComponent } from './navigation-bar/navigation-bar.component';
import { MessagesComponent } from './messages/messages.component';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { NotFoundComponent } from './not-found/not-found.component';
import {RouterModule, Routes} from "@angular/router";
import { LoginComponent } from './login/login.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { MatButtonModule } from '@angular/material/button';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import {ReactiveFormsModule} from "@angular/forms";
import {NgxWebstorageModule} from 'ngx-webstorage';
import { AuthGuard } from './auth/auth.guard';
import { PostsBoxComponent } from './posts-box/posts-box.component';
import {LikeComponent} from "./post/like/like.component";
import { SearchComponent } from './search/search.component';
import {MatGridListModule} from "@angular/material/grid-list";
import { ConnectButtonComponent } from './connect-button/connect-button.component';
import { ConversationsComponent } from './messages/conversations/conversations.component';

import { MessagesService } from './messages/messages.service';

const materialModules = [
  MatButtonModule
];

//we need to define here the list of all of our routes
// array of route objects -> one path and one component
const appRoutes : Routes = [
  // if authenticated then redirect to home
  { path: '', component: HomeComponent, canActivate: [AuthGuard]},
  { path: 'login', component: LoginComponent },
  {
    path: 'sign-up',
    component: SignUpComponent
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'messages',
    component: MessagesComponent,
    canActivate: [AuthGuard],
    children: [
      {path: '?conversation_name=:name', component: MessagesComponent}
    ]
  },
  {
    path: 'mynetwork',
    component: MyNetworkComponent,
    canActivate: [AuthGuard],
    children: [
      {path: '?query=:q', component: MyNetworkComponent}
    ]
  },
  {
    //default route
    path: '' ,
    component: LoginComponent ,
    pathMatch : 'full'
  },
  {
    // ** -> none of our routes are hit wildcard route
    path: '**',
    component: NotFoundComponent
  }
]

@NgModule({
  declarations: [
    AppComponent,
    NavigationBarComponent,
    MessagesComponent,
    HomeComponent,
    NotFoundComponent,
    LoginComponent,
    SignUpComponent,
    MyNetworkComponent,
    PostsBoxComponent,
    LikeComponent,
    SearchComponent,
    ConnectButtonComponent,
    ConversationsComponent
  ],
    imports: [
        BrowserModule,
        RouterModule.forRoot(appRoutes, {enableTracing: true}),
        BrowserAnimationsModule,
        MatButtonModule,
        HttpClientModule,
        ReactiveFormsModule,
        NgxWebstorageModule.forRoot(),
        MatGridListModule
    ],
  providers: [ AuthService, {
    provide: HTTP_INTERCEPTORS,
    useClass: TokenInterceptor,
    multi: true
  }, ConnectService, MessagesService],
  bootstrap: [AppComponent],
  exports: [RouterModule]

})
export class AppModule {

}
