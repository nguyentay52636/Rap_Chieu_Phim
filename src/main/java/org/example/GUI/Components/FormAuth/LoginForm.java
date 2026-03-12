package org.example.GUI.Components.FormAuth;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.example.GUI.Application.Application;

import net.miginfocom.swing.MigLayout;

public class LoginForm extends JPanel {
    private JPanel Left;
    private JPanel Right;
    private JButton btnForgotPassword;
    private JButton btnLogin;
    private JButton btnSignUp;
    private JLabel lblTitle;
    private JLabel lblEmail;
    private JLabel lblPassword;
    private JLabel lblLogo;
    private JLabel lblAppName;
    private JLabel lblFooter;
    private JPasswordField passwordField;
    private JTextField emailField;

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
                // Gradient mang tông màu rạp phim
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 30, 60), 0, getHeight(),
                        new Color(120, 40, 120));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        Left.setLayout(new MigLayout("fill, al center center", "[grow]", "push[]50[]30[]push"));
        Left.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        lblLogo = new JLabel(new ImageIcon(getClass().getResource("/org/example/GUI/menu/logo/logojavawing.png")));
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        Left.add(lblLogo, "center, wrap");

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

        // Right Panel (Form đăng nhập đặt vé xem phim)
        Right = new JPanel(new MigLayout("fill, al center center, wrap 1", "[grow]", "[]30[]15[]15[]20[]10[]"));
        Right.setBackground(Color.WHITE);

        lblTitle = new JLabel("ĐĂNG NHẬP ĐẶT VÉ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(new Color(0, 102, 102));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        Right.add(lblTitle, "center");

        // Email Field
        JPanel emailPanel = new JPanel(new MigLayout("fill", "[]10[grow]", "[]")); // Sửa 'g娶ow' thành 'grow'
        emailPanel.setBackground(Color.WHITE);
        lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEmail.setForeground(new Color(66, 80, 102));
        emailPanel.add(lblEmail);

        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        emailField.setBackground(Color.WHITE);
        emailField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnLogin.doClick();
                }
            }
        });
        emailPanel.add(emailField, "growx");
        Right.add(emailPanel, "width 300px, center");

        // Password Field
        JPanel passwordPanel = new JPanel(new MigLayout("fill", "[]10[grow]", "[]")); // Sửa 'g娶ow' thành 'grow'
        passwordPanel.setBackground(Color.WHITE);
        lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setForeground(new Color(66, 80, 102));
        passwordPanel.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        passwordField.setBackground(Color.WHITE);
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnLogin.doClick();
                }
            }
        });
        passwordPanel.add(passwordField, "growx");
        Right.add(passwordPanel, "width 300px, center");

        btnLogin = new JButton("Đăng nhập & vào rạp");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(0, 102, 102));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(0, 153, 153));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(0, 102, 102));
            }
        });
        // Bấm đăng nhập: chuyển sang màn hình chính Application
        btnLogin.addActionListener(e -> Application.login());
        Right.add(btnLogin, "center");

        btnForgotPassword = new JButton("Quên mật khẩu?");
        btnForgotPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnForgotPassword.setForeground(new Color(0, 102, 255));
        btnForgotPassword.setBorderPainted(false);
        btnForgotPassword.setContentAreaFilled(false);
        btnForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnForgotPassword.addActionListener(e -> {
        });
        Right.add(btnForgotPassword, "center");

 

        container.add(Left, "grow");
        container.add(Right, "grow");
        add(container, "center");
    }
}