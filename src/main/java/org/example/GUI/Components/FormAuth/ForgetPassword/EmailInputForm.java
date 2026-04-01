package org.example.GUI.Components.FormAuth.ForgetPassword;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import org.example.Services.EmailServices;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class EmailInputForm extends JFrame {

    private JTextField emailField;
    private JButton sendButton;

    public EmailInputForm() {
        setTitle("Quên mật khẩu");
        setSize(450, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("BẠN QUÊN MẬT KHẨU?");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        JLabel emailLabel = new JLabel("Nhập Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(emailLabel, gbc);

        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setBackground(new Color(43, 43, 43));
        emailField.setForeground(Color.WHITE);
        emailField.setCaretColor(Color.WHITE);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(emailField, gbc);

        sendButton = new JButton("Gửi mã xác thực");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(new Color(50, 120, 255));
        sendButton.setForeground(Color.WHITE);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        add(sendButton, gbc);

        sendButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập email!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Gửi Email và tạo mã
            String code = EmailServices.generateCode();
            String subject = "Mã xác thực lấy lại mật khẩu";
            String htmlPath = "src/main/java/org/example/Services/email-content.html";

            boolean Success = EmailServices.sendEmail(email, subject, htmlPath, code);

            if (Success) {
                JOptionPane.showMessageDialog(this, "Mã xác thực đã được gửi đến " + email + "!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                // Mở form xác minh mã
                new CodeVerificationForm(email, code).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi gửi email. Vui lòng kiểm tra lại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

}
