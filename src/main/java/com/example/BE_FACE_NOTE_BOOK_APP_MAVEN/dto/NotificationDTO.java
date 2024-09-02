package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private String title;
    private String status;
    private UserNotificationDTO idSendTo;
    private UserNotificationDTO idAction;
    private Long typeId;
    private String type;
    private Date createAt;

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", idSendTo=" + idSendTo +
                ", idAction=" + idAction +
                ", typeId=" + typeId +
                ", type='" + type + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}
