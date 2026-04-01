package org.example.GUI.Components.FormAuth;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import org.example.GUI.Application.Application;
import org.example.BUS.TaiKhoanBUS;
import org.example.DTO.TaiKhoanDTO;
import org.example.GUI.Components.FormAuth.ForgetPassword.CodeVerificationForm;
import org.example.Services.EmailServices;
import net.miginfocom.swing.MigLayout;

public class LoginForm extends JPanel {
    private JPanel Left;
    private JPanel Right;
    private JButton btnForgotPassword;
    private JButton btnTogglePassword;
    private JButton btnLogin;
    private JLabel lblTitle;
    private JLabel lblUser;
    private JLabel lblPassword;
    private JLabel lblLogo;
    private JLabel lblAppName;
    private JLabel lblFooter;
    private JPasswordField passwordField;
    private JTextField userField;
    
    private final TaiKhoanBUS taiKhoanBUS = new TaiKhoanBUS();
    private final org.example.DAO.TaiKhoanDAO taiKhoanDAO = new org.example.DAO.TaiKhoanDAO();

    public LoginForm() {
        initComponents();
    }

    private void initComponents() {
        setBackground(new Color(240, 242, 245));
        setLayout(new MigLayout("fill, al center center", "[grow]", "[grow]"));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JPanel container = new JPanel(new MigLayout("ins 0", "[400px][400px]", "[500px]"));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));

        Left = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 30, 60), 0, getHeight(),
                        new Color(120, 40, 120));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        Left.setLayout(new MigLayout("fill, al center center", "[grow]", "push[]50[]30[]push"));
        Left.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        try {
            java.net.URL logoUrl = getClass().getResource("/org/example/GUI/menu/logo/logojavawing.png");
            if (logoUrl != null) {
                lblLogo = new JLabel(new ImageIcon(logoUrl));
                lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
                Left.add(lblLogo, "center, wrap");
            }
        } catch (Exception e) {}

        lblAppName = new JLabel("ĐẶT VÉ XEM PHIM");
        lblAppName.setFont(new Font("Showcard Gothic", Font.BOLD, 24));
        lblAppName.setForeground(Color.WHITE);
        lblAppName.setHorizontalAlignment(SwingConstants.CENTER);
        Left.add(lblAppName, "center, wrap");

        lblFooter = new JLabel("Trải nghiệm rạp phim hiện đại");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFooter.setForeground(new Color(204, 204, 204));
        lblFooter.setHorizontalAlignment(SwingConstants.CENTER);
        Left.add(lblFooter, "center");

        // Right Panel
        Right = new JPanel(new MigLayout("fill, al center center, wrap 1", "[grow]", "[]30[]15[]15[]20[]10[]"));
        Right.setBackground(Color.WHITE);

        lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(0, 102, 102));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        Right.add(lblTitle, "center");

        // User Field
        JPanel userPanel = new JPanel(new MigLayout("fill", "[]10[grow]", "[]"));
        userPanel.setBackground(Color.WHITE);
        lblUser = new JLabel("Tên đăng nhập:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUser.setForeground(new Color(66, 80, 102));
        userPanel.add(lblUser);

        userField = new JTextField();
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        userField.setBackground(Color.WHITE);
        userField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocusInWindow();
                }
            }
        });
        userPanel.add(userField, "growx");
        Right.add(userPanel, "width 320px, center");

        // Password Field
        JPanel passwordPanel = new JPanel(new MigLayout("fill", "[]10[grow]", "[]"));
        passwordPanel.setBackground(Color.WHITE);
        lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setForeground(new Color(66, 80, 102));
        passwordPanel.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setLayout(new BorderLayout());
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 5)));
        passwordField.setBackground(Color.WHITE);
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnLogin.doClick();
                }
            }
        });
        
        btnTogglePassword = new JButton();
        btnTogglePassword.setPreferredSize(new Dimension(20, 20));
        btnTogglePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTogglePassword.setBorderPainted(false);
        btnTogglePassword.setContentAreaFilled(false);

        try {
            java.net.URL urlCLOSED = getClass().getResource("/org/example/GUI/resources/images/hide.png");
            java.net.URL urlOPEN = getClass().getResource("/org/example/GUI/resources/images/view1.png");
            if (urlCLOSED != null && urlOPEN != null) {
                ImageIcon iconClosed = new ImageIcon(new ImageIcon(urlCLOSED).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
                ImageIcon iconOpen = new ImageIcon(new ImageIcon(urlOPEN).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
                btnTogglePassword.setIcon(iconClosed);
                btnTogglePassword.addActionListener(e -> {
                    if (passwordField.getEchoChar() != (char) 0) {
                        passwordField.setEchoChar((char) 0);
                        btnTogglePassword.setIcon(iconOpen);
                    } else {
                        passwordField.setEchoChar('•');
                        btnTogglePassword.setIcon(iconClosed);
                    }
                });
            }
        } catch (Exception e) {}
        
        passwordField.add(btnTogglePassword, BorderLayout.EAST);
        passwordPanel.add(passwordField, "growx");
        Right.add(passwordPanel, "width 320px, center");

        // Login Button
        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setBackground(new Color(0, 102, 102));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(0, 45));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnLogin.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = String.valueOf(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            TaiKhoanDTO tk = taiKhoanBUS.login(username, password);
            if (tk != null) {
                JOptionPane.showMessageDialog(this, "✅ Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                Application.login();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Tên đăng nhập hoặc mật khẩu kô chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        Right.add(btnLogin, "width 320px, center, gaptop 20");

        btnForgotPassword = new JButton("Quên mật khẩu?");
        btnForgotPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnForgotPassword.setForeground(new Color(0, 102, 255));
        btnForgotPassword.setBorderPainted(false);
        btnForgotPassword.setContentAreaFilled(false);
        btnForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnForgotPassword.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(this, "Nhập tên tài khoản của bạn:", "Xác minh tài khoản", JOptionPane.QUESTION_MESSAGE);
            
            if (username == null || username.trim().isEmpty()) {
                return;
            }

            TaiKhoanDTO tk = taiKhoanDAO.getTaiKhoanByUsername(username.trim());
            
            if (tk == null) {
                JOptionPane.showMessageDialog(this, "❌ Tài khoản không tồn tại trên hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!tk.getVaiTro().equalsIgnoreCase("Admin")) {
                JOptionPane.showMessageDialog(this, "⚠️ Bạn không có quyền Admin. Liên hệ quản lý để được hỗ trợ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Hiển thị thông báo đang gửi
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            
            String adminEmail = "ngoctrinhcute52636@gmail.com";
            String code = EmailServices.generateCode();
            String subject = "Mã OTP khôi phục mật khẩu hệ thống - " + username;
            String htmlPath = "src/main/java/org/example/Services/email-content.html";
            
            // Chạy trong luồng riêng để không bị đơ GUI
            new Thread(() -> {
                boolean Success = EmailServices.sendEmail(adminEmail, subject, htmlPath, code);
                
                SwingUtilities.invokeLater(() -> {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    if (Success) {
                        JOptionPane.showMessageDialog(this, "✅ Đã gửi mã OTP tới Email Admin!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        new CodeVerificationForm(username.trim(), code).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "❌ Lỗi khi gửi Email. Kiểm tra cấu hình!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }).start();
        });
        Right.add(btnForgotPassword, "center");

        container.add(Left, "grow");
        container.add(Right, "grow");
        add(container, "center");
    }
}