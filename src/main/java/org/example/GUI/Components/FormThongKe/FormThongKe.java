package org.example.GUI.Components.FormThongKe;

import org.example.BUS.ThongKeBUS;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
        btnPrint.addActionListener(e -> xuatBaoCaoExcel());
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

    private void xuatBaoCaoExcel() {
        try {
            String tuNgay = txtTuNgay.getText().trim();
            String denNgay = txtDenNgay.getText().trim();

            Object[] tongQuan = thongKeBUS.getTongQuanThongKe(tuNgay, denNgay);
            ArrayList<Object[]> dsThongKe = thongKeBUS.getBangXepHangDoanhThuTheoPhim(tuNgay, denNgay);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu báo cáo Excel");
            fileChooser.setSelectedFile(new File(taoTenFileMacDinh(tuNgay, denNgay)));

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

            ghiFileExcel(file,
                    tuNgay,
                    denNgay,
                    ((Number) tongQuan[0]).longValue(),
                    ((Number) tongQuan[1]).intValue(),
                    ((Number) tongQuan[2]).intValue(),
                    dsThongKe);

            JOptionPane.showMessageDialog(this,
                    "Xuất báo cáo Excel thành công:\n" + file.getAbsolutePath(),
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Xuất báo cáo Excel thất bại. Vui lòng thử lại.",
                    "Lỗi hệ thống",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private String taoTenFileMacDinh(String tuNgay, String denNgay) {
        String from = (tuNgay == null || tuNgay.isEmpty()) ? "tat-ca" : tuNgay;
        String to = (denNgay == null || denNgay.isEmpty()) ? "tat-ca" : denNgay;
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return "BaoCaoThongKe_" + from + "_" + to + "_" + timestamp + ".xlsx";
    }

    private void ghiFileExcel(File file,
                              String tuNgay,
                              String denNgay,
                              long tongDoanhThu,
                              int tongSoVeBan,
                              int tongSoSanPhamBan,
                              List<Object[]> dsThongKe) throws IOException {

        List<List<ExcelCell>> summaryRows = taoDuLieuSheetTongHop(
                tuNgay,
                denNgay,
                tongDoanhThu,
                tongSoVeBan,
                tongSoSanPhamBan
        );

        List<List<ExcelCell>> detailRows = taoDuLieuSheetChiTiet(dsThongKe);

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

    private List<List<ExcelCell>> taoDuLieuSheetTongHop(String tuNgay,
                                                        String denNgay,
                                                        long tongDoanhThu,
                                                        int tongSoVeBan,
                                                        int tongSoSanPhamBan) {
        List<List<ExcelCell>> rows = new ArrayList<>();

        rows.add(List.of(
                ExcelCell.text("BÁO CÁO THỐNG KÊ DOANH THU", 1)
        ));
        rows.add(new ArrayList<>());
        rows.add(List.of(
                ExcelCell.text("Từ ngày", 2),
                ExcelCell.text(giaTriBoLoc(tuNgay), 4),
                ExcelCell.text("Đến ngày", 2),
                ExcelCell.text(giaTriBoLoc(denNgay), 4)
        ));
        rows.add(List.of(
                ExcelCell.text("Ngày xuất báo cáo", 2),
                ExcelCell.text(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), 4)
        ));
        rows.add(new ArrayList<>());
        rows.add(List.of(
                ExcelCell.text("CHỈ SỐ", 3),
                ExcelCell.text("GIÁ TRỊ", 3)
        ));
        rows.add(List.of(
                ExcelCell.text("Tổng doanh thu", 2),
                ExcelCell.number(tongDoanhThu, 5)
        ));
        rows.add(List.of(
                ExcelCell.text("Tổng số vé bán", 2),
                ExcelCell.number(tongSoVeBan, 6)
        ));
        rows.add(List.of(
                ExcelCell.text("Tổng sản phẩm bán ra", 2),
                ExcelCell.number(tongSoSanPhamBan, 6)
        ));

        return rows;
    }

    private List<List<ExcelCell>> taoDuLieuSheetChiTiet(List<Object[]> dsThongKe) {
        List<List<ExcelCell>> rows = new ArrayList<>();

        rows.add(List.of(
                ExcelCell.text("BẢNG XẾP HẠNG DOANH THU THEO PHIM", 1)
        ));
        rows.add(new ArrayList<>());
        rows.add(List.of(
                ExcelCell.text("Top", 3),
                ExcelCell.text("Mã Phim", 3),
                ExcelCell.text("Tên Phim", 3),
                ExcelCell.text("Số Vé Đã Bán", 3),
                ExcelCell.text("Tổng Doanh Thu (VNĐ)", 3)
        ));

        if (dsThongKe == null || dsThongKe.isEmpty()) {
            rows.add(List.of(
                    ExcelCell.text("Không có dữ liệu trong khoảng thời gian đã chọn", 4)
            ));
            return rows;
        }

        for (Object[] row : dsThongKe) {
            long doanhThu = ((Number) row[4]).longValue();
            rows.add(List.of(
                    ExcelCell.number(((Number) row[0]).longValue(), 4),
                    ExcelCell.number(((Number) row[1]).longValue(), 4),
                    ExcelCell.text(String.valueOf(row[2]), 7),
                    ExcelCell.number(((Number) row[3]).longValue(), 6),
                    ExcelCell.number(doanhThu, 5)
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
                + "<sheet name=\"ChiTietTheoPhim\" sheetId=\"2\" r:id=\"rId2\"/>"
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
                + "<border>"
                + "<left style=\"thin\"/><right style=\"thin\"/><top style=\"thin\"/><bottom style=\"thin\"/><diagonal/>"
                + "</border>"
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
                + "<TitlesOfParts><vt:vector size=\"2\" baseType=\"lpstr\"><vt:lpstr>TongHop</vt:lpstr><vt:lpstr>ChiTietTheoPhim</vt:lpstr></vt:vector></TitlesOfParts>"
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
                + "<dc:title>Bao cao thong ke doanh thu</dc:title>"
                + "<dc:creator>FormThongKe</dc:creator>"
                + "<cp:lastModifiedBy>FormThongKe</cp:lastModifiedBy>"
                + "<dcterms:created xsi:type=\"dcterms:W3CDTF\">" + now + "Z</dcterms:created>"
                + "<dcterms:modified xsi:type=\"dcterms:W3CDTF\">" + now + "Z</dcterms:modified>"
                + "</cp:coreProperties>";
    }

    private String sheetSummaryXml(List<List<ExcelCell>> rows) {
        List<Double> widths = calculateColumnWidths(rows, 4);
        List<String> merges = List.of("A1:D1");
        return buildSheetXml(rows, widths, merges, "A6:B9");
    }

    private String sheetDetailXml(List<List<ExcelCell>> rows) {
        List<Double> widths = calculateColumnWidths(rows, 5);
        List<String> merges = new ArrayList<>();
        merges.add("A1:E1");
        if (rows.size() == 4 && rows.get(3).size() == 1) {
            merges.add("A4:E4");
        }
        return buildSheetXml(rows, widths, merges, "A3:E" + Math.max(3, rows.size()));
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
                    .append("\" width=\"").append(String.format(java.util.Locale.US, "%.2f", width))
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

    private String formatCurrency(long value) {
        return String.format("%,d VNĐ", value);
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