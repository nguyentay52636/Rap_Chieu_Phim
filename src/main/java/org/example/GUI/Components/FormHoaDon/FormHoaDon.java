package org.example.GUI.Components.FormHoaDon;

    


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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

public class FormHoaDon extends JPanel {

    private DefaultTableModel model;
    private JTable table;

    // Nhận tham số title từ MainForm truyền vào (case 6)
    public FormHoaDon(String title) {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 250));

        // ================== KHU VỰC PHÍA TRÊN (THÔNG TIN & BỘ LỌC) ==================
        JPanel pnlNorth = new JPanel(new GridLayout(1, 2, 15, 0)); // Chia làm 2 cột bằng nhau
        pnlNorth.setBackground(new Color(245, 245, 250));

        // ------ BOX 1: THÔNG TIN HÓA ĐƠN ------
        JPanel pnlInfo = new JPanel(new BorderLayout(0, 10));
        pnlInfo.setBackground(Color.WHITE);
        pnlInfo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Thông tin hoá đơn", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)));

        JPanel pnlInfoDetails = new JPanel(new GridLayout(4, 2, 10, 15));
        pnlInfoDetails.setBackground(Color.WHITE);
        pnlInfoDetails.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        pnlInfoDetails.add(new JLabel("Mã hoá đơn:")); pnlInfoDetails.add(new JLabel("___"));
        pnlInfoDetails.add(new JLabel("Khách hàng:")); pnlInfoDetails.add(new JLabel("___"));
        pnlInfoDetails.add(new JLabel("Nhân viên:"));  pnlInfoDetails.add(new JLabel("___"));
        pnlInfoDetails.add(new JLabel("Ngày lập:"));   pnlInfoDetails.add(new JLabel("___"));
        pnlInfoDetails.add(new JLabel("Tổng tiền:"));  pnlInfoDetails.add(new JLabel("___", JLabel.LEFT));

        JPanel pnlInfoBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlInfoBtn.setBackground(Color.WHITE);
        JButton btnChiTiet = createButton("Xem chi tiết", new Color(0, 123, 255));
        pnlInfoBtn.add(btnChiTiet);

        pnlInfo.add(pnlInfoDetails, BorderLayout.CENTER);
        pnlInfo.add(pnlInfoBtn, BorderLayout.SOUTH);

        // ------ BOX 2: BỘ LỌC ------
        JPanel pnlFilter = new JPanel(new BorderLayout(0, 10));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Bộ lọc", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)));

        JPanel pnlFilterFields = new JPanel(new GridLayout(4, 2, 10, 15));
        pnlFilterFields.setBackground(Color.WHITE);
        pnlFilterFields.setBorder(new EmptyBorder(10, 15, 10, 15));

        pnlFilterFields.add(new JLabel("Mã hoá đơn:"));   pnlFilterFields.add(new JTextField());
        pnlFilterFields.add(new JLabel("Mã khách hàng:")); pnlFilterFields.add(new JTextField());
        pnlFilterFields.add(new JLabel("Mã nhân viên:"));  pnlFilterFields.add(new JTextField());
        pnlFilterFields.add(new JLabel("Chọn thời gian:")); 
        pnlFilterFields.add(new JComboBox<>(new String[]{"Tất cả", "Hôm nay", "Tuần này", "Tháng này"}));

        JPanel pnlFilterBtn = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlFilterBtn.setBackground(Color.WHITE);
        JButton btnTimKiem = createButton("Tìm kiếm", new Color(91, 192, 222)); // Màu xanh lơ
        JButton btnXoa = createButton("Xóa", new Color(217, 83, 79)); // Màu đỏ
        pnlFilterBtn.add(btnTimKiem);
        pnlFilterBtn.add(btnXoa);

        pnlFilter.add(pnlFilterFields, BorderLayout.CENTER);
        pnlFilter.add(pnlFilterBtn, BorderLayout.SOUTH);

        // Add 2 box vào Panel phía trên
        pnlNorth.add(pnlInfo);
        pnlNorth.add(pnlFilter);

        // ================== KHU VỰC BẢNG (TABLE) ==================
        // Cập nhật Cột khớp 100% với Database HoaDon
        String[] columns = {"Mã Hóa Đơn", "Mã Khách Hàng", "Mã Nhân Viên", "Ngày Lập", "Tổng Thanh Toán"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(66, 103, 178));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(35);

        // Dữ liệu giả định dựa theo script Insert SQL của ông
        model.addRow(new Object[]{"1", "1", "1", "2026-03-10", "240,000"});
        model.addRow(new Object[]{"2", "2", "2", "2026-03-11", "145,000"});
        model.addRow(new Object[]{"3", "3", "3", "2026-03-12", "470,000"});

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // ================== KHU VỰC NÚT XUẤT EXCEL ==================
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSouth.setBackground(new Color(245, 245, 250));
        JButton btnExcel = createButton("Xuất Excel", new Color(40, 167, 69)); // Xanh lá cây
        pnlSouth.add(btnExcel);

        // Gắn tất cả vào Form
        add(pnlNorth, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(pnlSouth, BorderLayout.SOUTH);
    }

    // Hàm tiện ích tạo nút bấm chuẩn size và màu
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        return btn;
    }
}