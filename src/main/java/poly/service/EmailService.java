package poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nhanhtpy00134@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendSharedArticle(String to, String articleTitle, String articleUrl, String sharedBy) {
        String subject = "Bài viết được chia sẻ: " + articleTitle;
        String text = String.format(
            "Xin chào,\n\n" +
            "%s đã chia sẻ bài viết sau với bạn:\n\n" +
            "Tiêu đề: %s\n" +
            "Link bài viết: %s\n\n" +
            "Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!\n" +
            "Trân trọng,\n" +
            "Đội ngũ DATN",
            sharedBy, articleTitle, articleUrl
        );
        sendSimpleMessage(to, subject, text);
    }
} 