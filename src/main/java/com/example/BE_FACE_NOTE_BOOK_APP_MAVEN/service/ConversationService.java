package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Conversation;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConversationService extends GeneralService<Conversation> {

    Iterable<Conversation> findAll();

    List<Conversation> findAllByIdSender(Long idSender);

    List<Conversation> listConversationByIdUserNotFriend(@Param("idUser") Long idUser);

    List<Conversation> getConversationBySenderIdOrReceiverId(@Param("senderId") Long senderId,
                                                             @Param("receiverId") Long receiverId);

    void delete(Conversation entity);

    boolean existsConversationsById(Long idConversation);
}
