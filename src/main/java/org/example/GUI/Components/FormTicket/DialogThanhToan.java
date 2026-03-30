package org.example.GUI.Components.FormTicket;

import org.example.DAO.BanVeDAO;
import org.example.DAO.EmployeeDAO;
import org.example.DAO.KhachHangDAO;
import org.example.DTO.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DialogThanhToan extends JDialog 
{
    private int maSuatChieu;
    private int giaVeGoc;

    // Danh sách ghế khách đã chọn từ Form trước truyền sang
    private List<TrangThaiGheDTO> danhSachGhe;

    private int tongTien;
    private KhachHangDTO khachHangHienTai = null;

    private JComboBox<ComboItemNhanVien> cbNhanVien;
    private JTextField txtSDT;
    private JLabel lblTenKH, lblDiemTL;
    private JButton btnThanhToan;

    private Font font = new Font("Segoe UI", Font.PLAIN, 15);

    public DialogThanhToan(JDialog owner, int maSuatChieu, int giaVeGoc, List<TrangThaiGheDTO> danhSachGhe) 
    {
        super(owner, "Bước 3: Thanh Toán Hóa Đơn", true);
        this.maSuatChieu = maSuatChieu;
        this.giaVeGoc = giaVeGoc;
        this.danhSachGhe = danhSachGhe;
        this.tongTien = danhSachGhe.size() * giaVeGoc; // Tính tổng tiền

        init(owner);
    }

    private void init(JDialog owner) 
    {
        setSize(800, 550);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(UIManager.getColor("Panel.background"));

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setOpaque(false);

        // ================= TRÁI: THÔNG TIN VÉ & TỔNG TIỀN (BIÊN LAI) =================
        JPanel pnlLeft = new JPanel(new BorderLayout(0, 15));
        pnlLeft.setOpaque(false);
        pnlLeft.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Chi Tiết Giao Dịch", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));

        JTextArea txtBienLai = new JTextArea();
        txtBienLai.setEditable(false); // Khóa không cho sửa biên lai
        txtBienLai.setFont(new Font("Monospaced", Font.PLAIN, 15));
        txtBienLai.setBackground(new Color(245, 245, 245));
        txtBienLai.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Format bill tính tiền
        StringBuilder sb = new StringBuilder();
        sb.append("MÃ SUẤT CHIẾU : ").append(maSuatChieu).append("\n");
        sb.append("SỐ LƯỢNG VÉ   : ").append(danhSachGhe.size()).append(" vé\n");
        sb.append("DANH SÁCH GHẾ : ");

        // Lấy tên ghế từ TrangThaiGheDTO
        for (TrangThaiGheDTO g : danhSachGhe) {
            sb.append(g.getHangGhe()).append(g.getSoGhe()).append(", ");
        }

        sb.append("\n----------------------------------\n");
        sb.append(String.format("GIÁ VÉ GỐC    : %,d VNĐ/vé\n", giaVeGoc));
        sb.append("\n==================================\n");
        sb.append(String.format("TỔNG THÀNH TIỀN: %,d VNĐ", tongTien));
        txtBienLai.setText(sb.toString());

        pnlLeft.add(new JScrollPane(txtBienLai), BorderLayout.CENTER);
        mainPanel.add(pnlLeft);

        // ================= PHẢI: NHÂN VIÊN & KHÁCH HÀNG =================
        JPanel pnlRight = new JPanel(new GridLayout(2, 1, 0, 15));
        pnlRight.setOpaque(false);

        // 1. Panel Nhân Viên
        JPanel pnlNhanVien = new JPanel(new GridLayout(2, 1, 0, 5));
        pnlNhanVien.setOpaque(false);
        pnlNhanVien.setBorder(BorderFactory.createTitledBorder("Nhân viên thu ngân"));
        cbNhanVien = new JComboBox<>();
        cbNhanVien.setFont(font);
        loadNhanVien();
        pnlNhanVien.add(cbNhanVien);
        pnlRight.add(pnlNhanVien);

        // 2. Panel Khách Hàng
        JPanel pnlKhachHang = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlKhachHang.setOpaque(false);
        pnlKhachHang.setBorder(BorderFactory.createTitledBorder("Thẻ thành viên (Tùy chọn)"));

        pnlKhachHang.add(new JLabel("SĐT Khách:"));
        txtSDT = new JTextField(12);
        txtSDT.setFont(font);
        pnlKhachHang.add(txtSDT);

        JButton btnTimKH = new JButton("Kiểm tra");
        btnTimKH.setBackground(new Color(0, 123, 255));
        btnTimKH.setForeground(Color.WHITE);
        btnTimKH.addActionListener(e -> timKhachHang());
        pnlKhachHang.add(btnTimKH);

        lblTenKH = new JLabel("Khách hàng: Khách vãng lai");
        lblTenKH.setPreferredSize(new Dimension(350, 25));
        lblTenKH.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTenKH.setForeground(new Color(40, 167, 69));

        lblDiemTL = new JLabel("Điểm tích lũy: 0");
        lblDiemTL.setPreferredSize(new Dimension(350, 25));
        lblDiemTL.setFont(font);

        pnlKhachHang.add(lblTenKH);
        pnlKhachHang.add(lblDiemTL);
        pnlRight.add(pnlKhachHang);

        mainPanel.add(pnlRight);
        add(mainPanel, BorderLayout.CENTER);

        // ================= BOTTOM: NÚT XÁC NHẬN =================
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        pnlBottom.setOpaque(false);

        JButton btnHuy = new JButton("Quay lại");
        btnHuy.setPreferredSize(new Dimension(100, 40));
        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnHuy.addActionListener(e -> setVisible(false));

        btnThanhToan = new JButton("XÁC NHẬN & IN VÉ");
        btnThanhToan.setPreferredSize(new Dimension(180, 40));
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnThanhToan.setBackground(new Color(220, 53, 69));
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.addActionListener(e -> xuLyThanhToan());

        pnlBottom.add(btnHuy);
        pnlBottom.add(btnThanhToan);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    // =========================================================================
    // CÁC HÀM XỬ LÝ NGHIỆP VỤ
    // =========================================================================

    private void loadNhanVien() 
    {
        EmployeeDAO empDAO = new EmployeeDAO();
        List<EmployeeDTO> listEmp = empDAO.selectAll();
        for (EmployeeDTO nv : listEmp) {
            cbNhanVien.addItem(new ComboItemNhanVien(nv.getMaNV(), nv.getHoTen()));
        }
    }

    private void timKhachHang() 
    {
        String sdt = txtSDT.getText().trim();
        if (sdt.isEmpty()) {
            khachHangHienTai = null;
            lblTenKH.setText("Khách hàng: Khách vãng lai");
            lblDiemTL.setText("Điểm tích lũy: 0");
            return;
        }

        KhachHangDAO khDAO = new KhachHangDAO();
        List<KhachHangDTO> kq = khDAO.search("SDT", sdt);
        if (kq != null && !kq.isEmpty()) {
            khachHangHienTai = kq.get(0);
            lblTenKH.setText("Khách hàng: " + khachHangHienTai.getHoTen());
            lblDiemTL.setText("Điểm tích lũy hiện tại: " + khachHangHienTai.getDiemTichLuy());
        } else {
            khachHangHienTai = null;
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            lblTenKH.setText("Khách hàng: Khách vãng lai");
            lblDiemTL.setText("Điểm tích lũy: 0");
        }
    }

    private void xuLyThanhToan() 
    {
        ComboItemNhanVien nvChon = (ComboItemNhanVien) cbNhanVien.getSelectedItem();
        if (nvChon == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên thu ngân!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                String.format("Xác nhận thanh toán số tiền %,d VNĐ?", tongTien),
                "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        // 1. Chuẩn bị đối tượng Hóa Đơn
        int maKH = (khachHangHienTai != null) ? khachHangHienTai.getMaKH() : 0;
        java.sql.Date ngayBan = new java.sql.Date(System.currentTimeMillis());

        HoaDonDTO hoaDon = new HoaDonDTO(0, maKH, nvChon.maNV, ngayBan, danhSachGhe.size(), tongTien, 0, null, 0, tongTien);

        // 2. Chuẩn bị danh sách Vé
        List<VeDTO> danhSachVe = new ArrayList<>();
        for (TrangThaiGheDTO g : danhSachGhe) {
            // Lấy chính xác Mã Ghế từ TrangThaiGheDTO để tạo vé, giữ nguyên chữ "Da Ban" theo Database gốc của nhóm
            VeDTO ve = new VeDTO(0, g.getMaGhe(), maSuatChieu, giaVeGoc, "Da Ban");
            danhSachVe.add(ve);
        }

        // 3. Gửi xuống DAO để thực hiện Transaction
        BanVeDAO banVeDAO = new BanVeDAO();
        boolean thanhCong = banVeDAO.thanhToanGiaoDich(hoaDon, danhSachVe, null);

        if (thanhCong) {
            JOptionPane.showMessageDialog(this, "Thanh toán thành công! Vé đã được in.", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // CHIÊU BÀI DỌN DẸP GIAO DIỆN: 
            // Tắt toàn bộ các Popup (Thanh Toán, Chọn Ghế, Chọn Suất Chiếu) để về lại Bảng Quản Lý Vé
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof JDialog) {
                    window.dispose();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Thanh toán thất bại! Vui lòng kiểm tra lại kết nối.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Class phụ để bọc dữ liệu Nhân Viên cho Combobox
    private class ComboItemNhanVien 
    {
        int maNV;
        String tenNV;
        
        public ComboItemNhanVien(int maNV, String tenNV) 
        {
            this.maNV = maNV;
            this.tenNV = tenNV;
        }
        
        @Override
        public String toString() 
        { 
            return tenNV; 
        }
    }
}