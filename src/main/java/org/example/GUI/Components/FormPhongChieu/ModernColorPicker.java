package org.example.GUI.Components.FormPhongChieu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class ModernColorPicker extends JPanel {
    private Color currentColor = new Color(195, 18, 216); // Màu mặc định giống ảnh
    private JPanel colorPreview;
    private JTextField txtR, txtG, txtB, txtHex;
    private boolean isUpdating = false;

    public ModernColorPicker() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        setBackground(new Color(43, 45, 48)); // Màu nền tối

        // 1. Ô xem trước màu (Hình tròn hoặc vuông)
        colorPreview = new JPanel();
        colorPreview.setPreferredSize(new Dimension(40, 40));
        colorPreview.setBackground(currentColor);
        colorPreview.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));

        // Nút bấm mở JColorChooser mặc định nếu muốn chọn màu trực quan
        JButton btnPick = new JButton("...");
        btnPick.setPreferredSize(new Dimension(30, 40));
        btnPick.setBackground(new Color(60, 63, 65));
        btnPick.setForeground(Color.WHITE);
        btnPick.setFocusPainted(false);
        btnPick.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Chọn màu", currentColor);
            if (c != null) updateColor(c);
        });

        // 2. Tạo các ô nhập liệu R, G, B, Hex
        txtR = createInputBox("R");
        txtG = createInputBox("G");
        txtB = createInputBox("B");
        txtHex = createInputBox("Hex");
        txtHex.setPreferredSize(new Dimension(70, 30));

        // 3. Gắn sự kiện (Khi gõ vào R, G, B sẽ tự cập nhật Hex và ngược lại)
        DocumentListener rgbListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { syncFromRGB(); }
            public void removeUpdate(DocumentEvent e) { syncFromRGB(); }
            public void changedUpdate(DocumentEvent e) { syncFromRGB(); }
        };
        txtR.getDocument().addDocumentListener(rgbListener);
        txtG.getDocument().addDocumentListener(rgbListener);
        txtB.getDocument().addDocumentListener(rgbListener);

        txtHex.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { syncFromHex(); }
            public void removeUpdate(DocumentEvent e) { syncFromHex(); }
            public void changedUpdate(DocumentEvent e) { syncFromHex(); }
        });

        // 4. Lắp ráp giao diện
        add(btnPick);
        add(colorPreview);
        add(createLabeledPanel("R", txtR));
        add(createLabeledPanel("G", txtG));
        add(createLabeledPanel("B", txtB));
        add(createLabeledPanel("Hex", txtHex));

        updateColor(currentColor); // Khởi tạo giá trị ban đầu
    }

    private JTextField createInputBox(String name) {
        JTextField txt = new JTextField(3);
        txt.setBackground(new Color(30, 31, 34));
        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.WHITE);
        txt.setHorizontalAlignment(JTextField.CENTER);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70)),
                new EmptyBorder(5, 5, 5, 5)
        ));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return txt;
    }

    private JPanel createLabeledPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(new Color(43, 45, 48));
        JLabel lbl = new JLabel(labelText, SwingConstants.CENTER);
        lbl.setForeground(new Color(170, 170, 170));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    public void updateColor(Color c) {
        if (isUpdating || c == null) return;
        isUpdating = true;
        currentColor = c;
        colorPreview.setBackground(c);
        txtR.setText(String.valueOf(c.getRed()));
        txtG.setText(String.valueOf(c.getGreen()));
        txtB.setText(String.valueOf(c.getBlue()));

        // Chuyển RGB sang chuỗi Hex
        String hex = String.format("%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
        txtHex.setText(hex);
        isUpdating = false;
    }

    private void syncFromRGB() {
        if (isUpdating) return;
        try {
            int r = Math.min(255, Math.max(0, Integer.parseInt(txtR.getText())));
            int g = Math.min(255, Math.max(0, Integer.parseInt(txtG.getText())));
            int b = Math.min(255, Math.max(0, Integer.parseInt(txtB.getText())));
            updateColor(new Color(r, g, b));
        } catch (NumberFormatException ignored) {} // Bỏ qua nếu người dùng gõ chữ cái
    }

    private void syncFromHex() {
        if (isUpdating) return;
        String hex = txtHex.getText().trim();
        if (hex.length() == 6) {
            try {
                int r = Integer.valueOf(hex.substring(0, 2), 16);
                int g = Integer.valueOf(hex.substring(2, 4), 16);
                int b = Integer.valueOf(hex.substring(4, 6), 16);
                updateColor(new Color(r, g, b));
            } catch (NumberFormatException ignored) {}
        }
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    // Hàm main để bạn chạy thử component độc lập
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Color Picker Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new ModernColorPicker());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}