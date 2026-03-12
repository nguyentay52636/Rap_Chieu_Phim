package org.example.GUI.Components.FormTypeProduct; 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class FormTypeProduct extends JPanel { 

    private DefaultTableModel model;
    private JTable table;

    public FormTypeProduct() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // ================== KHU VỰC PHÍA TRÊN ==================
        JPanel pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.setBackground(Color.WHITE);
        pnlNorth.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Quản Lý Thể Loại Phim", TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 14)));

        // 1. Dàn nút chức năng bên trái (Màu y chang ảnh ông gửi)
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlButtons.setBackground(Color.WHITE);

        pnlButtons.add(createButton("Xem", new Color(0, 123, 255), Color.WHITE));   // Xanh dương
        pnlButtons.add(createButton("Sửa", new Color(230, 230, 230), Color.BLACK)); // Xám nhạt
        pnlButtons.add(createButton("Xóa", new Color(220, 53, 69), Color.WHITE));   // Đỏ
        pnlButtons.add(createButton("Thêm", new Color(40, 167, 69), Color.WHITE));  // Xanh lá

        // 2. Khu vực tìm kiếm bên phải
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlSearch.setBackground(Color.WHITE);

        // Đã FIX: Đổi "MaPhim" thành các trường đúng của Thể Loại Phim
        JComboBox<String> cbSearch = new JComboBox<>(new String[]{"Mã Thể Loại", "Tên Thể Loại"});
        cbSearch.setPreferredSize(new Dimension(130, 35));
        cbSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTextField txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 35));

        JButton btnRefresh = createButton("Làm mới", new Color(230, 230, 230), Color.BLACK);

        pnlSearch.add(cbSearch);
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnRefresh);

        pnlNorth.add(pnlButtons, BorderLayout.WEST);
        pnlNorth.add(pnlSearch, BorderLayout.EAST);

        // ================== KHU VỰC BẢNG (TABLE) ==================
        // Bảng đã khớp 100% DB
        String[] columns = {"Mã Thể Loại Phim", "Tên Thể Loại Phim"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(66, 103, 178));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(35);

        // Dữ liệu mẫu bốc từ câu Insert của ông
        model.addRow(new Object[]{"1", "Hành động"});
        model.addRow(new Object[]{"2", "Kinh dị"});
        model.addRow(new Object[]{"3", "Hài hước"});
        model.addRow(new Object[]{"4", "Lãng mạn"});

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(pnlNorth, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Hàm tiện ích tạo nút bấm
    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        return btn;
    }
}