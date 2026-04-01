package org.example.GUI.Components.FormAuth.ForgetPassword;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import org.example.BUS.TaiKhoanBUS;

public class PasswordResetUI extends JFrame {

    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton resetButton;
    private String email;

    public PasswordResetUI(String email) {
        this.email = email;
        setTitle("Lấy lại mật khẩu");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Chỉnh background thành màu trắng
        getContentPane().setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("ĐẶT LẠI MẬT KHẨU");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0, 102, 102)); // Màu tiêu đề dark teal
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        JLabel newPasswordLabel = new JLabel("Mật khẩu mới:");
        newPasswordLabel.setForeground(Color.BLACK);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(newPasswordLabel, gbc);

        newPasswordField = new JPasswordField(20);
        newPasswordField.setBackground(new Color(245, 245, 245));
        newPasswordField.setForeground(Color.BLACK);
        newPasswordField.setCaretColor(Color.BLACK);
        gbc.gridx = 1;
        add(newPasswordField, gbc);

        JLabel confirmPasswordLabel = new JLabel("Xác nhận mật khẩu:");
        confirmPasswordLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setBackground(new Color(245, 245, 245));
        confirmPasswordField.setForeground(Color.BLACK);
        confirmPasswordField.setCaretColor(Color.BLACK);
        gbc.gridx = 1;
        add(confirmPasswordField, gbc);

        resetButton = new JButton("Cập nhật mật khẩu");
        resetButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resetButton.setBackground(new Color(0, 102, 102));
        resetButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(resetButton, gbc);

        resetButton.addActionListener(e -> {
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TaiKhoanBUS accountBUS = new TaiKhoanBUS();
            boolean isUpdated = accountBUS.updatePassword(this.email, newPassword);
            if (isUpdated) {
                // Log ra system.out theo yêu cầu
                System.out.println("✅ Mật khẩu của tài khoản [" + this.email + "] đã được cập nhật thành công.");
                
                JOptionPane.showMessageDialog(this, "Mật khẩu đã được đặt lại thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Đóng form sau khi thành công
            } else {
                JOptionPane.showMessageDialog(this, "Đã có lỗi xảy ra khi cập nhật mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}