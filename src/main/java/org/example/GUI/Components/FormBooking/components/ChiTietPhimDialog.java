package org.example.GUI.Components.FormBooking.components;

import org.example.BUS.PhimBUS;
import org.example.DTO.PhimDTO;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ChiTietPhimDialog extends JDialog {

    private final PhimBUS phimBUS = new PhimBUS();

    public ChiTietPhimDialog(int maPhim) {
        setTitle("Chi Tiết Phim");
        setSize(500, 400);
        setModal(true);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);

        PhimDTO phim = phimBUS.getById(maPhim);
        if (phim == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không tìm thấy phim",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
            dispose();
            return;
        }

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel lblTenPhim = createStyledLabel("Tên phim:", labelFont);
        JTextField txtTenPhim = createStyledTextField(phim.getTenPhim(), textFont);

        JLabel lblThoiLuong = createStyledLabel("Thời lượng:", labelFont);
        JTextField txtThoiLuong = createStyledTextField(String.valueOf(phim.getThoiLuong()), textFont);

        JLabel lblDaoDien = createStyledLabel("Đạo diễn:", labelFont);
        JTextField txtDaoDien = createStyledTextField(phim.getDaoDien(), textFont);

        JLabel lblNamSX = createStyledLabel("Năm sản xuất:", labelFont);
        JTextField txtNamSX = createStyledTextField(String.valueOf(phim.getNamSanXuat()), textFont);

        JLabel lblPoster = createStyledLabel("Poster:", labelFont);
        JLabel imgPoster = new JLabel();
        if (phim.getPosterURL() != null) {
            imgPoster.setIcon(new ImageIcon(phim.getPosterURL()));
        }

        panel.add(lblTenPhim);
        panel.add(txtTenPhim);
        panel.add(lblThoiLuong);
        panel.add(txtThoiLuong);
        panel.add(lblDaoDien);
        panel.add(txtDaoDien);
        panel.add(lblNamSX);
        panel.add(txtNamSX);
        panel.add(lblPoster);
        panel.add(imgPoster);

        JButton btnClose = new JButton("Đóng");
        btnClose.setBackground(new Color(220, 53, 69));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e -> dispose());
        panel.add(new JLabel());
        panel.add(btnClose);

        add(panel);
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
        field.setEnabled(false);
        field.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        field.setBackground(new Color(248, 249, 250));
        return field;
    }
}

