import { PostModel } from './../post/post.model';
import { throwError } from 'rxjs';
import { AuthService } from './../auth/auth.service';
import { PostService } from './../post/post.service';
import { Component, OnInit, Input, ViewEncapsulation, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { stringify } from '@angular/compiler/src/util';

@Component({
  selector: 'app-posts-box',
  templateUrl: './posts-box.component.html',
  styleUrls: ['./posts-box.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class PostsBoxComponent implements OnInit {
  more_suggestions_triggered : number = 0;

  posts: PostModel[] = [];



  constructor(private router: Router, private _postService: PostService, private _authService: AuthService) { }

  ngOnInit(): void {

    this._postService.getPostsFromConnectedUsers(this._authService.getUserName()).subscribe(posts => {
      this.posts = posts;
      // this.posts.sort((a, b) => (a.createdDateLong < b.createdDateLong) ? 1 : -1);
    });

  }

  @HostListener("window:scroll", [])
  onScroll(): void {
    if ((window.innerHeight + window.scrollY) >= document.body.scrollHeight && this.more_suggestions_triggered <= 4) {
      console.log("END reached")
      this._postService.getMorePostSuggestions(this.posts).subscribe( more_sug => {
        for (let p of more_sug) {
          this.posts.push(p);
          console.log("new post id = ", p.postId)
        }
        this.more_suggestions_triggered += 1;
          
      }, err => throwError(err));         
    }
  }

  goToPost(id: number): void {
    this.router.navigateByUrl('/view-post/' + id);
  }

}
