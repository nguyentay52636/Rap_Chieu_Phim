package org.example.GUI.Components.FormPhim;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import org.example.GUI.Components.FormPhim.Dialog.DialogAddPhim;
import org.example.GUI.Components.FormPhim.Dialog.DialogEditPhim;
import org.example.BUS.PhimBUS;
import org.example.BUS.TheLoaiPhimBUS;
import org.example.DTO.PhimDTO;
import org.example.DTO.TheLoaiPhimDTO;
import java.util.List;
import java.util.stream.Collectors;

public class FormPhim extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel lblPoster;
    private JTextField txtSearch;
    private JComboBox<String> cbFilterTrangThai;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;

    private final Map<Integer, String> posterMap = new HashMap<>();
    private PhimBUS phimBUS;
    private TheLoaiPhimBUS theLoaiPhimBUS;

    public FormPhim() {
        try {
            phimBUS = new PhimBUS();
            theLoaiPhimBUS = new TheLoaiPhimBUS();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        setBackground(new Color(245, 245, 250));
        setLayout(new BorderLayout(15, 15));
        initUI();
        loadFromBus();
    }

    private void initUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 245, 250));
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("QUẢN LÝ PHIM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        header.add(lblTitle, BorderLayout.CENTER);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setOpaque(false);

        cbFilterTrangThai = new JComboBox<>(new String[]{
            "Tất cả trạng thái", "Đang chiếu", "Sắp chiếu", "Ngừng chiếu"
        });
        cbFilterTrangThai.setPreferredSize(new Dimension(180, 40));
        cbFilterTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbFilterTrangThai.setBackground(Color.WHITE);

        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(250, 40));
        txtSearch.setToolTipText("Tìm theo tên phim...");
        btnTimKiem = createStyledButton("Tìm kiếm", new Color(0, 123, 255), Color.WHITE);
        
        searchPanel.add(new JLabel("Lọc:"));
        searchPanel.add(cbFilterTrangThai);
        searchPanel.add(txtSearch);
        searchPanel.add(btnTimKiem);
        header.add(searchPanel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Table
        String[] columns = {"Mã Phim", "Tên Phim", "Thể Loại", "Thời Lượng", "Đạo Diễn", "Năm SX", "Ngày Khởi Chiếu", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(50);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Poster Panel
        JPanel posterPanel = new JPanel(new BorderLayout());
        posterPanel.setPreferredSize(new Dimension(350, 0));
        posterPanel.setBorder(BorderFactory.createTitledBorder(" POSTER "));
        lblPoster = new JLabel("Chọn phim để xem", SwingConstants.CENTER);
        posterPanel.add(lblPoster, BorderLayout.CENTER);

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        btnThem = createStyledButton("Thêm Phim", new Color(40, 167, 69), Color.WHITE);
        btnSua = createStyledButton("Sửa Phim", new Color(255, 193, 7), Color.BLACK);
        btnXoa = createStyledButton("Xóa Phim", new Color(220, 53, 69), Color.WHITE);
        btnLamMoi = createStyledButton("Làm mới", new Color(108, 117, 125), Color.WHITE);
        toolbar.add(btnThem); toolbar.add(btnSua); toolbar.add(btnXoa); toolbar.add(btnLamMoi);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(toolbar, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
        add(posterPanel, BorderLayout.EAST);

        // Actions
        btnThem.addActionListener(e -> themPhimDialog());
        btnSua.addActionListener(e -> suaPhimDialog());
        btnLamMoi.addActionListener(e -> {
            if (phimBUS != null) phimBUS.refreshList();
            loadFromBus();
        });
        btnXoa.addActionListener(e -> xoaPhim());
        btnTimKiem.addActionListener(e -> locPhim());
        cbFilterTrangThai.addActionListener(e -> locPhim());
        
        table.getSelectionModel().addListSelectionListener(e -> showPoster());
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) suaPhimDialog();
            }
        });

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                locPhim();
            }
        });
    }

    private void loadFromBus() {
        if (phimBUS == null || theLoaiPhimBUS == null) return;
        displayOnTable(phimBUS.getList());
    }

    private void displayOnTable(List<PhimDTO> list) {
        tableModel.setRowCount(0);
        posterMap.clear();
        List<TheLoaiPhimDTO> listTL = theLoaiPhimBUS.getList();
        for (PhimDTO p : list) {
            String tenTheLoai = "Khác";
            if (listTL != null) {
                tenTheLoai = listTL.stream()
                        .filter(tl -> tl.getMaLoaiPhim() == p.getMaTheLoaiPhim())
                        .findFirst()
                        .map(TheLoaiPhimDTO::getTenLoaiPhim)
                        .orElse("Mã: " + p.getMaTheLoaiPhim());
            }
            tableModel.addRow(new Object[]{
                    p.getMaPhim(), p.getTenPhim(), tenTheLoai, p.getThoiLuong() + " phút",
                    p.getDaoDien(), p.getNamSanXuat(), p.getNgayKhoiChieu(), p.getTrangThai()
            });
            posterMap.put(p.getMaPhim(), p.getPosterURL());
        }
    }

    private void locPhim() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        String trangThaiSelected = (String) cbFilterTrangThai.getSelectedItem();
        
        List<PhimDTO> filteredList = phimBUS.getList();
        
        // Lọc theo tên phim
        if (!keyword.isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(p -> p.getTenPhim().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
        }
        
        // Lọc theo trạng thái
        if (!trangThaiSelected.equals("Tất cả trạng thái")) {
            filteredList = filteredList.stream()
                    .filter(p -> p.getTrangThai().equalsIgnoreCase(trangThaiSelected))
                    .collect(Collectors.toList());
        }
        
        displayOnTable(filteredList);
    }

    private void themPhimDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        DialogAddPhim dialog = new DialogAddPhim(owner);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                int maLoaiPhim = theLoaiPhimBUS.getList().stream()
                        .filter(tl -> tl.getTenLoaiPhim().equalsIgnoreCase(dialog.getTheLoai()))
                        .findFirst().map(TheLoaiPhimDTO::getMaLoaiPhim).orElse(1);

                PhimDTO p = new PhimDTO();
                p.setTenPhim(dialog.getTenPhim());
                p.setMaTheLoaiPhim(maLoaiPhim);
                p.setThoiLuong(dialog.getThoiLuong());
                p.setDaoDien(dialog.getDaoDien());
                p.setNamSanXuat(dialog.getNamSanXuat());
                p.setPosterURL(dialog.getPosterUrl());
                p.setTrangThai(dialog.getTrangThai());
                p.setNgayKhoiChieu(java.sql.Date.valueOf(dialog.getNgayKhoiChieu()));

                if (phimBUS.add(p)) {
                    JOptionPane.showMessageDialog(this, "✅ Đã thêm phim mới!");
                    loadFromBus();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi: " + ex.getMessage());
            }
        }
    }

    private void suaPhimDialog() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int maPhim = (int) tableModel.getValueAt(row, 0);
        String tenPhim = (String) tableModel.getValueAt(row, 1);
        String theLoai = (String) tableModel.getValueAt(row, 2);
        String thoiLuongStr = tableModel.getValueAt(row, 3).toString().replace(" phút", "");
        String daoDien = (String) tableModel.getValueAt(row, 4);
        String namStr = tableModel.getValueAt(row, 5).toString();
        String ngayKC = tableModel.getValueAt(row, 6).toString();
        String trangThai = (String) tableModel.getValueAt(row, 7);
        String poster = posterMap.get(maPhim);

        Window owner = SwingUtilities.getWindowAncestor(this);
        DialogEditPhim dialog = new DialogEditPhim(owner, tenPhim, theLoai, thoiLuongStr, daoDien, namStr, ngayKC, trangThai, poster);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            try {
                int maLoaiPhim = theLoaiPhimBUS.getList().stream()
                        .filter(tl -> tl.getTenLoaiPhim().equalsIgnoreCase(dialog.getTheLoai()))
                        .findFirst().map(TheLoaiPhimDTO::getMaLoaiPhim).orElse(1);

                PhimDTO p = new PhimDTO();
                p.setMaPhim(maPhim);
                p.setTenPhim(dialog.getTenPhim());
                p.setMaTheLoaiPhim(maLoaiPhim);
                p.setThoiLuong(dialog.getThoiLuong());
                p.setDaoDien(dialog.getDaoDien());
                p.setNamSanXuat(dialog.getNamSanXuat());
                p.setPosterURL(dialog.getPosterUrl());
                p.setTrangThai(dialog.getTrangThai());
                p.setNgayKhoiChieu(java.sql.Date.valueOf(dialog.getNgayKhoiChieu()));

                if (phimBUS.update(p)) {
                    JOptionPane.showMessageDialog(this, "✅ Cập nhật thành công!");
                    loadFromBus();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }

    private void xoaPhim() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int maPhim = (int) tableModel.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "Xóa phim này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (phimBUS.delete(maPhim)) {
                JOptionPane.showMessageDialog(this, "Đã xóa!");
                loadFromBus();
            }
        }
    }

    private void showPoster() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        String fileName = posterMap.get((int) tableModel.getValueAt(row, 0));
        lblPoster.setIcon(null);
        if (fileName == null || fileName.isEmpty()) {
            lblPoster.setText("Không có ảnh");
            return;
        }
        new Thread(() -> {
            try {
                File file = new File("src/main/java/org/example/GUI/resources/images/ImagePhim/" + fileName);
                if (file.exists()) {
                    BufferedImage img = ImageIO.read(file);
                    Image scaled = img.getScaledInstance(320, 480, Image.SCALE_SMOOTH);
                    SwingUtilities.invokeLater(() -> lblPoster.setIcon(new ImageIcon(scaled)));
                } else {
                    SwingUtilities.invokeLater(() -> lblPoster.setText("Không tìm thấy file ảnh"));
                }
            } catch (Exception ex) {}
        }).start();
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg); btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        return btn;
    }
}