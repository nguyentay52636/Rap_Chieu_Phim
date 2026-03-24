package org.example.GUI.Components.FormShowTime;

import org.example.BUS.SuatChieuPhimBUS;
import org.example.DTO.SuatChieuPhimDTO;

import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class FormShowTime extends JPanel {

    private DefaultTableModel model;
    private JTable table;

    // Các Component nhập liệu
    private JTextField txtMaSC, txtMaPhim, txtMaPhong, txtGiaVe, txtSearch;
    private JButton btnXem, btnSua, btnXoa, btnThem, btnTimKiem, btnLamMoi, btnTimNangCao, btnXuatExcel, btnNhapExcel;
    private JComboBox<String> cbTieuChi;

    // Sử dụng JSpinner thuần của Java thay vì Textfield hay Thư viện ngoài
    private JSpinner spnBatDau;
    private JSpinner spnKetThuc;

    // Tầng nghiệp vụ và định dạng
    private final SuatChieuPhimBUS suatChieuBUS = new SuatChieuPhimBUS();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public FormShowTime() {
        initComponents();
        addEvents();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // ================== KHU VỰC TRÊN (NÚT BẤM & TÌM KIẾM) ==================
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10)); // Khoảng cách dọc 10px giữa 2 dòng

        // 1. DÒNG TRÊN: Các nút chức năng (Không viền)
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));

        // Cập nhật màu sắc giống màn hình Nhân viên
        btnXem = createButton("Xem", new Color(92, 184, 92), Color.WHITE);
        btnSua = createButton("Sửa", new Color(240, 173, 78), Color.WHITE);
        btnXoa = createButton("Xóa", new Color(217, 83, 79), Color.WHITE);
        btnThem = createButton("Thêm", new Color(2, 117, 216), Color.WHITE);

        // Thêm 2 nút Excel
        btnNhapExcel = createButton("Nhập Excel", new Color(138, 43, 226), Color.WHITE);
        btnXuatExcel = createButton("Xuất Excel", new Color(40, 167, 69), Color.WHITE);
        pnlButtons.add(btnXem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnThem);
        pnlButtons.add(btnNhapExcel);
        pnlButtons.add(btnXuatExcel);

        // 2. DÒNG DƯỚI: Khu vực Tìm kiếm (Có viền TitledBorder)
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        pnlFilter.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.PLAIN, 13)));

        cbTieuChi = new JComboBox<>(new String[]{"Mã SC", "Tên Phim", "Phòng"});
        cbTieuChi.setPrototypeDisplayValue("Tên Phim      "); // Chống teo nhỏ combobox
        cbTieuChi.setPreferredSize(new Dimension(110, 35));

        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(200, 35));

        // Nút Làm mới đổi màu xanh ngọc giống ảnh
        btnLamMoi = createButton("Làm mới", new Color(91, 192, 222), Color.WHITE);

        // Vẫn giữ lại 2 nút Tìm kiếm này (bạn có thể ẩn đi nếu form Nhân viên tìm kiếm tự động realtime)
        btnTimKiem = createButton("Tìm kiếm", null, null);
        btnTimNangCao = createButton("Tìm nâng cao", null, null);
        btnTimNangCao.setPreferredSize(new Dimension(120, 35));

        // Sự kiện cho nút làm mới
        btnLamMoi.addActionListener(e ->{
            if (table.getRowSorter() != null) {
                table.getRowSorter().setSortKeys(null);
            }
            txtSearch.setText("");
            cbTieuChi.setSelectedIndex(0);
            loadDataToTable();
        });

        pnlFilter.add(cbTieuChi);
        pnlFilter.add(txtSearch);
        pnlFilter.add(btnTimKiem);
        pnlFilter.add(btnLamMoi);
        pnlFilter.add(btnTimNangCao);

        // Ghép 2 dòng vào pnlNorth
        pnlNorth.add(pnlButtons, BorderLayout.NORTH);
        pnlNorth.add(pnlFilter, BorderLayout.CENTER);


        // ================== KHU VỰC GIỮA (BẢNG DỮ LIỆU) ==================
        String[] columns = {"Mã SC", "Mã Phim", "Phòng", "Bắt đầu", "Kết thúc", "Giá Vé"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);


        // ================== KHU VỰC DƯỚI (CHI TIẾT) ==================
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Chi tiết Suất Chiếu", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)));

        JPanel pnlForm = new JPanel(new GridLayout(3, 4, 15, 10));
        pnlForm.setBorder(new EmptyBorder(10, 50, 10, 50));

        txtMaSC = new JTextField();
        txtMaSC.setEditable(false);
        txtMaSC.setFocusable(false);

        txtMaPhim = new JTextField();
        txtMaPhong = new JTextField();
        txtGiaVe = new JTextField();

        spnBatDau = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorBatDau = new JSpinner.DateEditor(spnBatDau, "dd/MM/yyyy HH:mm");
        spnBatDau.setEditor(editorBatDau);

        spnKetThuc = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorKetThuc = new JSpinner.DateEditor(spnKetThuc, "dd/MM/yyyy HH:mm");
        spnKetThuc.setEditor(editorKetThuc);

        pnlForm.add(createLabel("Mã SC (Tự động):")); pnlForm.add(txtMaSC);
        pnlForm.add(createLabel("Bắt đầu:"));         pnlForm.add(spnBatDau);
        pnlForm.add(createLabel("Mã Phim:"));         pnlForm.add(txtMaPhim);
        pnlForm.add(createLabel("Kết thúc:"));        pnlForm.add(spnKetThuc);
        pnlForm.add(createLabel("Phòng chiếu:"));     pnlForm.add(txtMaPhong);
        pnlForm.add(createLabel("Giá vé (VNĐ):"));    pnlForm.add(txtGiaVe);

        pnlSouth.add(pnlForm, BorderLayout.CENTER);

        // GHÉP TẤT CẢ VÀO FORM CHÍNH
        add(pnlNorth, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(pnlSouth, BorderLayout.SOUTH); // Mình vẫn giữ lại phần Chi tiết ở dưới cho bạn nhập liệu nhé
    }

    private void loadDataToTable() {
        model.setRowCount(0);
        List<SuatChieuPhimDTO> list = suatChieuBUS.getListSuatChieu();
        for (SuatChieuPhimDTO sc : list) {
            model.addRow(new Object[]{
                    sc.getMaSuatChieu(),
                    sc.getMaPhim(),
                    sc.getMaPhong(),
                    sc.getGioBatDau() != null ? sc.getGioBatDau().format(formatter) : "",
                    sc.getGioKetThuc() != null ? sc.getGioKetThuc().format(formatter) : "",
                    String.format("%,.0f", sc.getGiaVeGoc())
            });
        }
    }

    private void addEvents() {
        // Click bảng hiển thị lên Form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int modelRow = table.convertRowIndexToModel(row);

                txtMaSC.setText(model.getValueAt(modelRow, 0).toString());
                txtMaPhim.setText(model.getValueAt(modelRow, 1).toString());
                txtMaPhong.setText(model.getValueAt(modelRow, 2).toString());

                // Đọc chuỗi thời gian từ bảng -> chuyển thành java.util.Date để nạp vào JSpinner
                String strBatDau = model.getValueAt(modelRow, 3).toString();
                String strKetThuc = model.getValueAt(modelRow, 4).toString();

                if (!strBatDau.isEmpty()) {
                    LocalDateTime ldtBatDau = LocalDateTime.parse(strBatDau, formatter);
                    Date dateBatDau = Date.from(ldtBatDau.atZone(ZoneId.systemDefault()).toInstant());
                    spnBatDau.setValue(dateBatDau);
                }

                if (!strKetThuc.isEmpty()) {
                    LocalDateTime ldtKetThuc = LocalDateTime.parse(strKetThuc, formatter);
                    Date dateKetThuc = Date.from(ldtKetThuc.atZone(ZoneId.systemDefault()).toInstant());
                    spnKetThuc.setValue(dateKetThuc);
                }

                txtGiaVe.setText(model.getValueAt(modelRow, 5).toString().replace(",", ""));
            }
        });

        btnXem.addActionListener(e -> {
            suatChieuBUS.refreshList();
            loadDataToTable();
            clearForm();
        });

        btnThem.addActionListener(e -> {
            try {
                SuatChieuPhimDTO sc = getFormInput(false);
                if (suatChieuBUS.add(sc)) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công!");
                    loadDataToTable();
                    clearForm();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnSua.addActionListener(e -> {
            try {
                SuatChieuPhimDTO sc = getFormInput(true);
                if (suatChieuBUS.update(sc)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    loadDataToTable();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnXoa.addActionListener(e -> {
            if (txtMaSC.getText().isEmpty()) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?", "Xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(txtMaSC.getText());
                if (suatChieuBUS.delete(id)) {
                    JOptionPane.showMessageDialog(this, "Đã xóa!");
                    loadDataToTable();
                    clearForm();
                }
            }
        });
        btnTimNangCao.addActionListener(e -> {
            // 1. Mở Dialog
            ScAdvancedSearchDialog dialog = new ScAdvancedSearchDialog((Frame) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);

            // 2. Nếu người dùng bấm "Lọc Kết Quả"
            if (dialog.isConfirmed()) {
                List<SuatChieuPhimDTO> kq = suatChieuBUS.searchAdvanced(
                        dialog.getMaPhim(),
                        dialog.getMaPhong(),
                        dialog.getTuNgay(),
                        dialog.getDenNgay()
                );

                // 3. Hiển thị lên bảng
                model.setRowCount(0);
                for (SuatChieuPhimDTO sc : kq) {
                    model.addRow(new Object[]{
                            sc.getMaSuatChieu(), sc.getMaPhim(), sc.getMaPhong(),
                            sc.getGioBatDau().format(formatter), sc.getGioKetThuc().format(formatter),
                            String.format("%,.0f", sc.getGiaVeGoc())
                    });
                }
            }
        });
        btnXuatExcel.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                // Tự động thêm đuôi .xlsx nếu người dùng quên gõ
                if (!fileToSave.getName().endsWith(".xlsx")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".xlsx");
                }

                try {
                    suatChieuBUS.exportExcel(fileToSave);
                    JOptionPane.showMessageDialog(this, "Xuất Excel thành công!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xuất file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnNhapExcel.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn file Excel để nhập");
            int userSelection = fileChooser.showOpenDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToOpen = fileChooser.getSelectedFile();
                try {
                    String result = suatChieuBUS.importExcel(fileToOpen);
                    JOptionPane.showMessageDialog(this, result, "Kết quả nhập Excel", JOptionPane.INFORMATION_MESSAGE);
                    loadDataToTable(); // Cập nhật lại bảng
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "File không đúng định dạng hoặc bị lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnTimKiem.addActionListener(e -> {
            String tieuChi = cbTieuChi.getSelectedItem().toString();
            String tuKhoa = txtSearch.getText();
            List<SuatChieuPhimDTO> result = suatChieuBUS.search(tieuChi, tuKhoa);
            model.setRowCount(0);
            for (SuatChieuPhimDTO sc : result) {
                model.addRow(new Object[]{
                        sc.getMaSuatChieu(), sc.getMaPhim(), sc.getMaPhong(),
                        sc.getGioBatDau().format(formatter), sc.getGioKetThuc().format(formatter),
                        String.format("%,.0f", sc.getGiaVeGoc())
                });
            }
        });

        spnBatDau.addChangeListener(e -> {
            // Lấy thời gian bắt đầu hiện tại trong Spinner
            Date dateBatDau = (Date) spnBatDau.getValue();
            LocalDateTime batDau = dateBatDau.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            // Tự động làm tròn phút (để gợi ý người dùng chọn giờ đẹp 00 hoặc 30)
            int phut = batDau.getMinute();
            if (phut != 0 && phut != 30) {
                // Nếu phút < 15 thì làm tròn về 00, > 15 và < 45 thì về 30, còn lại đẩy lên giờ tiếp theo
                if (phut < 15) phut = 0;
                else if (phut < 45) phut = 30;
                else {
                    phut = 0;
                    batDau = batDau.plusHours(1);
                }
                // Tạm thời vô hiệu hóa để tránh lặp vô tận (infinite loop) khi tự đổi value của chính nó
                spnBatDau.removeChangeListener((javax.swing.event.ChangeListener) spnBatDau.getChangeListeners()[0]);

                batDau = batDau.withMinute(phut).withSecond(0).withNano(0);
                spnBatDau.setValue(Date.from(batDau.atZone(ZoneId.systemDefault()).toInstant()));

                // Cài đặt lại Listener
                spnBatDau.addChangeListener((javax.swing.event.ChangeListener) e.getSource());
            }

            // Tự động set Giờ kết thúc = Giờ bắt đầu + 3 tiếng (Tối đa)
            LocalDateTime ketThuc = batDau.plusHours(3);
            spnKetThuc.setValue(Date.from(ketThuc.atZone(ZoneId.systemDefault()).toInstant()));
        });
    }

    private SuatChieuPhimDTO getFormInput(boolean isUpdate) throws Exception {
        try {
            int maSC = isUpdate ? Integer.parseInt(txtMaSC.getText()) : 0;
            int maPhim = Integer.parseInt(txtMaPhim.getText().trim());
            int maPhong = Integer.parseInt(txtMaPhong.getText().trim());
            double giaVe = Double.parseDouble(txtGiaVe.getText().trim());

            // Lấy thời gian từ JSpinner (java.util.Date) rồi đổi sang LocalDateTime
            Date dateBatDau = (Date) spnBatDau.getValue();
            LocalDateTime batDau = dateBatDau.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            Date dateKetThuc = (Date) spnKetThuc.getValue();
            LocalDateTime ketThuc = dateKetThuc.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            return new SuatChieuPhimDTO(maSC, maPhim, maPhong, batDau, ketThuc, giaVe);

        } catch (NumberFormatException e) {
            throw new Exception("Lỗi: Mã phim, phòng hoặc giá vé phải là số hợp lệ!");
        }
    }

    private void clearForm() {
        txtMaSC.setText(""); txtMaPhim.setText(""); txtMaPhong.setText(""); txtGiaVe.setText("");
        // Đặt lại thời gian hiện tại cho 2 ô Spinner
        spnBatDau.setValue(new Date());
        spnKetThuc.setValue(new Date());
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(100, 35));
        // Chỉ set màu nền nếu bg != null, để FlatLaf tự chỉnh các nút chức năng khác
        if(bg != null) btn.setBackground(bg);
        if(fg != null) btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        return btn;
    }

    private JButton createButton(String text, Color bg, Color fg, String iconPath) {
        JButton btn = createButton(text, bg, fg);
        try {
            java.net.URL imgURL = getClass().getResource(iconPath);
            if (imgURL != null) {
                btn.setIcon(new ImageIcon(imgURL));
            }
        } catch (Exception e) {
            System.err.println("Không tìm thấy icon: " + iconPath);
        }
        return btn;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.RIGHT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return lbl;
    }
}