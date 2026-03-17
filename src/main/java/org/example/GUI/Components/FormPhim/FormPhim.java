package org.example.GUI.Components.FormPhim;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;

public class FormPhim extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel lblPoster;
    private JTextField txtSearch;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;

    // Chỉ dùng để preview poster (demo)
    private final Map<Integer, String> posterMap = new HashMap<>();
    private int nextMaPhim = 6;

    public FormPhim() {
        setBackground(new Color(245, 245, 250));
        setLayout(new BorderLayout(15, 15));

        // ==================== HEADER ====================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 245, 250));
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("QUẢN LÝ PHIM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(new Color(33, 37, 41));
        header.add(lblTitle, BorderLayout.CENTER);

        // Search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        txtSearch = new JTextField(28);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtSearch.setBackground(Color.WHITE);
        txtSearch.setForeground(new Color(33, 37, 41));
        txtSearch.setCaretColor(new Color(33, 37, 41));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        btnTimKiem = createStyledButton("Tìm kiếm", new Color(0, 123, 255), Color.WHITE);
        btnTimKiem.setIcon(loadImageIcon("/org/example/GUI/resources/images/view.png"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnTimKiem);
        header.add(searchPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ==================== BẢNG PHIM ====================
        String[] columns = {"Mã Phim", "Tên Phim", "Thể Loại", "Thời Lượng", "Đạo Diễn", "Năm SX", "Ngày Khởi Chiếu", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(55);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setBackground(Color.WHITE);
        table.setForeground(new Color(33, 37, 41));
        table.setSelectionBackground(new Color(66, 103, 178));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(66, 103, 178));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // ==================== POSTER PANEL (BÊN PHẢI) ====================
        JPanel posterPanel = new JPanel(new BorderLayout());
        posterPanel.setPreferredSize(new Dimension(360, 0));
        posterPanel.setBackground(new Color(245, 245, 250));
        posterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(25, 20, 25, 20),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(148, 163, 184), 2),
                        "   POSTER PHIM   ",
                        0, 0, null, new Color(33, 37, 41))
        ));

        lblPoster = new JLabel("Chọn phim để xem poster", SwingConstants.CENTER);
        lblPoster.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblPoster.setForeground(new Color(100, 116, 139));
        lblPoster.setPreferredSize(new Dimension(320, 480));
        posterPanel.add(lblPoster, BorderLayout.CENTER);

        // ==================== TOOLBAR ====================
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        toolbar.setBackground(new Color(245, 245, 250));

        btnThem   = createStyledButton("Thêm", new Color(40, 167, 69), Color.WHITE);
        btnSua    = createStyledButton("Sửa", new Color(255, 193, 7), Color.BLACK);
        btnXoa    = createStyledButton("Xóa", new Color(220, 53, 69), Color.WHITE);
        btnLamMoi = createStyledButton("Làm mới", new Color(108, 117, 125), Color.WHITE);

        // Icon giống FormProduct
        btnThem.setIcon(loadImageIcon("/org/example/GUI/resources/images/plus.png"));
        btnSua.setIcon(loadImageIcon("/org/example/GUI/resources/images/editing.png"));
        btnXoa.setIcon(loadImageIcon("/org/example/GUI/resources/images/bin.png"));
        btnLamMoi.setIcon(loadImageIcon("/org/example/GUI/resources/images/icons8_data_backup_30px.png"));

        toolbar.add(btnThem);
        toolbar.add(btnSua);
        toolbar.add(btnXoa);
        toolbar.add(btnLamMoi);

        // ==================== MAIN LAYOUT ====================
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(new Color(245, 245, 250));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(toolbar, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
        add(posterPanel, BorderLayout.EAST);

        // ==================== DỮ LIỆU DEMO ====================
        loadDemoData();

        // ==================== SỰ KIỆN ====================
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showPoster();
        });

        btnTimKiem.addActionListener(e -> timKiem());
        txtSearch.addActionListener(e -> timKiem());

        btnThem.addActionListener(e -> themPhimDialog());
        btnSua.addActionListener(e -> suaPhimDialog());
        btnXoa.addActionListener(e -> xoaPhim());
        btnLamMoi.addActionListener(e -> loadDemoData());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) suaPhimDialog();
            }
        });
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private ImageIcon loadImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        }
        return null;
    }

    private void loadDemoData() {
        tableModel.setRowCount(0);
        posterMap.clear();

        Object[][] data = {
            {1, "Avengers: Endgame", "Hành động", "181 phút", "Anh em nhà Russo", 2019, "2025-12-01", "Đang chiếu", "https://picsum.photos/id/1015/800/1200"},
            {2, "Titanic", "Tình cảm", "195 phút", "James Cameron", 1997, "2026-01-15", "Đang chiếu", "https://picsum.photos/id/201/800/1200"},
            {3, "The Conjuring", "Kinh dị", "112 phút", "James Wan", 2013, "2026-02-20", "Đang chiếu", "https://picsum.photos/id/301/800/1200"},
            {4, "Deadpool & Wolverine", "Hài hước", "128 phút", "Shawn Levy", 2024, "2025-07-10", "Đang chiếu", "https://picsum.photos/id/401/800/1200"},
            {5, "Dune: Part Two", "Khoa học viễn tưởng", "166 phút", "Denis Villeneuve", 2024, "2026-03-01", "Đang chiếu", "https://picsum.photos/id/501/800/1200"}
        };

        for (Object[] row : data) {
            tableModel.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]});
            posterMap.put((Integer) row[0], (String) row[8]);
        }
        nextMaPhim = 6;
    }

    private void showPoster() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String url = posterMap.get((Integer) tableModel.getValueAt(row, 0));
        lblPoster.setText("Đang tải poster...");
        lblPoster.setIcon(null);

        new Thread(() -> {
            try {
                BufferedImage img = ImageIO.read(new URL(url));
                Image scaled = img.getScaledInstance(320, 480, Image.SCALE_SMOOTH);
                SwingUtilities.invokeLater(() -> {
                    lblPoster.setIcon(new ImageIcon(scaled));
                    lblPoster.setText("");
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> lblPoster.setText("❌ Không tải được poster"));
            }
        }).start();
    }

    private void timKiem() {
        String key = txtSearch.getText().trim().toLowerCase();
        if (key.isEmpty()) {
            loadDemoData();
            return;
        }

        // Lọc trực tiếp trên bảng
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            boolean match = false;
            for (int c = 1; c < 8; c++) {
                if (tableModel.getValueAt(i, c).toString().toLowerCase().contains(key)) {
                    match = true;
                    break;
                }
            }
            if (!match) tableModel.removeRow(i);
        }
    }

    private void themPhimDialog() {
        showDialog(null);
    }

    private void suaPhimDialog() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phim để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        showDialog(row);
    }

    private void showDialog(Integer editRow) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                editRow == null ? "➕ Thêm Phim Mới" : "✏️ Sửa Phim", true);
        dialog.setSize(580, 720);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(15, 23, 42));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(15, 23, 42));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 25, 12, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField ten = new JTextField(30);
        JComboBox<String> theLoai = new JComboBox<>(new String[]{"Hành động", "Tình cảm", "Kinh dị", "Hài hước", "Khoa học viễn tưởng"});
        JTextField thoiLuong = new JTextField();
        JTextField daoDien = new JTextField();
        JTextField nam = new JTextField();
        JTextField ngayKC = new JTextField();
        JComboBox<String> trangThai = new JComboBox<>(new String[]{"Đang chiếu", "Sắp chiếu", "Ngừng chiếu"});
        JTextField posterUrl = new JTextField();

        if (editRow != null) {
            ten.setText((String) tableModel.getValueAt(editRow, 1));
            theLoai.setSelectedItem(tableModel.getValueAt(editRow, 2));
            thoiLuong.setText(((String) tableModel.getValueAt(editRow, 3)).replace(" phút", ""));
            daoDien.setText((String) tableModel.getValueAt(editRow, 4));
            nam.setText(tableModel.getValueAt(editRow, 5).toString());
            ngayKC.setText((String) tableModel.getValueAt(editRow, 6));
            trangThai.setSelectedItem(tableModel.getValueAt(editRow, 7));
            posterUrl.setText(posterMap.getOrDefault((Integer) tableModel.getValueAt(editRow, 0), ""));
        }

        addFormRow(form, gbc, 0, "Tên phim:", ten);
        addFormRow(form, gbc, 1, "Thể loại:", theLoai);
        addFormRow(form, gbc, 2, "Thời lượng (phút):", thoiLuong);
        addFormRow(form, gbc, 3, "Đạo diễn:", daoDien);
        addFormRow(form, gbc, 4, "Năm sản xuất:", nam);
        addFormRow(form, gbc, 5, "Ngày khởi chiếu:", ngayKC);
        addFormRow(form, gbc, 6, "Trạng thái:", trangThai);
        addFormRow(form, gbc, 7, "URL Poster:", posterUrl);

        JButton luu = createStyledButton("💾 LƯU PHIM", new Color(40, 167, 69), Color.WHITE);
        gbc.gridy = 8; gbc.gridwidth = 2;
        form.add(luu, gbc);

        dialog.add(form);

        luu.addActionListener(e -> {
            try {
                if (editRow == null) {
                    Object[] rowData = {
                            nextMaPhim++,
                            ten.getText().trim(),
                            theLoai.getSelectedItem(),
                            thoiLuong.getText() + " phút",
                            daoDien.getText(),
                            Integer.parseInt(nam.getText()),
                            ngayKC.getText(),
                            trangThai.getSelectedItem()
                    };
                    tableModel.addRow(rowData);
                    posterMap.put((Integer) rowData[0], posterUrl.getText());
                } else {
                    tableModel.setValueAt(ten.getText().trim(), editRow, 1);
                    tableModel.setValueAt(theLoai.getSelectedItem(), editRow, 2);
                    tableModel.setValueAt(thoiLuong.getText() + " phút", editRow, 3);
                    tableModel.setValueAt(daoDien.getText(), editRow, 4);
                    tableModel.setValueAt(Integer.parseInt(nam.getText()), editRow, 5);
                    tableModel.setValueAt(ngayKC.getText(), editRow, 6);
                    tableModel.setValueAt(trangThai.getSelectedItem(), editRow, 7);
                    posterMap.put((Integer) tableModel.getValueAt(editRow, 0), posterUrl.getText());
                }
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng kiểm tra dữ liệu nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void addFormRow(JPanel p, GridBagConstraints gbc, int y, String label, JComponent comp) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        p.add(lbl, gbc);

        gbc.gridx = 1;
        p.add(comp, gbc);
    }

    private void xoaPhim() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phim cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận xóa phim này?\nHành động không thể hoàn tác.",
                "Xóa phim", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            posterMap.remove((Integer) tableModel.getValueAt(row, 0));
            tableModel.removeRow(row);
        }
    }

    // ==================== CHẠY THỬ (test nhanh) ====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("BookTicket - Form Quản Lý Phim");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1480, 880);
            frame.setLocationRelativeTo(null);
            frame.add(new FormPhim());
            frame.setVisible(true);
        });
    }
}