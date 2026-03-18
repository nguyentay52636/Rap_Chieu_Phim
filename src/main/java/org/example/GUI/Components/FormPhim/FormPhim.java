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

import org.example.GUI.Components.FormPhim.Dialog.DialogAddPhim;
import org.example.GUI.Components.FormPhim.Dialog.DialogEditPhim;
import org.example.BUS.PhimBUS;
import org.example.DTO.PhimDTO;
import java.util.List;

public class FormPhim extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel lblPoster;
    private JTextField txtSearch;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;

    private final Map<Integer, String> posterMap = new HashMap<>();
    private int nextMaPhim = 6;
    private final PhimBUS phimBUS = new PhimBUS();
    private List<PhimDTO> listPhim;

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

        // ==================== DỮ LIỆU TỪ BUS/DAO ====================
        loadFromBus();

        // ==================== SỰ KIỆN ====================
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showPoster();
        });

        btnTimKiem.addActionListener(e -> timKiem());
        txtSearch.addActionListener(e -> timKiem());

        btnThem.addActionListener(e -> themPhimDialog());
        btnSua.addActionListener(e -> suaPhimDialog());
        btnXoa.addActionListener(e -> xoaPhim());
        btnLamMoi.addActionListener(e -> loadFromBus());

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

    private void loadFromBus() {
        tableModel.setRowCount(0);
        posterMap.clear();
        listPhim = phimBUS.getList();
        for (PhimDTO p : listPhim) {
            tableModel.addRow(new Object[]{
                    p.getMaPhim(),
                    p.getTenPhim(),
                    p.getMaTheLoaiPhim(),        // tạm hiển thị mã thể loại
                    p.getThoiLuong() + " phút",
                    p.getDaoDien(),
                    p.getNamSanXuat(),
                    p.getNgayKhoiChieu(),
                    p.getTrangThai()
            });
            posterMap.put(p.getMaPhim(), p.getPosterURL());
        }
        nextMaPhim = listPhim.stream().mapToInt(PhimDTO::getMaPhim).max().orElse(0) + 1;
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
            loadFromBus();
            return;
        }

        tableModel.setRowCount(0);
        for (PhimDTO p : listPhim) {
            String combined = (p.getTenPhim() + " " + p.getDaoDien()).toLowerCase();
            if (combined.contains(key)) {
                tableModel.addRow(new Object[]{
                        p.getMaPhim(),
                        p.getTenPhim(),
                        p.getMaTheLoaiPhim(),
                        p.getThoiLuong() + " phút",
                        p.getDaoDien(),
                        p.getNamSanXuat(),
                        p.getNgayKhoiChieu(),
                        p.getTrangThai()
                });
            }
        }
    }

    private void themPhimDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        DialogAddPhim dialog = new DialogAddPhim(owner);
        dialog.setVisible(true);
        if (!dialog.isSaved()) {
            return;
        }

        Integer thoiLuong = dialog.getThoiLuong();
        Integer namSX = dialog.getNamSanXuat();

        Object[] rowData = {
                nextMaPhim++,
                dialog.getTenPhim(),
                dialog.getTheLoai(),
                (thoiLuong != null ? thoiLuong : 0) + " phút",
                dialog.getDaoDien(),
                namSX != null ? namSX : 0,
                dialog.getNgayKhoiChieu(),
                dialog.getTrangThai()
        };
        tableModel.addRow(rowData);
        posterMap.put((Integer) rowData[0], dialog.getPosterUrl());
    }

    private void suaPhimDialog() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phim để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int maPhim = (Integer) tableModel.getValueAt(modelRow, 0);

        String tenPhim = String.valueOf(tableModel.getValueAt(modelRow, 1));
        String theLoai = String.valueOf(tableModel.getValueAt(modelRow, 2));
        String thoiLuongText = String.valueOf(tableModel.getValueAt(modelRow, 3)).replace(" phút", "").trim();
        String daoDien = String.valueOf(tableModel.getValueAt(modelRow, 4));
        String namText = String.valueOf(tableModel.getValueAt(modelRow, 5));
        String ngayKC = String.valueOf(tableModel.getValueAt(modelRow, 6));
        String trangThai = String.valueOf(tableModel.getValueAt(modelRow, 7));
        String posterUrl = posterMap.getOrDefault(maPhim, "");

        Window owner = SwingUtilities.getWindowAncestor(this);
        DialogEditPhim dialog = new DialogEditPhim(owner, tenPhim, theLoai, thoiLuongText, daoDien, namText, ngayKC, trangThai, posterUrl);
        dialog.setVisible(true);
        if (!dialog.isSaved()) {
            return;
        }

        tableModel.setValueAt(dialog.getTenPhim(), modelRow, 1);
        tableModel.setValueAt(dialog.getTheLoai(), modelRow, 2);
        tableModel.setValueAt(dialog.getThoiLuongPhutText() + " phút", modelRow, 3);
        tableModel.setValueAt(dialog.getDaoDien(), modelRow, 4);
        tableModel.setValueAt(Integer.parseInt(dialog.getNamSanXuatText()), modelRow, 5);
        tableModel.setValueAt(dialog.getNgayKhoiChieu(), modelRow, 6);
        tableModel.setValueAt(dialog.getTrangThai(), modelRow, 7);
        posterMap.put(maPhim, dialog.getPosterUrl());
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