package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Modifying
    @Query(value = "select * from conversation where sender_id= :idUser or receiver_id= :idUser", nativeQuery = true)
    List<Conversation> findAllByIdSender(@Param("idUser") Long idUser);

    @Modifying
    @Query(value = "select *\n" +
            "from conversation\n" +
            "where (sender_id not in\n" +
            "      (select id_friend from friend_relation where id_user = :idUser and friend_relation.status_friend = 'Friend')\n" +
            "  and receiver_id not in\n" +
            "      (select id_friend from friend_relation where id_user = :idUser and friend_relation.status_friend = 'Friend'))\n" +
            "and (sender_id = :idUser or receiver_id = :idUser)", nativeQuery = true)
    List<Conversation> listConversationByIdUserNotFriend(@Param("idUser") Long idUser);

    @Modifying
    @Query(value = "select * from conversation where receiver_id in (:senderId, :receiverId) or sender_id in (:senderId, :receiverId);", nativeQuery = true)
    List<Conversation> getConversationBySenderIdOrReceiverId(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    void delete(Conversation entity);

    boolean existsConversationsById(Long idConversation);
}
