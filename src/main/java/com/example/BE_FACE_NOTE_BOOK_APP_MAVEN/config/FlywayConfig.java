//package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.config;
//
//import org.flywaydb.core.Flyway;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class FlywayConfig {
//
//    @Value("${spring.flyway.locations}")
//    private String[] locations;
//
//    @Value("${spring.datasource.url}")
//    private String datasource;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//    @Bean
//    public Flyway flyway() {
//        Flyway flyway = Flyway.configure().dataSource(dataSource()).locations(locations).baselineOnMigrate(true).load();
//        flyway.migrate();
//        return flyway;
//    }
//
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setUrl(datasource);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        return dataSource;
//    }
//}
