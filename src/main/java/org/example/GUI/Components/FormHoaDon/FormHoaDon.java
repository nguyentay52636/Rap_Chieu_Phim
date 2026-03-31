package org.example.GUI.Components.FormHoaDon;

import org.example.BUS.HoaDonBUS;
import org.example.DTO.HoaDonDTO;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FormHoaDon extends JPanel {

    private final HoaDonBUS hoaDonBUS;
    private DefaultTableModel model;
    private JTable table;

    private JLabel lblMaHoaDonValue;
    private JLabel lblKhachHangValue;
    private JLabel lblNhanVienValue;
    private JLabel lblNgayLapValue;
    private JLabel lblTongTienValue;

    private JTextField txtMaHoaDon;
    private JTextField txtMaKH;
    private JTextField txtMaNV;
    private JComboBox<String> cboThoiGian;

    public FormHoaDon(String title) {
        this.hoaDonBUS = new HoaDonBUS();

        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 250));

        JPanel pnlNorth = new JPanel(new GridLayout(1, 2, 15, 0));
        pnlNorth.setBackground(new Color(245, 245, 250));

        JPanel pnlInfo = new JPanel(new BorderLayout(0, 10));
        pnlInfo.setBackground(Color.WHITE);
        pnlInfo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                title == null || title.trim().isEmpty() ? "Thông tin hoá đơn" : title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)));

        JPanel pnlInfoDetails = new JPanel(new GridLayout(5, 2, 10, 15));
        pnlInfoDetails.setBackground(Color.WHITE);
        pnlInfoDetails.setBorder(new EmptyBorder(10, 15, 10, 15));

        lblMaHoaDonValue = new JLabel("___");
        lblKhachHangValue = new JLabel("___");
        lblNhanVienValue = new JLabel("___");
        lblNgayLapValue = new JLabel("___");
        lblTongTienValue = new JLabel("___", SwingConstants.LEFT);

        pnlInfoDetails.add(new JLabel("Mã hoá đơn:"));
        pnlInfoDetails.add(lblMaHoaDonValue);
        pnlInfoDetails.add(new JLabel("Khách hàng:"));
        pnlInfoDetails.add(lblKhachHangValue);
        pnlInfoDetails.add(new JLabel("Nhân viên:"));
        pnlInfoDetails.add(lblNhanVienValue);
        pnlInfoDetails.add(new JLabel("Ngày lập:"));
        pnlInfoDetails.add(lblNgayLapValue);
        pnlInfoDetails.add(new JLabel("Tổng tiền:"));
        pnlInfoDetails.add(lblTongTienValue);

        JPanel pnlInfoBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlInfoBtn.setBackground(Color.WHITE);
        JButton btnChiTiet = createButton("Xem chi tiết", new Color(0, 123, 255));
        pnlInfoBtn.add(btnChiTiet);

        pnlInfo.add(pnlInfoDetails, BorderLayout.CENTER);
        pnlInfo.add(pnlInfoBtn, BorderLayout.SOUTH);

        JPanel pnlFilter = new JPanel(new BorderLayout(0, 10));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Bộ lọc", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)));

        JPanel pnlFilterFields = new JPanel(new GridLayout(4, 2, 10, 15));
        pnlFilterFields.setBackground(Color.WHITE);
        pnlFilterFields.setBorder(new EmptyBorder(10, 15, 10, 15));

        txtMaHoaDon = new JTextField();
        txtMaKH = new JTextField();
        txtMaNV = new JTextField();
        cboThoiGian = new JComboBox<>(new String[]{"Tất cả", "Hôm nay", "Tuần này", "Tháng này"});

        pnlFilterFields.add(new JLabel("Mã hoá đơn:"));
        pnlFilterFields.add(txtMaHoaDon);
        pnlFilterFields.add(new JLabel("Khách hàng (mã/tên):"));
        pnlFilterFields.add(txtMaKH);
        pnlFilterFields.add(new JLabel("Nhân viên (mã/tên):"));
        pnlFilterFields.add(txtMaNV);
        pnlFilterFields.add(new JLabel("Chọn thời gian:"));
        pnlFilterFields.add(cboThoiGian);

        JPanel pnlFilterBtn = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlFilterBtn.setBackground(Color.WHITE);
        JButton btnTimKiem = createButton("Tìm kiếm", new Color(91, 192, 222));
        JButton btnXoa = createButton("Xóa", new Color(217, 83, 79));
        pnlFilterBtn.add(btnTimKiem);
        pnlFilterBtn.add(btnXoa);

        pnlFilter.add(pnlFilterFields, BorderLayout.CENTER);
        pnlFilter.add(pnlFilterBtn, BorderLayout.SOUTH);

        pnlNorth.add(pnlInfo);
        pnlNorth.add(pnlFilter);

        String[] columns = {"Mã Hóa Đơn", "Khách Hàng", "Nhân Viên", "Ngày Lập", "Tổng Thanh Toán"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(66, 103, 178));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(35);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSouth.setBackground(new Color(245, 245, 250));
        JButton btnExcel = createButton("Xuất Excel", new Color(40, 167, 69));
        pnlSouth.add(btnExcel);

        add(pnlNorth, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(pnlSouth, BorderLayout.SOUTH);

        btnTimKiem.addActionListener(e -> timKiemHoaDon());
        btnXoa.addActionListener(e -> xoaBoLoc());
        btnChiTiet.addActionListener(e -> xemChiTietHoaDon());
        btnExcel.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Chức năng xuất Excel chưa được cài trong các file hiện tại.",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                capNhatThongTinHoaDonDangChon();
            }
        });

        loadDataToTable(hoaDonBUS.getAll());
    }

    private void timKiemHoaDon() {
        try {
            String maHoaDon = txtMaHoaDon.getText().trim();
            String maKH = txtMaKH.getText().trim();
            String maNV = txtMaNV.getText().trim();
            String thoiGian = (String) cboThoiGian.getSelectedItem();

            ArrayList<HoaDonDTO> ds = hoaDonBUS.search(maHoaDon, maKH, maNV, thoiGian);
            loadDataToTable(ds);

            if (ds.isEmpty()) {
                clearInfo();
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy hóa đơn phù hợp.",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tìm kiếm hóa đơn: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaBoLoc() {
        txtMaHoaDon.setText("");
        txtMaKH.setText("");
        txtMaNV.setText("");
        cboThoiGian.setSelectedIndex(0);
        loadDataToTable(hoaDonBUS.getAll());
    }

    private void loadDataToTable(ArrayList<HoaDonDTO> dsHoaDon) {
        model.setRowCount(0);

        if (dsHoaDon == null) {
            clearInfo();
            return;
        }

        for (HoaDonDTO hd : dsHoaDon) {
            model.addRow(new Object[]{
                    hd.getMaHoaDon(),
                    buildDisplayName(hd.getTenKhachHang(), hd.getMaKH()),
                    buildDisplayName(hd.getTenNhanVien(), hd.getMaNV()),
                    formatDateTime(hd.getNgayLapHoaDon()),
                    formatCurrency(hd.getTongThanhToan())
            });
        }

        if (model.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
            capNhatThongTinHoaDonDangChon();
        } else {
            clearInfo();
        }
    }

    private void capNhatThongTinHoaDonDangChon() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            clearInfo();
            return;
        }

        Object maHoaDonObj = table.getValueAt(selectedRow, 0);
        if (maHoaDonObj == null) {
            clearInfo();
            return;
        }

        HoaDonDTO hd = hoaDonBUS.findById(Integer.parseInt(maHoaDonObj.toString()));
        if (hd == null) {
            clearInfo();
            return;
        }

        lblMaHoaDonValue.setText(String.valueOf(hd.getMaHoaDon()));
        lblKhachHangValue.setText(buildDisplayName(hd.getTenKhachHang(), hd.getMaKH()));
        lblNhanVienValue.setText(buildDisplayName(hd.getTenNhanVien(), hd.getMaNV()));
        lblNgayLapValue.setText(formatDateTime(hd.getNgayLapHoaDon()));
        lblTongTienValue.setText(formatCurrency(hd.getTongThanhToan()));
    }

    private void xemChiTietHoaDon() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một hóa đơn trong bảng.",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        HoaDonDTO hd = hoaDonBUS.findById(Integer.parseInt(table.getValueAt(selectedRow, 0).toString()));
        if (hd == null) {
            JOptionPane.showMessageDialog(this,
                    "Không lấy được thông tin chi tiết hóa đơn.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String detail = "Mã hóa đơn: " + hd.getMaHoaDon() +
                "\nKhách hàng: " + buildDisplayName(hd.getTenKhachHang(), hd.getMaKH()) +
                "\nNhân viên: " + buildDisplayName(hd.getTenNhanVien(), hd.getMaNV()) +
                "\nNgày lập hóa đơn: " + formatDateTime(hd.getNgayLapHoaDon()) +
                "\nTổng tiền vé: " + formatCurrency(hd.getTongTienVe()) +
                "\nTổng tiền sản phẩm: " + formatCurrency(hd.getTongTienSanPham()) +
                "\nTổng thanh toán: " + formatCurrency(hd.getTongThanhToan());

        JOptionPane.showMessageDialog(this, detail, "Chi tiết hóa đơn", JOptionPane.INFORMATION_MESSAGE);
    }

    private String buildDisplayName(String hoTen, int ma) {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            return String.valueOf(ma);
        }
        return hoTen + " (" + ma + ")";
    }

    private void clearInfo() {
        lblMaHoaDonValue.setText("___");
        lblKhachHangValue.setText("___");
        lblNhanVienValue.setText("___");
        lblNgayLapValue.setText("___");
        lblTongTienValue.setText("___");
    }

    private String formatCurrency(int amount) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return nf.format(amount) + " đ";
    }

    private String formatDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(timestamp);
    }

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
