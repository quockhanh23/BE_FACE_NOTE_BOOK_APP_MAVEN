//package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.component;
//
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.stereotype.Service;
//
//@Service
//public class KafkaConsumerService {
//
//    @KafkaListener(topics = "my-topic", groupId = "my-consumer-group")
//    public void listen(String message, Acknowledgment ack) {
//        ack.acknowledge();
//        System.out.println("Received Message in group my-consumer-group: " + message);
//    }
//}
