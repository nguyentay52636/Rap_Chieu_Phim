package org.example.GUI.Components.FormTicket;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class FormTicket extends JPanel {

    private DefaultTableModel model;
    private JTable tableTicket;

    public FormTicket() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 250));

        // ================== TOP PANEL (TÌM KIẾM & LỌC) ==================
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        topPanel.setBackground(new Color(245, 245, 250));
        topPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Tra Cứu Vé Đã Bán",
                TitledBorder.LEFT,
                TitledBorder.TOP));

        topPanel.add(new JLabel("Mã Vé:"));
        JTextField txtTimMa = new JTextField(10);
        txtTimMa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        topPanel.add(txtTimMa);

        topPanel.add(new JLabel("Tên Phim:"));
        JTextField txtTimPhim = new JTextField(15);
        txtTimPhim.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        topPanel.add(txtTimPhim);

        topPanel.add(new JLabel("Trạng thái:"));
        JComboBox<String> cbTrangThai = new JComboBox<>(new String[]{"Tất cả", "Đã thanh toán", "Đã Check-in", "Đã Hủy"});
        cbTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        topPanel.add(cbTrangThai);

        JButton btnTim = new JButton("Lọc / Tìm kiếm");
        btnTim.setBackground(new Color(0, 123, 255));
        btnTim.setForeground(Color.WHITE);
        btnTim.setFont(new Font("Segoe UI", Font.BOLD, 14));
        topPanel.add(btnTim);

        // ================== CENTER PANEL (BẢNG DỮ LIỆU) ==================
        String[] columnNames = {"Mã Vé", "Khách Hàng", "Tên Phim", "Phòng", "Ghế", "Ngày Chiếu", "Giờ", "Giá Tiền", "Trạng Thái"};
        model = new DefaultTableModel(columnNames, 0);
        tableTicket = new JTable(model);

        tableTicket.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableTicket.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTicket.getTableHeader().setBackground(new Color(66, 103, 178));
        tableTicket.getTableHeader().setForeground(Color.WHITE);
        tableTicket.setRowHeight(40);

        // Dữ liệu giả demo cho đẹp đội hình
        model.addRow(new Object[]{"V001", "Nguyễn Văn A", "Lật Mặt 7", "Phòng 1", "H5", "20/10/2026", "18:00", "80,000", "Đã thanh toán"});
        model.addRow(new Object[]{"V002", "Trần Thị B", "Avengers: Endgame", "Phòng 2", "J10", "20/10/2026", "19:00", "90,000", "Đã Check-in"});

        JScrollPane scrollPane = new JScrollPane(tableTicket);
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}