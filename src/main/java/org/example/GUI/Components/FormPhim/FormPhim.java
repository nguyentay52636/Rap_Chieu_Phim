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

public class FormPhim extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel lblPoster;
    private JTextField txtSearch;
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

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        txtSearch = new JTextField(28);
        txtSearch.setPreferredSize(new Dimension(300, 40));
        btnTimKiem = createStyledButton("Tìm kiếm", new Color(0, 123, 255), Color.WHITE);
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
        table.getSelectionModel().addListSelectionListener(e -> showPoster());
        
        // Double click to edit
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) suaPhimDialog();
            }
        });
    }

    private void loadFromBus() {
        if (phimBUS == null || theLoaiPhimBUS == null) return;
        tableModel.setRowCount(0);
        posterMap.clear();
        List<PhimDTO> list = phimBUS.getList();
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
                    p.getMaPhim(), p.getTenPhim(), tenTheLoai, p.getThoiLuong(),
                    p.getDaoDien(), p.getNamSanXuat(), p.getNgayKhoiChieu(), p.getTrangThai()
            });
            posterMap.put(p.getMaPhim(), p.getPosterURL());
        }
    }

    private void themPhimDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        DialogAddPhim dialog = new DialogAddPhim(owner);
        dialog.setVisible(true);
        
        if (!dialog.isSaved()) return;

        try {
            int maLoaiPhim = theLoaiPhimBUS.getList().stream()
                    .filter(tl -> tl.getTenLoaiPhim().equalsIgnoreCase(dialog.getTheLoai()))
                    .findFirst()
                    .map(TheLoaiPhimDTO::getMaLoaiPhim)
                    .orElse(1);

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
                JOptionPane.showMessageDialog(this, "✅ THÀNH CÔNG: Đã thêm phim mới!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadFromBus();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ LỖI: " + ex.getMessage());
        }
    }

    private void suaPhimDialog() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phim cần sửa!");
            return;
        }

        int maPhim = (int) tableModel.getValueAt(row, 0);
        String tenPhim = (String) tableModel.getValueAt(row, 1);
        String theLoai = (String) tableModel.getValueAt(row, 2);
        String thoiLuongStr = tableModel.getValueAt(row, 3).toString();
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
                        .findFirst()
                        .map(TheLoaiPhimDTO::getMaLoaiPhim)
                        .orElse(1);

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
                JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + ex.getMessage());
            }
        }
    }

    private void xoaPhim() {
        if (phimBUS == null) return;
        int row = table.getSelectedRow();
        if (row == -1) return;
        int maPhim = (int) tableModel.getValueAt(row, 0);
        if (confirmDelete()) {
            if (phimBUS.delete(maPhim)) {
                JOptionPane.showMessageDialog(this, "Đã xóa!");
                loadFromBus();
            }
        }
    }

    private boolean confirmDelete() {
        return JOptionPane.showConfirmDialog(this, "Xóa phim này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private void showPoster() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Object val = tableModel.getValueAt(row, 0);
        if (val == null) return;
        String fileName = posterMap.get((int) val);
        
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