package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.config;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.component.RequestLimitingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class AppContextConfig {
    @Bean
    public MappedInterceptor myInterceptor() {
        return new MappedInterceptor(null, new RequestLimitingInterceptor());
    }
}
