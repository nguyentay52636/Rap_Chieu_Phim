package org.example.GUI.Components.FormTicket;

import org.example.DAO.PhongChieuDAO;
import org.example.DAO.SuatChieuPhimDAO;
import org.example.DTO.PhongChieuDTO;
import org.example.DTO.TrangThaiGheDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DialogChonGhe extends JDialog {

    private int maSuatChieu, maPhong, giaVeGoc;

    // Danh sách lưu trữ các ghế người dùng đang click chọn
    private List<TrangThaiGheDTO> danhSachDangChon = new ArrayList<>();

    private JPanel pnlGhe;
    private JLabel lblThongTin, lblTongTien;
    private JButton btnXacNhan;

    // Bảng màu quy ước cho ghế
    private final Color COLOR_TRONG = new Color(189, 189, 189);   // Xám
    private final Color COLOR_DA_BAN = new Color(239, 83, 80);    // Đỏ
    private final Color COLOR_CHON = new Color(255, 167, 38);     // Cam

    public DialogChonGhe(JFrame owner, int maSuatChieu, int maPhong, int giaVeGoc) {
        super(owner, "Bước 2: Chọn Ghế Ngồi", true);
        this.maSuatChieu = maSuatChieu;
        this.maPhong = maPhong;
        this.giaVeGoc = giaVeGoc;
        init();
    }

    private void init() {
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(UIManager.getColor("Panel.background"));

        // ================= NORTH: MÀN HÌNH CHIẾU =================
        JPanel pnlScreen = new JPanel(new BorderLayout());
        pnlScreen.setOpaque(false);
        pnlScreen.setBorder(new EmptyBorder(10, 100, 20, 100));

        JLabel lblScreen = new JLabel("MÀN HÌNH CHIẾU", SwingConstants.CENTER);
        lblScreen.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblScreen.setOpaque(true);
        lblScreen.setBackground(new Color(120, 144, 156));
        lblScreen.setForeground(Color.WHITE);
        lblScreen.setPreferredSize(new Dimension(0, 40));
        pnlScreen.add(lblScreen, BorderLayout.CENTER);
        add(pnlScreen, BorderLayout.NORTH);

        // ================= CENTER: MA TRẬN GHẾ =================
        renderSeatGrid();

        // ================= SOUTH: THÀNH TIỀN & CHÚ THÍCH =================
        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setBackground(UIManager.getColor("Panel.background"));
        pnlBottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIManager.getColor("Component.borderColor")));

        // 1. Chú thích màu sắc
        JPanel pnlLegend = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        pnlLegend.setOpaque(false);
        pnlLegend.add(createLegendItem("Ghế Trống", COLOR_TRONG));
        pnlLegend.add(createLegendItem("Ghế Đang Chọn", COLOR_CHON));
        pnlLegend.add(createLegendItem("Ghế Đã Bán", COLOR_DA_BAN));

        // 2. Khu vực hiển thị thông tin và Nút xác nhận
        JPanel pnlAction = new JPanel(new BorderLayout());
        pnlAction.setOpaque(false);
        pnlAction.setBorder(new EmptyBorder(10, 20, 10, 20));

        lblThongTin = new JLabel("Ghế đã chọn: (Chưa chọn)");
        lblThongTin.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        lblTongTien = new JLabel("TỔNG TIỀN: 0 VNĐ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTongTien.setForeground(new Color(239, 83, 80));

        btnXacNhan = new JButton("TIẾP TỤC THANH TOÁN");
        btnXacNhan.setPreferredSize(new Dimension(220, 45));
        btnXacNhan.setBackground(new Color(76, 175, 80));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXacNhan.setEnabled(false); // Khóa nút khi chưa chọn ghế nào

        // Sự kiện click chuyển sang Bước 3
        btnXacNhan.addActionListener(e -> {
            DialogThanhToan dialogTT = new DialogThanhToan(DialogChonGhe.this, maSuatChieu, giaVeGoc, danhSachDangChon);
            dialogTT.setVisible(true);
        });

        JPanel pnlPrice = new JPanel(new GridLayout(2, 1));
        pnlPrice.setOpaque(false);
        pnlPrice.add(lblThongTin);
        pnlPrice.add(lblTongTien);

        pnlAction.add(pnlPrice, BorderLayout.WEST);
        pnlAction.add(btnXacNhan, BorderLayout.EAST);

        pnlBottom.add(pnlLegend, BorderLayout.NORTH);
        pnlBottom.add(pnlAction, BorderLayout.SOUTH);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    private void renderSeatGrid() {
        // Lấy số hàng và số cột của phòng chiếu để vẽ lưới Grid
        PhongChieuDAO pcDAO = new PhongChieuDAO();
        int rows = 10, cols = 10;
        for (PhongChieuDTO pc : pcDAO.selectAll()) {
            if (pc.getMaPhong() == maPhong) {
                rows = pc.getSoHang();
                cols = pc.getSoGheMoiHang();
                break;
            }
        }

        pnlGhe = new JPanel(new GridLayout(rows, cols, 8, 8));
        pnlGhe.setOpaque(false);
        pnlGhe.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Lấy danh sách ghế cùng trạng thái từ CSDL (đã dùng LEFT JOIN)
        SuatChieuPhimDAO scDAO = new SuatChieuPhimDAO();
        List<TrangThaiGheDTO> list = scDAO.layTrangThaiGhe(maSuatChieu, maPhong);

        for (TrangThaiGheDTO ghe : list) {
            JButton btn = new JButton(ghe.getHangGhe() + ghe.getSoGhe());
            btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Kiểm tra trạng thái để tô màu và khóa/mở nút
            if ("Da Ban".equalsIgnoreCase(ghe.getTrangThai()) || "Đã Bán".equalsIgnoreCase(ghe.getTrangThai())) {
                btn.setBackground(COLOR_DA_BAN);
                btn.setForeground(Color.WHITE);
                //btn.setEnabled(false); // Ghế đã bán thì không cho click
            } else {
                btn.setBackground(COLOR_TRONG);
                btn.setForeground(Color.BLACK);
                btn.addActionListener(e -> handleSelectSeat(btn, ghe));
            }
            pnlGhe.add(btn);
        }

        JScrollPane scroll = new JScrollPane(pnlGhe);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);
    }

    private void handleSelectSeat(JButton btn, TrangThaiGheDTO ghe) {
        // Nếu ghế đã nằm trong danh sách chọn -> Hủy chọn
        if (danhSachDangChon.contains(ghe)) {
            danhSachDangChon.remove(ghe);
            btn.setBackground(COLOR_TRONG);
        }
        // Nếu chưa chọn -> Thêm vào danh sách
        else {
            danhSachDangChon.add(ghe);
            btn.setBackground(COLOR_CHON);
        }
        updateUIInfo(); // Cập nhật lại tổng tiền bên dưới
    }

    private void updateUIInfo() {
        if (danhSachDangChon.isEmpty()) {
            lblThongTin.setText("Ghế đã chọn: (Chưa chọn)");
            lblTongTien.setText("TỔNG TIỀN: 0 VNĐ");
            btnXacNhan.setEnabled(false);
            return;
        }

        StringBuilder sb = new StringBuilder("Ghế đã chọn: ");
        for (TrangThaiGheDTO g : danhSachDangChon) {
            sb.append(g.getHangGhe()).append(g.getSoGhe()).append(", ");
        }
        lblThongTin.setText(sb.substring(0, sb.length() - 2));

        // Tính tổng tiền = số lượng ghế * giá vé gốc
        long total = (long) danhSachDangChon.size() * giaVeGoc;
        lblTongTien.setText(String.format("TỔNG TIỀN: %,d VNĐ", total));
        btnXacNhan.setEnabled(true);
    }

    // Hàm phụ trợ để tạo ô màu chú thích
    private JPanel createLegendItem(String text, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setOpaque(false);
        JLabel box = new JLabel();
        box.setOpaque(true);
        box.setBackground(color);
        box.setPreferredSize(new Dimension(20, 20));
        p.add(box);
        p.add(new JLabel(text));
        return p;
    }
}