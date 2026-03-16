package org.example.GUI.Components.FormCustomer;

import org.example.DTO.KhachHangDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ViewCustomerDialog extends JDialog {
    private KhachHangDTO kh;
    private Font fontTitle = new Font("Segoe UI", Font.BOLD, 15);
    private Font fontData = new Font("Segoe UI", Font.PLAIN, 15);

    public ViewCustomerDialog(JFrame owner, KhachHangDTO kh) {
        super(owner, "Hồ Sơ Chi Tiết Khách Hàng", true);
        this.kh = kh;
        init(owner);
    }

    private void init(JFrame owner) {
        this.setSize(750, 550); // Mở rộng form để chứa đủ bảng lịch sử
        this.setLayout(new BorderLayout(10, 10));
        this.getContentPane().setBackground(Color.WHITE);

        // ==========================================
        // PHẦN 1: NỬA TRÊN - THÔNG TIN KHÁCH HÀNG
        // ==========================================
        JPanel panelInfo = new JPanel(new GridLayout(3, 2, 20, 15));
        panelInfo.setBackground(Color.WHITE);

        // Tạo viền (Border) có tiêu đề bao quanh phần thông tin
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Thông Tin Chung");
        titledBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        titledBorder.setTitleColor(new Color(66, 103, 178));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 10, 10, 10), titledBorder));

        // Format ngày sinh cho đẹp (dd/MM/yyyy)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String ngaySinhStr = kh.getNgaySinh().format(formatter);

        // Đổ dữ liệu vào các Label
        panelInfo.add(createDetailPanel("Mã Khách Hàng:", String.valueOf(kh.getMaKH())));
        panelInfo.add(createDetailPanel("Họ và Tên:", kh.getHoTen()));
        panelInfo.add(createDetailPanel("Số Điện Thoại:", kh.getSDT()));
        panelInfo.add(createDetailPanel("Ngày Sinh:", ngaySinhStr));
        panelInfo.add(createDetailPanel("Điểm Tích Lũy:", String.valueOf(kh.getDiemTichLuy())));

        // Nổi bật Hạng thành viên
        JPanel hangPanel = createDetailPanel("Hạng Thành Viên:", kh.getHangThanhVien());
        ((JLabel) hangPanel.getComponent(1)).setForeground(new Color(255, 140, 0)); // Màu cam đậm
        ((JLabel) hangPanel.getComponent(1)).setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelInfo.add(hangPanel);


        // ==========================================
        // PHẦN 2: NỬA DƯỚI - BẢNG LỊCH SỬ GIAO DỊCH (Mock data)
        // ==========================================
        JPanel panelHistory = new JPanel(new BorderLayout());
        panelHistory.setBackground(Color.WHITE);
        TitledBorder historyBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Lịch Sử Giao Dịch (Sắp ra mắt)");
        historyBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        historyBorder.setTitleColor(new Color(66, 103, 178));
        panelHistory.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 10, 10, 10), historyBorder));

        // Tạo dữ liệu giả (Mock Data) để trình diễn đồ án trước
        String[] columns = {"Mã Hóa Đơn", "Ngày Mua", "Phim Đã Xem", "Tổng Tiền", "Điểm Cộng"};
        Object[][] mockData = {
                {"HD00123", "16/03/2026", "Mai", "180,000 VND", "+18"},
                {"HD00045", "14/02/2026", "Đào, Phở và Piano", "90,000 VND", "+9"},
                {"HD00012", "01/01/2026", "Aquaman 2", "220,000 VND", "+22"}
        };

        DefaultTableModel historyModel = new DefaultTableModel(mockData, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable historyTable = new JTable(historyModel);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        historyTable.getTableHeader().setBackground(new Color(240, 240, 240));
        historyTable.setRowHeight(30);

        // Căn giữa các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < historyTable.getColumnCount(); i++) {
            historyTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollHistory = new JScrollPane(historyTable);
        panelHistory.add(scrollHistory, BorderLayout.CENTER);


        // ==========================================
        // PHẦN 3: NÚT ĐÓNG
        // ==========================================
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBottom.setBackground(Color.WHITE);
        JButton btnDong = new JButton("Đóng Hồ Sơ");
        btnDong.setPreferredSize(new Dimension(130, 40));
        btnDong.setBackground(new Color(108, 117, 125)); // Màu xám chuẩn UI
        btnDong.setForeground(Color.WHITE);
        btnDong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDong.setFocusPainted(false);
        btnDong.addActionListener(e -> setVisible(false));
        panelBottom.add(btnDong);

        // Gắn tất cả vào Dialog
        this.add(panelInfo, BorderLayout.NORTH);
        this.add(panelHistory, BorderLayout.CENTER);
        this.add(panelBottom, BorderLayout.SOUTH);

        this.setLocationRelativeTo(owner);
    }

    // Hàm tiện ích để ghép Tiêu đề và Nội dung lại với nhau
    private JPanel createDetailPanel(String title, String data) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(fontTitle);
        lblTitle.setForeground(Color.GRAY);
        lblTitle.setPreferredSize(new Dimension(130, 25)); // Đẩy cho các cột thẳng hàng nhau

        JLabel lblData = new JLabel(data);
        lblData.setFont(fontData);
        lblData.setForeground(Color.BLACK);

        p.add(lblTitle);
        p.add(lblData);
        return p;
    }
}
