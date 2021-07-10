import { HomeComponent } from './home/home.component';
import { MessagesComponent } from './messages/messages.component';
import { MyNetworkComponent } from './my-network/my-network.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {path: 'mynetwork', component: MyNetworkComponent},
  {path: 'messages', component: MessagesComponent},
  {path: 'home', component: HomeComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }


export const routingComponents = [MyNetworkComponent, MessagesComponent, HomeComponent];