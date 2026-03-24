package org.example.GUI.Components.FormShowTime;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ScAdvancedSearchDialog extends JDialog {
    private JTextField txtMaPhim, txtMaPhong, txtGiaTu, txtGiaDen;
    private JSpinner spnTuNgay, spnDenNgay;
    private JButton btnLoc, btnLamMoi, btnHuy;
    private boolean confirmed = false;

    public ScAdvancedSearchDialog(Frame parent) {
        super(parent, "Bộ lọc nâng cao", true);
        initComponents();
        setSize(500, 450);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel pnlMain = new JPanel(new GridBagLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // --- Các trường nhập liệu ---
        addFilterRow(pnlMain, gbc, 0, "Mã Phim:", txtMaPhim = new JTextField());
        addFilterRow(pnlMain, gbc, 1, "Mã Phòng:", txtMaPhong = new JTextField());

        // Dòng ngày tháng (Từ - Đến)
        gbc.gridy = 2; gbc.gridx = 0; pnlMain.add(new JLabel("Ngày chiếu (Từ - Đến):"), gbc);
        JPanel pnlDate = new JPanel(new GridLayout(1, 2, 5, 0));
        pnlDate.setOpaque(false);
        pnlDate.add(spnTuNgay = createDateSpinner());
        pnlDate.add(spnDenNgay = createDateSpinner());
        gbc.gridx = 1; pnlMain.add(pnlDate, gbc);

        // Dòng giá vé (Từ - Đến)
        gbc.gridy = 3; gbc.gridx = 0; pnlMain.add(new JLabel("Khoảng giá vé:"), gbc);
        JPanel pnlPrice = new JPanel(new GridLayout(1, 2, 5, 0));
        pnlPrice.setOpaque(false);
        pnlPrice.add(txtGiaTu = new JTextField());
        pnlPrice.add(txtGiaDen = new JTextField());
        gbc.gridx = 1; pnlMain.add(pnlPrice, gbc);

        // --- Footer Buttons ---
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        pnlFooter.setBackground(new Color(245, 245, 245));

        btnLamMoi = createStyledButton("Làm mới", Color.WHITE, Color.BLACK);
        btnLoc = createStyledButton("Lọc Kết Quả", new Color(0, 123, 255), Color.WHITE);
        btnHuy = createStyledButton("Hủy", Color.WHITE, Color.BLACK);

        pnlFooter.add(btnLamMoi);
        pnlFooter.add(btnLoc);
        pnlFooter.add(btnHuy);

        add(pnlMain, BorderLayout.CENTER);
        add(pnlFooter, BorderLayout.SOUTH);

        // Events
        btnHuy.addActionListener(e -> dispose());
        btnLoc.addActionListener(e -> { confirmed = true; dispose(); });
        btnLamMoi.addActionListener(e -> {
            txtMaPhim.setText(""); txtMaPhong.setText("");
            txtGiaTu.setText(""); txtGiaDen.setText("");
            spnTuNgay.setValue(new Date()); spnDenNgay.setValue(new Date());
        });
    }

    private void addFilterRow(JPanel pnl, GridBagConstraints gbc, int y, String label, JComponent comp) {
        gbc.gridy = y; gbc.gridx = 0; gbc.weightx = 0.3;
        pnl.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        pnl.add(comp, gbc);
    }

    private JSpinner createDateSpinner() {
        JSpinner sp = new JSpinner(new SpinnerDateModel());
        sp.setEditor(new JSpinner.DateEditor(sp, "dd/MM/yyyy"));
        return sp;
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return btn;
    }

    // Getters để lấy dữ liệu ra
    public boolean isConfirmed() { return confirmed; }
    public String getMaPhim() { return txtMaPhim.getText(); }
    public String getMaPhong() { return txtMaPhong.getText(); }
    public LocalDateTime getTuNgay() { return toLDT((Date) spnTuNgay.getValue()).withHour(0).withMinute(0); }
    public LocalDateTime getDenNgay() { return toLDT((Date) spnDenNgay.getValue()).withHour(23).withMinute(59); }
    private LocalDateTime toLDT(Date d) { return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(); }
}