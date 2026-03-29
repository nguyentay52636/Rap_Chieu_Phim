package org.example.GUI.Components.FormPhongChieu;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.example.BUS.LoaiGheBUS;
import org.example.BUS.PhongChieuBUS;
import org.example.DTO.GheDTO;
import org.example.DTO.LoaiGheDTO;
import org.example.DTO.PhongChieuDTO;

public class FormPhongChieu extends JPanel {

    private DefaultTableModel model;
    private JTable tablePhongChieu;
    private final PhongChieuBUS pcBUS = new PhongChieuBUS();
    private final LoaiGheBUS lgBUS = new LoaiGheBUS();

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

                if (soHang <= 0 || soGhe <= 0 || soHang > 26) {
                    JOptionPane.showMessageDialog(dialog, "Số hàng (tối đa 26, bảng chữ cái English) và số ghế phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Gắn MaPhong = 0, CSDL sẽ tự động tăng
                PhongChieuDTO pc = new PhongChieuDTO(0, tenPhong, loaiPhong, soHang, soGhe);

                // Lấy mã loại ghế mặc định (Lấy loại đầu tiên trong CSDL)
                List<LoaiGheDTO> listLoaiGhe = lgBUS.getList();
                int defaultMaLoai = listLoaiGhe.isEmpty() ? 1 : listLoaiGhe.get(0).getMaLoaiGhe();

                // ---> GỌI HÀM MỚI TẠO ĐỂ THÊM PHÒNG & TẠO GHẾ CÙNG LÚC <---
                if (pcBUS.addRoomWithSeats(pc, defaultMaLoai)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm phòng chiếu và khởi tạo sơ đồ ghế thành công!");
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

        // --- BỘ NHỚ TẠM THỜI (IN-MEMORY STATE) ---
        int[] currentDim = {SoHang, SoGheMoiHang};
        List<String> selectedSeats = new ArrayList<>();
        List<GheDTO> localSeats = new ArrayList<>();
        if (danhSachGhe != null) {
            localSeats.addAll(danhSachGhe);
        }
        int idPhong = Integer.parseInt(maPhong);

        // --- TOP PANEL: ALL CONTROLS ---
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(new Color(245, 245, 250));

        // Gom nhóm các công cụ sang bên trái (Nằm trên cùng 1 dòng)
        JPanel leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftControls.setOpaque(false);

        // 1. Chỉnh sửa loại ghế
        JPanel chairPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        chairPanel.setOpaque(false);
        chairPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Đổi Loại", TitledBorder.LEFT, TitledBorder.TOP));

        List<LoaiGheDTO> listLoaiGhe = lgBUS.getList();
        String[] dsLoaiGhe = listLoaiGhe.stream().map(LoaiGheDTO::getTenLoaiGhe).toArray(String[]::new);
        JComboBox<String> optLoaiGhe = createStyledComboBox(dsLoaiGhe);
        JButton btnApplySeatType = createStyledButton("Đổi", new Color(255, 193, 7), Color.BLACK);
        chairPanel.add(optLoaiGhe);
        chairPanel.add(btnApplySeatType);

        // 2. Chỉnh sửa kích thước phòng
        JPanel dimPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dimPanel.setOpaque(false);
        dimPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Kích Thước", TitledBorder.LEFT, TitledBorder.TOP));

        dimPanel.add(createLabel("Hàng:"));
        JTextField txtSoHang = new JTextField(String.valueOf(currentDim[0]), 3);
        txtSoHang.setHorizontalAlignment(JTextField.CENTER);
        dimPanel.add(txtSoHang);

        dimPanel.add(createLabel("Ghế/Hàng:"));
        JTextField txtSoGhe = new JTextField(String.valueOf(currentDim[1]), 3);
        txtSoGhe.setHorizontalAlignment(JTextField.CENTER);
        dimPanel.add(txtSoGhe);

        JButton btnApplySize = createStyledButton("Tạo lại", new Color(0, 123, 255), Color.WHITE);
        dimPanel.add(btnApplySize);

        // 3. Chọn nhanh (Select All, Clear, Select Row)
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        selectionPanel.setOpaque(false);
        selectionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Chọn Nhanh", TitledBorder.LEFT, TitledBorder.TOP));

        JButton btnSelectAll = createStyledButton("Tất cả", new Color(23, 162, 184), Color.WHITE);
        JButton btnClearSel = createStyledButton("Bỏ chọn", new Color(108, 117, 125), Color.WHITE);
        btnClearSel.setPreferredSize(new Dimension(90, 35));
        JComboBox<String> optRowSelect = createStyledComboBox(new String[]{}); // Sẽ được cập nhật tự động
        JButton btnSelectRow = createStyledButton("Chọn Hàng", new Color(23, 162, 184), Color.WHITE);
        btnSelectRow.setPreferredSize(new Dimension(120, 35));

        selectionPanel.add(btnSelectAll);
        selectionPanel.add(btnClearSel);
        selectionPanel.add(optRowSelect);
        selectionPanel.add(btnSelectRow);

        // Gắn tất cả vào leftControls (Nằm ngang nhau)
        leftControls.add(chairPanel);
        leftControls.add(dimPanel);
        leftControls.add(selectionPanel);

        // ---> BỌC KHU VỰC BÊN TRÁI VÀO JSCROLLPANE <---
        JScrollPane scrollLeftControls = new JScrollPane(leftControls);
        scrollLeftControls.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER); // Tắt cuộn dọc
        scrollLeftControls.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Bật cuộn ngang
        scrollLeftControls.setBorder(BorderFactory.createEmptyBorder()); // Bỏ viền để nhìn liền mạch
        scrollLeftControls.setOpaque(false);
        scrollLeftControls.getViewport().setOpaque(false);
        scrollLeftControls.getHorizontalScrollBar().setUnitIncrement(16); // Cuộn chuột mượt mà

        // 4. Nút Lưu & Thoát (Đặt bên phải)
        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        rightControls.setOpaque(false);
        rightControls.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Thoát", TitledBorder.LEFT, TitledBorder.TOP));
        JButton btnCancel = createStyledButton("Hủy", new Color(108, 117, 125), Color.WHITE);
        JButton btnSave = createStyledButton("Lưu", new Color(40, 167, 69), Color.WHITE);
        rightControls.add(btnCancel);
        rightControls.add(btnSave);

        // ---> THAY ĐỔI VỊ TRÍ ADD VÀO topPanel <---
        // Đặt scrollLeftControls ở CENTER để nó tự động co lại và hiện thanh cuộn khi cửa sổ nhỏ
        // Đặt rightControls ở EAST để nó luôn giữ nguyên kích thước và bám sát lề phải
        topPanel.add(scrollLeftControls, BorderLayout.CENTER);
        topPanel.add(rightControls, BorderLayout.EAST);
        root.add(topPanel, BorderLayout.NORTH);

        // --- CENTER PANEL: MÀN HÌNH & BẢN ĐỒ GHẾ ---
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

        JPanel grid = new JPanel();
        grid.setBackground(Color.WHITE);
        JScrollPane scrollGrid = new JScrollPane(grid);

        scrollGrid.getVerticalScrollBar().setUnitIncrement(SoHang);
        scrollGrid.getHorizontalScrollBar().setUnitIncrement(SoGheMoiHang);

        DefaultTableModel seatModel = new DefaultTableModel(new String[]{"Mã Ghế", "Hàng", "Số", "Loại", "Trạng thái"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable seatTable = new JTable(seatModel);
        seatTable.setRowHeight(30);
        JScrollPane scrollTable = new JScrollPane(seatTable);

        scrollTable.getVerticalScrollBar().setUnitIncrement(SoHang);
        scrollTable.getHorizontalScrollBar().setUnitIncrement(SoGheMoiHang);

        Color colorSelected = new Color(255, 193, 7); // Vàng
        Color colorThuong = new Color(66, 103, 178); // Xanh dương mặc định cho mọi ghế

        // --- HÀM RENDER LẠI GRID VÀ BẢNG DỰA TRÊN STATE ---
        Runnable renderGrid = () -> {
            grid.removeAll();
            grid.setLayout(new GridLayout(currentDim[0], currentDim[1], 8, 8));
            seatModel.setRowCount(0);
            selectedSeats.clear();

            // Cập nhật lại danh sách hàng trong ComboBox "Chọn Hàng"
            optRowSelect.removeAllItems();
            for (int i = 0; i < currentDim[0]; i++) {
                optRowSelect.addItem(String.valueOf((char) ('A' + i)));
            }

            for (int i = 0; i < currentDim[0]; i++) {
                char hang = (char) ('A' + i);
                for (int j = 1; j <= currentDim[1]; j++) {
                    int finalJ = j;
                    String maGhe = hang + String.format("%02d", finalJ);
                    JButton btn = new JButton(maGhe);
                    btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    btn.setFocusPainted(false);
                    btn.setPreferredSize(new Dimension(54, 44));

                    GheDTO gheDb = localSeats.stream()
                            .filter(g -> g.getHangGhe().equals(String.valueOf(hang)) && g.getSoGhe() == finalJ)
                            .findFirst().orElse(null);

                    if (gheDb == null) {
                        int defaultMaLoai = listLoaiGhe.isEmpty() ? 1 : listLoaiGhe.get(0).getMaLoaiGhe();
                        // Khởi tạo DTO với String
                        gheDb = new GheDTO(0, idPhong, defaultMaLoai, String.valueOf(hang), finalJ);
                        localSeats.add(gheDb);
                    }

                    String tenLoai = "Unknown";
                    for (LoaiGheDTO loai : listLoaiGhe) {
                        if (loai.getMaLoaiGhe() == gheDb.getMaLoaiGhe()) {
                            tenLoai = loai.getTenLoaiGhe();
                            break;
                        }
                    }

                    btn.setBackground(colorThuong);
                    btn.setForeground(Color.WHITE);
                    btn.putClientProperty("originalColor", colorThuong);

                    seatModel.addRow(new Object[]{maGhe, String.valueOf(hang), finalJ, tenLoai, ""});

                    // Xử lý click chọn 1 ghế
                    btn.addActionListener(e -> {
                        boolean isSelecting = !btn.getBackground().equals(colorSelected);
                        if (isSelecting) {
                            btn.setBackground(colorSelected);
                            btn.setForeground(Color.BLACK);
                            selectedSeats.add(maGhe);
                        } else {
                            btn.setBackground((Color) btn.getClientProperty("originalColor"));
                            btn.setForeground(Color.WHITE);
                            selectedSeats.remove(maGhe);
                        }

                        for (int r = 0; r < seatModel.getRowCount(); r++) {
                            if (seatModel.getValueAt(r, 0).equals(maGhe)) {
                                seatModel.setValueAt(isSelecting ? "Đang chọn..." : "", r, 4);
                                break;
                            }
                        }
                    });

                    grid.add(btn);
                }
            }
            grid.revalidate();
            grid.repaint();
        };

        renderGrid.run(); // Chạy lần đầu

        // --- SỰ KIỆN CHO CÁC NÚT MỚI THÊM VÀO ---
        btnSelectAll.addActionListener(e -> {
            for (Component comp : grid.getComponents()) {
                if (comp instanceof JButton btn) {
                    String maGhe = btn.getText();
                    if (!selectedSeats.contains(maGhe)) {
                        btn.setBackground(colorSelected);
                        btn.setForeground(Color.BLACK);
                        selectedSeats.add(maGhe);
                    }
                }
            }
            // Update toàn bộ cột trạng thái của bảng
            for (int r = 0; r < seatModel.getRowCount(); r++) {
                seatModel.setValueAt("Đang chọn...", r, 4);
            }
        });

        btnClearSel.addActionListener(e -> {
            selectedSeats.clear();
            for (Component comp : grid.getComponents()) {
                if (comp instanceof JButton btn) {
                    btn.setBackground((Color) btn.getClientProperty("originalColor"));
                    btn.setForeground(Color.WHITE);
                }
            }
            // Xóa toàn bộ cột trạng thái của bảng
            for (int r = 0; r < seatModel.getRowCount(); r++) {
                seatModel.setValueAt("", r, 4);
            }
        });

        btnSelectRow.addActionListener(e -> {
            String targetRow = (String) optRowSelect.getSelectedItem();
            if (targetRow == null) return;

            for (Component comp : grid.getComponents()) {
                if (comp instanceof JButton btn) {
                    String maGhe = btn.getText();
                    // Nếu mã ghế bắt đầu bằng chữ cái của hàng (VD: "A")
                    if (maGhe.startsWith(targetRow) && !selectedSeats.contains(maGhe)) {
                        btn.setBackground(colorSelected);
                        btn.setForeground(Color.BLACK);
                        selectedSeats.add(maGhe);
                    }
                }
            }
            // Update bảng chỉ cho những ghế thuộc hàng đó
            for (int r = 0; r < seatModel.getRowCount(); r++) {
                if (seatModel.getValueAt(r, 0).toString().startsWith(targetRow)) {
                    seatModel.setValueAt("Đang chọn...", r, 4);
                }
            }
        });

        // --- CÁC SỰ KIỆN CŨ ---
        btnApplySize.addActionListener(e -> {
            try {
                int newRow = Integer.parseInt(txtSoHang.getText().trim());
                int newCol = Integer.parseInt(txtSoGhe.getText().trim());
                if (newRow <= 0 || newCol <= 0 || newRow > 26) {
                    JOptionPane.showMessageDialog(root, "Kích thước không hợp lệ! (Hàng tối đa 26)");
                    return;
                }
                currentDim[0] = newRow;
                currentDim[1] = newCol;

                localSeats.removeIf(g -> g.getHangGhe().charAt(0) > ('A' + currentDim[0] - 1) || g.getSoGhe() > currentDim[1]);
                renderGrid.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(root, "Vui lòng nhập số hợp lệ!");
            }
        });

        btnApplySeatType.addActionListener(e -> {
            if (selectedSeats.isEmpty()) {
                JOptionPane.showMessageDialog(root, "Vui lòng chọn ghế trên bản đồ trước!"); return;
            }
            int maLoaiGheMoi = listLoaiGhe.get(optLoaiGhe.getSelectedIndex()).getMaLoaiGhe();

            for (String seatCode : selectedSeats) {
                String hangGhe = String.valueOf(seatCode.charAt(0));
                int soGhe = Integer.parseInt(seatCode.substring(1));
                localSeats.stream()
                        .filter(g -> g.getHangGhe().equals(hangGhe) && g.getSoGhe() == soGhe)
                        .forEach(g -> g.setMaLoaiGhe(maLoaiGheMoi));
            }
            renderGrid.run();
        });

        btnCancel.addActionListener(e -> SwingUtilities.getWindowAncestor(root).dispose());

        btnSave.addActionListener(e -> {
            PhongChieuDTO updatedRoom = new PhongChieuDTO(idPhong, tenPhong, loaiPhong, currentDim[0], currentDim[1]);
            updatedRoom.setGheList(localSeats);

            if (pcBUS.updateRoomAndSeats(updatedRoom)) {
                JOptionPane.showMessageDialog(root, "Cập nhật phòng chiếu thành công!");
                loadDataToTable();
                SwingUtilities.getWindowAncestor(root).dispose();
            } else {
                JOptionPane.showMessageDialog(root, "Lỗi khi cập nhật CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- BỌC MÀN HÌNH VÀ BẢN ĐỒ VÀO CHUNG BÊN TRÁI ---
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setOpaque(false);
        leftPanel.add(screen, BorderLayout.NORTH); // Đặt màn hình lên trên
        leftPanel.add(scrollGrid, BorderLayout.CENTER); // Đặt lưới ghế ở dưới

        // Bỏ leftPanel vào nửa bên trái của SplitPane, bảng vào nửa bên phải
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, scrollTable);
        split.setResizeWeight(0.75);
        split.setDividerSize(8);
        seatArea.add(split, BorderLayout.CENTER);

        root.add(seatArea, BorderLayout.CENTER);

        return root;
    }

    private JPanel createPreviewPanel(String maPhong, String tenPhong, String loaiPhong, int SoHang, int SoGheMoiHang, List<GheDTO> danhSachGhe) {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        root.setBackground(new Color(245, 245, 250));

        // --- TOP HEADER: THÔNG TIN PHÒNG ---
        JLabel header = new JLabel("Phòng: " + tenPhong + " | Loại: " + loaiPhong + " | Mã: " + maPhong, SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setOpaque(true);
        header.setBackground(new Color(66, 103, 178));
        header.setForeground(Color.WHITE);
        header.setBorder(new EmptyBorder(10, 10, 10, 10));
        root.add(header, BorderLayout.NORTH);

        // --- CENTER PANEL: MÀN HÌNH & BẢN ĐỒ GHẾ ---
        JPanel seatArea = new JPanel(new BorderLayout(10, 10));
        seatArea.setOpaque(false);

        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(new Color(30, 30, 35));
        screen.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel lblScreen = new JLabel("MÀN HÌNH", SwingConstants.CENTER);
        lblScreen.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblScreen.setForeground(Color.WHITE);
        screen.add(lblScreen, BorderLayout.CENTER);

        JPanel grid = new JPanel(new GridLayout(SoHang, SoGheMoiHang, 8, 8));
        grid.setBackground(Color.WHITE);
        grid.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Sơ đồ ghế (Chỉ xem)",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)));

        JScrollPane scrollGrid = new JScrollPane(grid);
        scrollGrid.getViewport().setBackground(Color.WHITE);
        // Chỉnh tốc độ cuộn mượt mà (dùng 16 pixel thay vì SoHang)
        scrollGrid.getVerticalScrollBar().setUnitIncrement(16);
        scrollGrid.getHorizontalScrollBar().setUnitIncrement(16);

        // Bảng cập nhật cho giống UpdatePanel (Bỏ cột Trạng thái vì chỉ xem)
        DefaultTableModel seatModel = new DefaultTableModel(new String[]{"Mã Ghế", "Hàng", "Số", "Loại"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable seatTable = new JTable(seatModel);
        seatTable.setRowHeight(30);
        seatTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        seatTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollTable = new JScrollPane(seatTable);
        scrollTable.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Danh sách ghế",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)));
        scrollTable.getVerticalScrollBar().setUnitIncrement(16);
        scrollTable.getHorizontalScrollBar().setUnitIncrement(16);

        Color colorThuong = new Color(66, 103, 178); // Màu xanh dương mặc định
        List<LoaiGheDTO> listLoaiGhe = lgBUS.getList(); // Lấy danh sách tên loại ghế

        // --- ĐỔ DỮ LIỆU VÀO GRID VÀ TABLE ---
        for (int i = 0; i < SoHang; i++) {
            char hang = (char) ('A' + i);
            for (int j = 1; j <= SoGheMoiHang; j++) {
                String maGhe = hang + String.format("%02d", j);
                JButton btn = new JButton(maGhe);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btn.setFocusPainted(false);
                btn.setPreferredSize(new Dimension(54, 44));

                // 1. TÌM GHẾ TRONG DB (Sử dụng String cho HangGhe)
                GheDTO gheDb = null;
                if (danhSachGhe != null) {
                    for (GheDTO g : danhSachGhe) {
                        if (g.getHangGhe().equals(String.valueOf(hang)) && g.getSoGhe() == j) {
                            gheDb = g;
                            break;
                        }
                    }
                }

                // 2. TÌM TÊN LOẠI GHẾ
                String tenLoai = "Unknown";
                if (gheDb != null) {
                    for (LoaiGheDTO loai : listLoaiGhe) {
                        if (loai.getMaLoaiGhe() == gheDb.getMaLoaiGhe()) {
                            tenLoai = loai.getTenLoaiGhe();
                            break;
                        }
                    }
                } else {
                    tenLoai = "Chưa khởi tạo";
                }

                btn.setBackground(colorThuong);
                btn.setForeground(Color.WHITE);

                seatModel.addRow(new Object[]{maGhe, String.valueOf(hang), j, tenLoai});

                // CHẾ ĐỘ CHỈ XEM: Vô hiệu hóa mọi hiệu ứng click/hover
                btn.setModel(new javax.swing.DefaultButtonModel() {
                    @Override public boolean isArmed() { return false; }
                    @Override public boolean isPressed() { return false; }
                    @Override public boolean isRollover() { return false; }
                });
                btn.setFocusable(false);

                grid.add(btn);
            }
        }

        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setOpaque(false);
        leftPanel.add(screen, BorderLayout.NORTH);
        leftPanel.add(scrollGrid, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, scrollTable);
        split.setResizeWeight(0.75); // Căn tỷ lệ giống màn hình Update
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