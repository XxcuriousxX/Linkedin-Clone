import { UserService } from './user.service';
import { User } from './user';
import { AuthService } from './auth.service';

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
import { HttpClientModule } from '@angular/common/http';
import {ReactiveFormsModule} from "@angular/forms";



const materialModules = [
  MatButtonModule
];

//we need to define here the list of all of our routes
// array of route objects -> one path and one component
const appRoutes : Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'sign-up',
    component: SignUpComponent
  },
  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'messages',
    component: MessagesComponent
  },
  {
    path: 'mynetwork',
    component: MyNetworkComponent
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
    MyNetworkComponent
  ],
    imports: [
        BrowserModule,
        RouterModule.forRoot(appRoutes, {enableTracing: true}),
        BrowserAnimationsModule,
        MatButtonModule,
        HttpClientModule,
        ReactiveFormsModule
    ],
  providers: [AuthService, UserService],
  bootstrap: [AppComponent]
})
export class AppModule {

}
