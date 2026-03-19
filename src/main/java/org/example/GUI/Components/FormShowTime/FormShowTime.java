package org.example.GUI.Components.FormShowTime;

import org.example.BUS.SuatChieuPhimBUS;
import org.example.DTO.SuatChieuPhimDTO;

import java.awt.*;
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
    private JButton btnXem, btnSua, btnXoa, btnThem, btnTimKiem, btnLamMoi;
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
        setBackground(new Color(245, 245, 250));

        // ================== KHU VỰC TRÊN (NÚT BẤM & TÌM KIẾM) ==================
        JPanel pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.setOpaque(false);
        pnlNorth.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Quản Lý Lịch Chiếu Phim", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)));

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlButtons.setOpaque(false);
        btnXem = createButton("Xem", new Color(0, 123, 255), Color.WHITE);
        btnSua = createButton("Sửa", new Color(255, 193, 7), Color.BLACK);
        btnXoa = createButton("Xóa", new Color(220, 53, 69), Color.WHITE);
        btnThem = createButton("Thêm", new Color(40, 167, 69), Color.WHITE);
        btnLamMoi = createButton("Làm mới", new Color(100, 181, 246), Color.WHITE, "/org/example/GUI/resources/images/icons8_data_backup_30px.png");
        btnLamMoi.addActionListener(e ->{
            if (table.getRowSorter() != null) {
                table.getRowSorter().setSortKeys(null);
            }
            txtSearch.setText("");
            cbTieuChi.setSelectedIndex(0);
            loadDataToTable();
        });

        pnlButtons.add(btnXem); pnlButtons.add(btnSua); pnlButtons.add(btnXoa); pnlButtons.add(btnThem); pnlButtons.add(btnLamMoi);

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlFilter.setOpaque(false);
        cbTieuChi = new JComboBox<>(new String[]{"Mã SC", "Tên Phim", "MaPhong"});
        cbTieuChi.setPreferredSize(new Dimension(100, 35));
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 35));
        btnTimKiem = createButton("Tìm kiếm", new Color(230, 230, 230), Color.BLACK);

        pnlFilter.add(cbTieuChi); pnlFilter.add(txtSearch); pnlFilter.add(btnTimKiem);
        pnlNorth.add(pnlButtons, BorderLayout.WEST); pnlNorth.add(pnlFilter, BorderLayout.EAST);

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
        table.getTableHeader().setBackground(new Color(66, 103, 178));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // ================== KHU VỰC DƯỚI (CHI TIẾT) ==================
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBackground(Color.WHITE);
        pnlSouth.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Chi tiết Suất Chiếu", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)));

        JPanel pnlForm = new JPanel(new GridLayout(3, 4, 15, 10));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(new EmptyBorder(10, 50, 10, 50));

        txtMaSC = new JTextField();
        txtMaSC.setEditable(false);
        txtMaSC.setBackground(new Color(230, 230, 230));
        txtMaSC.setFocusable(false);

        txtMaPhim = new JTextField();
        txtMaPhong = new JTextField();
        txtGiaVe = new JTextField();

        // Khởi tạo JSpinner cho thời gian
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

        add(pnlNorth, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(pnlSouth, BorderLayout.SOUTH);
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
        btn.setBackground(bg);
        btn.setForeground(fg);
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