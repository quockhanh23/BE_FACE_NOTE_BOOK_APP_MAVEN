//package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.component;
//
//import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Common;
//import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.config.RabbitMQConfig;
//import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.UserDTO;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class RabbitMQConsumer {
//
//    @RabbitHandler
//    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
//    public void processMessage(String message) {
//        ObjectMapper objectMapper = Common.intObjectMapper();
//        try {
//            UserDTO[] myObjects = objectMapper.readValue(message, UserDTO[].class);
//            for (UserDTO obj : myObjects) {
//                System.out.println(obj);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
