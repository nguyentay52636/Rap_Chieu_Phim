package org.example.GUI.Components.FormCustomer;

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

public class FormCustomer extends JPanel {

    private DefaultTableModel model;
    private JTable table;

    // Nhận tham số title từ MainForm truyền vào
    public FormCustomer(String title) {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE); 

        // ================== KHU VỰC PHÍA TRÊN (NÚT BẤM + TÌM KIẾM) ==================
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));
        pnlNorth.setBackground(Color.WHITE);

        // 1. Dàn nút chức năng (Giữ nguyên tông màu chuẩn)
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlButtons.setBackground(Color.WHITE);
        
        pnlButtons.add(createButton("Xem", new Color(92, 184, 92)));       
        pnlButtons.add(createButton("Sửa", new Color(240, 173, 78)));      
        pnlButtons.add(createButton("Xóa", new Color(217, 83, 79)));       
        pnlButtons.add(createButton("Thêm", new Color(2, 117, 216)));      
        pnlButtons.add(createButton("Nhập Excel", new Color(138, 43, 226))); 

        // 2. Khu vực Tìm kiếm
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP));

        // Combo box tìm kiếm được cập nhật khớp với Khách Hàng
        String[] searchOptions = {"Mã KH", "Họ Tên", "Số Điện Thoại"};
        JComboBox<String> cbSearch = new JComboBox<>(searchOptions);
        cbSearch.setPreferredSize(new Dimension(120, 35));
        
        JTextField txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(200, 35));
        
        JButton btnRefresh = createButton("Làm mới", new Color(91, 192, 222)); 

        pnlSearch.add(cbSearch);
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnRefresh);

        pnlNorth.add(pnlButtons, BorderLayout.NORTH);
        pnlNorth.add(pnlSearch, BorderLayout.CENTER);

        // ================== KHU VỰC BẢNG (TABLE) ==================
        // Cập nhật Cột khớp 100% với bảng KhachHang trong MySQL
        String[] columns = {"Mã KH", "Họ Tên", "Số Điện Thoại", "Ngày Sinh", "Điểm Tích Lũy", "Hạng Thành Viên"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        // Trang trí bảng
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(66, 103, 178)); 
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(35);

        // Thêm dữ liệu demo cho đỡ trống
        model.addRow(new Object[]{"1", "Nguyen Van A", "0123456789", "1990-01-01", "100", "Thành viên mới"});
        model.addRow(new Object[]{"2", "Tran Thi B", "0987654321", "1995-05-05", "500", "VIP"});

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(pnlNorth, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        return btn;
    }
}