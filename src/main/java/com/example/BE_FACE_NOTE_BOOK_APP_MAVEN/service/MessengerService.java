package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.MessengerDTO2;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Conversation;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Messenger;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessengerService extends GeneralService<Messenger> {

    List<Messenger> findAllByConversationOrderById(Conversation conversation);

    void delete(Messenger entity);

    void saveAll(List<Messenger> messengers);

    List<Messenger> findImageByConversation(@Param("idConversation") Long idConversation);

    List<Messenger> findAllByConversation_IdAndContentNotNullOrderByIdDesc(Long conversation_id);

    List<Messenger> findAllByConversation_Id(Long id);

    Messenger lastMessage(@Param("idConversation") Long idConversation);

    Messenger createDefaultMessage(Messenger messenger, User user, String type, String status);

    Object lastTimeMessage(@Param("idConversation") Long idConversation);

    List<Messenger> findAllByConversation_IdIn(List<Long> conversation_id);

    List<Messenger> searchMessage(@Param("searchText") String searchText, @Param("idConversation") Long idConversation);

    List<MessengerDTO2> testConverterJPA(@Param("idConversation") Long idConversation);
}
