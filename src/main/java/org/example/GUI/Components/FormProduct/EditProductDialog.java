package org.example.GUI.Components.FormProduct;

import org.example.BUS.SanPhamBUS;
import org.example.DTO.SanPhamDTO;

import java.awt.Dialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class EditProductDialog extends JDialog {

    private final SanPhamBUS sanPhamBUS;
    private final FormProduct parent;
    private final int maSanPham;

    private JTextField txtTenSanPham;
    private JTextField txtHinhAnh;
    private JTextField txtGiaBan;
    private JComboBox<String> comboKichThuoc;
    private JTextField txtSoLuong;
    private JComboBox<String> comboTrangThai;
    private JLabel imagePreviewLabel;

    public EditProductDialog(FormProduct parent, SanPhamBUS sanPhamBUS, SanPhamDTO sp) {
        super(SwingUtilities.getWindowAncestor(parent), "Sửa Sản Phẩm", Dialog.ModalityType.APPLICATION_MODAL);
        this.parent = parent;
        this.sanPhamBUS = sanPhamBUS;
        this.maSanPham = sp.getMaSanPham();

        setSize(680, 420);
        setLocationRelativeTo(parent);
        setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 14);

        formPanel.add(createStyledLabel("Tên sản phẩm:", labelFont));
        txtTenSanPham = createStyledTextField(nullToEmpty(sp.getTenSanPham()), textFont);
        formPanel.add(txtTenSanPham);

        formPanel.add(createStyledLabel("Hình ảnh:", labelFont));
        JPanel panelHinhAnh = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelHinhAnh.setBackground(Color.WHITE);
        txtHinhAnh = createStyledTextField(nullToEmpty(sp.getHinhAnh()), textFont);
        txtHinhAnh.setColumns(15);
        JButton btnChonAnh = new JButton("Chọn ảnh");
        btnChonAnh.setFont(textFont);
        btnChonAnh.setFocusPainted(false);
        btnChonAnh.addActionListener(e -> chooseImageFile());
        panelHinhAnh.add(txtHinhAnh);
        panelHinhAnh.add(btnChonAnh);
        formPanel.add(panelHinhAnh);

        formPanel.add(createStyledLabel("Giá bán:", labelFont));
        txtGiaBan = createStyledTextField(String.valueOf(sp.getGiaBan()), textFont);
        formPanel.add(txtGiaBan);

        formPanel.add(createStyledLabel("Kích thước:", labelFont));
        comboKichThuoc = new JComboBox<>(new String[] { "S", "M", "L" });
        comboKichThuoc.setFont(textFont);
        comboKichThuoc.setBackground(Color.WHITE);
        comboKichThuoc.setSelectedItem(nullToEmpty(sp.getKichThuoc()).isEmpty() ? "M" : sp.getKichThuoc());
        formPanel.add(comboKichThuoc);

        formPanel.add(createStyledLabel("Số lượng:", labelFont));
        txtSoLuong = createStyledTextField(String.valueOf(sp.getSoLuong()), textFont);
        formPanel.add(txtSoLuong);

        formPanel.add(createStyledLabel("Trạng thái:", labelFont));
        comboTrangThai = new JComboBox<>(new String[] { "Đang bán", "Không bán" });
        comboTrangThai.setFont(textFont);
        comboTrangThai.setBackground(Color.WHITE);
        comboTrangThai.setSelectedItem(normalizeTrangThai(sp.getTrangThai()));
        formPanel.add(comboTrangThai);

        JPanel imagePanel = new JPanel(new BorderLayout(5, 5));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Ảnh xem trước",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(80, 80, 80)));
        imagePanel.setPreferredSize(new java.awt.Dimension(200, 240));

        imagePreviewLabel = new JLabel("Chưa chọn ảnh", JLabel.CENTER);
        imagePreviewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        imagePreviewLabel.setForeground(new Color(120, 120, 120));
        imagePreviewLabel.setOpaque(true);
        imagePreviewLabel.setBackground(new Color(248, 248, 248));
        imagePanel.add(imagePreviewLabel, BorderLayout.CENTER);

        // preload preview if has file
        if (sp.getHinhAnh() != null && !sp.getHinhAnh().isEmpty()) {
            File file = new File(System.getProperty("user.dir"), "imageTopic" + File.separator + sp.getHinhAnh());
            if (file.isFile()) {
                showPreviewImage(file);
            }
        }

        JButton btnSave = new JButton("Lưu");
        btnSave.setBackground(new Color(40, 167, 69));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(e -> updateSanPham());

        JButton btnCancel = new JButton("Hủy");
        btnCancel.setBackground(new Color(220, 53, 69));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(imagePanel, BorderLayout.EAST);

        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateSanPham() {
        String tenSanPham = txtTenSanPham.getText().trim();
        String hinhAnh = txtHinhAnh.getText().trim();
        String giaBanStr = txtGiaBan.getText().trim();
        String kichThuoc = (String) comboKichThuoc.getSelectedItem();
        String soLuongStr = txtSoLuong.getText().trim();
        String trangThai = (String) comboTrangThai.getSelectedItem();

        if (tenSanPham.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sản phẩm.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            txtTenSanPham.requestFocus();
            return;
        }

        int giaBan;
        try {
            giaBan = Integer.parseInt(giaBanStr);
            if (giaBan < 0) {
                JOptionPane.showMessageDialog(this, "Giá bán phải là số không âm.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                txtGiaBan.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá bán phải là số nguyên.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            txtGiaBan.requestFocus();
            return;
        }

        int soLuong;
        try {
            soLuong = Integer.parseInt(soLuongStr);
            if (soLuong < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải là số không âm.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                txtSoLuong.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            txtSoLuong.requestFocus();
            return;
        }

        String hinhAnhSave = (hinhAnh != null && !hinhAnh.isEmpty()) ? hinhAnh : "";
        if (hinhAnhSave.length() > 255) {
            hinhAnhSave = hinhAnhSave.substring(0, 255);
        }

        SanPhamDTO updated = new SanPhamDTO(maSanPham, tenSanPham, hinhAnhSave, giaBan,
                kichThuoc != null ? kichThuoc : "", soLuong, trangThai != null ? trangThai : "Đang bán");
        boolean ok = sanPhamBUS.update(updated);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            parent.refreshTable();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thất bại. Kiểm tra database.", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Mở hộp chọn file ảnh từ máy tính, copy vào thư mục imageTopic và điền tên file vào ô. */
    private void chooseImageFile() {
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        chooser.setDialogTitle("Chọn hình ảnh sản phẩm");
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Ảnh (jpg, png, gif)", "jpg", "jpeg", "png", "gif", "bmp"));
        if (chooser.showOpenDialog(this) != javax.swing.JFileChooser.APPROVE_OPTION) {
            return;
        }
        File source = chooser.getSelectedFile();
        if (source == null || !source.isFile()) {
            return;
        }
        String fileName = source.getName();
        Path destDir = Paths.get(System.getProperty("user.dir"), "imageTopic");
        try {
            Files.createDirectories(destDir);
            Path dest = destDir.resolve(fileName);
            Files.copy(source.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            txtHinhAnh.setText(fileName);
            showPreviewImage(dest.toFile());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Không thể sao chép ảnh: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showPreviewImage(File imageFile) {
        if (imageFile == null || !imageFile.isFile()) {
            imagePreviewLabel.setIcon(null);
            imagePreviewLabel.setText("Chưa chọn ảnh");
            return;
        }
        try {
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(160, 180, Image.SCALE_SMOOTH);
            imagePreviewLabel.setIcon(new ImageIcon(img));
            imagePreviewLabel.setText(null);
        } catch (Exception e) {
            imagePreviewLabel.setIcon(null);
            imagePreviewLabel.setText("Không hiển thị được");
        }
    }

    private JLabel createStyledLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(new Color(33, 37, 41));
        return label;
    }

    private JTextField createStyledTextField(String text, Font font) {
        JTextField field = new JTextField(text);
        field.setFont(font);
        field.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        field.setBackground(Color.WHITE);
        return field;
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private static String normalizeTrangThai(String trangThai) {
        if (trangThai == null) return "Đang bán";
        String tt = trangThai.trim().toLowerCase();
        if (tt.contains("không") || tt.contains("khong") || tt.contains("ngừng") || tt.contains("ngung")) {
            return "Không bán";
        }
        return "Đang bán";
    }
}
