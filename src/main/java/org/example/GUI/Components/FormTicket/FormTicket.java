package org.example.GUI.Components.FormTicket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;

public class FormTicket extends JPanel 
{
    private DefaultTableModel model;
    private JTable tableTicket;

    // Khai báo các nút chức năng trên giao diện
    private JButton btnView, btnAdd, btnHuy, btnXuat, btnLamMoi;

    // Khởi tạo Form
    public FormTicket() 
    {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(UIManager.getColor("Panel.background"));

        // ================== TOP PANEL (CHỨA NÚT VÀ TÌM KIẾM) ==================
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        topPanel.setBackground(UIManager.getColor("Panel.background"));

        // --- Panel Chức năng (Các nút Thêm, Xem, Hủy...) ---
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlActions.setBackground(UIManager.getColor("Panel.background"));
        pnlActions.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")),
                "Quản Lý Vé Phim", TitledBorder.LEFT, TitledBorder.TOP));

        // Khởi tạo các nút (Dùng hàm tự viết ở cuối file cho lẹ)
        btnView = createStyledButton("Xem Chi Tiết", new Color(102, 187, 106), Color.WHITE);
        btnView.addActionListener(e -> actionXemChiTiet());
        pnlActions.add(btnView);

        btnAdd = createStyledButton("Thêm Vé Mới", new Color(0, 123, 255), Color.WHITE);
        btnAdd.addActionListener(e -> actionThemVe());
        pnlActions.add(btnAdd);

        btnHuy = createStyledButton("Hủy Vé", new Color(220, 53, 69), Color.WHITE);
        btnHuy.addActionListener(e -> actionHuyVe());
        pnlActions.add(btnHuy);

        btnXuat = createStyledButton("Xuất Excel", new Color(153, 102, 255), Color.WHITE);
        btnXuat.addActionListener(e -> xuatExcel());
        pnlActions.add(btnXuat);

        // --- Panel Tìm kiếm (Tra cứu vé) ---
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnlSearch.setBackground(UIManager.getColor("Panel.background"));
        pnlSearch.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")),
                "Tra Cứu Vé Đã Bán", TitledBorder.LEFT, TitledBorder.TOP));

        pnlSearch.add(new JLabel("Tìm theo:"));
        JComboBox<String> cbTieuChi = new JComboBox<>(new String[]{"Tất cả", "Mã Vé", "Tên Phim", "Khách Hàng"});
        cbTieuChi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlSearch.add(cbTieuChi);

        JTextField txtTuKhoa = new JTextField(15);
        txtTuKhoa.putClientProperty("JTextField.placeholderText", "Nhập từ khóa...");
        txtTuKhoa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlSearch.add(txtTuKhoa);

        pnlSearch.add(new JLabel("Trạng thái:"));
        JComboBox<String> cbTrangThai = new JComboBox<>(new String[]{"Tất cả", "Chưa thanh toán", "Đã thanh toán", "Đã Hủy", "Da Ban"});
        cbTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlSearch.add(cbTrangThai);

        
        // Sự kiện tìm kiếm chung (Gọi BUS lấy dữ liệu ném lên bảng)
        Runnable runSearch = () -> 
        {
            String tieuChi = cbTieuChi.getSelectedItem().toString();
            String tuKhoa = txtTuKhoa.getText();
            String trangThai = cbTrangThai.getSelectedItem().toString();

            org.example.BUS.VeBUS veBUS = new org.example.BUS.VeBUS();
            java.util.List<org.example.DTO.VeDTO> ketQua = veBUS.timKiemVe(tieuChi, tuKhoa, trangThai);

            model.setRowCount(0);
            java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");
            for (org.example.DTO.VeDTO v : ketQua) {
                model.addRow(new Object[]{
                        v.getMaVe(), v.getKhachHang(), v.getTenPhim(), v.getTenPhong(), v.getTenGhe(),
                        v.getNgayChieu(), v.getGioBatDau(), df.format(v.getGiaVe()) + " VNĐ", v.getTrangThai()
                });
            }
        };

        // Gắn sự kiện (Gõ ký tự tới đâu lọc tới đó ngay lập tức)
        txtTuKhoa.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { runSearch.run(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { runSearch.run(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { runSearch.run(); }
        });

        // Bấm chọn combobox cũng lọc luôn
        cbTieuChi.addActionListener(e -> runSearch.run());
        cbTrangThai.addActionListener(e -> runSearch.run());

        // Nút reset form tìm kiếm
        btnLamMoi = createStyledButton("Làm mới", new Color(108, 117, 125), Color.WHITE);
        btnLamMoi.addActionListener(e -> {
            cbTieuChi.setSelectedIndex(0);
            cbTrangThai.setSelectedIndex(0);
            txtTuKhoa.setText("");
            loadDataToTable();
        });
        pnlSearch.add(btnLamMoi);

        topPanel.add(pnlActions);
        topPanel.add(pnlSearch);

        // ================== CENTER PANEL (BẢNG JTABLE) ==================
        String[] columnNames = {"Mã Vé", "Khách Hàng", "Tên Phim", "Phòng", "Ghế", "Ngày Chiếu", "Giờ", "Giá Tiền", "Trạng Thái"};
        model = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Khóa không cho gõ bậy vào bảng
        };

        tableTicket = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) 
            {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) 
                {
                    ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                }
                // Tạo màu xen kẽ cho các dòng của bảng (Zebra striping)
                if (!isRowSelected(row)) 
                {
                    Color bg = UIManager.getColor("Table.background");
                    if (bg != null) 
                    {
                        if (row % 2 == 0) c.setBackground(bg);
                        else 
                        {
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

            // In chữ "DANH SÁCH TRỐNG" nếu bảng không có dữ liệu
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                if (getRowCount() == 0) 
                {
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

        // Làm đẹp bảng JTable
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

        // Load dữ liệu lên bảng khi vừa mở form
        loadDataToTable();
    }

    // =========================================================================
    // CÁC HÀM XỬ LÝ SỰ KIỆN
    // =========================================================================

    // Hàm lấy danh sách vé từ DB và đẩy lên bảng
    public void loadDataToTable() 
    {
        model.setRowCount(0);
        org.example.BUS.VeBUS veBUS = new org.example.BUS.VeBUS();
        java.util.List<org.example.DTO.VeDTO> danhSach = veBUS.getAllVe();
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");

        for (org.example.DTO.VeDTO v : danhSach) 
        {
            model.addRow(new Object[]{
                    v.getMaVe(), v.getKhachHang(), v.getTenPhim(), v.getTenPhong(), v.getTenGhe(),
                    v.getNgayChieu(), v.getGioBatDau(), df.format(v.getGiaVe()) + " VNĐ", v.getTrangThai()
            });
        }
    }

    // Mở popup xem chi tiết vé
    private void actionXemChiTiet() 
    {
        int selectedRow = tableTicket.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 vé trên bảng để xem chi tiết!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy dữ liệu từ các cột tương ứng (0: Mã vé, 1: Khách hàng, 2: Tên phim,...)
        String maVe = tableTicket.getValueAt(selectedRow, 0).toString();
        String khachHang = tableTicket.getValueAt(selectedRow, 1).toString();
        String tenPhim = tableTicket.getValueAt(selectedRow, 2).toString();
        String phong = tableTicket.getValueAt(selectedRow, 3).toString();
        String ghe = tableTicket.getValueAt(selectedRow, 4).toString();
        String ngay = tableTicket.getValueAt(selectedRow, 5).toString();
        String gio = tableTicket.getValueAt(selectedRow, 6).toString();
        String gia = tableTicket.getValueAt(selectedRow, 7).toString();
        String trangThai = tableTicket.getValueAt(selectedRow, 8).toString();

        Window owner = SwingUtilities.getWindowAncestor(this);
        DialogChiTietVe dialog = new DialogChiTietVe(owner, maVe, khachHang, tenPhim, phong, ghe, ngay, gio, gia, trangThai);
        dialog.setVisible(true);
    }

    // Mở popup thêm vé
    private void actionThemVe() 
    {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        DialogChonSuatChieu dialogChonSuat = new DialogChonSuatChieu(owner);
        dialogChonSuat.setVisible(true);
        loadDataToTable(); // Load lại bảng sau khi tắt popup
    }

    // Xử lý hủy vé
    private void actionHuyVe() 
    {
        int selectedRow = tableTicket.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 vé để hủy!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy Mã Vé (Cột 0) và Trạng Thái (Cột 8)
        String maVe = tableTicket.getValueAt(selectedRow, 0).toString();
        String trangThai = tableTicket.getValueAt(selectedRow, 8).toString();

        if (trangThai.equalsIgnoreCase("Đã Hủy")) 
        {
            JOptionPane.showMessageDialog(this, "Vé này đã được hủy trước đó!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn HỦY vé " + maVe + " không?\nHành động này sẽ cập nhật trạng thái ghế thành 'Trống'.",
                "Xác nhận hủy vé", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) 
        {
            org.example.BUS.VeBUS veBUS = new org.example.BUS.VeBUS();
            // Gọi BUS để hủy dưới Database
            if (veBUS.delete(Integer.parseInt(maVe))) 
            {
                tableTicket.setValueAt("Đã Hủy", selectedRow, 8); // Cập nhật lại cột 8 trên JTable
                JOptionPane.showMessageDialog(this, "Đã hủy vé " + maVe + " thành công!");
            } 
            else 
            {
                JOptionPane.showMessageDialog(this, "Lỗi khi hủy vé dưới Database!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Xử lý xuất file Excel
    private void xuatExcel() 
    {
        try 
        {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setDialogTitle("Chọn nơi lưu file Excel");
            int choose = jFileChooser.showSaveDialog(this);

            if (choose == JFileChooser.APPROVE_OPTION) 
                {
                File file = jFileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".xlsx")) 
                {
                    file = new File(file.getParentFile(), file.getName() + ".xlsx");
                }

                org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
                org.apache.poi.xssf.usermodel.XSSFSheet sheet = workbook.createSheet("DanhSachVe");

                // Ghi Header
                org.apache.poi.xssf.usermodel.XSSFRow headerRow = sheet.createRow(0);
                for (int i = 0; i < tableTicket.getColumnCount(); i++) {
                    headerRow.createCell(i).setCellValue(tableTicket.getColumnName(i));
                }

                // Ghi dữ liệu từng dòng
                for (int i = 0; i < tableTicket.getRowCount(); i++) {
                    org.apache.poi.xssf.usermodel.XSSFRow row = sheet.createRow(i + 1);
                    for (int j = 0; j < tableTicket.getColumnCount(); j++) {
                        Object value = tableTicket.getValueAt(i, j);
                        row.createCell(j).setCellValue(value != null ? value.toString() : "");
                    }
                }

                FileOutputStream fos = new FileOutputStream(file);
                workbook.write(fos);
                fos.close();
                workbook.close();

                JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!\nĐã lưu tại: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi xuất Excel: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // =========================================================================
    // HÀM TIỆN ÍCH
    // =========================================================================

    // Hàm tạo giao diện cho nút bấm (dùng chung trong class này)
    private JButton createStyledButton(String text, Color bg, Color fg) 
    {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(145, 40));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { button.setBackground(bg.brighter()); }
            @Override
            public void mouseExited(MouseEvent e) { button.setBackground(bg); }
        });
        return button;
    }
}