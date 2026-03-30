package org.example.GUI.Components.FormTicket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DialogChiTietVe extends JDialog {

    public DialogChiTietVe(Window owner, String maVe, String khachHang, String tenPhim, 
                           String phong, String ghe, String ngay, String gio, 
                           String gia, String trangThai) {
        // Cài đặt cơ bản cho Dialog
        super(owner, "Chi Tiết Vé - " + maVe, Dialog.ModalityType.APPLICATION_MODAL);
        setSize(400, 520);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // ==========================================
        // 1. HEADER CỦA VÉ (Màu xanh đậm)
        // ==========================================
        JLabel lblTitle = new JLabel("THÔNG TIN VÉ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBorder(new EmptyBorder(15, 0, 15, 0));
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(66, 103, 178));
        lblTitle.setForeground(Color.WHITE);
        add(lblTitle, BorderLayout.NORTH);

        // ==========================================
        // 2. PHẦN THÂN: HIỂN THỊ DỮ LIỆU
        // ==========================================
        JPanel pnlBody = new JPanel(new GridLayout(9, 2, 10, 15));
        pnlBody.setBorder(new EmptyBorder(20, 30, 20, 30));
        pnlBody.setBackground(Color.WHITE);

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fontValue = new Font("Segoe UI", Font.PLAIN, 15);

        // Mảng chứa các nhãn và giá trị tương ứng
        String[] labels = {"Mã Vé:", "Khách Hàng:", "Tên Phim:", "Phòng Chiếu:", "Ghế Ngồi:", "Ngày Chiếu:", "Giờ Chiếu:", "Giá Vé:", "Trạng Thái:"};
        String[] values = {maVe, khachHang, tenPhim, phong, ghe, ngay, gio, gia, trangThai};

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(fontLabel);
            lbl.setForeground(new Color(100, 100, 100)); // Màu chữ xám cho nhãn

            JLabel val = new JLabel(values[i]);
            val.setFont(fontValue);
            
            // Tô màu nhấn nhá cho vài thông tin quan trọng
            if (i == 2 || i == 4) val.setForeground(new Color(220, 53, 69)); // Phim và Ghế màu đỏ
            if (i == 8) val.setForeground(new Color(40, 167, 69)); // Trạng thái màu xanh lá

            pnlBody.add(lbl);
            pnlBody.add(val);
        }
        add(pnlBody, BorderLayout.CENTER);

        // ==========================================
        // 3. FOOTER: NÚT ĐÓNG
        // ==========================================
        JPanel pnlFooter = new JPanel();
        pnlFooter.setBackground(Color.WHITE);
        pnlFooter.setBorder(new EmptyBorder(10, 0, 20, 0));
        
        JButton btnDong = new JButton("Đóng lại");
        btnDong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDong.setBackground(new Color(108, 117, 125));
        btnDong.setForeground(Color.WHITE);
        btnDong.setPreferredSize(new Dimension(120, 35));
        btnDong.setFocusPainted(false);
        btnDong.addActionListener(e -> dispose()); // Bấm là tắt popup
        
        pnlFooter.add(btnDong);
        add(pnlFooter, BorderLayout.SOUTH);
    }
}