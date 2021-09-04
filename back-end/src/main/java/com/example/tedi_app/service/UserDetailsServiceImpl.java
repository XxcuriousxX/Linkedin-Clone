package com.example.tedi_app.service;

import com.example.tedi_app.dto.*;
import com.example.tedi_app.exceptions.SpringTediException;
import com.example.tedi_app.mapper.PostMapper;
import com.example.tedi_app.model.*;
import com.example.tedi_app.repo.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.*;
import java.util.Optional;

import static java.util.Collections.list;
import static java.util.Collections.singletonList;


@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;
    private final PersonalinfoRepository personalInfoRepository;
    private final PostRepository postRepository;
    private final JobPostRepository jobPostRepository;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;
    private final PostMapper postMapper;



//    private  JobPostService jobPostService;
//    private  PostService postService;
//    private   VoteService voteService;



//    public List<JobPostResponse> mapAllJobPostsToDto(List<JobPost> jpList) {
//        List<JobPostResponse> jprList = new ArrayList<>();
//        for (JobPost jp : jpList) {
//            jprList.add(JobPostService.mapToDto(jp));
//        }
//        return jprList;
//    }
    public List<PostResponse> mapAllPostsToDto(List<Post> postList) {
        List<PostResponse> post_respList = new ArrayList<>();
        for (Post p : postList) {
            post_respList.add(postMapper.mapToDto(p));
        }
        return post_respList;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));

        return new org.springframework.security
                .core.userdetails.User(user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true,
                true, getAuthorities("USER"));
    }

    public User getUserById(Long uid) {
        Optional<User> userOptional = userRepository.findByUserId(uid);
        User user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with id : " + uid));
        return user;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return singletonList(new SimpleGrantedAuthority(role));
    }


    public List<User> getAllUsers() {
        Optional<List<User>> listOpt = userRepository.getAll();
        if (listOpt.isEmpty()) return new ArrayList<>();
        return listOpt.orElseThrow(() -> new SpringTediException("No users found"));
    }

    public List<DetailedUser> getAllDetailedUsers(String[] usernames) {
        List<DetailedUser> L = new ArrayList<>();
        Optional<List<User>> listOpt = userRepository.getAll();
        if (listOpt.isEmpty()) return new ArrayList<>();
        List<User> users = listOpt.orElseThrow(() -> new SpringTediException("No users found"));
        for (User u : users) {
            for (String username : usernames) {
                if (u.getUsername().equals(username)) {
                    Personalinfo personalInfo = personalInfoRepository.findByUserUserId(u.getUserId()).orElseThrow(() -> new SpringTediException("Personal info not found"));
                      List<Post> allPosts = postRepository.findByUser(u);
                      if (allPosts == null) allPosts = new ArrayList<>();
                        List<PostResponse> allPostResponses = this.mapAllPostsToDto(allPosts);

//                    List<PostResponse> allPostResponses = postService.getPostsByUsername(u.getUsername());


                    List<JobPost> allJobPosts = jobPostRepository.getAllByUserUserId(u.getUserId());
                    if (allJobPosts == null) allJobPosts = new ArrayList<>();
                    List<JobPostResponse> allJobPostResponses = JobPostService.mapAllJobPostsToDto(allJobPosts);


                    List<Comment> comments = commentRepository.getAllByUserUserId(u.getUserId());
                    if (comments == null) comments = new ArrayList<>();
                    List<CommentResponse> allCommentResponses = PostService.mapAllCommentsToDto(comments);

                    List<Vote> votes = voteRepository.getAllByUserUserId(u.getUserId());
                    if (votes == null) votes = new ArrayList<>();
                    List<VoteData> voteDataList = VoteService.mapAllToVoteData(votes);

                    List<String> friends_usernames = new ArrayList<>();
                    List<User> friends = this.get_all_connected_users(u.getUsername());
                    for (User friend : friends) {
                        friends_usernames.add(friend.getUsername());
                    }
                    L.add(new DetailedUser(u.getUserId(), u.getUsername(), u.getEmail(), u.getPhone(),
                            u.getFirst_name(), u.getLast_name(), u.getCompany_name(),
                            personalInfo.getWork_desc(), personalInfo.getStud_desc(), personalInfo.getAbilities_desc(),
                            allPostResponses, allJobPostResponses, allCommentResponses,
                            voteDataList, friends_usernames));
                    break;
                }
            }
        }

        return L;
    }

    // returns a list of Users who are friends of username
    public List<User> get_all_connected_users(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));


        List<Friends> F = friendsRepository.getAllConnectedUsers(user.getUserId());

        ArrayList<User> friends_list = new ArrayList<>();
        Long id = user.getUserId();

        for (Friends friend : F) {
            if (!friend.getUser_id1().equals(id)) {
                User u1 = userRepository.findByUserId(friend.getUser_id1()).orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));
                friends_list.add(u1);
            }
            if (!friend.getUser_id2().equals(id)) {
                User u2 = userRepository.findByUserId(friend.getUser_id2()).orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));
                friends_list.add(u2);
            }
        }


        return friends_list;

    }

    public void makeConnectionRequest(String sender_username, String receiver_username) {
        Optional<User> receiverUserOptional = userRepository.findByUsername(receiver_username);
        User receiver_user = receiverUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + receiver_username));
        Optional<User> senderUserOptional = userRepository.findByUsername(sender_username);
        User sender_user = senderUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + sender_username));

        // create a friends row with accepted value: 'false'
        friendsRepository.save(new Friends(sender_user.getUserId(), receiver_user.getUserId()));

    }

    public void acceptRequest(String sender_username,String receiver_username ) {
        Optional<User> receiverUserOptional = userRepository.findByUsername(receiver_username);
        User receiver_user = receiverUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + receiver_username));
        Optional<User> senderUserOptional = userRepository.findByUsername(sender_username);
        User sender_user = senderUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + sender_username));

        Optional<Friends> requestOptional = friendsRepository
                        .getFriendsByUserId1AndUserId2(sender_user.getUserId(), receiver_user.getUserId());
        Friends request = requestOptional
                        .orElseThrow(() -> new UsernameNotFoundException("No such connection request was found"));
        Optional<Friends> connectionOptional = friendsRepository
                        .getConnection(sender_user.getUserId(), receiver_user.getUserId());
        if (connectionOptional.isPresent()) {
//            Boolean areConnected = areConnectedOptional.orElseThrow(() -> new UsernameNotFoundException("No connection " +
//                    "from : " + sender_username));
            throw new SpringTediException("Already connected!");
        }
        if (!requestOptional.isPresent())
            throw new SpringTediException("Request does not exist. Accept operation failed");
        // replace the request with the same id, but with accepted = true
        friendsRepository.deleteById(request.getId());
        friendsRepository.save(new Friends(request.getUser_id1(), request.getUser_id2(), true));
        //throw new UsernameNotFoundException("Request from " + sender_username + " to " + receiver_username + " was not found");

    }


    //    reject request
    // direction matters
    public void rejectRequest(String sender_username,String receiver_username ) {
        Optional<User> receiverUserOptional = userRepository.findByUsername(receiver_username);
        User receiver_user = receiverUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + receiver_username));
        Optional<User> senderUserOptional = userRepository.findByUsername(sender_username);
        User sender_user = senderUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + sender_username));

        Optional<Friends> requestOptional = friendsRepository
                .getFriendsByUserId1AndUserId2(sender_user.getUserId(), receiver_user.getUserId());
        Friends request = requestOptional
                .orElseThrow(() -> new UsernameNotFoundException("No connection request was found"));


        Optional<Boolean> areConnectedOptional = friendsRepository // maybe call connectionRequestExists()
                .CheckForRequest(sender_user.getUserId(), receiver_user.getUserId());
        if (areConnectedOptional.isPresent()) { // if request exists
            Boolean areConnected = areConnectedOptional.orElseThrow(() -> new UsernameNotFoundException("No connection " +
                    "from : " + sender_username));
            if (areConnected)
                throw new SpringTediException("Already connected!");


            friendsRepository.deleteById(request.getId());
            return;


        }
        throw new UsernameNotFoundException("Request from " + sender_username + " to " + receiver_username + " was not found");

    }


    public List<User> getUsersByQuery(String query) {
        Optional<List<User>> fetched_users_optional = userRepository.getUsersByQuery(query);
        if (!fetched_users_optional.isPresent())
            return new ArrayList<User>(); // empty set
        List<User> fetched_users = fetched_users_optional.orElseThrow(() -> new UsernameNotFoundException("No such connection request was found"));
        return fetched_users;
    }

    public FriendResponse areConnected(String username1, String username2) {
        Optional<User> receiverUserOptional = userRepository.findByUsername(username1);
        User user1 = receiverUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username1));
        Optional<User> senderUserOptional = userRepository.findByUsername(username2);
        User user2 = senderUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username2));

        Optional<Friends> areConnectedOptional = friendsRepository
                .getConnection(user1.getUserId(), user2.getUserId());
        if (!areConnectedOptional.isPresent()) { // if connection does not exist
                return new FriendResponse(false);
        } else
            return new FriendResponse(true);
    }


    // true if sender waits for the receiver to accept it
    // false if there has been no connection request
    // Just checks if there is a tuple {sender, receiver}  (DIRECTION MATTERS)
    public FriendResponse isPendingConnectionRequest(String username1, String username2) {
        Optional<User> senderUserOptional = userRepository.findByUsername(username1);
        User user1 = senderUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username1));
        Optional<User> receiverUserOptional = userRepository.findByUsername(username2);
        User user2 = receiverUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username2));
        // get a pending request from username1 to username2
        Optional<Friends> pendingRequestOptional = friendsRepository
                .getPendingRequest(user1.getUserId(), user2.getUserId());


        if (pendingRequestOptional.isPresent()) // if there is a pending request
            return new FriendResponse(true);
        else
            return new FriendResponse(false);
    }


    // removes connection or connection request between username1 and username2. Direction does not matter
    public void removeConnection(String username1, String username2) {
        Optional<User> receiverUserOptional = userRepository.findByUsername(username1);
        User user1 = receiverUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username1));
        Optional<User> senderUserOptional = userRepository.findByUsername(username2);
        User user2 = senderUserOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username2));
        Optional<Friends> requestOptional = friendsRepository
                .getExistingTuple(user1.getUserId(), user2.getUserId());
        if (!requestOptional.isPresent())
            throw new SpringTediException(String.format("There is no connection or connection request between %d and %d to be removed", user1.getUserId(),
                    user2.getUserId()));

        Friends request = requestOptional.orElseThrow(() -> new UsernameNotFoundException("No connection tuple " +
                "between : " + username1 + " and " + username2));
        friendsRepository.deleteById(request.getId()); // remove the connection from the DB
    }


    public List<User> getAllPendingRequestsSentToUser(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));
        List<User> returning_list = new ArrayList<>();
        List<String> username_list = new ArrayList<>();
        Optional<List<String>> L = friendsRepository.getAllPendingRequestsSentToUser(user.getUserId());
        username_list = L.orElseThrow(() -> new UsernameNotFoundException("List is empty"));
        for (String uname : username_list) {
            Optional<User> u_optional = userRepository.findByUsername(uname);
            User tempUser = u_optional
                    .orElseThrow(() -> new UsernameNotFoundException("No user " +
                            "Found with username : " + username));
            returning_list.add(tempUser);
        }

        return returning_list;
    }



    public User get_user_info(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));



        return user;
    }


    public String getUserImage(String username) {

        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));

        if ( user.getProfile_picture() != null){
            return user.getProfile_picture();
        }
        else
            return null;



    }
}