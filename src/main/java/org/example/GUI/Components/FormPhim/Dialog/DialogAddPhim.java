package org.example.GUI.Components.FormPhim.Dialog;

import javax.swing.*;
import java.awt.*;

public class DialogAddPhim extends JDialog {

    private boolean saved = false;

    private final JTextField ten = createStyledTextField(30);
    private final JComboBox<String> theLoai = createStyledComboBox(new String[] {
            "Hành động", "Tình cảm", "Kinh dị", "Hài hước", "Khoa học viễn tưởng", "Hoạt hình", "Phiêu lưu", "Tâm lý",
            "Hình sự"
    });
    private final JTextField thoiLuong = createStyledTextField(10);
    private final JTextField daoDien = createStyledTextField(30);
    private final JTextField nam = createStyledTextField(10);
    private final JTextField ngayKC = createStyledTextField(15);
    private final JComboBox<String> trangThai = createStyledComboBox(new String[] {
            "Đang chiếu", "Sắp chiếu", "Ngừng chiếu"
    });
    private final JTextField posterUrl = createStyledTextField(40);

    public DialogAddPhim(Window owner) {
        super(owner, "➕ Thêm Phim Mới", ModalityType.APPLICATION_MODAL);
        initUI();
    }

    private void initUI() {
        setSize(620, 740);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        getContentPane().setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 30, 30));

        JLabel titleLabel = new JLabel("THÊM PHIM MỚI", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form chính
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Các hàng form
        addFormRow(form, gbc, 0, "Tên phim *", ten, true);
        addFormRow(form, gbc, 1, "Thể loại", theLoai, false);
        addFormRow(form, gbc, 2, "Thời lượng (phút) *", thoiLuong, true);
        addFormRow(form, gbc, 3, "Đạo diễn", daoDien, false);
        addFormRow(form, gbc, 4, "Năm sản xuất *", nam, true);
        addFormRow(form, gbc, 5, "Ngày khởi chiếu (yyyy-MM-dd)", ngayKC, false);
        addFormRow(form, gbc, 6, "Trạng thái", trangThai, false);
        addFormRow(form, gbc, 7, "URL Poster", posterUrl, false);

        // Nút Lưu
        JButton btnLuu = new JButton("💾 LƯU PHIM");
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLuu.setBackground(new Color(46, 204, 113)); // xanh lá hiện đại
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFocusPainted(false);
        btnLuu.setBorder(BorderFactory.createEmptyBorder(14, 40, 14, 40));
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLuu.addActionListener(e -> onSave());

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(btnLuu, gbc);

        mainPanel.add(form, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JTextField createStyledTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBackground(Color.WHITE);
        tf.setForeground(new Color(33, 37, 41));
        tf.setCaretColor(new Color(33, 37, 41));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204), 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
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
        label.setForeground(new Color(189, 195, 199));
        if (required) {
            label.setText(labelText + " *");
            label.setForeground(new Color(231, 76, 60)); // đỏ nhẹ cho trường bắt buộc
        }
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(component, gbc);
    }

    private void onSave() {
        try {
            String tenPhim = ten.getText().trim();
            if (tenPhim.isEmpty()) {
                throw new IllegalArgumentException("Tên phim không được để trống");
            }
            String namStr = nam.getText().trim();
            if (!namStr.isEmpty()) {
                Integer.parseInt(namStr);
            }
            String thoiLuongStr = thoiLuong.getText().trim();
            if (!thoiLuongStr.isEmpty()) {
                Integer.parseInt(thoiLuongStr);
            }
            // Có thể thêm validate ngày khởi chiếu nếu muốn (regex yyyy-MM-dd)

            saved = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Năm sản xuất và thời lượng phải là số nguyên hợp lệ!",
                    "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public String getTenPhim() {
        return ten.getText().trim();
    }

    public String getTheLoai() {
        return (String) theLoai.getSelectedItem();
    }

    public Integer getThoiLuong() {
        String s = thoiLuong.getText().trim();
        return s.isEmpty() ? null : Integer.parseInt(s);
    }

    public String getDaoDien() {
        return daoDien.getText().trim();
    }

    public Integer getNamSanXuat() {
        String s = nam.getText().trim();
        return s.isEmpty() ? null : Integer.parseInt(s);
    }

    public String getNgayKhoiChieu() {
        return ngayKC.getText().trim();
    }

    public String getTrangThai() {
        return (String) trangThai.getSelectedItem();
    }

    public String getPosterUrl() {
        return posterUrl.getText().trim();
    }

    // Để test nhanh dialog
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JButton openBtn = new JButton("Mở Dialog Thêm Phim");
            openBtn.addActionListener(e -> {
                DialogAddPhim dialog = new DialogAddPhim(frame);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    System.out.println("Tên phim: " + dialog.getTenPhim());
                    System.out.println("Thể loại: " + dialog.getTheLoai());
                    // ... in ra các giá trị khác nếu cần
                }
            });
            frame.add(openBtn, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}