package com.example.tedi_app.repo;

import com.example.tedi_app.model.Friends;
import com.example.tedi_app.model.ListOfFriends;
import com.example.tedi_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<Friends,Long> {
//    Optional<User> findByUsername(String username);
//    Optional<User> findByEmail(String email);

    //@Query(value = "SELECT c from User c")
    //List<User> getAllUsers();

    @Query(value = "select id,user_id1,user_id2 from Friends where user_id1 = :param OR user_id2 = :param",nativeQuery = true)
    List<Friends> getAllConnectedUsers(@Param("param") Long user_id);

     //@Query( "select u.user_id2 from Friends u where u.user_id1 = ?1 union  select u.user_id1 from Friends u where u.user_id2 = ?1 ")
    //List<Integer> getAllFriends(Integer user_id);
}



//@Query(select u1 where u1 == ?1)
//findConnected(Integer id);
