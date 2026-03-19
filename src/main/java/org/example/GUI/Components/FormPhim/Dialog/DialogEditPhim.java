package org.example.GUI.Components.FormPhim.Dialog;

import javax.swing.*;
import java.awt.*;

public class DialogEditPhim extends JDialog {

    private boolean saved = false;

    private final JTextField ten = createStyledTextField(30);
    private final JComboBox<String> theLoai = createStyledComboBox(new String[]{
            "Hành động", "Tình cảm", "Kinh dị", "Hài hước", "Khoa học viễn tưởng", "Hoạt hình", "Phiêu lưu", "Tâm lý", "Hình sự"
    });
    private final JTextField thoiLuong = createStyledTextField(10);
    private final JTextField daoDien = createStyledTextField(30);
    private final JTextField nam = createStyledTextField(10);
    private final JTextField ngayKC = createStyledTextField(15);
    private final JComboBox<String> trangThai = createStyledComboBox(new String[]{
            "Đang chiếu", "Sắp chiếu", "Ngừng chiếu"
    });
    private final JTextField posterUrl = createStyledTextField(40);

    public DialogEditPhim(Window owner,
                          String tenPhim,
                          String theLoaiText,
                          String thoiLuongText,
                          String daoDienText,
                          String namText,
                          String ngayKhoiChieuText,
                          String trangThaiText,
                          String posterUrlText) {
        super(owner, "✏️ Sửa Phim", ModalityType.APPLICATION_MODAL);
        initUI();
        ten.setText(tenPhim);
        theLoai.setSelectedItem(theLoaiText);
        thoiLuong.setText(thoiLuongText);
        daoDien.setText(daoDienText);
        nam.setText(namText);
        ngayKC.setText(ngayKhoiChieuText);
        trangThai.setSelectedItem(trangThaiText);
        posterUrl.setText(posterUrlText);
    }

    private void initUI() {
        setSize(580, 720);
        setLocationRelativeTo(getOwner());
        getContentPane().setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 30, 30));

        JLabel titleLabel = new JLabel("SỬA THÔNG TIN PHIM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 25, 12, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(form, gbc, 0, "Tên phim *", ten, true);
        addFormRow(form, gbc, 1, "Thể loại", theLoai, false);
        addFormRow(form, gbc, 2, "Thời lượng (phút) *", thoiLuong, true);
        addFormRow(form, gbc, 3, "Đạo diễn", daoDien, false);
        addFormRow(form, gbc, 4, "Năm sản xuất *", nam, true);
        addFormRow(form, gbc, 5, "Ngày khởi chiếu (yyyy-MM-dd)", ngayKC, false);
        addFormRow(form, gbc, 6, "Trạng thái", trangThai, false);
        addFormRow(form, gbc, 7, "URL Poster", posterUrl, false);

        JButton btnLuu = new JButton("💾 LƯU THAY ĐỔI");
        btnLuu.setBackground(new Color(255, 193, 7));
        btnLuu.setForeground(Color.BLACK);
        btnLuu.setFocusPainted(false);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLuu.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        btnLuu.addActionListener(e -> onSave());
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        form.add(btnLuu, gbc);

        mainPanel.add(form, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void onSave() {
        try {
            String tenPhim = ten.getText().trim();
            if (tenPhim.isEmpty()) {
                throw new IllegalArgumentException("Tên phim không được để trống");
            }
            Integer.parseInt(nam.getText().trim());
            Integer.parseInt(thoiLuong.getText().trim());
            saved = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng kiểm tra dữ liệu nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTextField createStyledTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBackground(Color.WHITE);
        tf.setForeground(new Color(33, 37, 41));
        tf.setCaretColor(new Color(33, 37, 41));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204), 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        return tf;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBackground(Color.WHITE);
        cb.setForeground(new Color(33, 37, 41));
        cb.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1, true));
        if (cb.getRenderer() instanceof JLabel lbl) {
            lbl.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        }
        return cb;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row,
                            String labelText, JComponent component, boolean required) {

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(89, 89, 89));
        if (required) {
            label.setText(labelText + " *");
            label.setForeground(new Color(231, 76, 60));
        }
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(component, gbc);
    }

    public boolean isSaved() {
        return saved;
    }

    public String getTenPhim() {
        return ten.getText().trim();
    }

    public String getTheLoai() {
        return String.valueOf(theLoai.getSelectedItem());
    }

    public String getThoiLuongPhutText() {
        return thoiLuong.getText().trim();
    }

    public String getDaoDien() {
        return daoDien.getText().trim();
    }

    public String getNamSanXuatText() {
        return nam.getText().trim();
    }

    public String getNgayKhoiChieu() {
        return ngayKC.getText().trim();
    }

    public String getTrangThai() {
        return String.valueOf(trangThai.getSelectedItem());
    }

    public String getPosterUrl() {
        return posterUrl.getText().trim();
    }
}