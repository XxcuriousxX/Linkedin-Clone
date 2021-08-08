import { AdminAuthGuard } from './admin/admin-auth.guard';
import { JobsService } from './jobs/jobs.service';
import { SettingsComponent } from './settings/settings.component';
import { PersonalInfoComponent } from './personal-info/personal-info.component';
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
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgxWebstorageModule} from 'ngx-webstorage';
import { AuthGuard } from './auth/auth.guard';
import { PostsBoxComponent } from './posts-box/posts-box.component';
import {LikeComponent} from "./post/like/like.component";
import { SearchComponent } from './search/search.component';
import {MatGridListModule} from "@angular/material/grid-list";
import { ConnectButtonComponent } from './connect-button/connect-button.component';
import { ConversationsComponent } from './messages/conversations/conversations.component';

import { MessagesService } from './messages/messages.service';
import { NotificationsComponent } from './notifications/notifications.component';
import { PublicButtonsComponent } from './personal-info/public-buttons/public-buttons.component';
import { FullPostComponent } from './post/full-post/full-post.component';
import { UserprofileComponent } from './userprofile/userprofile.component';
import {MatExpansionModule} from "@angular/material/expansion";
import { JobsComponent } from './jobs/jobs.component';
import { AdminComponent } from './admin/admin.component';
import { CreateJobPostComponent } from './jobs/create-job-post/create-job-post.component';
import { FullJobPostViewComponent } from './jobs/full-job-post-view/full-job-post-view.component';

const materialModules = [
  MatButtonModule
];

//we need to define here the list of all of our routes
// array of route objects -> one path and one component
const appRoutes : Routes = [

  // if authenticated then redirect to home
  { path: '', component: AdminComponent, canActivate: [AdminAuthGuard]},
  // { path: 'home', component: AdminComponent, canActivate: [AdminAuthGuard]},
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
    canActivate: [AuthGuard]
  },
  {
    path: 'post/:id',
    component: FullPostComponent,
    canActivate: [AuthGuard]
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
    path: 'notifications',
    component: NotificationsComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'jobs',
    component: JobsComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'jobs/:id',
    component: FullJobPostViewComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'createjobpost',
    component: CreateJobPostComponent,
    canActivate: [AuthGuard]
  },
  {
    //default route
    path: '' ,
    component: LoginComponent ,
    pathMatch : 'full'
  },
  {
    path: 'personal-Info',
    component: PersonalInfoComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'user/:username',
    component: UserprofileComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'mysettings',
    component: SettingsComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [AdminAuthGuard]
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
    ConversationsComponent,
    NotificationsComponent,
    SettingsComponent,
    PersonalInfoComponent,
    PublicButtonsComponent,
    FullPostComponent,
    UserprofileComponent,
    JobsComponent,
    AdminComponent,
    CreateJobPostComponent,
    FullJobPostViewComponent
  ],
    imports: [
        BrowserModule,
        RouterModule.forRoot(appRoutes, {enableTracing: true}),
        BrowserAnimationsModule,
        MatButtonModule,
        HttpClientModule,
        ReactiveFormsModule,
        NgxWebstorageModule.forRoot(),
        MatGridListModule,
        FormsModule,
        MatExpansionModule
    ],
  providers: [ AuthService, {
    provide: HTTP_INTERCEPTORS,
    useClass: TokenInterceptor,
    multi: true
  }, ConnectService, MessagesService, JobsService],
  bootstrap: [AppComponent],
  exports: [RouterModule]

})
export class AppModule {

}
