package com.example.tedi_app.controller;

import com.example.tedi_app.dto.*;
import com.example.tedi_app.model.Comment;
import com.example.tedi_app.model.Post;
import com.example.tedi_app.service.PostRecommendationService;
import com.example.tedi_app.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts/")
@AllArgsConstructor
public class PostController {

    private final PostService postService;
    private  final PostRecommendationService postRecommendationService;

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody PostRequest postRequest) {
        System.out.println("HELLO : " + postRequest.getDescription());
        postService.save(postRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return status(HttpStatus.OK).body(postService.getPost(id));
    }



    @GetMapping("by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String name) {
        return status(HttpStatus.OK).body(postService.getPostsByUsername(name));
    }

    @GetMapping("/get_all_posts_from_connections/{username}")
    public  ResponseEntity<Collection<PostResponse>> getPostsFromConnected(@PathVariable String username) {
        return status(HttpStatus.OK).body(postService.getPostsFromConnectedUsers(username));
    }

    /////// comments

    @GetMapping("/get_all_comments_by_post/{post_id}")
    public ResponseEntity<List<CommentResponse>> getAllCommentsByPost(@PathVariable String post_id) {
        Long postId = Long.parseLong(post_id);
        return status(HttpStatus.OK).body(postService.getAllCommentsByPostId(postId));
    }

    @PostMapping("/add_comment")
    public ResponseEntity<List<Comment>> addComment(@RequestBody CommentRequest commentRequest) {
        postService.addComment(commentRequest);
        System.out.println("Addded comment!!!!");
        return new ResponseEntity<>(HttpStatus.CREATED);

    }


    @GetMapping("suggestions/{username}")
    public ResponseEntity<Collection<PostResponse>> getSuggestions(@PathVariable String username) {
        System.out.println("ALL SUggestions COMMENTS - LIKES - VIEWS!!!");
        Collection<PostResponse> col = postService.getPostsFromConnectedUsers(username);
        List<PostResponse> suggested = postRecommendationService.get_all_post_suggestions(username);
        for (PostResponse p : col) {
            if (suggested.contains(p)) {
                suggested.remove(p);
            }
        }

        col.addAll(suggested);


        return status(HttpStatus.OK).body(col);
    }


}
