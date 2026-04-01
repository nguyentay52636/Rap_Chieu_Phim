package org.example.GUI.Components.FormHoaDon;

import org.example.BUS.HoaDonBUS;
import org.example.DTO.HoaDonDTO;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    private ArrayList<HoaDonDTO> danhSachHoaDonHienTai;

    public FormHoaDon(String title) {
        this.hoaDonBUS = new HoaDonBUS();
        this.danhSachHoaDonHienTai = new ArrayList<>();

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
        btnExcel.addActionListener(e -> xuatDanhSachHoaDonExcel());

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
        danhSachHoaDonHienTai = (dsHoaDon == null) ? new ArrayList<>() : new ArrayList<>(dsHoaDon);

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

    private void xuatDanhSachHoaDonExcel() {
        try {
            if (danhSachHoaDonHienTai == null || danhSachHoaDonHienTai.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không có dữ liệu hóa đơn để xuất.",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu danh sách hóa đơn");
            fileChooser.setSelectedFile(new File(taoTenFileMacDinh()));

            int luaChon = fileChooser.showSaveDialog(this);
            if (luaChon != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".xlsx")) {
                file = new File(file.getAbsolutePath() + ".xlsx");
            }

            if (file.exists()) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "File đã tồn tại. Bạn có muốn ghi đè không?",
                        "Xác nhận ghi đè",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            ghiFileExcel(file, danhSachHoaDonHienTai);

            JOptionPane.showMessageDialog(this,
                    "Xuất Excel thành công:\n" + file.getAbsolutePath(),
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Xuất Excel thất bại. Vui lòng thử lại.",
                    "Lỗi hệ thống",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private String taoTenFileMacDinh() {
        String maHD = txtMaHoaDon.getText().trim();
        String maKH = txtMaKH.getText().trim();
        String maNV = txtMaNV.getText().trim();
        String thoiGian = String.valueOf(cboThoiGian.getSelectedItem()).replaceAll("\\s+", "-").toLowerCase(Locale.ROOT);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String suffix = (maHD.isEmpty() ? "tatca" : "hd-" + maHD)
                + "_" + (maKH.isEmpty() ? "kh-tatca" : "kh-" + maKH)
                + "_" + (maNV.isEmpty() ? "nv-tatca" : "nv-" + maNV)
                + "_" + thoiGian;
        return "DanhSachHoaDon_" + suffix + "_" + timestamp + ".xlsx";
    }

    private void ghiFileExcel(File file, List<HoaDonDTO> dsHoaDon) throws IOException {
        List<List<ExcelCell>> summaryRows = taoDuLieuSheetTongHop(dsHoaDon);
        List<List<ExcelCell>> detailRows = taoDuLieuSheetChiTiet(dsHoaDon);

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file))) {
            writeZipEntry(zos, "[Content_Types].xml", contentTypesXml());
            writeZipEntry(zos, "_rels/.rels", rootRelsXml());
            writeZipEntry(zos, "docProps/app.xml", appXml());
            writeZipEntry(zos, "docProps/core.xml", coreXml());

            writeZipEntry(zos, "xl/workbook.xml", workbookXml());
            writeZipEntry(zos, "xl/_rels/workbook.xml.rels", workbookRelsXml());
            writeZipEntry(zos, "xl/styles.xml", stylesXml());
            writeZipEntry(zos, "xl/worksheets/sheet1.xml", sheetSummaryXml(summaryRows));
            writeZipEntry(zos, "xl/worksheets/sheet2.xml", sheetDetailXml(detailRows));
        }
    }

    private List<List<ExcelCell>> taoDuLieuSheetTongHop(List<HoaDonDTO> dsHoaDon) {
        List<List<ExcelCell>> rows = new ArrayList<>();
        long tongTienVe = 0;
        long tongTienSanPham = 0;
        long tongThanhToan = 0;

        for (HoaDonDTO hd : dsHoaDon) {
            tongTienVe += hd.getTongTienVe();
            tongTienSanPham += hd.getTongTienSanPham();
            tongThanhToan += hd.getTongThanhToan();
        }

        rows.add(List.of(ExcelCell.text("BÁO CÁO DANH SÁCH HÓA ĐƠN", 1)));
        rows.add(new ArrayList<>());
        rows.add(List.of(
                ExcelCell.text("Mã hóa đơn", 2), ExcelCell.text(giaTriBoLoc(txtMaHoaDon.getText().trim()), 4),
                ExcelCell.text("Khách hàng", 2), ExcelCell.text(giaTriBoLoc(txtMaKH.getText().trim()), 4)
        ));
        rows.add(List.of(
                ExcelCell.text("Nhân viên", 2), ExcelCell.text(giaTriBoLoc(txtMaNV.getText().trim()), 4),
                ExcelCell.text("Thời gian lọc", 2), ExcelCell.text(giaTriBoLoc(String.valueOf(cboThoiGian.getSelectedItem())), 4)
        ));
        rows.add(List.of(
                ExcelCell.text("Ngày xuất báo cáo", 2),
                ExcelCell.text(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), 4)
        ));
        rows.add(new ArrayList<>());
        rows.add(List.of(ExcelCell.text("CHỈ SỐ", 3), ExcelCell.text("GIÁ TRỊ", 3)));
        rows.add(List.of(ExcelCell.text("Số lượng hóa đơn", 2), ExcelCell.number(dsHoaDon.size(), 6)));
        rows.add(List.of(ExcelCell.text("Tổng tiền vé", 2), ExcelCell.number(tongTienVe, 5)));
        rows.add(List.of(ExcelCell.text("Tổng tiền sản phẩm", 2), ExcelCell.number(tongTienSanPham, 5)));
        rows.add(List.of(ExcelCell.text("Tổng thanh toán", 2), ExcelCell.number(tongThanhToan, 5)));

        return rows;
    }

    private List<List<ExcelCell>> taoDuLieuSheetChiTiet(List<HoaDonDTO> dsHoaDon) {
        List<List<ExcelCell>> rows = new ArrayList<>();

        rows.add(List.of(ExcelCell.text("DANH SÁCH HÓA ĐƠN", 1)));
        rows.add(new ArrayList<>());
        rows.add(List.of(
                ExcelCell.text("Mã Hóa Đơn", 3),
                ExcelCell.text("Khách Hàng", 3),
                ExcelCell.text("Nhân Viên", 3),
                ExcelCell.text("Ngày Lập", 3),
                ExcelCell.text("Tổng Tiền Vé", 3),
                ExcelCell.text("Tổng Tiền Sản Phẩm", 3),
                ExcelCell.text("Tổng Thanh Toán", 3)
        ));

        if (dsHoaDon == null || dsHoaDon.isEmpty()) {
            rows.add(List.of(ExcelCell.text("Không có dữ liệu hóa đơn", 4)));
            return rows;
        }

        for (HoaDonDTO hd : dsHoaDon) {
            rows.add(List.of(
                    ExcelCell.number(hd.getMaHoaDon(), 4),
                    ExcelCell.text(buildDisplayName(hd.getTenKhachHang(), hd.getMaKH()), 7),
                    ExcelCell.text(buildDisplayName(hd.getTenNhanVien(), hd.getMaNV()), 7),
                    ExcelCell.text(formatDateTime(hd.getNgayLapHoaDon()), 4),
                    ExcelCell.number(hd.getTongTienVe(), 5),
                    ExcelCell.number(hd.getTongTienSanPham(), 5),
                    ExcelCell.number(hd.getTongThanhToan(), 5)
            ));
        }

        return rows;
    }

    private void writeZipEntry(ZipOutputStream zos, String name, String content) throws IOException {
        ZipEntry entry = new ZipEntry(name);
        zos.putNextEntry(entry);
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        zos.write(bytes);
        zos.closeEntry();
    }

    private String contentTypesXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">"
                + "<Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>"
                + "<Default Extension=\"xml\" ContentType=\"application/xml\"/>"
                + "<Override PartName=\"/xl/workbook.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml\"/>"
                + "<Override PartName=\"/xl/styles.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml\"/>"
                + "<Override PartName=\"/xl/worksheets/sheet1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>"
                + "<Override PartName=\"/xl/worksheets/sheet2.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>"
                + "<Override PartName=\"/docProps/core.xml\" ContentType=\"application/vnd.openxmlformats-package.core-properties+xml\"/>"
                + "<Override PartName=\"/docProps/app.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.extended-properties+xml\"/>"
                + "</Types>";
    }

    private String rootRelsXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"xl/workbook.xml\"/>"
                + "<Relationship Id=\"rId2\" Type=\"http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties\" Target=\"docProps/core.xml\"/>"
                + "<Relationship Id=\"rId3\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties\" Target=\"docProps/app.xml\"/>"
                + "</Relationships>";
    }

    private String workbookXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<workbook xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\" "
                + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">"
                + "<sheets>"
                + "<sheet name=\"TongHop\" sheetId=\"1\" r:id=\"rId1\"/>"
                + "<sheet name=\"DanhSachHoaDon\" sheetId=\"2\" r:id=\"rId2\"/>"
                + "</sheets>"
                + "</workbook>";
    }

    private String workbookRelsXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet1.xml\"/>"
                + "<Relationship Id=\"rId2\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet2.xml\"/>"
                + "<Relationship Id=\"rId3\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles\" Target=\"styles.xml\"/>"
                + "</Relationships>";
    }

    private String stylesXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<styleSheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">"
                + "<numFmts count=\"1\">"
                + "<numFmt numFmtId=\"164\" formatCode=\"#,##0 &quot;VNĐ&quot;\"/>"
                + "</numFmts>"
                + "<fonts count=\"3\">"
                + "<font><sz val=\"11\"/><name val=\"Calibri\"/></font>"
                + "<font><b/><sz val=\"11\"/><name val=\"Calibri\"/><color rgb=\"FFFFFFFF\"/></font>"
                + "<font><b/><sz val=\"16\"/><name val=\"Calibri\"/></font>"
                + "</fonts>"
                + "<fills count=\"5\">"
                + "<fill><patternFill patternType=\"none\"/></fill>"
                + "<fill><patternFill patternType=\"gray125\"/></fill>"
                + "<fill><patternFill patternType=\"solid\"><fgColor rgb=\"FF1F4E78\"/><bgColor indexed=\"64\"/></patternFill></fill>"
                + "<fill><patternFill patternType=\"solid\"><fgColor rgb=\"FFD9EAF7\"/><bgColor indexed=\"64\"/></patternFill></fill>"
                + "<fill><patternFill patternType=\"solid\"><fgColor rgb=\"FFF2F2F2\"/><bgColor indexed=\"64\"/></patternFill></fill>"
                + "</fills>"
                + "<borders count=\"2\">"
                + "<border><left/><right/><top/><bottom/><diagonal/></border>"
                + "<border><left style=\"thin\"/><right style=\"thin\"/><top style=\"thin\"/><bottom style=\"thin\"/><diagonal/></border>"
                + "</borders>"
                + "<cellStyleXfs count=\"1\"><xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\"/></cellStyleXfs>"
                + "<cellXfs count=\"8\">"
                + "<xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\" xfId=\"0\"/>"
                + "<xf numFmtId=\"0\" fontId=\"2\" fillId=\"0\" borderId=\"0\" xfId=\"0\" applyFont=\"1\" applyAlignment=\"1\"><alignment horizontal=\"center\" vertical=\"center\"/></xf>"
                + "<xf numFmtId=\"0\" fontId=\"0\" fillId=\"4\" borderId=\"1\" xfId=\"0\" applyFill=\"1\" applyBorder=\"1\" applyAlignment=\"1\"><alignment horizontal=\"left\" vertical=\"center\"/></xf>"
                + "<xf numFmtId=\"0\" fontId=\"1\" fillId=\"2\" borderId=\"1\" xfId=\"0\" applyFont=\"1\" applyFill=\"1\" applyBorder=\"1\" applyAlignment=\"1\"><alignment horizontal=\"center\" vertical=\"center\"/></xf>"
                + "<xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"1\" xfId=\"0\" applyBorder=\"1\" applyAlignment=\"1\"><alignment horizontal=\"center\" vertical=\"center\"/></xf>"
                + "<xf numFmtId=\"164\" fontId=\"0\" fillId=\"0\" borderId=\"1\" xfId=\"0\" applyNumberFormat=\"1\" applyBorder=\"1\" applyAlignment=\"1\"><alignment horizontal=\"right\" vertical=\"center\"/></xf>"
                + "<xf numFmtId=\"1\" fontId=\"0\" fillId=\"0\" borderId=\"1\" xfId=\"0\" applyNumberFormat=\"1\" applyBorder=\"1\" applyAlignment=\"1\"><alignment horizontal=\"center\" vertical=\"center\"/></xf>"
                + "<xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"1\" xfId=\"0\" applyBorder=\"1\" applyAlignment=\"1\"><alignment horizontal=\"left\" vertical=\"center\" wrapText=\"1\"/></xf>"
                + "</cellXfs>"
                + "<cellStyles count=\"1\"><cellStyle name=\"Normal\" xfId=\"0\" builtinId=\"0\"/></cellStyles>"
                + "</styleSheet>";
    }

    private String appXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<Properties xmlns=\"http://schemas.openxmlformats.org/officeDocument/2006/extended-properties\" "
                + "xmlns:vt=\"http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes\">"
                + "<Application>Java</Application>"
                + "<TitlesOfParts><vt:vector size=\"2\" baseType=\"lpstr\"><vt:lpstr>TongHop</vt:lpstr><vt:lpstr>DanhSachHoaDon</vt:lpstr></vt:vector></TitlesOfParts>"
                + "<HeadingPairs><vt:vector size=\"2\" baseType=\"variant\"><vt:variant><vt:lpstr>Worksheets</vt:lpstr></vt:variant><vt:variant><vt:i4>2</vt:i4></vt:variant></vt:vector></HeadingPairs>"
                + "</Properties>";
    }

    private String coreXml() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<cp:coreProperties xmlns:cp=\"http://schemas.openxmlformats.org/package/2006/metadata/core-properties\" "
                + "xmlns:dc=\"http://purl.org/dc/elements/1.1/\" "
                + "xmlns:dcterms=\"http://purl.org/dc/terms/\" "
                + "xmlns:dcmitype=\"http://purl.org/dc/dcmitype/\" "
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "<dc:title>Danh sach hoa don</dc:title>"
                + "<dc:creator>FormHoaDon</dc:creator>"
                + "<cp:lastModifiedBy>FormHoaDon</cp:lastModifiedBy>"
                + "<dcterms:created xsi:type=\"dcterms:W3CDTF\">" + now + "Z</dcterms:created>"
                + "<dcterms:modified xsi:type=\"dcterms:W3CDTF\">" + now + "Z</dcterms:modified>"
                + "</cp:coreProperties>";
    }

    private String sheetSummaryXml(List<List<ExcelCell>> rows) {
        List<Double> widths = calculateColumnWidths(rows, 4);
        List<String> merges = List.of("A1:D1");
        return buildSheetXml(rows, widths, merges, "A7:B11");
    }

    private String sheetDetailXml(List<List<ExcelCell>> rows) {
        List<Double> widths = calculateColumnWidths(rows, 7);
        List<String> merges = new ArrayList<>();
        merges.add("A1:G1");
        if (rows.size() == 4 && rows.get(3).size() == 1) {
            merges.add("A4:G4");
        }
        return buildSheetXml(rows, widths, merges, "A3:G" + Math.max(3, rows.size()));
    }

    private String buildSheetXml(List<List<ExcelCell>> rows,
                                 List<Double> widths,
                                 List<String> mergeRefs,
                                 String autoFilterRef) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">\n");
        sb.append("<sheetViews><sheetView workbookViewId=\"0\"/></sheetViews>");
        sb.append("<sheetFormatPr defaultRowHeight=\"20\"/>");
        sb.append(buildColsXml(widths));
        sb.append("<sheetData>");

        for (int r = 0; r < rows.size(); r++) {
            List<ExcelCell> row = rows.get(r);
            int rowNumber = r + 1;
            sb.append("<row r=\"").append(rowNumber).append("\"");
            if (rowNumber == 1) {
                sb.append(" ht=\"26\" customHeight=\"1\"");
            }
            sb.append(">");

            for (int c = 0; c < row.size(); c++) {
                ExcelCell cell = row.get(c);
                if (cell == null || cell.value == null || String.valueOf(cell.value).isEmpty()) {
                    continue;
                }
                String cellRef = excelColumnName(c + 1) + rowNumber;
                sb.append(buildCellXml(cellRef, cell));
            }
            sb.append("</row>");
        }

        sb.append("</sheetData>");

        if (autoFilterRef != null && !autoFilterRef.isBlank()) {
            sb.append("<autoFilter ref=\"").append(autoFilterRef).append("\"/>");
        }

        if (mergeRefs != null && !mergeRefs.isEmpty()) {
            sb.append("<mergeCells count=\"").append(mergeRefs.size()).append("\">");
            for (String mergeRef : mergeRefs) {
                sb.append("<mergeCell ref=\"").append(mergeRef).append("\"/>");
            }
            sb.append("</mergeCells>");
        }

        sb.append("</worksheet>");
        return sb.toString();
    }

    private String buildColsXml(List<Double> widths) {
        StringBuilder sb = new StringBuilder();
        sb.append("<cols>");
        for (int i = 0; i < widths.size(); i++) {
            double width = widths.get(i);
            sb.append("<col min=\"").append(i + 1)
                    .append("\" max=\"").append(i + 1)
                    .append("\" width=\"").append(String.format(Locale.US, "%.2f", width))
                    .append("\" customWidth=\"1\"/>");
        }
        sb.append("</cols>");
        return sb.toString();
    }

    private List<Double> calculateColumnWidths(List<List<ExcelCell>> rows, int totalColumns) {
        Map<Integer, Integer> maxLengths = new HashMap<>();
        for (int i = 0; i < totalColumns; i++) {
            maxLengths.put(i, 10);
        }

        for (List<ExcelCell> row : rows) {
            for (int c = 0; c < row.size() && c < totalColumns; c++) {
                ExcelCell cell = row.get(c);
                if (cell == null || cell.value == null) {
                    continue;
                }
                String text = cell.isNumber ? formatNumberForWidth(cell.value, cell.styleIndex) : String.valueOf(cell.value);
                int len = text.length();
                if (len > maxLengths.get(c)) {
                    maxLengths.put(c, len);
                }
            }
        }

        List<Double> widths = new ArrayList<>();
        for (int i = 0; i < totalColumns; i++) {
            int len = maxLengths.get(i);
            double width = Math.min(Math.max(len + 3, 12), 35);
            widths.add(width);
        }
        return widths;
    }

    private String formatNumberForWidth(Object value, int styleIndex) {
        long number = ((Number) value).longValue();
        if (styleIndex == 5) {
            return formatCurrency(number);
        }
        return String.format("%,d", number);
    }

    private String buildCellXml(String cellRef, ExcelCell cell) {
        if (cell.isNumber) {
            return "<c r=\"" + cellRef + "\" s=\"" + cell.styleIndex + "\"><v>" + cell.value + "</v></c>";
        }
        return "<c r=\"" + cellRef + "\" s=\"" + cell.styleIndex + "\" t=\"inlineStr\"><is><t>"
                + escapeXml(String.valueOf(cell.value))
                + "</t></is></c>";
    }

    private String excelColumnName(int columnNumber) {
        StringBuilder sb = new StringBuilder();
        int num = columnNumber;
        while (num > 0) {
            int remainder = (num - 1) % 26;
            sb.insert(0, (char) ('A' + remainder));
            num = (num - 1) / 26;
        }
        return sb.toString();
    }

    private String escapeXml(String input) {
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    private String giaTriBoLoc(String value) {
        return (value == null || value.isBlank()) ? "Tất cả" : value;
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

    private String formatCurrency(long amount) {
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

    private static class ExcelCell {
        private final Object value;
        private final int styleIndex;
        private final boolean isNumber;

        private ExcelCell(Object value, int styleIndex, boolean isNumber) {
            this.value = value;
            this.styleIndex = styleIndex;
            this.isNumber = isNumber;
        }

        public static ExcelCell text(String value, int styleIndex) {
            return new ExcelCell(value, styleIndex, false);
        }

        public static ExcelCell number(Number value, int styleIndex) {
            return new ExcelCell(value, styleIndex, true);
        }
    }
}
