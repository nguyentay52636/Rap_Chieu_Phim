package org.example.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailServices {
    static final String from = "ngoctrinhcute52636@gmail.com";
    static final String password = "yyeh numm gxrh qgue";
    private static final SecureRandom random = new SecureRandom();

    public static String generateCode() {
        int code = 100000 + random.nextInt(900000);
        return Integer.toString(code);
    }

    public static boolean sendEmail(String to, String subject, String htmlFilePath, String code) {
        // Kiểm tra địa chỉ email người nhận
        if (!isValidEmailAddress(to)) {
            System.out.println("Địa chỉ email người nhận không hợp lệ.");
            return false;
        }

        // Đọc nội dung HTML từ file
        String htmlContent;
        try {
            htmlContent = new String(Files.readAllBytes(Paths.get(htmlFilePath)));
        } catch (IOException e) {
            System.out.println("Không thể đọc file HTML.");
            e.printStackTrace();
            return false;
        }
        htmlContent = htmlContent.replace("${code}", code);

        // Properties : khai báo các thuộc tính
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP HOST
        props.put("mail.smtp.port", "587"); // TLS 587 SSL 465
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Tạo Authenticator
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };

        // Phiên làm việc
        Session session = Session.getInstance(props, auth);

        // Tạo một tin nhắn
        MimeMessage msg = new MimeMessage(session);

        try {
            // Kiểu nội dung
            msg.setContent(htmlContent, "text/html; charset=UTF-8");

            // Người gửi
            msg.setFrom(new InternetAddress(from));

            // Người nhận
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

            // Tiêu đề email
            msg.setSubject(subject);

            // Quy định ngày gửi
            msg.setSentDate(new Date());

            // Gửi email
            Transport.send(msg);
            System.out.println("Gửi email thành công");
            return true;
        } catch (Exception e) {
            System.out.println("Gặp lỗi trong quá trình gửi email");
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isValidEmailAddress(String email) {
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String recipient = "ngoctrinhcute52636@gmail.com";
        String subject = "Kiểm tra hệ thống gửi mã xác nhận";
        String htmlFilePath = "src/main/java/org/example/Services/email-content.html";
        String code = generateCode();

        boolean result = sendEmail(recipient, subject, htmlFilePath, code);
        if (result) {
            System.out.println("Kiểm tra gửi email thành công!");
        } else {
            System.out.println("Kiểm tra gửi email thất bại!");
        }
    }
}