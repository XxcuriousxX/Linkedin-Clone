package com.example.tedi_app.repo;
import com.example.tedi_app.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessagesRepository extends JpaRepository<Message,Long> {

    @Query( value = "select * from Message where (sender_id = :id1 and receiver_id = :id2) or (sender_id = :id2 and receiver_id = :id1)",nativeQuery = true)
    Optional<List<Message>> getConversation(@Param("id1") Long id1, @Param("id2") Long id2);

    @Query( value = "select * from Message where time_created > :time", nativeQuery = true)
    List<Message> loadMessagesAfterDate(@Param("time") Instant time_created);

    @Query( value = "select * from Message where sender_id = :myid or receiver_id = :myid"
            , nativeQuery = true)
    List<Message> get_open_conversations(@Param("myid") Long myid);

    @Query(value = "select * from Message where (sender_id = :param OR receiver_id = :param)",nativeQuery = true)
    List<Message> getAllConnectedUsersMessage(@Param("param") Long user_id);
}
