# Sử dụng image Java
FROM amazoncorretto:21.0.4

# Đặt thư mục làm việc
WORKDIR /app

# Sao chép tệp JAR vào container
COPY target/BE_FACE_NOTE_BOOK_APP_MAVEN-0.0.1-SNAPSHOT.jar app.jar

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
