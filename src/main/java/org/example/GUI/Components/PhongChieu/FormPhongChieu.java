import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FormPhongChieu extends JPanel {

    // ====================== DANH SÁCH PHÒNG ======================
    private JTable tablePhong;
    private DefaultTableModel modelPhong;

    // ====================== CHI TIẾT PHÒNG (khi chọn) ======================
    private JPanel panelChiTiet;
    private JLabel lblThongTinPhong;
    private JButton btnTuDongSinh, btnThemGhe, btnXoaGheDaChon, btnLamMoi;

    // Bản đồ ghế (hiện đại, đẹp)
    private JPanel panelBanDoGhe;
    private ArrayList<JButton> danhSachButtonGhe = new ArrayList<>();

    // Bảng danh sách ghế chi tiết (dưới bản đồ)
    private JTable tableDanhSachGhe;
    private DefaultTableModel modelGhe;

    // Dữ liệu demo
    private int phongDangChon = -1;

    public FormPhongChieu() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(15, 23, 42)); // Dark modern theme
        initComponents();
        loadDuLieuPhongDemo();
    }

    private void initComponents() {
        // ====================== TRÁI: DANH SÁCH PHÒNG ======================
        JPanel panelTrai = new JPanel(new BorderLayout());
        panelTrai.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(59, 130, 246), 2), 
                "📋 DANH SÁCH PHÒNG CHIẾU", TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), Color.WHITE));
        panelTrai.setBackground(new Color(15, 23, 42));

        modelPhong = new DefaultTableModel(
                new String[]{"Mã", "Tên Phòng", "Loại", "Số Hàng", "Ghế/Hàng", "Tổng Ghế"}, 0);
        tablePhong = new JTable(modelPhong);
        tablePhong.setRowHeight(38);
        tablePhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablePhong.setBackground(new Color(30, 41, 59));
        tablePhong.setForeground(Color.WHITE);
        tablePhong.setSelectionBackground(new Color(59, 130, 246));
        tablePhong.getTableHeader().setBackground(new Color(15, 23, 42));
        tablePhong.getTableHeader().setForeground(Color.WHITE);
        tablePhong.setGridColor(new Color(59, 130, 246));

        JScrollPane scrollPhong = new JScrollPane(tablePhong);
        panelTrai.add(scrollPhong, BorderLayout.CENTER);

        // Nút trên danh sách phòng
        JToolBar toolBarTrai = new JToolBar();
        toolBarTrai.setFloatable(false);
        toolBarTrai.setBackground(new Color(15, 23, 42));
        JButton btnThemPhong = createModernButton("➕ Thêm Phòng", new Color(34, 197, 151));
        JButton btnSuaPhong = createModernButton("✏️ Sửa", new Color(234, 179, 8));
        JButton btnXoaPhong = createModernButton("🗑️ Xóa", new Color(239, 68, 68));
        
        toolBarTrai.add(btnThemPhong);
        toolBarTrai.add(btnSuaPhong);
        toolBarTrai.add(btnXoaPhong);
        panelTrai.add(toolBarTrai, BorderLayout.NORTH);

        // ====================== PHẢI: CHI TIẾT PHÒNG (HIỆN ĐẠI) ======================
        panelChiTiet = new JPanel(new BorderLayout(10, 10));
        panelChiTiet.setBackground(new Color(15, 23, 42));
        panelChiTiet.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(59, 130, 246), 2), 
                "🪑 CHI TIẾT PHÒNG & QUẢN LÝ GHẾ", TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), Color.WHITE));

        // Thông tin phòng hiện đại (header)
        lblThongTinPhong = new JLabel("Chọn một phòng để xem chi tiết", SwingConstants.CENTER);
        lblThongTinPhong.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblThongTinPhong.setForeground(new Color(147, 197, 253));
        lblThongTinPhong.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panelChiTiet.add(lblThongTinPhong, BorderLayout.NORTH);

        // Toolbar quản lý ghế
        JToolBar toolBarGhe = new JToolBar();
        toolBarGhe.setFloatable(false);
        toolBarGhe.setBackground(new Color(15, 23, 42));

        btnTuDongSinh = createModernButton("🔄 Tự động sinh ghế", new Color(147, 197, 253));
        btnThemGhe = createModernButton("➕ Thêm ghế thủ công", new Color(34, 197, 151));
        btnXoaGheDaChon = createModernButton("🗑️ Xóa ghế đã chọn", new Color(239, 68, 68));
        btnLamMoi = createModernButton("🔄 Làm mới", new Color(163, 163, 163));

        toolBarGhe.add(btnTuDongSinh);
        toolBarGhe.add(btnThemGhe);
        toolBarGhe.add(btnXoaGheDaChon);
        toolBarGhe.add(btnLamMoi);
        panelChiTiet.add(toolBarGhe, BorderLayout.SOUTH);

        // SplitPane dọc: Bản đồ ghế (trên) + Bảng danh sách ghế (dưới)
        JSplitPane splitChiTiet = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitChiTiet.setResizeWeight(0.55);
        splitChiTiet.setDividerSize(8);
        splitChiTiet.setBackground(new Color(15, 23, 42));

        // === BẢN ĐỒ GHẾ ===
        panelBanDoGhe = new JPanel();
        panelBanDoGhe.setBackground(new Color(15, 23, 42));
        JScrollPane scrollBanDo = new JScrollPane(panelBanDoGhe);
        scrollBanDo.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Color.WHITE, 1), "🪑 BẢN ĐỒ GHẾ PHÒNG (Click ghế để chỉnh)", 
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), Color.WHITE));
        splitChiTiet.setTopComponent(scrollBanDo);

        // === BẢNG DANH SÁCH GHẾ ===
        modelGhe = new DefaultTableModel(
                new String[]{"Mã Ghế", "Hàng", "Số", "Loại Ghế", "Trạng Thái", "Mã Phòng"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableDanhSachGhe = new JTable(modelGhe);
        tableDanhSachGhe.setRowHeight(32);
        tableDanhSachGhe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableDanhSachGhe.setBackground(new Color(30, 41, 59));
        tableDanhSachGhe.setForeground(Color.WHITE);
        tableDanhSachGhe.setSelectionBackground(new Color(59, 130, 246));
        tableDanhSachGhe.getTableHeader().setBackground(new Color(15, 23, 42));
        tableDanhSachGhe.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollDanhSachGhe = new JScrollPane(tableDanhSachGhe);
        scrollDanhSachGhe.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Color.WHITE, 1), "📋 DANH SÁCH GHẾ CHI TIẾT", 
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), Color.WHITE));
        splitChiTiet.setBottomComponent(scrollDanhSachGhe);

        panelChiTiet.add(splitChiTiet, BorderLayout.CENTER);

        // ====================== SPLIT TOÀN BỘ ======================
        JSplitPane splitToanBo = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelTrai, panelChiTiet);
        splitToanBo.setDividerLocation(420);
        splitToanBo.setResizeWeight(0.35);
        add(splitToanBo, BorderLayout.CENTER);

        // ====================== SỰ KIỆN ======================
        tablePhong.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablePhong.getSelectedRow() != -1) {
                phongDangChon = tablePhong.getSelectedRow();
                hienThiChiTietPhong(phongDangChon);
            }
        });

        btnThemPhong.addActionListener(e -> themPhongMoi());
        btnSuaPhong.addActionListener(e -> suaPhong());
        btnXoaPhong.addActionListener(e -> xoaPhong());

        btnTuDongSinh.addActionListener(e -> tuDongSinhGhe());
        btnThemGhe.addActionListener(e -> themGheThuCong());
        btnXoaGheDaChon.addActionListener(e -> xoaGheDaChon());
        btnLamMoi.addActionListener(e -> lamMoiDuLieu());
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void loadDuLieuPhongDemo() {
        modelPhong.setRowCount(0);
        modelPhong.addRow(new Object[]{1, "Standard 01", "Standard", 6, 10, 60});
        modelPhong.addRow(new Object[]{2, "VIP Cinema 02", "VIP", 5, 8, 40});
        modelPhong.addRow(new Object[]{3, "IMAX Hall 03", "IMAX", 8, 12, 96});
    }

    private void hienThiChiTietPhong(int row) {
        int maPhong = (int) modelPhong.getValueAt(row, 0);
        String tenPhong = (String) modelPhong.getValueAt(row, 1);
        String loai = (String) modelPhong.getValueAt(row, 2);
        int soHang = (int) modelPhong.getValueAt(row, 3);
        int gheMoiHang = (int) modelPhong.getValueAt(row, 4);

        lblThongTinPhong.setText("Phòng: " + tenPhong + " | Loại: " + loai + 
                                 " | Tổng ghế: " + (soHang * gheMoiHang));

        // Xóa dữ liệu cũ
        panelBanDoGhe.removeAll();
        danhSachButtonGhe.clear();
        modelGhe.setRowCount(0);

        // Tạo bản đồ ghế hiện đại
        panelBanDoGhe.setLayout(new GridLayout(soHang, gheMoiHang, 8, 8));
        String[] trangThaiMau = {"Trống", "Đang đặt", "Đã bán", "Bảo trì"};
        Color[] mau = {new Color(34, 197, 151), new Color(234, 179, 8), new Color(239, 68, 68), Color.GRAY};
        Random rand = new Random();

        for (int h = 0; h < soHang; h++) {
            char chu = (char) ('A' + h);
            for (int s = 1; s <= gheMoiHang; s++) {
                String maGhe = chu + String.format("%02d", s);

                JButton btn = new JButton(maGhe);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btn.setPreferredSize(new Dimension(62, 62));
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

                int idx = rand.nextInt(4);
                btn.setBackground(mau[idx]);
                btn.setForeground(Color.WHITE);
                btn.setToolTipText(maGhe + " - " + trangThaiMau[idx]);

                // Click ghế trong bản đồ
                btn.addActionListener(ev -> {
                    int choice = JOptionPane.showOptionDialog(this,
                            "Ghế " + maGhe + "\nChọn hành động:",
                            "Quản lý ghế", JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null,
                            new String[]{"Sửa trạng thái", "Xem chi tiết", "Xóa ghế"}, null);

                    if (choice == 0) doiTrangThaiGhe(btn);
                    else if (choice == 1) xemChiTietGhe(maGhe);
                    else if (choice == 2) xoaMotGhe(btn);
                });

                panelBanDoGhe.add(btn);
                danhSachButtonGhe.add(btn);

                // Thêm vào bảng danh sách ghế
                modelGhe.addRow(new Object[]{maGhe, chu, s, "Thường", trangThaiMau[idx], maPhong});
            }
        }

        panelBanDoGhe.revalidate();
        panelBanDoGhe.repaint();
    }

    private void doiTrangThaiGhe(JButton btn) {
        String[] options = {"Trống", "Đang đặt", "Đã bán", "Bảo trì"};
        String newStatus = (String) JOptionPane.showInputDialog(this,
                "Chọn trạng thái mới cho ghế " + btn.getText(),
                "Cập nhật trạng thái", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (newStatus != null) {
            Color newColor = switch (newStatus) {
                case "Trống" -> new Color(34, 197, 151);
                case "Đang đặt" -> new Color(234, 179, 8);
                case "Đã bán" -> new Color(239, 68, 68);
                default -> Color.GRAY;
            };
            btn.setBackground(newColor);
            btn.setToolTipText(btn.getText() + " - " + newStatus);

            // Cập nhật cả bảng danh sách ghế
            for (int i = 0; i < modelGhe.getRowCount(); i++) {
                if (modelGhe.getValueAt(i, 0).equals(btn.getText())) {
                    modelGhe.setValueAt(newStatus, i, 4);
                    break;
                }
            }
        }
    }

    private void xemChiTietGhe(String maGhe) {
        JOptionPane.showMessageDialog(this,
                "=== CHI TIẾT GHẾ ===\n" +
                "Mã ghế: " + maGhe + "\n" +
                "Phòng: " + modelPhong.getValueAt(phongDangChon, 1) + "\n" +
                "Trạng thái: Đang hiển thị trên bản đồ\n" +
                "Loại ghế: Thường (có thể mở rộng thêm MaLoaiGhe)\n\n" +
                "Sẵn sàng kết nối với bảng Ghe trong DB!",
                "Xem chi tiết ghế", JOptionPane.INFORMATION_MESSAGE);
    }

    private void xoaMotGhe(JButton btn) {
        if (JOptionPane.showConfirmDialog(this, "Xóa ghế " + btn.getText() + "?", 
                "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            panelBanDoGhe.remove(btn);
            danhSachButtonGhe.remove(btn);
            panelBanDoGhe.revalidate();
            panelBanDoGhe.repaint();

            // Xóa trong bảng
            for (int i = 0; i < modelGhe.getRowCount(); i++) {
                if (modelGhe.getValueAt(i, 0).equals(btn.getText())) {
                    modelGhe.removeRow(i);
                    break;
                }
            }
        }
    }

    private void tuDongSinhGhe() {
        if (phongDangChon == -1) {
            JOptionPane.showMessageDialog(this, "Chọn phòng trước!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        hienThiChiTietPhong(phongDangChon);
        JOptionPane.showMessageDialog(this, "Đã tự động sinh lại toàn bộ ghế!\n(Trong code thật sẽ INSERT INTO Ghe)", 
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private void themGheThuCong() {
        if (phongDangChon == -1) {
            JOptionPane.showMessageDialog(this, "Chọn phòng trước!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField txtHang = new JTextField("A");
        JTextField txtSo = new JTextField("01");
        String[] loaiOptions = {"Thường", "VIP", "Đôi"};
        JComboBox<String> cbLoai = new JComboBox<>(loaiOptions);

        Object[] msg = {"Hàng (A,B,C...):", txtHang, "Số ghế:", txtSo, "Loại ghế:", cbLoai};
        if (JOptionPane.showConfirmDialog(this, msg, "Thêm ghế thủ công", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            String maGheMoi = txtHang.getText().trim().toUpperCase() + String.format("%02d", Integer.parseInt(txtSo.getText().trim()));

            JButton btnMoi = new JButton(maGheMoi);
            btnMoi.setBackground(new Color(34, 197, 151));
            btnMoi.setForeground(Color.WHITE);
            btnMoi.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnMoi.setPreferredSize(new Dimension(62, 62));

            panelBanDoGhe.add(btnMoi);
            danhSachButtonGhe.add(btnMoi);
            panelBanDoGhe.revalidate();
            panelBanDoGhe.repaint();

            modelGhe.addRow(new Object[]{maGheMoi, txtHang.getText().trim().toUpperCase(), 
                    Integer.parseInt(txtSo.getText().trim()), cbLoai.getSelectedItem(), "Trống", 
                    modelPhong.getValueAt(phongDangChon, 0)});
        }
    }

    private void xoaGheDaChon() {
        int[] rows = tableDanhSachGhe.getSelectedRows();
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, "Chọn ít nhất 1 ghế trong bảng để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Xóa " + rows.length + " ghế đã chọn?", 
                "Xác nhận xóa hàng loạt", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            for (int i = rows.length - 1; i >= 0; i--) {
                String maGheXoa = (String) modelGhe.getValueAt(rows[i], 0);
                modelGhe.removeRow(rows[i]);

                // Xóa button tương ứng
                danhSachButtonGhe.removeIf(btn -> btn.getText().equals(maGheXoa));
            }
            panelBanDoGhe.revalidate();
            panelBanDoGhe.repaint();
        }
    }

    private void lamMoiDuLieu() {
        if (phongDangChon != -1) hienThiChiTietPhong(phongDangChon);
    }

    private void themPhongMoi() { /* Giữ nguyên code cũ, bạn có thể copy từ lần trước */ }
    private void suaPhong() { /* Giữ nguyên code cũ */ }
    private void xoaPhong() { /* Giữ nguyên code cũ */ }
}