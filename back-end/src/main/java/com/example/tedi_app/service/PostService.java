package com.example.tedi_app.service;

import com.example.tedi_app.mapper.PostMapper;
import com.example.tedi_app.dto.PostRequest;
import com.example.tedi_app.dto.PostResponse;
import com.example.tedi_app.exceptions.PostNotFoundException;
import com.example.tedi_app.model.Action;
import com.example.tedi_app.model.Post;
import com.example.tedi_app.model.User;
import com.example.tedi_app.repo.ActionsRepository;
import com.example.tedi_app.repo.PostRepository;
import com.example.tedi_app.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
//    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    private final UserDetailsServiceImpl userService;

    public void save(PostRequest postRequest) {
        postRepository.save(postMapper.map(postRequest, authService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }



    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public Collection<PostResponse> getPostsFromConnectedUsers(String username) {
        Collection<User> connectedUsers = userService.get_all_connected_users(username);
        Collection<PostResponse> all_posts = new ArrayList<>();
        all_posts.addAll(this.getPostsByUsername(username)); // add posts of myself
        for (User u : connectedUsers) {
            all_posts.addAll(this.getPostsByUsername(u.getUsername()));
        }
        return all_posts;
    }
}
