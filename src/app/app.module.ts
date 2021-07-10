import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule, routingComponents } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavigationBarComponent } from './navigation-bar/navigation-bar.component';
import { MyNetworkComponent } from './my-network/my-network.component';
import { MessagesComponent } from './messages/messages.component';
import { HomeComponent } from './home/home.component';
import { NotFoundComponent } from './not-found/not-found.component';
import {RouterModule, Routes} from "@angular/router";

//we need to define here the list of all of our routes
// array of route objects -> one path and one component
const appRoutes : Routes = [
  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'messaging',
    component: MessagesComponent
  },
  {
    path: 'my-network',
    component: MyNetworkComponent
  },
  {
    //default route
    path: '' ,
    component: HomeComponent ,
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
    routingComponents,
    MessagesComponent,
    HomeComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
