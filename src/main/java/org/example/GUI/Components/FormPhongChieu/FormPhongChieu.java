package org.example.GUI.Components.FormPhongChieu;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.example.BUS.KhachHangBUS;
import org.example.BUS.PhongChieuBUS;
import org.example.DTO.GheDTO;
import org.example.DTO.KhachHangDTO;
import org.example.DTO.PhongChieuDTO;
import org.example.GUI.Components.FormCustomer.AddCustomerDialog;
import org.example.GUI.Components.FormCustomer.FormCustomer;
import org.example.UltisTable.TableUtils;
import org.example.UtilsDate.FormattedDatePicker;

public class FormPhongChieu extends JPanel {

    private DefaultTableModel model;
    private JTable tablePhongChieu;
    private final PhongChieuBUS pcBUS = new PhongChieuBUS();

    public FormPhongChieu() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 250));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 250));
        topPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Quản Lý Phòng Chiếu",
                TitledBorder.LEFT,
                TitledBorder.TOP));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttonPanel.setOpaque(false);

        JButton btnAdd = createStyledButton("Thêm", new Color(40, 167, 69), Color.WHITE);
        JButton btnEdit = createStyledButton("Sửa", new Color(255, 193, 7), Color.BLACK);
        JButton btnDelete = createStyledButton("Xóa", new Color(220, 53, 69), Color.WHITE);
        JButton btnView = createStyledButton("Xem", new Color(0, 123, 255), Color.WHITE);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnView);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        searchPanel.setOpaque(false);
        
        searchPanel.add(new JLabel("Tìm mã/tên phòng:"));
        JTextField txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 35));
        searchPanel.add(txtSearch);
        
        JButton btnSearch = createStyledButton("Lọc", new Color(0, 123, 255), Color.WHITE);
        searchPanel.add(btnSearch);

        topPanel.add(buttonPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        String[] columnNames = {"Mã Phòng", "Tên Phòng", "Loại Phòng", "Số Hàng", "Ghế/Hàng"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePhongChieu = new JTable(model);

        tablePhongChieu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablePhongChieu.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablePhongChieu.getTableHeader().setBackground(new Color(66, 103, 178));
        tablePhongChieu.getTableHeader().setForeground(Color.WHITE);
        tablePhongChieu.setRowHeight(40);

        loadDataToTable();

        JScrollPane scrollPane = new JScrollPane(tablePhongChieu);
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addRoom());
        btnEdit.addActionListener(e -> moSuaPhongDaChon());
        btnView.addActionListener(e -> moXemPhongDaChon());
        btnDelete.addActionListener(e ->deleteRoom());
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(80, 35));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        return button;
    }


    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBackground(Color.WHITE);
        cb.setForeground(new Color(33, 37, 41));
        cb.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1, true));
        if (cb.getRenderer() instanceof JLabel lbl) {
            lbl.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        }
        return cb;
    }

    private void loadDataToTable() {
        model.setRowCount(0);
        for (PhongChieuDTO pc : pcBUS.getList()) {
            model.addRow(new Object[]{
                    pc.getMaPhong(),
                    pc.getTenPhong(),
                    pc.getLoaiPhong(),
                    pc.getSoHang(),
                    pc.getSoGheMoiHang()
            });
        }
    }

    private void addRoom() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Thêm Phòng Chiếu Mới", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(owner);

        JPanel panelCenter = new JPanel(new GridLayout(4, 2, 10, 20));
        panelCenter.setBorder(new EmptyBorder(20, 20, 20, 20));
        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        panelCenter.add(createLabel("Tên Phòng: "));
        JTextField txtTenPhong = new JTextField();
        txtTenPhong.setFont(font);
        panelCenter.add(txtTenPhong);

        panelCenter.add(createLabel("Loại Phòng: "));
        JComboBox<String> optLoaiPhong = createStyledComboBox(new String[]{"2D", "3D", "4DX", "IMAX"});
        panelCenter.add(optLoaiPhong);

        panelCenter.add(createLabel("Số Hàng: "));
        JTextField txtSoHang = new JTextField();
        txtSoHang.setFont(font);
        panelCenter.add(txtSoHang);

        panelCenter.add(createLabel("Số Ghế/Hàng: "));
        JTextField txtGheHang = new JTextField();
        txtGheHang.setFont(font);
        panelCenter.add(txtGheHang);

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton btnHuy = createStyledButton("Hủy", new Color(108, 117, 125), Color.WHITE);
        JButton btnThem = createStyledButton("Lưu", new Color(40, 167, 69), Color.WHITE);

        panelBottom.add(btnThem);
        panelBottom.add(btnHuy);

        dialog.setLayout(new BorderLayout());
        dialog.add(panelCenter, BorderLayout.CENTER);
        dialog.add(panelBottom, BorderLayout.SOUTH);

        btnHuy.addActionListener(e -> dialog.dispose());

        btnThem.addActionListener(e -> {
            String tenPhong = txtTenPhong.getText().trim();
            String loaiPhong = (String) optLoaiPhong.getSelectedItem();
            String soHangStr = txtSoHang.getText().trim();
            String soGheStr = txtGheHang.getText().trim();

            if (tenPhong.isEmpty() || soHangStr.isEmpty() || soGheStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int soHang = Integer.parseInt(soHangStr);
                int soGhe = Integer.parseInt(soGheStr);

                if (soHang <= 0 || soGhe <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Số hàng và số ghế phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Gắn MaPhong = 0, CSDL sẽ tự động tăng (Auto Increment)
                PhongChieuDTO pc = new PhongChieuDTO(0, tenPhong, loaiPhong, soHang, soGhe);

                if (pcBUS.add(pc)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm phòng chiếu thành công!");
                    loadDataToTable(); // Refresh lại bảng
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thêm thất bại. Vui lòng kiểm tra lại CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Số hàng và số ghế phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void moXemPhongDaChon() {
        int viewRow = tablePhongChieu.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Chọn 1 phòng trong bảng trước!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }


        int modelRow = tablePhongChieu.convertRowIndexToModel(viewRow);
        String maPhong = String.valueOf(model.getValueAt(modelRow, 0));
        String tenPhong = String.valueOf(model.getValueAt(modelRow, 1));
        String loaiPhong = String.valueOf(model.getValueAt(modelRow, 2));
        int SoHang = (Integer) model.getValueAt(modelRow, 3);
        int SoGheMoiHang = (Integer) model.getValueAt(modelRow, 4);

        // (Bên trong moXemPhongDaChon và moSuaPhongDaChon)
        int idPhong = Integer.parseInt(maPhong);

        // Gọi BUS để lấy Phòng Chiếu KÈM THEO DANH SÁCH GHẾ
        PhongChieuDTO phong = pcBUS.getPhongChieuById(idPhong);
        List<GheDTO> danhSachGhe = phong.getGheList();

        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner);
        dialog.setTitle("Xem phòng: " + tenPhong + " (" + maPhong + ")");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(createPreviewPanel(maPhong, tenPhong, loaiPhong, SoHang, SoGheMoiHang, danhSachGhe));        dialog.setSize(1270, 640);
        dialog.setLocationRelativeTo(owner);
        dialog.setModal(false);
        dialog.setVisible(true);
    }

    private void moSuaPhongDaChon() {
        int viewRow = tablePhongChieu.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Chọn 1 phòng trong bảng trước!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tablePhongChieu.convertRowIndexToModel(viewRow);
        String maPhong = String.valueOf(model.getValueAt(modelRow, 0));
        String tenPhong = String.valueOf(model.getValueAt(modelRow, 1));
        String loaiPhong = String.valueOf(model.getValueAt(modelRow, 2));
        int SoHang = (Integer) model.getValueAt(modelRow, 3);
        int SoGheMoiHang = (Integer) model.getValueAt(modelRow, 4);

        // (Bên trong moXemPhongDaChon và moSuaPhongDaChon)
        int idPhong = Integer.parseInt(maPhong);

        // Gọi BUS để lấy Phòng Chiếu KÈM THEO DANH SÁCH GHẾ
        PhongChieuDTO phong = pcBUS.getPhongChieuById(idPhong);
        List<GheDTO> danhSachGhe = phong.getGheList();

        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner);
        dialog.setTitle("Sửa phòng: " + tenPhong + " (" + maPhong + ")");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(createUpdatePanel(maPhong, tenPhong, loaiPhong, SoHang, SoGheMoiHang, danhSachGhe));
        dialog.setSize(1270, 640);
        dialog.setLocationRelativeTo(owner);
        dialog.setModal(false);
        dialog.setVisible(true);
    }

    private JPanel createUpdatePanel(String maPhong, String tenPhong, String loaiPhong, int SoHang, int SoGheMoiHang, List<GheDTO> danhSachGhe) {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        root.setBackground(new Color(245, 245, 250));

        List<String> selectedSeats = new ArrayList<String>();

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 250));

        JPanel chairPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        chairPanel.setBackground(new Color(245, 245, 250));
        chairPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Quản Lý Ghế",
                TitledBorder.LEFT,
                TitledBorder.TOP));
        chairPanel.setOpaque(false);

        JButton btnEdit = createStyledButton("Sửa", new Color(255, 193, 7), Color.BLACK);
        JButton btnView = createStyledButton("Xem", new Color(0, 123, 255), Color.WHITE);

        chairPanel.add(btnEdit);
        chairPanel.add(btnView);

        topPanel.add(chairPanel, BorderLayout.WEST);

        JPanel dimPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        dimPanel.setBackground(new Color(245, 245, 250));
        dimPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Quản Lý Cấu Trúc",
                TitledBorder.LEFT,
                TitledBorder.TOP));
        dimPanel.setOpaque(false);

        dimPanel.add(createLabel("Độ dài(Ghế):"));
        JTextField x = new JTextField();
        x.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        dimPanel.add(createLabel("Độ rộng(Ghế):"));

        topPanel.add(dimPanel);

        root.add(topPanel, BorderLayout.NORTH);



        JPanel seatArea = new JPanel(new BorderLayout(10, 10));
        seatArea.setOpaque(false);

        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(new Color(30, 30, 35));
        screen.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel lblScreen = new JLabel("MÀN HÌNH", SwingConstants.CENTER);
        lblScreen.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblScreen.setForeground(Color.WHITE);
        screen.add(lblScreen, BorderLayout.CENTER);
        seatArea.add(screen, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(SoHang, SoGheMoiHang, 8, 8));
        grid.setBackground(Color.WHITE);
        grid.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Bản đồ ghế (demo)",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)));

        DefaultTableModel seatModel = new DefaultTableModel(new String[]{"Mã Ghế", "Hàng", "Số", "Trạng thái"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

// ... (khởi tạo bảng màu)
        Color colorThuong = new Color(66, 103, 178); // Xanh dương
        Color colorVIP = new Color(220, 53, 69);     // Đỏ
        Color colorSelected = new Color(255, 193, 7); // Vàng

        for (int i = 0; i < SoHang; i++) {
            char hang = (char) ('A' + i);
            for (int j = 1; j <= SoGheMoiHang; j++) {
                String maGhe = hang + String.format("%02d", j);
                JButton btn = new JButton(maGhe);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btn.setFocusPainted(false);
                btn.setPreferredSize(new Dimension(54, 44));

                // 1. TÌM GHẾ NÀY TRONG CSDL
                GheDTO gheDb = null;
                if (danhSachGhe != null) {
                    for (GheDTO g : danhSachGhe) {
                        if (g.getHangGhe() == (int) hang && g.getSoGhe() == j) {
                            gheDb = g;
                            break;
                        }
                    }
                }

                // 2. TÔ MÀU THEO LOẠI GHẾ
                Color currentColor = colorThuong; // Mặc định
                String tenLoai = "Thường";

                if (gheDb != null) {
                    // Giả sử MaLoaiGhe = 2 là ghế VIP
                    if (gheDb.getMaLoaiGhe() == 2) {
                        currentColor = colorVIP;
                        tenLoai = "VIP";
                    }
                }

                btn.setBackground(currentColor);
                btn.setForeground(Color.WHITE);
                btn.putClientProperty("originalColor", currentColor);
                seatModel.addRow(new Object[]{maGhe, String.valueOf(hang), j, tenLoai});

                // Chế độ sửa: Cho phép chọn
                Color finalCurrentColor = currentColor;
                btn.addActionListener(e -> {
                    JButton clickedBtn = (JButton) e.getSource();
                    if (!clickedBtn.getBackground().equals(colorSelected)) {
                        clickedBtn.setBackground(colorSelected);
                        clickedBtn.setForeground(Color.BLACK);
                        selectedSeats.add(maGhe);
                    } else {
                        // Trả về màu gốc (VIP hoặc Thường) khi bỏ chọn
                        clickedBtn.setBackground(finalCurrentColor);
                        clickedBtn.setForeground(Color.WHITE);
                        selectedSeats.remove(maGhe);
                    }
                });

                grid.add(btn);
            }
        }

        JScrollPane scrollGrid = new JScrollPane(grid);
        scrollGrid.getViewport().setBackground(Color.WHITE);

        JTable seatTable = new JTable(seatModel);
        seatTable.setRowHeight(32);
        seatTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        seatTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        JScrollPane scrollTable = new JScrollPane(seatTable);
        scrollTable.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Danh sách ghế (demo)",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)));

        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setOpaque(false);
        leftPanel.add(screen, BorderLayout.NORTH);
        leftPanel.add(scrollGrid, BorderLayout.CENTER);

        JPanel bottomControlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomControlPanel.setOpaque(false);

        JButton btnClear = new JButton("Bỏ chọn tất cả");
        btnClear.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnClear.setBackground(new Color(108, 117, 125)); // Màu xám (như nút Secondary của Bootstrap)
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);

        btnClear.addActionListener(e -> {
            if (selectedSeats.isEmpty()) return; // Không có gì để xóa thì thôi

            selectedSeats.clear(); // Xóa sạch danh sách đang chọn

            // Duyệt qua tất cả các nút ghế trên bản đồ và trả về màu gốc
            for (Component comp : grid.getComponents()) {
                if (comp instanceof JButton) {
                    JButton b = (JButton) comp;
                    Color originalColor = (Color) b.getClientProperty("originalColor");
                    if (originalColor != null) {
                        b.setBackground(originalColor);
                        b.setForeground(Color.WHITE);
                    }
                }
            }
            System.out.println("Đã reset! Danh sách ghế đang chọn: " + selectedSeats);
        });

        bottomControlPanel.add(btnClear);
        leftPanel.add(bottomControlPanel, BorderLayout.SOUTH); // Gắn xuống đáy của sơ đồ ghế

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, scrollTable);
        split.setResizeWeight(0.80);
        split.setDividerSize(8);
        seatArea.add(split, BorderLayout.CENTER);

        root.add(seatArea, BorderLayout.CENTER);
        return root;
    }

    private JPanel createPreviewPanel(String maPhong, String tenPhong, String loaiPhong, int SoHang, int SoGheMoiHang, List<GheDTO> danhSachGhe) {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        root.setBackground(new Color(245, 245, 250));

        JLabel header = new JLabel("Phòng: " + tenPhong + " | Loại: " + loaiPhong + " | Mã: " + maPhong, SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setOpaque(true);
        header.setBackground(new Color(66, 103, 178));
        header.setForeground(Color.WHITE);
        header.setBorder(new EmptyBorder(10, 10, 10, 10));
        root.add(header, BorderLayout.NORTH);

        JPanel seatArea = new JPanel(new BorderLayout(10, 10));
        seatArea.setOpaque(false);

        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(new Color(30, 30, 35));
        screen.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel lblScreen = new JLabel("MÀN HÌNH", SwingConstants.CENTER);
        lblScreen.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblScreen.setForeground(Color.WHITE);
        screen.add(lblScreen, BorderLayout.CENTER);
        seatArea.add(screen, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(SoHang, SoGheMoiHang, 8, 8));
        grid.setBackground(Color.WHITE);
        grid.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Bản đồ ghế (demo)",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)));

        DefaultTableModel seatModel = new DefaultTableModel(new String[]{"Mã Ghế", "Hàng", "Số", "Trạng thái"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

// ... (khởi tạo bảng màu)
        Color colorThuong = new Color(66, 103, 178); // Xanh dương
        Color colorVIP = new Color(220, 53, 69);     // Đỏ

        for (int i = 0; i < SoHang; i++) {
            char hang = (char) ('A' + i);
            for (int j = 1; j <= SoGheMoiHang; j++) {
                String maGhe = hang + String.format("%02d", j);
                JButton btn = new JButton(maGhe);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btn.setFocusPainted(false);
                btn.setPreferredSize(new Dimension(54, 44));

                // 1. TÌM GHẾ NÀY TRONG CSDL
                GheDTO gheDb = null;
                if (danhSachGhe != null) {
                    for (GheDTO g : danhSachGhe) {
                        if (g.getHangGhe() == (int) hang && g.getSoGhe() == j) {
                            gheDb = g;
                            break;
                        }
                    }
                }

                // 2. TÔ MÀU THEO LOẠI GHẾ
                Color currentColor = colorThuong; // Mặc định
                String tenLoai = "Thường";

                if (gheDb != null) {
                    // Giả sử MaLoaiGhe = 2 là ghế VIP
                    if (gheDb.getMaLoaiGhe() == 2) {
                        currentColor = colorVIP;
                        tenLoai = "VIP";
                    }
                }

                btn.setBackground(currentColor);
                btn.setForeground(Color.WHITE);
                btn.putClientProperty("originalColor", currentColor);
                seatModel.addRow(new Object[]{maGhe, String.valueOf(hang), j, tenLoai});


                // Chế độ xem: Khóa click
                btn.setModel(new javax.swing.DefaultButtonModel() {
                    @Override public boolean isArmed() { return false; }
                    @Override public boolean isPressed() { return false; }
                    @Override public boolean isRollover() { return false; }
                });
                btn.setFocusable(false);
                grid.add(btn);
            }
        }

        JScrollPane scrollGrid = new JScrollPane(grid);
        scrollGrid.getViewport().setBackground(Color.WHITE);

        JTable seatTable = new JTable(seatModel);
        seatTable.setRowHeight(32);
        seatTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        seatTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        JScrollPane scrollTable = new JScrollPane(seatTable);
        scrollTable.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Danh sách ghế (demo)",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)));

        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setOpaque(false);
        leftPanel.add(screen, BorderLayout.NORTH);
        leftPanel.add(scrollGrid, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, scrollTable);
        split.setResizeWeight(0.80);
        split.setDividerSize(8);
        seatArea.add(split, BorderLayout.CENTER);

        root.add(seatArea, BorderLayout.CENTER);
        return root;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return lbl;
    }


    private void deleteRoom() {
        // 1. Lấy danh sách các dòng đang được chọn
        int[] selectedRows = tablePhongChieu.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất 1 phòng chiếu để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. TẠO BẢNG PREVIEW
        String[] columns = {"Mã Phòng", "Tên Phòng", "Loại Phòng", "Số Hàng", "Ghế/Hàng"};
        Object[][] data = new Object[selectedRows.length][5];

        for (int i = 0; i < selectedRows.length; i++) {
            int modelRow = tablePhongChieu.convertRowIndexToModel(selectedRows[i]);
            data[i][0] = model.getValueAt(modelRow, 0);
            data[i][1] = model.getValueAt(modelRow, 1);
            data[i][2] = model.getValueAt(modelRow, 2);
            data[i][3] = model.getValueAt(modelRow, 3);
            data[i][4] = model.getValueAt(modelRow, 4);
        }

        JTable previewTable = new JTable(data, columns);
        previewTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        previewTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        previewTable.getTableHeader().setBackground(new Color(220, 53, 69)); // Nền đỏ cảnh báo
        previewTable.getTableHeader().setForeground(Color.WHITE);
        previewTable.setRowHeight(35);
        previewTable.setEnabled(false);
        previewTable.getTableHeader().setReorderingAllowed(false);
        //previewTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // ---> THUẬT TOÁN TỰ ĐỘNG ĐO VÀ CO GIÃN CỘT (GIỐNG HÀM ADD) <---
        for (int column = 0; column < previewTable.getColumnCount(); column++) {
            int maxWidth = 50;

            // Đo Header
            javax.swing.table.TableCellRenderer headerRenderer = previewTable.getTableHeader().getDefaultRenderer();
            Object headerValue = previewTable.getColumnModel().getColumn(column).getHeaderValue();
            Component headerComp = headerRenderer.getTableCellRendererComponent(previewTable, headerValue, false, false, 0, column);
            maxWidth = Math.max(headerComp.getPreferredSize().width, maxWidth);

            // Đo Data của tất cả các dòng sắp xóa
            for (int row = 0; row < previewTable.getRowCount(); row++) {
                javax.swing.table.TableCellRenderer cellRenderer = previewTable.getCellRenderer(row, column);
                Component cellComp = previewTable.prepareRenderer(cellRenderer, row, column);
                maxWidth = Math.max(cellComp.getPreferredSize().width, maxWidth);
            }

            // Chốt kích thước cột
            previewTable.getColumnModel().getColumn(column).setPreferredWidth(maxWidth + 15);
        }

        // --- TÍNH TOÁN CHIỀU CAO LINH HOẠT CHO SCROLLPANE ---
        // Nếu xóa 1 người thì bảng thấp, xóa nhiều người thì bảng cao (Max 250px)
        // Cộng thêm 20px để chừa chỗ cho thanh cuộn ngang (Horizontal Scrollbar)
        int tableHeight = Math.min(selectedRows.length * 35 + 35, 250) + 20;

        JScrollPane scrollPreview = new JScrollPane(previewTable);
        scrollPreview.setPreferredSize(new Dimension(600, tableHeight));

        JPanel panelConfirm = new JPanel(new BorderLayout(0, 10));
        JLabel lblConfirm = new JLabel("CẢNH BÁO: Bạn có chắc chắn muốn XÓA VĨNH VIỄN " + selectedRows.length + " phòng chiếu này không? (khi xóa sẽ xóa hết các thứ liên quan!)");
        lblConfirm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblConfirm.setForeground(Color.RED);

        panelConfirm.add(lblConfirm, BorderLayout.NORTH);
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.add(scrollPreview);
        panelConfirm.add(wrapper, BorderLayout.CENTER);

        // 3. HIỂN THỊ XÁC NHẬN
        int confirm = JOptionPane.showConfirmDialog(
                this, panelConfirm, "Xác nhận xóa dữ liệu",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
        );

        // 4. THỰC THI XÓA XUỐNG CSDL
        if (confirm == JOptionPane.YES_OPTION) {
            int successCount = 0;
            for (int i = 0; i < selectedRows.length; i++) {
                int modelRow = tablePhongChieu.convertRowIndexToModel(selectedRows[i]);
                int maPC = (int) model.getValueAt(modelRow, 0);

                if (pcBUS.delete(maPC)) {
                    successCount++;
                }
            }
            JOptionPane.showMessageDialog(this, "Đã xóa thành công " + successCount + " Phòng Chiếu!");
            loadDataToTable(); // Cập nhật lại giao diện chính
        }
    }
}