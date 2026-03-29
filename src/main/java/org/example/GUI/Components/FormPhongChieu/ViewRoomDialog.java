package org.example.GUI.Components.FormPhongChieu;

import org.example.DTO.GheDTO;
import org.example.DTO.LoaiGheDTO;
import org.example.DTO.PhongChieuDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewRoomDialog {
    public static void open(FormPhongChieu parent) {
        JTable table = parent.getTablePhongChieu();
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) return;

        int modelRow = table.convertRowIndexToModel(viewRow);
        String maPhong = String.valueOf(parent.getModel().getValueAt(modelRow, 0));
        String tenPhong = String.valueOf(parent.getModel().getValueAt(modelRow, 1));
        String loaiPhong = String.valueOf(parent.getModel().getValueAt(modelRow, 2));
        int SoHang = (Integer) parent.getModel().getValueAt(modelRow, 3);
        int SoGheMoiHang = (Integer) parent.getModel().getValueAt(modelRow, 4);

        PhongChieuDTO phong = parent.getPcBUS().getPhongChieuById(Integer.parseInt(maPhong));

        Window owner = SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(owner, "Xem phòng: " + tenPhong);
        dialog.setContentPane(createPanel(parent, maPhong, tenPhong, loaiPhong, SoHang, SoGheMoiHang, phong.getGheList()));
        dialog.setSize(1270, 640);
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
    }

    private static JPanel createPanel(FormPhongChieu parent, String maPhong, String tenPhong, String loaiPhong, int SoHang, int SoGheMoiHang, List<GheDTO> danhSachGhe) {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel header = new JLabel("Phòng: " + tenPhong + " | Loại: " + loaiPhong + " | Mã: " + maPhong, SwingConstants.CENTER);
        header.setOpaque(true); header.setBackground(new Color(66, 103, 178)); header.setForeground(Color.WHITE);
        root.add(header, BorderLayout.NORTH);

        JPanel seatArea = new JPanel(new BorderLayout(10, 10));

        // 1. LẤY LẠI MÀU TRẮNG VÀ ĐỘ RỘNG CHO MÀN HÌNH
        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(new Color(30, 30, 35));
        screen.setBorder(new EmptyBorder(12, 12, 12, 12)); // Trả lại padding
        JLabel lblScreen = new JLabel("MÀN HÌNH", SwingConstants.CENTER);
        lblScreen.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblScreen.setForeground(Color.WHITE); // Trả lại màu chữ trắng
        screen.add(lblScreen, BorderLayout.CENTER);

        // 2. LẤY LẠI VIỀN CHO LƯỚI GHẾ
        JPanel grid = new JPanel(new GridLayout(SoHang, SoGheMoiHang, 8, 8));
        grid.setBackground(Color.WHITE);
        grid.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Sơ đồ ghế (Chỉ xem)",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)));
        JScrollPane scrollGrid = new JScrollPane(grid);
        scrollGrid.getVerticalScrollBar().setUnitIncrement(16);
        scrollGrid.getHorizontalScrollBar().setUnitIncrement(16);

        // 3. LẤY LẠI FONT CHỮ VÀ VIỀN CHO BẢNG
        DefaultTableModel seatModel = new DefaultTableModel(new String[]{"Mã Ghế", "Hàng", "Số", "Loại"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
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

        List<LoaiGheDTO> listLoaiGhe = parent.getLgBUS().getList();
        for (int i = 0; i < SoHang; i++) {
            char hang = (char) ('A' + i);
            for (int j = 1; j <= SoGheMoiHang; j++) {
                String maGhe = hang + String.format("%02d", j);
                JButton btn = new JButton(maGhe);

                // ---> 1. THÊM LẠI FONT VÀ KÍCH THƯỚC CỐ ĐỊNH <---
                btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btn.setMinimumSize(new Dimension(54, 44));

                GheDTO gheDb = null;
                if (danhSachGhe != null) {
                    for (GheDTO g : danhSachGhe) if (g.getHangGhe().equals(String.valueOf(hang)) && g.getSoGhe() == j) { gheDb = g; break; }
                }
                String tenLoai = "Unknown";
                if (gheDb != null) {
                    for (LoaiGheDTO l : listLoaiGhe) if (l.getMaLoaiGhe() == gheDb.getMaLoaiGhe()) tenLoai = l.getTenLoaiGhe();
                }

                btn.setBackground(new Color(66, 103, 178));
                btn.setForeground(Color.WHITE);
                btn.setFocusable(false);

                // ---> 2. THÊM LẠI MÔ HÌNH CHẶN HIỆU ỨNG CLICK/HOVER <---
                btn.setModel(new javax.swing.DefaultButtonModel() {
                    @Override public boolean isArmed() { return false; }
                    @Override public boolean isPressed() { return false; }
                    @Override public boolean isRollover() { return false; }
                });

                seatModel.addRow(new Object[]{maGhe, String.valueOf(hang), j, tenLoai});
                grid.add(btn);
            }
        }

        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.add(screen, BorderLayout.NORTH); leftPanel.add(scrollGrid, BorderLayout.CENTER);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, scrollTable);
        split.setResizeWeight(0.75);
        seatArea.add(split, BorderLayout.CENTER);
        root.add(seatArea, BorderLayout.CENTER);
        return root;
    }
}