package com.example.tedi_app.controller;

import com.example.tedi_app.dto.*;
import com.example.tedi_app.model.PublicButton;
import com.example.tedi_app.model.Action;

import com.example.tedi_app.model.User;
import com.example.tedi_app.repo.PersonalinfoRepository;
import com.example.tedi_app.repo.UserRepository;
import com.example.tedi_app.service.*;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserDetailsServiceImpl userService;
    private final AuthService authService;
    private UserRepository userRepository;
    private PersonalinfoRepository personalinfoRepository;
    private PersonalinfoService personalinfoService;
    private PublicButtonService publicButtonService;
    private final ActionsService actionsService;
    private final UserProfileService userProfileService;
    private final ImageStoreService imageStoreService;
    public static final String Directory = System.getProperty("user.home") + "/Downloads/uploads/";



    @GetMapping("/get_all_users")
    public ResponseEntity<List<User>> getAllUsers() {
        return status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @PostMapping("/get_all_detailed_users")
    public ResponseEntity<List<DetailedUser>> getAllDetailedUsers(@RequestBody String[] usernames) {
        System.out.println("First user = " + usernames[0]);
        return status(HttpStatus.OK).body(userService.getAllDetailedUsers(usernames));
    }

    @PostMapping("/get_all_detailed_users_to_xml")
    public ResponseEntity<List<DetailedUser>> getAllDetailedUsersToXML(@RequestBody String[] usernames) {
        List<DetailedUser> L = userService.getAllDetailedUsers(usernames);

        System.out.println("First user = " + usernames[0]);
        return status(HttpStatus.OK).body(userService.getAllDetailedUsers(usernames));
    }



    @GetMapping("/{username}")
    public ResponseEntity <List<User>> getAllConnectedUsers(@PathVariable String username){
        return status(HttpStatus.OK).body(userService.get_all_connected_users(username));
    }

    @GetMapping("/get_user_by_id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return status(HttpStatus.OK).body(userService.getUserById(id));
    }
//    make request for friend connection
    @PostMapping("/connect_request")
    public ResponseEntity<Void> makeConnectionRequest(@RequestBody FriendRequest friendRequest) {
        this.userService.makeConnectionRequest(friendRequest.getSender_username(), friendRequest.getReceiver_username());
        System.out.println("==makeConnectioRequest just executed\n");
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // Remember: sender of the request, has sent friendRequest = {sender_username, receiver_username}
    // Receiver user will also have to send friendRequest = { sender_username, receiver_username}
    @PostMapping("/accept_connection_request")
    public ResponseEntity<Void> acceptRequest(@RequestBody FriendRequest friendRequest) {
        this.userService.acceptRequest(friendRequest.getSender_username(), friendRequest.getReceiver_username());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Remember: sender of the request, has sent friendRequest = {sender_username, receiver_username}
    // Receiver user will also send friendRequest = { sender_username, receiver_username}
    @PostMapping("/reject_connection_request")
    public ResponseEntity<Void> rejectRequest(@RequestBody FriendRequest friendRequest) {
        this.userService.rejectRequest(friendRequest.getSender_username(), friendRequest.getReceiver_username());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<List<User>> getSearchResults(@PathVariable String query){

        return status(HttpStatus.OK).body(this.userService.getUsersByQuery(query));
    }

    // checks if users are connected. Direction does not matter {sender, receiver} is same with {receiver, sender}
    @PostMapping("/are_connected")
    public ResponseEntity<FriendResponse> areConnected(@RequestBody FriendRequest friendRequest) {
        return  status(HttpStatus.OK).body(this.userService.areConnected(friendRequest.getSender_username()
                                                    , friendRequest.getReceiver_username()));
    }

    // checks if a request exists (direction matters)
    @PostMapping("/pending_request")
    public ResponseEntity<FriendResponse> isPendingConnectionRequest(@RequestBody FriendRequest friendRequest) {
        return  status(HttpStatus.OK).body(this.userService.isPendingConnectionRequest(friendRequest.getSender_username()
                , friendRequest.getReceiver_username()));
    }

    // removes connection or connection request. Direction does not matter
    // This function removes the tuple {sender, receiver} or {receiver, sender}
    @PostMapping("/remove_connection")
    public ResponseEntity<Void> removeConnection(@RequestBody FriendRequest friendRequest) {
        this.userService.removeConnection(friendRequest.getSender_username()
                , friendRequest.getReceiver_username());
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get_all_pending_requests_sent_to_user/{username}")
    public ResponseEntity<List<User>> getAllPendingRequestsSentToUser(@PathVariable String username) {
        return status(HttpStatus.OK).body(this.userService.getAllPendingRequestsSentToUser(username));
    }

//    Personal Info (Work exp , Studies , Abilities)

    @PostMapping("/changepersonalinfo")
    public ResponseEntity<String> ChangePersonalInfo(@RequestBody ChangePersonInfoRequest changePersonInfoRequest){

        personalinfoService.changePersonalInfo(changePersonInfoRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }




    

//    buttons ---- Change / Fetch State
    @PostMapping("/changebuttonstate")
    public ResponseEntity<String> ChangeButtonState(@RequestBody ChangeButtonStateRequest changeButtonStateRequest){

        publicButtonService.changeButtonState(changeButtonStateRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }


    @GetMapping("/buttonstate/{username}")
    public ResponseEntity <PublicButton> getButtonState(@PathVariable String username){
        return status(HttpStatus.OK).body(publicButtonService.get_button_state(username));
    }




// ---------------------------------------------------------------------------------------------------------------------
    /// User info

    @PostMapping("/changeinfo")
    public ResponseEntity<String> ChangeInfo(@RequestBody ChangeInfoRequest changeInfoRequest){

        if(this.userRepository.findByEmail(changeInfoRequest.getEmail()).isPresent()){
            return new ResponseEntity<>("email exists", HttpStatus.BAD_REQUEST);
        }

        authService.changeInfo(changeInfoRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }


    @GetMapping("/userInfo/{username}")
    public ResponseEntity<User> getUserInfo(@PathVariable String username){
        return status(HttpStatus.OK).body(userService.get_user_info(username));
    }

    // Actions - notifications
    @GetMapping("/get_notifications/{username}")
    public ResponseEntity<List<ActionResponse>> getAllUserNotifications(@PathVariable String username) {
        return status(HttpStatus.OK).body(actionsService.getAllNotificationsByUsername(username));
    }



    @GetMapping("/userProfile/{username}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable String username){
        return status(HttpStatus.OK).body(userProfileService.getUserProfileInfo(username));
    }


    @PostMapping("/changeProfileImage")
    public ResponseEntity<String> changeProfileImage(@RequestPart("username") String username,
                                                     @RequestPart("profileImage") MultipartFile profileImage){

        ChangeProfileImageRequest changeProfileImageRequest = new ChangeProfileImageRequest();
        changeProfileImageRequest.setUsername(username);
        changeProfileImageRequest.setMultipartFile(profileImage);

        personalinfoService.changeProfileImage(changeProfileImageRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }


    @GetMapping(path = "/getUserImage/{username}")
    public ResponseEntity<ImageResponse> userImage(@PathVariable String username){

        String image_name = userService.getUserImage(username);
        System.out.println("photo of "+ username + " name is " + image_name);
        if (image_name != null){
            String image = imageStoreService.retrieve_img(image_name);
            ImageResponse img = new ImageResponse(image);
            System.out.println(image);
            return status(HttpStatus.OK).body(img);
        }
        else
            return status(HttpStatus.OK).body(null);
    }


}


