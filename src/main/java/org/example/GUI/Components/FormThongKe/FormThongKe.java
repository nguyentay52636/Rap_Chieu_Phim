package org.example.GUI.Components.FormThongKe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class FormThongKe extends JPanel {

    private DefaultTableModel model;
    private JTable table;

    public FormThongKe() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 250));

        // ================== KHU VỰC TRÊN (BỘ LỌC + THẺ TỔNG KẾT) ==================
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 15));
        pnlNorth.setOpaque(false);

        // 1. Thanh lọc thời gian
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        pnlFilter.add(new JLabel("Từ ngày (yyyy-mm-dd):"));
        pnlFilter.add(new JTextField(10));
        pnlFilter.add(new JLabel("Đến ngày:"));
        pnlFilter.add(new JTextField(10));
        
        JButton btnFilter = new JButton("Thống kê");
        btnFilter.setBackground(new Color(0, 123, 255));
        btnFilter.setForeground(Color.WHITE);
        btnFilter.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlFilter.add(btnFilter);
        
        // Thêm nút in báo cáo
        JButton btnPrint = new JButton("Xuất Báo Cáo Excel");
        btnPrint.setBackground(new Color(40, 167, 69));
        btnPrint.setForeground(Color.WHITE);
        btnPrint.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlFilter.add(btnPrint);

        // 2. Các thẻ (Card) tổng hợp dữ liệu siêu ngầu
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlCards.setOpaque(false);
        pnlCards.setPreferredSize(new Dimension(0, 100));

        // Màu sắc bắt mắt cho các thẻ
        pnlCards.add(createStatCard("TỔNG DOANH THU", "2,560,000 VNĐ", new Color(220, 53, 69))); // Đỏ
        pnlCards.add(createStatCard("TỔNG SỐ VÉ BÁN", "15 Vé", new Color(23, 162, 184)));        // Xanh lơ
        pnlCards.add(createStatCard("TỔNG SP BÁN RA", "7 SP", new Color(255, 193, 7)));         // Vàng

        pnlNorth.add(pnlFilter, BorderLayout.NORTH);
        pnlNorth.add(pnlCards, BorderLayout.CENTER);

        // ================== KHU VỰC DƯỚI (BẢNG CHI TIẾT DOANH THU PHIM) ==================
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Bảng Xếp Hạng Doanh Thu Theo Phim", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)));

        // Các cột này hoàn toàn có thể tính toán được từ các bảng: Phim JOIN SuatChieuPhim JOIN Ve JOIN ChiTietHoaDon
        String[] columns = {"Top", "Mã Phim", "Tên Phim", "Số Vé Đã Bán", "Tổng Doanh Thu (VNĐ)"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(108, 117, 125)); // Màu xám chuẩn thanh lịch
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(35);

        // Dữ liệu giả demo cho đẹp
        model.addRow(new Object[]{"1", "3", "Phim 3", "3", "450,000"});
        model.addRow(new Object[]{"2", "7", "Phim 7", "2", "320,000"});
        model.addRow(new Object[]{"3", "1", "Phim 1", "2", "200,000"});

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        pnlCenter.add(scrollPane, BorderLayout.CENTER);

        // ================== LẮP RÁP VÀO FORM ==================
        add(pnlNorth, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);
    }

    // Hàm tiện ích tạo thẻ Card rực rỡ
    private JPanel createStatCard(String title, String value, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setBorder(new EmptyBorder(10, 0, 5, 0));

        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValue.setBorder(new EmptyBorder(0, 0, 15, 0));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }
}