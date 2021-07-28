
import { AuthService } from './../../auth/auth.service';
import { CommentService } from './../comment.service';
import { FormGroup, Validators, FormControl } from '@angular/forms';
import { PostModel } from './../post.model';
import { throwError } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { PostService } from './../post.service';
import { Component, OnInit } from '@angular/core';
import { CommentResponse, CommentRequest } from '../comment';

@Component({
  selector: 'app-full-post',
  templateUrl: './full-post.component.html',
  styleUrls: ['./full-post.component.css']
})
export class FullPostComponent implements OnInit {
	postId: number = -1;
	post: PostModel = {
		postId: -1,
		description: "",
		username: "",
		likeCount: 0,
		commentCount: 0,
		duration: "",
		createdDateLong: 0
	};


	commentForm: FormGroup;
	comment: string;
	commentRequest: CommentRequest = new CommentRequest();
	commentsList: CommentResponse[] = [];
	constructor(private _postService: PostService, private _activatedRoute: ActivatedRoute, private _commentService: CommentService
												 , private _authService: AuthService) {
		 // get id from "/post/:id"
		 // to make it work, in router module, we must specify the "post/:id" path
		
		// while (this.postId == -1)
		this.postId = this._activatedRoute.snapshot.params.id;

		this.commentForm = new FormGroup({
			// username: new FormControl('', Validators.required),
			comment_text: new FormControl('', Validators.required),
			//like_count: new FormControl('', Validators.required),
			//comment_count: new FormControl('', Validators.required)
		  });
	}

	ngOnInit(): void {
		this.postId = this._activatedRoute.snapshot.params.id;

		this.getPostById();
		this.getAllComments();
	}

	getPostById() {
		this._postService.getPost(this.postId).subscribe(res => {
			this.post = res;
		}, err => throwError(err))
	}


	addComment() {
		this.commentRequest.username = this._authService.getUserName();
		this.commentRequest.text = this.commentForm.value.comment_text;
		this.commentRequest.postId = this.postId;
		this._commentService.addComment(this.commentRequest).subscribe(res => {
			this.ngOnInit();
		}, err => throwError(err));
	}

	getAllComments() {
		this._commentService.getAllCommentsByPost(this.postId).subscribe(res => {
			this.commentsList = res;
		}, err => throwError(err));
	}
}
