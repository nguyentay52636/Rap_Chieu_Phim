package org.example.GUI.Components.FormAuth;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;


import javax.swing.*;

import org.example.GUI.Application.Application;

import net.miginfocom.swing.MigLayout;

public class LoginForm extends JPanel {
    private JPanel Left;
    private JPanel Right;
    private JButton btnForgotPassword;
    private JButton btnTogglePassword;
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
                    if (emailField.getText().isEmpty()) {
                        btnLogin.doClick();
                    }
                    passwordField.requestFocusInWindow();
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
        passwordField.setLayout(new BorderLayout());
        passwordField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),BorderFactory.createEmptyBorder(5,10,5,5)));
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
        btnTogglePassword.setPreferredSize(new Dimension(20,20));
        btnTogglePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTogglePassword.setBorderPainted(false);
        btnTogglePassword.setOpaque(false);
//        btnTogglePassword.setBackground(new Color(55, 55, 55));
        btnTogglePassword.setContentAreaFilled(false);

        java.net.URL urlCLOSED = getClass().getResource("/org/example/GUI/resources/images/hide.png");
        java.net.URL urlOPEN = getClass().getResource("/org/example/GUI/resources/images/view1.png");
        ImageIcon iconClosed = null;
        ImageIcon iconOpen = null;
        if (urlCLOSED!=null&&urlOPEN!=null) {
            Image imgClosed = new ImageIcon(urlCLOSED).getImage().getScaledInstance(15,15, Image.SCALE_SMOOTH);
            Image imgOpen = new ImageIcon(urlOPEN).getImage().getScaledInstance(15,15, Image.SCALE_SMOOTH);
            iconClosed = new ImageIcon(imgClosed);
            iconOpen = new ImageIcon(imgOpen);
        }
        else {
            System.out.println("Error: File not found!");
        }
        if (iconClosed!=null) btnTogglePassword.setIcon(iconClosed);
        final ImageIcon finalIconClose = iconClosed;
        final ImageIcon finalIconOpen = iconOpen;

        btnTogglePassword.addActionListener(e -> {
            if (passwordField.getEchoChar() != (char) 0) {
                passwordField.setEchoChar((char) 0);
                if (finalIconOpen!=null) btnTogglePassword.setIcon(finalIconOpen);
            }
            else {
                passwordField.setEchoChar('•');
                if (finalIconClose!=null) btnTogglePassword.setIcon(finalIconClose);
            }
        });
        passwordField.add(btnTogglePassword, BorderLayout.EAST);
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
        btnLogin.addActionListener(e -> {
            String email = emailField.getText();
            String pass = String.valueOf(passwordField.getPassword());

            if (check(email, pass)) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                Application.login();
            }
            else {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập lại thông tin!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });
        Right.add(btnLogin, "center");

        JPanel pnlFogor = new JPanel();
        pnlFogor.setOpaque(false);
        btnForgotPassword = new JButton("Quên mật khẩu?");
        btnForgotPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnForgotPassword.setForeground(new Color(0, 102, 255));
        btnForgotPassword.setBorderPainted(false);
        btnForgotPassword.setContentAreaFilled(false);
        btnForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnForgotPassword.addActionListener(e -> {
        });
        pnlFogor.add(btnForgotPassword);

        JLabel lblHint = new JLabel();
        java.net.URL hintUrl = getClass().getResource("/org/example/GUI/resources/images/information.png");
        if (hintUrl != null) {
            Image img = new ImageIcon(hintUrl).getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
            lblHint.setIcon(new ImageIcon(img));
        } else {
            lblHint.setText("ⓘ");
            lblHint.setForeground(new Color(0, 102, 255));
        }
        lblHint.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHint.setOpaque(false);
        lblHint.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ToolTipManager.sharedInstance().setInitialDelay(100);
        lblHint.setToolTipText("Nếu không biết hãy để trống.");
        pnlFogor.add(lblHint);

        Right.add(pnlFogor, "center");

 

        container.add(Left, "grow");
        container.add(Right, "grow");
        add(container, "center");
    }

    private boolean check(String email, String pass) {
        List<List<String>> auth = List.of(
                List.of("", "email", "36"),
                List.of("", "12345", "363636")
        );

        for (int i = 0; i < auth.get(0).size(); i++) {
            if (auth.get(0).get(i).equals(email)&&auth.get(1).get(i).equals(pass)){
                return true;
            }
        }

        return false;
    }
}