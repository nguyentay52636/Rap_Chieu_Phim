package org.example.GUI.Components.FormEmployee;

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

public class FormEmployee extends JPanel {

    private DefaultTableModel model;
    private JTable table;

    // Nhận thêm tham số title từ MainForm truyền vào
    public FormEmployee(String title) {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE); // Nền trắng giống trong ảnh

        // ================== KHU VỰC PHÍA TRÊN (NÚT BẤM + TÌM KIẾM) ==================
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));
        pnlNorth.setBackground(Color.WHITE);

        // 1. Dàn nút chức năng (Giữ nguyên màu sắc chuẩn trong ảnh)
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlButtons.setBackground(Color.WHITE);
        
        pnlButtons.add(createButton("Xem", new Color(92, 184, 92)));       // Xanh lá nhạt
        pnlButtons.add(createButton("Sửa", new Color(240, 173, 78)));      // Vàng cam
        pnlButtons.add(createButton("Xóa", new Color(217, 83, 79)));       // Đỏ
        pnlButtons.add(createButton("Thêm", new Color(2, 117, 216)));      // Xanh dương
        pnlButtons.add(createButton("Nhập Excel", new Color(138, 43, 226))); // Tím

        // 2. Khu vực Tìm kiếm
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP));

        // Đã cập nhật ComboBox chỉ còn Mã NV và Tên NV (Khớp DB)
        String[] searchOptions = {"Mã NV", "Họ Tên"};
        JComboBox<String> cbSearch = new JComboBox<>(searchOptions);
        cbSearch.setPreferredSize(new Dimension(100, 35));
        
        JTextField txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(200, 35));
        
        JButton btnRefresh = createButton("Làm mới", new Color(91, 192, 222)); // Xanh lơ nhạt

        pnlSearch.add(cbSearch);
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnRefresh);

        // Gắn dàn nút và khu tìm kiếm vào phía trên
        pnlNorth.add(pnlButtons, BorderLayout.NORTH);
        pnlNorth.add(pnlSearch, BorderLayout.CENTER);

        // ================== KHU VỰC BẢNG (TABLE) ==================
        // Đã cập nhật Cột khớp 100% với bảng NhanVien trong MySQL
        String[] columns = {"Mã NV", "Họ Tên", "Ngày Sinh", "Ngày Vào Làm", "Lương Cơ Bản"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        // Trang trí bảng giống trong ảnh
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(66, 103, 178)); // Màu xanh dương đậm
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(35);

        // Thêm vài dòng dữ liệu demo cho đỡ trống (Lát nữa sẽ thay bằng dữ liệu móc từ MySQL lên)
        model.addRow(new Object[]{"1", "Nguyen Thi X", "1980-01-01", "2020-01-01", "10,000,000"});
        model.addRow(new Object[]{"2", "Tran Van Y", "1982-02-02", "2021-02-02", "12,000,000"});

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // ================== GẮN TẤT CẢ VÀO FORM ==================
        add(pnlNorth, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Hàm tiện ích tạo nút bấm chuẩn size và màu sắc
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        // btn.setIcon(...) -> Nếu ông có file icon thì sau này thêm vào đây
        return btn;
    }
}