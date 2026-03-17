package org.example.GUI.Components.FormPhim.Dialog;

import javax.swing.*;
import java.awt.*;

public class DialogEditPhim extends JDialog {

    private boolean saved = false;

    private final JTextField ten = new JTextField(30);
    private final JComboBox<String> theLoai = new JComboBox<>(new String[]{
            "Hành động", "Tình cảm", "Kinh dị", "Hài hước", "Khoa học viễn tưởng"
    });
    private final JTextField thoiLuong = new JTextField();
    private final JTextField daoDien = new JTextField();
    private final JTextField nam = new JTextField();
    private final JTextField ngayKC = new JTextField();
    private final JComboBox<String> trangThai = new JComboBox<>(new String[]{
            "Đang chiếu", "Sắp chiếu", "Ngừng chiếu"
    });
    private final JTextField posterUrl = new JTextField();

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
        getContentPane().setBackground(new Color(245, 245, 250));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 25, 12, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(form, gbc, 0, "Tên phim:", ten);
        addFormRow(form, gbc, 1, "Thể loại:", theLoai);
        addFormRow(form, gbc, 2, "Thời lượng (phút):", thoiLuong);
        addFormRow(form, gbc, 3, "Đạo diễn:", daoDien);
        addFormRow(form, gbc, 4, "Năm sản xuất:", nam);
        addFormRow(form, gbc, 5, "Ngày khởi chiếu:", ngayKC);
        addFormRow(form, gbc, 6, "Trạng thái:", trangThai);
        addFormRow(form, gbc, 7, "URL Poster:", posterUrl);

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

        setContentPane(form);
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

    private void addFormRow(JPanel p, GridBagConstraints gbc, int y, String label, JComponent comp) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setForeground(new Color(33, 37, 41));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        p.add(lbl, gbc);

        gbc.gridx = 1;
        p.add(comp, gbc);
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