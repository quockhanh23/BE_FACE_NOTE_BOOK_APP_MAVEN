//package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.component;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RedisBaseService {
//
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
//
//    // Lưu giá trị vào Redis với key và value
//    public void set(String key, String value) {
//        redisTemplate.opsForValue().set(key, value);
//    }
//
//    // Thêm giá trị vào đầu danh sách
//    public void leftPush(String key, String value) {
//        redisTemplate.opsForList().leftPush(key, value);
//    }
//
//    // Thêm giá trị vào cuối danh sách
//    public void rightPush(String key, String value) {
//        redisTemplate.opsForList().rightPush(key, value);
//    }
//
//    // Lấy giá trị từ Redis với key
//    public Object getObjectByKey(String key) {
//        return redisTemplate.opsForValue().get(key);
//    }
//
//    // Xóa key khỏi Redis
//    public Boolean delete(String key) {
//        return redisTemplate.delete(key);
//    }
//}
