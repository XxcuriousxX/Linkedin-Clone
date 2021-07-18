import { Component, OnInit, Input, ViewEncapsulation } from '@angular/core';
import {PostModel } from '../post/post.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-posts-box',
  templateUrl: './posts-box.component.html',
  styleUrls: ['./posts-box.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class PostsBoxComponent implements OnInit {


  @Input() posts: PostModel[] = [];


  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  goToPost(id: number): void {
    this.router.navigateByUrl('/view-post/' + id);
  }

}
