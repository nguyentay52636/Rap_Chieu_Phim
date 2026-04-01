package org.example.GUI.Components.FormAuth.ForgetPassword;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class CodeVerificationForm extends JFrame {

    private JTextField codeField;
    private JButton verifyButton;
    private String codeVerify;
    private String email;

    public CodeVerificationForm(String email, String codeVerify) {
        this.email = email;
        this.codeVerify = codeVerify;

        setTitle("Xác minh mã");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        getContentPane().setBackground(new Color(60, 63, 65));

        JLabel titleLabel = new JLabel("XÁC MINH MÃ OTP");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        JLabel codeLabel = new JLabel("Nhập mã OTP (6 số):");
        codeLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(codeLabel, gbc);

        codeField = new JTextField(6);
        codeField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        codeField.setBackground(new Color(43, 43, 43));
        codeField.setForeground(Color.WHITE);
        codeField.setCaretColor(Color.WHITE);
        gbc.gridx = 1;
        add(codeField, gbc);

        verifyButton = new JButton("Xác minh");
        verifyButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        verifyButton.setBackground(new Color(50, 120, 255));
        verifyButton.setForeground(Color.WHITE);
        verifyButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(verifyButton, gbc);

        verifyButton.addActionListener(e -> {
            String inputCode = codeField.getText().trim();

            if (inputCode.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (inputCode.equals(this.codeVerify)) {
                JOptionPane.showMessageDialog(this, "Mã xác thực chính xác!");
                new PasswordResetUI(this.email).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Mã OTP không đúng, vui lòng kiểm tra lại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}