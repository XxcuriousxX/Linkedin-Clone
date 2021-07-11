import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { NavigationBarComponent } from './navigation-bar/navigation-bar.component';
import { MyNetworkComponent } from './my-network/my-network.component';
import { MessagesComponent } from './messages/messages.component';
import { HomeComponent } from './home/home.component';
import { NotFoundComponent } from './not-found/not-found.component';
import {RouterModule, Routes} from "@angular/router";
import { LoginComponent } from './login/login.component';
import { MainViewComponent } from './main-view/main-view.component';

//we need to define here the list of all of our routes
// array of route objects -> one path and one component
const appRoutes : Routes = [
  {
    path: 'login',
    component: LoginComponent
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
    path: 'app',
    component: MainViewComponent,
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
    MainViewComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes,{enableTracing:true})
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {

}
