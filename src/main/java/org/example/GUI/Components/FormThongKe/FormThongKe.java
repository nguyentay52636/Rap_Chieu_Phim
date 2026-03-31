package org.example.GUI.Components.FormThongKe;

import org.example.BUS.ThongKeBUS;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class FormThongKe extends JPanel {

    private final ThongKeBUS thongKeBUS;

    private DefaultTableModel model;
    private JTable table;

    private JTextField txtTuNgay;
    private JTextField txtDenNgay;

    private JLabel lblTongDoanhThu;
    private JLabel lblTongSoVeBan;
    private JLabel lblTongSoSanPham;

    public FormThongKe() {
        this.thongKeBUS = new ThongKeBUS();

        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 250));

        JPanel pnlNorth = new JPanel(new BorderLayout(0, 15));
        pnlNorth.setOpaque(false);

        JPanel pnlFilter = createFilterPanel();
        JPanel pnlCards = createCardsPanel();
        JPanel pnlCenter = createCenterPanel();

        pnlNorth.add(pnlFilter, BorderLayout.NORTH);
        pnlNorth.add(pnlCards, BorderLayout.CENTER);

        add(pnlNorth, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);

        loadThongKe();
    }

    private JPanel createFilterPanel() {
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        pnlFilter.add(new JLabel("Từ ngày (yyyy-MM-dd):"));
        txtTuNgay = new JTextField(10);
        pnlFilter.add(txtTuNgay);

        pnlFilter.add(new JLabel("Đến ngày:"));
        txtDenNgay = new JTextField(10);
        pnlFilter.add(txtDenNgay);

        JButton btnFilter = new JButton("Thống kê");
        btnFilter.setBackground(new Color(0, 123, 255));
        btnFilter.setForeground(Color.WHITE);
        btnFilter.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnFilter.addActionListener(e -> loadThongKe());
        pnlFilter.add(btnFilter);

        JButton btnReset = new JButton("Làm mới");
        btnReset.setBackground(new Color(108, 117, 125));
        btnReset.setForeground(Color.WHITE);
        btnReset.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnReset.addActionListener(e -> {
            txtTuNgay.setText("");
            txtDenNgay.setText("");
            loadThongKe();
        });
        pnlFilter.add(btnReset);

        JButton btnPrint = new JButton("Xuất Báo Cáo Excel");
        btnPrint.setBackground(new Color(40, 167, 69));
        btnPrint.setForeground(Color.WHITE);
        btnPrint.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnPrint.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Chức năng xuất Excel chưa được tích hợp trong FormThongKe.",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE
        ));
        pnlFilter.add(btnPrint);

        return pnlFilter;
    }

    private JPanel createCardsPanel() {
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlCards.setOpaque(false);
        pnlCards.setPreferredSize(new Dimension(0, 100));

        lblTongDoanhThu = new JLabel("0 VNĐ", SwingConstants.CENTER);
        lblTongSoVeBan = new JLabel("0 Vé", SwingConstants.CENTER);
        lblTongSoSanPham = new JLabel("0 SP", SwingConstants.CENTER);

        pnlCards.add(createStatCard("TỔNG DOANH THU", lblTongDoanhThu, new Color(220, 53, 69)));
        pnlCards.add(createStatCard("TỔNG SỐ VÉ BÁN", lblTongSoVeBan, new Color(23, 162, 184)));
        pnlCards.add(createStatCard("TỔNG SP BÁN RA", lblTongSoSanPham, new Color(255, 193, 7)));

        return pnlCards;
    }

    private JPanel createCenterPanel() {
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Bảng Xếp Hạng Doanh Thu Theo Phim", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)));

        String[] columns = {"Top", "Mã Phim", "Tên Phim", "Số Vé Đã Bán", "Tổng Doanh Thu (VNĐ)"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(108, 117, 125));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(35);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        pnlCenter.add(scrollPane, BorderLayout.CENTER);

        return pnlCenter;
    }

    private JPanel createStatCard(String title, JLabel lblValue, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setBorder(new EmptyBorder(10, 0, 5, 0));

        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValue.setBorder(new EmptyBorder(0, 0, 15, 0));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    private void loadThongKe() {
        try {
            String tuNgay = txtTuNgay.getText().trim();
            String denNgay = txtDenNgay.getText().trim();

            Object[] tongQuan = thongKeBUS.getTongQuanThongKe(tuNgay, denNgay);
            long tongDoanhThu = (long) tongQuan[0];
            int tongSoVeBan = (int) tongQuan[1];
            int tongSoSanPhamBan = (int) tongQuan[2];

            lblTongDoanhThu.setText(formatCurrency(tongDoanhThu));
            lblTongSoVeBan.setText(String.format("%,d Vé", tongSoVeBan));
            lblTongSoSanPham.setText(String.format("%,d SP", tongSoSanPhamBan));

            loadBangXepHangPhim(tuNgay, denNgay);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Không thể tải thống kê. Vui lòng kiểm tra kết nối cơ sở dữ liệu.",
                    "Lỗi hệ thống",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadBangXepHangPhim(String tuNgay, String denNgay) {
        model.setRowCount(0);
        ArrayList<Object[]> dsThongKe = thongKeBUS.getBangXepHangDoanhThuTheoPhim(tuNgay, denNgay);

        for (Object[] row : dsThongKe) {
            model.addRow(new Object[]{
                    row[0],
                    row[1],
                    row[2],
                    row[3],
                    formatCurrency(((Number) row[4]).longValue())
            });
        }
    }

    private String formatCurrency(long value) {
        return String.format("%,d VNĐ", value);
    }
}
