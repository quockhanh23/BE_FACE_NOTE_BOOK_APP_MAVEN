package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.notification;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ResponseNotification {
    private LocalDateTime time = LocalDateTime.now();
    private String statusCode;
    private String message;
    private String description;

    private static ResponseNotification responseNotificationNotFound = new ResponseNotification
            (HttpStatus.NOT_FOUND.toString(), MessageResponse.ID_NOT_FOUND, MessageResponse.DESCRIPTION);
    private static ResponseNotification responseNotificationNoContent = new ResponseNotification
            (HttpStatus.BAD_REQUEST.toString(), MessageResponse.LIST_BLANK, MessageResponse.DESCRIPTION);
    private static ResponseNotification responseNotificationBadRequest = new ResponseNotification
            (HttpStatus.BAD_REQUEST.toString(), MessageResponse.WRONG_SOME_THING, MessageResponse.DESCRIPTION);

    public ResponseNotification(String status, String message, String description) {
        this.description = description;
        this.statusCode = status;
        this.message = message;
    }

    public ResponseNotification(String status, String message) {
        this.statusCode = status;
        this.message = message;
    }

    public ResponseNotification(String message) {
        this.message = message;
    }

    public static List<ResponseNotification> responseMessageList() {
        List<ResponseNotification> responseNotifications = new ArrayList<>();
        ResponseNotification responseNotification = new ResponseNotification(HttpStatus.NOT_FOUND.toString(),
                MessageResponse.LIST_BLANK, MessageResponse.DESCRIPTION2);
        responseNotifications.add(responseNotification);
        return responseNotifications;
    }

    public static ResponseNotification responseMessage(String typeId, Long id) {
        responseNotificationNotFound.setMessage(MessageResponse.ID_NOT_FOUND + typeId + ": " + id);
        responseNotificationNotFound.setDescription(MessageResponse.DESCRIPTION2);
        return responseNotificationNotFound;
    }

    public static ResponseNotification responseMessage(String dataField) {
        responseNotificationBadRequest.setMessage(MessageResponse.WRONG_SOME_THING);
        responseNotificationBadRequest.setDescription(MessageResponse.DESCRIPTION_REQUEST + dataField);
        return responseNotificationBadRequest;
    }

    public static ResponseNotification responseMessageDataField(String dataField) {
        responseNotificationBadRequest.setMessage(MessageResponse.WRONG_SOME_THING);
        responseNotificationBadRequest.setDescription(MessageResponse.DESCRIPTION_BLANK + dataField);
        return responseNotificationBadRequest;
    }

    public static ResponseNotification responseMessage() {
        return responseNotificationNoContent;
    }
}
