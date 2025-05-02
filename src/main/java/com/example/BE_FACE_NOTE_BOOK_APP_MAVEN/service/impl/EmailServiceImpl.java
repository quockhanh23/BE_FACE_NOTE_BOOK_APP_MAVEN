//package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;
//
//import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.EmailContent;
//import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.object.AvatarDefault;
//import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.EmailService;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//
//import java.nio.charset.StandardCharsets;
//
//@Service
//public class EmailServiceImpl implements EmailService {
//
//    private final JavaMailSender emailSender;
//
//    @Autowired
//    public EmailServiceImpl(JavaMailSender emailSender) {
//        this.emailSender = emailSender;
//    }
//
//    @Value("${spring.mail.username}")
//    private String mailSender;
//
//    @Override
//    public void sendMail(String email, String title, String content) {
//        try {
//            MimeMessage message = emailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(
//                    message,
//                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
//                    StandardCharsets.UTF_8.name());
//            helper.setFrom(mailSender);
//            helper.setTo(email);
//            helper.setSubject(title);
//            helper.setText(content);
//            emailSender.send(message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void sendEmailWelCome(EmailContent emailContent) {
//        AvatarDefault avatarDefault = new AvatarDefault();
//        String template = "<!DOCTYPE html>\n" +
//                "<html lang=\"vi\">\n" +
//                "<head>\n" +
//                "    <meta charset=\"UTF-8\">\n" +
//                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
//                "    <title>Chào mừng bạn đến với Face notebook</title>\n" +
//                "    <style>\n" +
//                "        body {\n" +
//                "            font-family: Arial, sans-serif;\n" +
//                "            background-color: #f4f4f4;\n" +
//                "            margin: 0;\n" +
//                "            padding: 20px;\n" +
//                "        }\n" +
//                "        .container {\n" +
//                "            background-color: #ffffff;\n" +
//                "            padding: 20px;\n" +
//                "            border-radius: 5px;\n" +
//                "            box-shadow: 0 2px 10px rgba(0,0,0,0.1);\n" +
//                "            max-width: 600px;\n" +
//                "            margin: auto;\n" +
//                "        }\n" +
//                "        h1 {\n" +
//                "            color: #333;\n" +
//                "        }\n" +
//                "        p {\n" +
//                "            color: #555;\n" +
//                "        }\n" +
//                "        a {\n" +
//                "            color: #007BFF;\n" +
//                "            text-decoration: none;\n" +
//                "        }\n" +
//                "        .footer {\n" +
//                "            margin-top: 20px;\n" +
//                "            font-size: 12px;\n" +
//                "            color: #aaa;\n" +
//                "        }\n" +
//                "    </style>\n" +
//                "</head>\n" +
//                "<body>\n" +
//                "    <div class=\"container\">\n" +
//                "        <h1>Chào mừng bạn đến với Face notebook!</h1>\n" +
//                "        <p>Kính gửi " + emailContent.getUsername() + ",</p>\n" +
//                "        <p>Chúng tôi rất vui mừng thông báo rằng bạn đã tạo tài khoản thành công tại Face notebook! \uD83C\uDF89</p>\n" +
//                "        <p>Nếu bạn có bất kỳ câu hỏi nào hoặc cần hỗ trợ, đừng ngần ngại liên hệ với chúng tôi qua email này hoặc truy cập vào trang hỗ trợ của chúng tôi.</p>\n" +
//                "        <p>Chúc bạn có những trải nghiệm tuyệt vời cùng Face notebook!</p>\n" +
//                "        <p class=\"footer\">Trân trọng,<br>Đội ngũ Face notebook</p>\n" +
//                "    </div>\n" +
//                "<img src=\"" + avatarDefault.getDEFAULT_AVATAR_1() + "\" alt=\"Image Description\" />\n" +
//                "</body>\n" +
//                "</html>\n";
//
//        try {
//            MimeMessage message = emailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(
//                    message,
//                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
//                    StandardCharsets.UTF_8.name());
//            helper.setFrom(mailSender);
//            helper.setTo(emailContent.getEmail());
//            helper.setSubject(emailContent.getTitle());
//            helper.setText(template, true);
//            emailSender.send(message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
