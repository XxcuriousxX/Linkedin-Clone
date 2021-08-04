package com.example.tedi_app.service;


import com.example.tedi_app.dto.CommentResponse;
import com.example.tedi_app.dto.JobPostResponse;
import com.example.tedi_app.dto.PostResponse;
import com.example.tedi_app.mapper.PostMapper;
import com.example.tedi_app.model.*;
import com.example.tedi_app.repo.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class PostRecommendationService {

    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;
    private final PostViewsRepository postViewsRepository;
    private final PostRepository postRepository;
    private final JobPostService jobPostService;
    private PostMapper postMapper;
    private final VoteRepository voteRepository;
    private final CommentRepository commentRepository;
    private final PostService postService;

    public List<PostResponse> getSuggestionsFromViews(String username,boolean from_friends) {
        Optional<User> user_opt = userRepository.findByUsername(username);
        User user = user_opt.orElseThrow(() -> new UsernameNotFoundException("No user " +
                "Found with username: "+ username));

        List<Friends> L_friends = friendsRepository.getAllConnectedUsers(user.getUserId());
        if (L_friends.isEmpty())
            return new ArrayList<>();


        List<PostViews> postViewsList = new ArrayList<>();
        List<User> users_list = new ArrayList<>();

        if (!from_friends) {
            for (Friends f : L_friends) { // for each friend  f of mine

                Long friend_id = (user.getUserId().equals(f.getUser_id1()) ? f.getUser_id2() : f.getUser_id1());
                for (Friends ff: friendsRepository.getAllConnectedUsers(friend_id)) { // get all friends of f
                    Long friend_of_friend_id = (friend_id.equals(ff.getUser_id1()) ? ff.getUser_id2() : ff.getUser_id1());

                    Collection<PostViews> postCollection = postViewsRepository.findAllByUser_UserId(friend_of_friend_id);
                    postViewsList.addAll(postCollection);


                    Optional<User> user_opt1 = userRepository.findByUserId(friend_of_friend_id);
                    User user1 = user_opt1.orElseThrow(() -> new UsernameNotFoundException("No user " +
                            "Found with username: " + username));
                    users_list.add(user1);
                }
            }
        }
        else {
            for (Friends f : L_friends) {
                Long friend_id = (user.getUserId().equals(f.getUser_id1()) ? f.getUser_id2() : f.getUser_id1());

                Collection<PostViews> postCollection = postViewsRepository.findAllByUser_UserId(friend_id);

                postViewsList.addAll(postCollection);


                Optional<User> user_opt1 = userRepository.findByUserId(friend_id);
                User user1 = user_opt1.orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username: "+ username));
                users_list.add(user1);

            }
        }
        // add my  post views
        users_list.add(user);
        Collection<PostViews> all_my_post_views = postViewsRepository.findAllByUser_UserId(user.getUserId());
        postViewsList.addAll(all_my_post_views);


        List<Post> postsList = getAllInvolvedPosts(postViewsList);
        if(postsList.isEmpty()) return new ArrayList<>(){};

        int N = users_list.size();
        int M = postsList.size();
        System.out.println("N = " + N + "   M = " + M);
        double[][] R = new double[N][M];

        for (int i = 0; i < N; i++)
            for (int j = 0; j < M; j++)
                R[i][j] = 0.0;

        Long[] userIds = new Long[N];
        Long[] jobPostsIds = new Long[M];
        int i = 0;
        int j;
        for (User u : users_list) {
            userIds[i++] = u.getUserId();
        }

        for (i = 0; i < N; i++) { // for each user
            Long user_id = userIds[i];
            for (j = 0; j < M; j++) {   // for each jobPost
                Long job_post_id = postsList.get(j).getPostId();
                for (PostViews jpv : postViewsList) {
                    if (jpv.getPost().getPostId().equals(job_post_id)
                            && jpv.getUser().getUserId().equals(user_id)) {
                        R[i][j] = jpv.getViews();
                        break;
                    }
                }
            }
        }

        jobPostService.print_array(R);
        System.out.println("\n");
        System.out.println("\n");




        int K = 10;
        double[][] P = jobPostService.random_array(N,K);
        double[][] Q = jobPostService.random_array(M,K);  //_in_range(M,K, 1.0, 5.0);

        pair<double[][], double[][]> p = jobPostService.matrix_factorization(R,P,Q,K);
        P = p.a;
        Q = p.b;
        Q = jobPostService.Transpose(Q);

        double[][] nR = jobPostService.dot_arrays(P,Q);
        jobPostService.print_array(nR);


        int row = R.length - 1;

        System.out.println("IDDDDDD = >" + userIds[row]);
        double[] results = new double[M];
        for (j = 0; j < M; j++) {
            results[j] = nR[row][j];
        }

        int max1_col_index = 0;
        double max1_col = results[0];
        for (j = 0; j < M; j++) {
            if (max1_col < results[j]){
                max1_col = results[j];
                max1_col_index = j;
            }
        }

        List<PostResponse> postResponseList = new ArrayList<>();
        for (j = 0; j < M; j++) {
            Post JP = postsList.get(j);
            System.out.println(results[j] + " " + JP.getPostId() + "\n");
            if (results[j] > 3.5) {
                postResponseList.add(postMapper.mapToDto(JP));
            }
        }
        return postResponseList;
    }


    //////////////////////////////////////////////////// LIKES   ////////////////////////////////////////////////////

    public List<PostResponse> getSuggestionsFromLikes(String username,boolean from_friends) {
        Optional<User> user_opt = userRepository.findByUsername(username);
        User user = user_opt.orElseThrow(() -> new UsernameNotFoundException("No user " +
                "Found with username: "+ username));

        List<Friends> L_friends = friendsRepository.getAllConnectedUsers(user.getUserId());
        if (L_friends.isEmpty())
            return new ArrayList<>();


        List<Vote> voteList = new ArrayList<>();
        List<User> users_list = new ArrayList<>();


        if (!from_friends) {
            for (Friends f : L_friends) { // for each friend  f of mine

                Long friend_id = (user.getUserId().equals(f.getUser_id1()) ? f.getUser_id2() : f.getUser_id1());
                for (Friends ff: friendsRepository.getAllConnectedUsers(friend_id)) { // get all friends of f
                    Long friend_of_friend_id = (friend_id.equals(ff.getUser_id1()) ? ff.getUser_id2() : ff.getUser_id1());

                    Collection<Vote> voteCollection = voteRepository.findAllByUser_UserId(friend_of_friend_id);
                    voteList.addAll(voteCollection);


                    Optional<User> user_opt1 = userRepository.findByUserId(friend_of_friend_id);
                    User user1 = user_opt1.orElseThrow(() -> new UsernameNotFoundException("No user " +
                            "Found with username: " + username));
                    users_list.add(user1);
                }
            }
        }
        else {
            for (Friends f : L_friends) {
                Long friend_id = (user.getUserId().equals(f.getUser_id1()) ? f.getUser_id2() : f.getUser_id1());

                Collection<Vote> voteCollection = voteRepository.findAllByUser_UserId(friend_id);

                voteList.addAll(voteCollection);


                Optional<User> user_opt1 = userRepository.findByUserId(friend_id);
                User user1 = user_opt1.orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username: "+ username));
                users_list.add(user1);

            }
        }


        // add my  post views
        users_list.add(user);
        Collection<Vote> all_my_votes = voteRepository.findAllByUser_UserId(user.getUserId());
        voteList.addAll(all_my_votes);


        List<Post> postsList = getAllInvolvedPostsFromVotes(voteList);
        if(postsList.isEmpty()) return new ArrayList<>();

        int N = users_list.size();
        int M = postsList.size();


        System.out.println("N = " + N + "   M = " + M);
        double[][] R = new double[N][M];

        for (int i = 0; i < N; i++)
            for (int j = 0; j < M; j++)
                R[i][j] = 0.0;

        Long[] userIds = new Long[N];
        Long[] jobPostsIds = new Long[M];
        int i = 0;
        int j;
        for (User u : users_list) {
            userIds[i++] = u.getUserId();
        }

        for (i = 0; i < N; i++) { // for each user
            Long user_id = userIds[i];
            for (j = 0; j < M; j++) {   // for each Post
                Long post_id = postsList.get(j).getPostId();
                for (Vote jpv : voteList) {
                    if (jpv.getPost().getPostId().equals(post_id)
                            && jpv.getUser().getUserId().equals(user_id)) {
                        R[i][j] = 1; // user_id has liked post with id: post_id
                        break;
                    }
                }
            }
        }

        System.out.println("LIKES R : ");
        jobPostService.print_array(R);
        System.out.println("\n");
        System.out.println("\n");




        int K = 10;
        double[][] P = jobPostService.random_array(N,K);
        double[][] Q = jobPostService.random_array(M,K);  //_in_range(M,K, 1.0, 5.0);

        pair<double[][], double[][]> p = jobPostService.matrix_factorization(R,P,Q,K);
        P = p.a;
        Q = p.b;
        Q = jobPostService.Transpose(Q);

        double[][] nR = jobPostService.dot_arrays(P,Q);

        System.out.println("LIKES nR : ");
        jobPostService.print_array(nR);


        int row = R.length - 1;

        System.out.println("IDDDDDD = >" + userIds[row]);
        double[] results = new double[M];
        for (j = 0; j < M; j++) {
            results[j] = nR[row][j];
        }


        List<PostResponse> postResponseList = new ArrayList<>();
        for (j = 0; j < M; j++) {
            Post JP = postsList.get(j);
            System.out.println(results[j] + " " + JP.getPostId() + "\n");
            if (results[j] > 1.2) {
                postResponseList.add(postMapper.mapToDto(JP));
            }
        }

        return postResponseList;
    }




    //////////////////////////////////////////////////// COMMENTS   ////////////////////////////////////////////////////

    public List<PostResponse> getSuggestionsFromComments(String username, boolean from_friends) {
        Optional<User> user_opt = userRepository.findByUsername(username);
        User user = user_opt.orElseThrow(() -> new UsernameNotFoundException("No user " +
                "Found with username: "+ username));

        List<Friends> L_friends = friendsRepository.getAllConnectedUsers(user.getUserId());
        if (L_friends.isEmpty())
            return new ArrayList<>();


        List<Comment> commentList = new ArrayList<>();
        List<User> users_list = new ArrayList<>();
        if (!from_friends) {
            for (Friends f : L_friends) { // for each friend  f of mine

                Long friend_id = (user.getUserId().equals(f.getUser_id1()) ? f.getUser_id2() : f.getUser_id1());
                for (Friends ff: friendsRepository.getAllConnectedUsers(friend_id)) { // get all friends of f
                    Long friend_of_friend_id = (friend_id.equals(ff.getUser_id1()) ? ff.getUser_id2() : ff.getUser_id1());

                    Collection<Comment> commentCollection = commentRepository.findAllByUser_UserId(friend_of_friend_id);
                    commentList.addAll(commentCollection);


                    Optional<User> user_opt1 = userRepository.findByUserId(friend_of_friend_id);
                    User user1 = user_opt1.orElseThrow(() -> new UsernameNotFoundException("No user " +
                            "Found with username: " + username));
                    users_list.add(user1);
                }
            }
        }
        else {
            for (Friends f : L_friends) {
                Long friend_id = (user.getUserId().equals(f.getUser_id1()) ? f.getUser_id2() : f.getUser_id1());

                Collection<Comment> commentCollection = commentRepository.findAllByUser_UserId(friend_id);

                commentList.addAll(commentCollection);


                Optional<User> user_opt1 = userRepository.findByUserId(friend_id);
                User user1 = user_opt1.orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username: "+ username));
                users_list.add(user1);

            }
        }


        // add my  post views
        users_list.add(user);
        Collection<Comment> all_my_comments = commentRepository.findAllByUser_UserId(user.getUserId());
        commentList.addAll(all_my_comments);


        List<Post> postsList = getAllInvolvedPostsFromComments(commentList);
        if(postsList.isEmpty()) return new ArrayList<>(){};

        int N = users_list.size();
        int M = postsList.size();

        System.out.println("N = " + N + "   M = " + M);
        double[][] R = new double[N][M];

        for (int i = 0; i < N; i++)
            for (int j = 0; j < M; j++)
                R[i][j] = 0.0;

        Long[] userIds = new Long[N];
        Long[] jobPostsIds = new Long[M];
        int i = 0;
        int j;
        for (User u : users_list) {
            userIds[i++] = u.getUserId();
        }

        for (i = 0; i < N; i++) { // for each user
            Long user_id = userIds[i];
            for (j = 0; j < M; j++) {   // for each jobPost
                Long post_id = postsList.get(j).getPostId();
                for (Comment jpv : commentList) {
                    if (jpv.getPost().getPostId().equals(post_id)
                            && jpv.getUser().getUserId().equals(user_id)) {
                        R[i][j] = 1;  /// user_id has commented at least once on post_id
                        break;
                    }
                }
            }
        }
        System.out.println("Comments R:");
        jobPostService.print_array(R);
        System.out.println("\n");
        System.out.println("\n");




        int K = 10;
        double[][] P = jobPostService.random_array(N,K);
        double[][] Q = jobPostService.random_array(M,K);  //_in_range(M,K, 1.0, 5.0);

        pair<double[][], double[][]> p = jobPostService.matrix_factorization(R,P,Q,K);
        P = p.a;
        Q = p.b;
        Q = jobPostService.Transpose(Q);

        double[][] nR = jobPostService.dot_arrays(P,Q);
        System.out.println("Comment nR");
        jobPostService.print_array(nR);


        int row = R.length - 1;

        System.out.println("IDDDDDD = >" + userIds[row]);
        double[] results = new double[M];
        for (j = 0; j < M; j++) {
            results[j] = nR[row][j];
        }

        int max1_col_index = 0;
        double max1_col = results[0];
        for (j = 0; j < M; j++) {
            if (max1_col < results[j]){
                max1_col = results[j];
                max1_col_index = j;
            }
        }




        List<PostResponse> postResponseList = new ArrayList<>();
        for (j = 0; j < M; j++) {
            Post JP = postsList.get(j);
            System.out.println(results[j] + " " + JP.getPostId() + "\n");
            if (results[j] > 2.5) {
                postResponseList.add(postMapper.mapToDto(JP));
            }
        }

        return postResponseList;
    }





///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    public List<Post> getAllInvolvedPosts(List<PostViews> postViewsList) {
        List<Post> postList = new ArrayList<>();
        for (PostViews jpv : postViewsList) {
            if (id_exists(jpv.getPost().getPostId(), postList)) // if job post exists in jobPostList
                continue;
            postList.add(postRepository.getByPostId(jpv.getPost().getPostId()));
        }
        return postList;
    }

    private boolean id_exists(Long jp_id, List<Post> postsList) {
        for (Post jp : postsList) {
            if (jp_id.equals(jp.getPostId()))
                return true;
        }
        return false;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<Post> getAllInvolvedPostsFromVotes(List<Vote> voteList) {
        List<Post> postList = new ArrayList<>();
        for (Vote jpv : voteList) {
            if (id_exists(jpv.getPost().getPostId(), postList)) // if job post exists in jobPostList
                continue;
            postList.add(postRepository.getByPostId(jpv.getPost().getPostId()));
        }
        return postList;
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<Post> getAllInvolvedPostsFromComments(List<Comment> commentList) {
        List<Post> postList = new ArrayList<>();
        for (Comment jpv : commentList) {
            if (id_exists(jpv.getPost().getPostId(), postList)) // if job post exists in jobPostList
                continue;
            postList.add(postRepository.getByPostId(jpv.getPost().getPostId()));
        }
        return postList;
    }


    public List<PostResponse> get_all_post_suggestions(String username){
        List<PostResponse> allPostsFromConnected = postService.getPostsFromConnectedUsers(username);
        List<PostResponse> fromComments = getSuggestionsFromComments(username,true);
        List<PostResponse> fromLikes = getSuggestionsFromLikes(username,true);
        List<PostResponse> fromViews = getSuggestionsFromViews(username,true);
        List<PostResponse> onlyFriends = new ArrayList<>();
        List<PostResponse> suggestedList = new ArrayList<>();
        int N_top_recent = 5;


        Comparator<PostResponse> compareByTime = (PostResponse o1, PostResponse o2) -> o1.getCreatedDateLong().compareTo( o2.getCreatedDateLong() );

        
        suggestedList.addAll(fromComments);
        suggestedList.addAll(fromLikes);
        suggestedList.addAll(fromViews);


        if (!suggestedList.isEmpty()) {
            for (PostResponse p : allPostsFromConnected) { // for each p in allPostsFromConnected
                for (PostResponse p2 : suggestedList) {  // if p.postId exists in suggestedList, then delete it!
                    if (p2.getPostId().equals(p.getPostId())) {
                        suggestedList.remove(p2);
                        //break;
                    }
                }
            }
        }


        // sort friends posts
        Collections.sort(allPostsFromConnected, (p2, p1) -> p1.getCreatedDateLong().compareTo(p2.getCreatedDateLong()));
        int k = (allPostsFromConnected.size() < N_top_recent ? allPostsFromConnected.size() : N_top_recent);
        for (int i = 0; i < k; i++) {       // add top 5 recent posts to the onlyFriends list
            onlyFriends.add(allPostsFromConnected.get(i));
        }

        // sort the suggested posts from friends
        Collections.sort(suggestedList, (p2, p1) -> p1.getCreatedDateLong().compareTo(p2.getCreatedDateLong()));
        // add top 5 recent posts to the onlyFriends list from the suggested lists of friends
        k = (suggestedList.size() < 5 ? suggestedList.size() : 5);
        for (int i = 0; i < k; i++) {
            onlyFriends.add(suggestedList.get(i));
        }
        // order the suggestion list
        Collections.sort(suggestedList, (p2, p1) -> p1.getCreatedDateLong().compareTo(p2.getCreatedDateLong()));
        // sort again the posts from friends (suggested + top_5_recent) combined
//        Collections.sort(onlyFriends, (p2, p1) -> p1.getCreatedDateLong().compareTo(p2.getCreatedDateLong()));
        suggestedList = removeDuplicates(suggestedList);
        onlyFriends.addAll(suggestedList); // add suggestions at the end
        return onlyFriends;
        //////////////////////////////////////////

//        Collections.sort(onlyFriends, compareByTime.reversed());
//
//
//        List<PostResponse> top_responses = new ArrayList<>();
//
//        int k = ( 5 > onlyFriends.size() ? onlyFriends.size() : 5);
//
//
//        for (int i = 0; i < k; i++) {
//            top_responses.add(onlyFriends.get(i));
//        }
//
//
//
//        List<PostResponse> fromComments_false = getSuggestionsFromComments(username,false);
//        List<PostResponse> fromLikes_false = getSuggestionsFromLikes(username,false);
//        List<PostResponse> fromViews_false = getSuggestionsFromViews(username,false);
//        List<PostResponse> finalList_false = new ArrayList<>();
//
//        finalList_false.addAll(fromComments);
//        finalList_false.addAll(fromLikes);
//        finalList_false.addAll(fromViews);
//
//
//        Collections.sort(finalList_false, compareByTime.reversed());
//
//        k = ( 5 > finalList_false.size() ? finalList_false.size() : 5);
//
//
//        for (int i = 0; i < k; i++) {
//            top_responses.add(finalList_false.get(i));
//        }
//        Collections.sort(top_responses, compareByTime.reversed());
//        return top_responses;

    }


    public List<PostResponse> removeDuplicates(List<PostResponse> L) {
        List<PostResponse> L2 = new ArrayList<>();
        boolean exists = false;
        for (PostResponse p : L) {
            exists = false;
            for (PostResponse p2 : L2) {
                if (p2.getPostId().equals(p.getPostId())) {
                    exists = true;
                    break;
                }
                if (!exists)
                    L2.add(p);
            }
        }
        return L2;
    }




}
