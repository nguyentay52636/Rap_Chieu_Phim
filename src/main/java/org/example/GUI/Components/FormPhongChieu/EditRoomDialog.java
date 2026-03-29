package org.example.GUI.Components.FormPhongChieu;

import org.example.DTO.GheDTO;
import org.example.DTO.LoaiGheDTO;
import org.example.DTO.PhongChieuDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EditRoomDialog {

    public static void open(FormPhongChieu parent) {
        JTable table = parent.getTablePhongChieu();
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(parent, "Chọn 1 phòng trong bảng trước!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);
        String maPhong = String.valueOf(parent.getModel().getValueAt(modelRow, 0));
        String tenPhong = String.valueOf(parent.getModel().getValueAt(modelRow, 1));
        String loaiPhong = String.valueOf(parent.getModel().getValueAt(modelRow, 2));
        int SoHang = (Integer) parent.getModel().getValueAt(modelRow, 3);
        int SoGheMoiHang = (Integer) parent.getModel().getValueAt(modelRow, 4);

        int idPhong = Integer.parseInt(maPhong);
        PhongChieuDTO phong = parent.getPcBUS().getPhongChieuById(idPhong);
        List<GheDTO> danhSachGhe = phong.getGheList();

        Window owner = SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(owner, "Sửa phòng: " + tenPhong + " (" + maPhong + ")");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(createPanel(parent, maPhong, tenPhong, loaiPhong, SoHang, SoGheMoiHang, danhSachGhe));
        dialog.setSize(1270, 640);
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
    }

    private static JPanel createPanel(FormPhongChieu parent, String maPhong, String tenPhong, String loaiPhong, int SoHang, int SoGheMoiHang, List<GheDTO> danhSachGhe) {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        root.setBackground(new Color(245, 245, 250));

        int[] currentDim = {SoHang, SoGheMoiHang};
        List<String> selectedSeats = new ArrayList<>();
        List<GheDTO> localSeats = new ArrayList<>();
        if (danhSachGhe != null) localSeats.addAll(danhSachGhe);
        int idPhong = Integer.parseInt(maPhong);

        // --- TOP PANEL ---
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(new Color(245, 245, 250));

        JPanel leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftControls.setOpaque(false);

        // Loại ghế
        JPanel chairPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        chairPanel.setBorder(BorderFactory.createTitledBorder("Đổi Loại"));
        List<LoaiGheDTO> listLoaiGhe = parent.getLgBUS().getList();
        String[] dsLoaiGhe = listLoaiGhe.stream().map(LoaiGheDTO::getTenLoaiGhe).toArray(String[]::new);
        JComboBox<String> optLoaiGhe = parent.createStyledComboBox(dsLoaiGhe);
        JButton btnApplySeatType = parent.createStyledButton("Đổi", new Color(255, 193, 7), Color.BLACK);
        chairPanel.add(optLoaiGhe); chairPanel.add(btnApplySeatType);

        // Kích thước
        JPanel dimPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dimPanel.setBorder(BorderFactory.createTitledBorder("Kích Thước"));
        JTextField txtSoHang = parent.createStyledTextField(String.valueOf(currentDim[0]), 3);
        JTextField txtSoGhe = parent.createStyledTextField(String.valueOf(currentDim[1]), 3);
        JButton btnApplySize = parent.createStyledButton("Tạo lại", new Color(0, 123, 255), Color.WHITE);
        dimPanel.add(parent.createLabel("Hàng:")); dimPanel.add(txtSoHang);
        dimPanel.add(parent.createLabel("Ghế:")); dimPanel.add(txtSoGhe); dimPanel.add(btnApplySize);

        // Chọn nhanh
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Chọn Nhanh"));

        JButton btnSelectAll = parent.createStyledButton("Tất cả", new Color(23, 162, 184), Color.WHITE);

        JButton btnClearSel = parent.createStyledButton("Bỏ chọn", new Color(108, 117, 125), Color.WHITE);
        btnClearSel.setPreferredSize(new Dimension(100, 35)); // <--- Thêm dòng này để nới rộng nút

        JComboBox<String> optRowSelect = parent.createStyledComboBox(new String[]{});

        JButton btnSelectRow = parent.createStyledButton("Chọn Hàng", new Color(23, 162, 184), Color.WHITE);
        btnSelectRow.setPreferredSize(new Dimension(120, 35)); // <--- Thêm dòng này để nới rộng nút

        selectionPanel.add(btnSelectAll);
        selectionPanel.add(btnClearSel);
        selectionPanel.add(optRowSelect);
        selectionPanel.add(btnSelectRow);

        leftControls.add(chairPanel); leftControls.add(dimPanel); leftControls.add(selectionPanel);

        JScrollPane scrollLeftControls = new JScrollPane(leftControls);
        scrollLeftControls.setBorder(null); scrollLeftControls.setOpaque(false); scrollLeftControls.getViewport().setOpaque(false);

        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        rightControls.setBorder(BorderFactory.createTitledBorder("Thoát"));
        JButton btnCancel = parent.createStyledButton("Hủy", new Color(108, 117, 125), Color.WHITE);
        JButton btnSave = parent.createStyledButton("Lưu", new Color(40, 167, 69), Color.WHITE);
        rightControls.add(btnCancel); rightControls.add(btnSave);

        topPanel.add(scrollLeftControls, BorderLayout.CENTER);
        topPanel.add(rightControls, BorderLayout.EAST);
        root.add(topPanel, BorderLayout.NORTH);

        // --- CENTER PANEL ---
        JPanel seatArea = new JPanel(new BorderLayout(10, 10));
        seatArea.setOpaque(false);
        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(new Color(30, 30, 35));
        screen.setBorder(new EmptyBorder(12, 12, 12, 12));
        JLabel lblScreen = new JLabel("MÀN HÌNH", SwingConstants.CENTER);
        lblScreen.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblScreen.setForeground(Color.WHITE);
        screen.add(lblScreen, BorderLayout.CENTER);

        JPanel grid = new JPanel();
        JScrollPane scrollGrid = new JScrollPane(grid);

        DefaultTableModel seatModel = new DefaultTableModel(new String[]{"Mã Ghế", "Hàng", "Số", "Loại", "Trạng thái"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable seatTable = new JTable(seatModel);
        seatTable.setRowHeight(30);
        JScrollPane scrollTable = new JScrollPane(seatTable);

        scrollGrid.getVerticalScrollBar().setUnitIncrement(16);
        scrollGrid.getHorizontalScrollBar().setUnitIncrement(16);
        scrollTable.getVerticalScrollBar().setUnitIncrement(16);
        scrollTable.getHorizontalScrollBar().setUnitIncrement(16);

        Color colorSelected = new Color(255, 193, 7);
        Color colorThuong = new Color(66, 103, 178);

        Runnable renderGrid = () -> {
            grid.removeAll();
            grid.setLayout(new GridLayout(currentDim[0], currentDim[1], 8, 8));
            seatModel.setRowCount(0); selectedSeats.clear();
            optRowSelect.removeAllItems();
            for (int i = 0; i < currentDim[0]; i++) optRowSelect.addItem(String.valueOf((char)('A'+i)));

            for (int i = 0; i < currentDim[0]; i++) {
                char hang = (char) ('A' + i);
                for (int j = 1; j <= currentDim[1]; j++) {
                    int finalJ = j;
                    String maGhe = hang + String.format("%02d", finalJ);
                    JButton btn = new JButton(maGhe);
                    btn.setPreferredSize(new Dimension(54, 44));

                    GheDTO gheDb = localSeats.stream().filter(g -> g.getHangGhe().equals(String.valueOf(hang)) && g.getSoGhe() == finalJ).findFirst().orElse(null);
                    if (gheDb == null) {
                        int defaultMa = listLoaiGhe.isEmpty() ? 1 : listLoaiGhe.get(0).getMaLoaiGhe();
                        gheDb = new GheDTO(0, idPhong, defaultMa, String.valueOf(hang), finalJ);
                        localSeats.add(gheDb);
                    }

                    String tenLoai = "Unknown";
                    for (LoaiGheDTO l : listLoaiGhe) { if (l.getMaLoaiGhe() == gheDb.getMaLoaiGhe()) tenLoai = l.getTenLoaiGhe(); }

                    btn.setBackground(colorThuong); btn.setForeground(Color.WHITE);
                    btn.putClientProperty("originalColor", colorThuong);
                    seatModel.addRow(new Object[]{maGhe, String.valueOf(hang), finalJ, tenLoai, ""});

                    btn.addActionListener(e -> {
                        boolean isSel = !btn.getBackground().equals(colorSelected);
                        if (isSel) { btn.setBackground(colorSelected); btn.setForeground(Color.BLACK); selectedSeats.add(maGhe); }
                        else { btn.setBackground(colorThuong); btn.setForeground(Color.WHITE); selectedSeats.remove(maGhe); }
                        for (int r = 0; r < seatModel.getRowCount(); r++) {
                            if (seatModel.getValueAt(r, 0).equals(maGhe)) seatModel.setValueAt(isSel ? "Đang chọn..." : "", r, 4);
                        }
                    });
                    grid.add(btn);
                }
            }
            grid.revalidate(); grid.repaint();
        };
        renderGrid.run();

        // Xử lý sự kiện (Rút gọn)
        btnApplySize.addActionListener(e -> {
            try {
                int newRow = Integer.parseInt(txtSoHang.getText());
                int newCol = Integer.parseInt(txtSoGhe.getText());
                if (newRow <= 0 || newCol <= 0 || newRow > 26) throw new Exception();
                currentDim[0] = newRow; currentDim[1] = newCol;
                localSeats.removeIf(g -> g.getHangGhe().charAt(0) > ('A' + currentDim[0] - 1) || g.getSoGhe() > currentDim[1]);
                renderGrid.run();
            } catch (Exception ex) { JOptionPane.showMessageDialog(root, "Lỗi kích thước!"); }
        });

        btnApplySeatType.addActionListener(e -> {
            if(selectedSeats.isEmpty()) return;
            int maLoai = listLoaiGhe.get(optLoaiGhe.getSelectedIndex()).getMaLoaiGhe();
            for(String s : selectedSeats) {
                String h = String.valueOf(s.charAt(0)); int num = Integer.parseInt(s.substring(1));
                localSeats.stream().filter(g -> g.getHangGhe().equals(h) && g.getSoGhe() == num).forEach(g -> g.setMaLoaiGhe(maLoai));
            }
            renderGrid.run();
        });

        // --- LOGIC CHO CÁC NÚT CHỌN NHANH ---
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

        btnCancel.addActionListener(e -> SwingUtilities.getWindowAncestor(root).dispose());
        btnSave.addActionListener(e -> {
            PhongChieuDTO updatedRoom = new PhongChieuDTO(idPhong, tenPhong, loaiPhong, currentDim[0], currentDim[1]);
            updatedRoom.setGheList(localSeats);
            if (parent.getPcBUS().updateRoomAndSeats(updatedRoom)) {
                JOptionPane.showMessageDialog(root, "Thành công!");
                parent.loadDataToTable(); SwingUtilities.getWindowAncestor(root).dispose();
            }
        });

        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setOpaque(false); leftPanel.add(screen, BorderLayout.NORTH); leftPanel.add(scrollGrid, BorderLayout.CENTER);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, scrollTable);
        split.setResizeWeight(0.75);
        seatArea.add(split, BorderLayout.CENTER);
        root.add(seatArea, BorderLayout.CENTER);

        return root;
    }
}