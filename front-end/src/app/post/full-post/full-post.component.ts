import { PostModel } from './../post.model';
import { throwError } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { PostService } from './../post.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-full-post',
  templateUrl: './full-post.component.html',
  styleUrls: ['./full-post.component.css']
})
export class FullPostComponent implements OnInit {
	postId: number;
	post: PostModel;
	constructor(private _postService: PostService, private _activatedRoute: ActivatedRoute) {
		 // get id from "/post/:id"
		 // to make it work, in router module, we must specify the "post/:id" path
		this.postId = this._activatedRoute.snapshot.params.id;
	}

	ngOnInit(): void {
		this.getPostById();
	}

	getPostById() {
		this._postService.getPost(this.postId).subscribe(res => {
			this.post = res;
		}, err => throwError(err))
	}

}
