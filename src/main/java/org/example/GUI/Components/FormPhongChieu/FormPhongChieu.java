package org.example.GUI.Components.FormPhongChieu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class FormPhongChieu extends JPanel {

    private DefaultTableModel model;
    private JTable tablePhongChieu;

    public FormPhongChieu() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 250));

        // ================== TOP PANEL (NÚT BẤM & TÌM KIẾM) ==================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 250));
        topPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Quản Lý Phòng Chiếu",
                TitledBorder.LEFT,
                TitledBorder.TOP));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttonPanel.setOpaque(false);

        JButton btnAdd = createStyledButton("Thêm", new Color(40, 167, 69), Color.WHITE);
        JButton btnEdit = createStyledButton("Sửa", new Color(255, 193, 7), Color.BLACK);
        JButton btnDelete = createStyledButton("Xóa", new Color(220, 53, 69), Color.WHITE);
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        searchPanel.setOpaque(false);
        
        searchPanel.add(new JLabel("Tìm mã/tên phòng:"));
        JTextField txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 35));
        searchPanel.add(txtSearch);
        
        JButton btnSearch = createStyledButton("Lọc", new Color(0, 123, 255), Color.WHITE);
        searchPanel.add(btnSearch);

        topPanel.add(buttonPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        // ================== CENTER PANEL (BẢNG DỮ LIỆU) ==================
        // Đã gọt lại đúng 3 cột theo Database của ông
        String[] columnNames = {"Mã Phòng", "Tên Phòng", "Loại Phòng"};
        model = new DefaultTableModel(columnNames, 0);
        tablePhongChieu = new JTable(model);

        tablePhongChieu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablePhongChieu.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablePhongChieu.getTableHeader().setBackground(new Color(66, 103, 178));
        tablePhongChieu.getTableHeader().setForeground(Color.WHITE);
        tablePhongChieu.setRowHeight(40);

        // Dữ liệu giả demo cho đúng cấu trúc
        model.addRow(new Object[]{"1", "Phòng 1", "2D"});
        model.addRow(new Object[]{"2", "Phòng 2", "3D"});
        model.addRow(new Object[]{"3", "Phòng 3", "2D"});

        JScrollPane scrollPane = new JScrollPane(tablePhongChieu);
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(80, 35));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        return button;
    }
}