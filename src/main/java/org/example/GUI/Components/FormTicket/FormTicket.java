package org.example.GUI.Components.FormTicket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormTicket extends JPanel {

    private DefaultTableModel model;
    private JTable tableTicket;

    // Khai báo các nút chức năng
    private JButton btnView, btnAdd, btnHuy, btnNhap, btnXuat, btnLamMoi; // Thêm btnLamMoi

    public FormTicket() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(UIManager.getColor("Panel.background"));

        // ================== TOP PANEL (CHỨC NĂNG & TÌM KIẾM) ==================
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        topPanel.setBackground(UIManager.getColor("Panel.background"));

        // --- Dòng 1: Panel Chức năng ---
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlActions.setBackground(UIManager.getColor("Panel.background"));
        pnlActions.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")),
                "Quản Lý Vé Phim", TitledBorder.LEFT, TitledBorder.TOP));

        btnView = createStyledButton("Xem", new Color(102, 187, 106), Color.WHITE, "/org/example/GUI/resources/images/view.png");
        btnView.addActionListener(e -> actionXemChiTiet());
        pnlActions.add(btnView);

        btnAdd = createStyledButton("Thêm", new Color(0, 123, 255), Color.WHITE, "/org/example/GUI/resources/images/plus.png");
        btnAdd.addActionListener(e -> actionThemVe());
        pnlActions.add(btnAdd);

        btnHuy = createStyledButton("Hủy", new Color(220, 53, 69), Color.WHITE, "/org/example/GUI/resources/images/icons8-cancel-64.png");
        btnHuy.addActionListener(e -> actionHuyVe());
        pnlActions.add(btnHuy);

        btnNhap = createStyledButton("Nhập Excel", new Color(153, 102, 255), Color.WHITE, "/org/example/GUI/resources/images/icons8_ms_excel_30px.png");
        btnNhap.addActionListener(e -> nhapExcel());
        pnlActions.add(btnNhap);

        btnXuat = createStyledButton("Xuất Excel", new Color(153, 102, 255), Color.WHITE, "/org/example/GUI/resources/images/icons8_ms_excel_30px.png");
        btnXuat.addActionListener(e -> xuatExcel());
        pnlActions.add(btnXuat);

        // --- Dòng 2: Panel Tìm kiếm ---
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnlSearch.setBackground(UIManager.getColor("Panel.background"));
        pnlSearch.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")),
                "Tra Cứu Vé Đã Bán", TitledBorder.LEFT, TitledBorder.TOP));

        pnlSearch.add(new JLabel("Mã Vé:"));
        JTextField txtTimMa = new JTextField(10);
        txtTimMa.putClientProperty("JTextField.placeholderText", "Nhập mã vé...");
        txtTimMa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlSearch.add(txtTimMa);

        pnlSearch.add(new JLabel("Tên Phim:"));
        JTextField txtTimPhim = new JTextField(15);
        txtTimPhim.putClientProperty("JTextField.placeholderText", "Nhập tên phim...");
        txtTimPhim.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlSearch.add(txtTimPhim);

        pnlSearch.add(new JLabel("Trạng thái:"));
        JComboBox<String> cbTrangThai = new JComboBox<>(new String[]{"Tất cả", "Đã thanh toán", "Đã Check-in", "Đã Hủy"});
        cbTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlSearch.add(cbTrangThai);

        JButton btnTim = new JButton("Lọc / Tìm kiếm");
        btnTim.setBackground(new Color(0, 123, 255));
        btnTim.setForeground(Color.WHITE);
        btnTim.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlSearch.add(btnTim);

        // NÚT LÀM MỚI (MỚI THÊM)
        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setBackground(new Color(108, 117, 125));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLamMoi.addActionListener(e -> loadDataToTable()); // Gọi hàm load data
        pnlSearch.add(btnLamMoi);

        topPanel.add(pnlActions);
        topPanel.add(pnlSearch);

        // ================== CENTER PANEL (BẢNG DỮ LIỆU) ==================
        String[] columnNames = {"Mã Vé", "Khách Hàng", "Tên Phim", "Phòng", "Ghế", "Ngày Chiếu", "Giờ", "Giá Tiền", "Trạng Thái"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tableTicket = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                }
                if (!isRowSelected(row)) {
                    Color bg = UIManager.getColor("Table.background");
                    if (bg != null) {
                        if (row % 2 == 0) c.setBackground(bg);
                        else {
                            int r = bg.getRed(), g = bg.getGreen(), b = bg.getBlue();
                            int offset = (r < 128) ? 12 : -12;
                            c.setBackground(new Color(
                                    Math.max(0, Math.min(255, r + offset)),
                                    Math.max(0, Math.min(255, g + offset)),
                                    Math.max(0, Math.min(255, b + offset))
                            ));
                        }
                    }
                }
                return c;
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getRowCount() == 0) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 28));
                    g2d.setColor(new Color(180, 180, 180));
                    String text = "DANH SÁCH TRỐNG";
                    FontMetrics fm = g2d.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(text)) / 2;
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2d.drawString(text, x, y);
                    g2d.dispose();
                }
            }
        };

        tableTicket.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableTicket.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTicket.getTableHeader().setBackground(new Color(66, 103, 178));
        tableTicket.getTableHeader().setForeground(Color.WHITE);
        tableTicket.setRowHeight(40);
        tableTicket.setGridColor(UIManager.getColor("Table.gridColor"));
        tableTicket.setShowGrid(true);
        tableTicket.setSelectionBackground(new Color(66, 103, 178));
        tableTicket.setSelectionForeground(Color.WHITE);
        tableTicket.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableTicket.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tableTicket);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Khởi động load data lần đầu
        loadDataToTable();
    }

    // =========================================================================
    // HÀM LOAD DỮ LIỆU TỪ DATABASE
    // =========================================================================
    public void loadDataToTable() {
        // 1. Xóa sạch dữ liệu cũ trên bảng
        model.setRowCount(0);

        // 2. Gọi BUS để lấy danh sách vé từ CSDL
        org.example.BUS.VeBUS veBUS = new org.example.BUS.VeBUS();
        java.util.List<org.example.DTO.VeDTO> danhSach = veBUS.getAllVe();

        // 3. Định dạng tiền tệ cho đẹp (VD: 80000 -> 80,000)
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");

        // 4. Đổ từng vé vào các hàng của JTable
        for (org.example.DTO.VeDTO v : danhSach) {
            model.addRow(new Object[]{
                    v.getMaVe(),
                    v.getKhachHang(),
                    v.getTenPhim(),
                    v.getTenPhong(),
                    v.getTenGhe(),
                    v.getNgayChieu(),
                    v.getGioBatDau(),
                    df.format(v.getGiaVe()) + " VNĐ",
                    v.getTrangThai()
            });
        }
    }


    // =========================================================================
    // CÁC HÀM XỬ LÝ SỰ KIỆN
    // =========================================================================

    private void actionXemChiTiet() {
        int selectedRow = tableTicket.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 vé để xem chi tiết!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maVe = tableTicket.getValueAt(selectedRow, 0).toString();
        JOptionPane.showMessageDialog(this, "Hiển thị Dialog xem chi tiết vé: " + maVe);
    }

    private void actionThemVe() {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        DialogChonSuatChieu dialogChonSuat = new DialogChonSuatChieu(owner);

        // Mở Dialog (Code sẽ DỪNG Ở ĐÂY chờ đến khi bạn bán vé xong và Dialog tắt đi)
        dialogChonSuat.setVisible(true);

        // Ngay sau khi Dialog tắt, hệ thống sẽ TỰ ĐỘNG LOAD LẠI BẢNG
        loadDataToTable();
    }

    private void actionHuyVe() {
        int selectedRow = tableTicket.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 vé để hủy!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maVe = tableTicket.getValueAt(selectedRow, 0).toString();
        String trangThai = tableTicket.getValueAt(selectedRow, 8).toString();

        if (trangThai.equalsIgnoreCase("Đã Hủy")) {
            JOptionPane.showMessageDialog(this, "Vé này đã được hủy trước đó!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn HỦY vé " + maVe + " không?\nHành động này sẽ cập nhật trạng thái ghế thành 'Trống'.",
                "Xác nhận hủy vé", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            tableTicket.setValueAt("Đã Hủy", selectedRow, 8);
            JOptionPane.showMessageDialog(this, "Đã hủy vé " + maVe + " thành công!");
        }
    }

    private void nhapExcel() {
        JOptionPane.showMessageDialog(this, "Chức năng đọc file Excel và import danh sách vé vào CSDL!");
    }

    private void xuatExcel() {
        JOptionPane.showMessageDialog(this, "Chức năng xuất dữ liệu bảng Vé ra file Excel!");
    }

    // =========================================================================
    // CÁC HÀM TIỆN ÍCH GIAO DIỆN
    // =========================================================================

    private JButton createStyledButton(String text, Color bg, Color fg, String iconPath) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(145, 40));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        ImageIcon icon = loadImageIcon(iconPath, 30, 30);
        if (icon != null) {
            button.setIcon(icon);
        }

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { button.setBackground(bg.brighter()); }
            @Override
            public void mouseExited(MouseEvent e) { button.setBackground(bg); }
        });
        return button;
    }

    private ImageIcon loadImageIcon(String path, int width, int height) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);
                java.awt.Image img = originalIcon.getImage();
                java.awt.Image scaledImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImg);
            } else {
                System.err.println("Không tìm thấy icon tại: " + path);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}