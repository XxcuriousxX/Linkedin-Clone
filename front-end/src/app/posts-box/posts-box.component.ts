import { PostModel } from './../post/post.model';
import { throwError } from 'rxjs';
import { AuthService } from './../auth/auth.service';
import { PostService } from './../post/post.service';
import { Component, OnInit, Input, ViewEncapsulation, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { stringify } from '@angular/compiler/src/util';
import { LoadingComponent } from '../loading/loading/loading.component';

@Component({
  selector: 'app-posts-box',
  templateUrl: './posts-box.component.html',
  styleUrls: ['./posts-box.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class PostsBoxComponent implements OnInit {
  more_suggestions_triggered : number = 0;

  posts: PostModel[] = [];

  isLoaded = false;

  constructor(private router: Router, private _postService: PostService, private _authService: AuthService) { }

  ngOnInit(): void {

    this._postService.getPostsFromConnectedUsers(this._authService.getUserName()).subscribe(posts => {
      this.posts = posts;
      this.isLoaded = true
      
    });

  }

  @HostListener("window:scroll", [])
  onScroll(): void {

    if ((window.innerHeight + window.scrollY)  >= document.body.scrollHeight   && this.more_suggestions_triggered <= 6) {
      this._postService.getMorePostSuggestions(this.posts).subscribe( more_sug => {
        for (let p of more_sug) {
          this.posts.push(p);
        }
        this.more_suggestions_triggered += 1;
        this.isLoaded = true;
      }, err => throwError(err));         
    }
  }

  goToPost(id: number): void {
    this.router.navigateByUrl('/view-post/' + id);
  }

}
