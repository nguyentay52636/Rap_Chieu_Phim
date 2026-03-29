package org.example.GUI.Components.FormPhongChieu;

import org.example.DTO.PhongChieuDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SearchRoomHandler {

    private static String removeAccents(String str) {
        if (str == null) return "";
        String normalized = java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
    }

    private static int parseIntOrDefault(String text, int defaultValue) {
        try { return (text == null || text.trim().isEmpty()) ? defaultValue : Integer.parseInt(text.trim()); }
        catch (Exception e) { return defaultValue; }
    }

    public static void searchBasic(FormPhongChieu parent, String keyword, int searchType) {
        keyword = keyword.trim();
        if (keyword.isEmpty() && searchType != 2) {
            parent.loadDataToTable(); return;
        }

        if ((searchType == 0 || searchType == 3 || searchType == 4) && !keyword.matches("\\d+")) {
            JOptionPane.showMessageDialog(parent, "Vui lòng chỉ nhập số!", "Lỗi", JOptionPane.ERROR_MESSAGE); return;
        }

        List<PhongChieuDTO> allRooms = parent.getPcBUS().getList();
        List<PhongChieuDTO> filtered = new ArrayList<>();
        String normalizedKey = removeAccents(keyword.toLowerCase());

        for (PhongChieuDTO pc : allRooms) {
            boolean match = false;
            switch (searchType) {
                case 0: match = String.valueOf(pc.getMaPhong()).contains(keyword); break;
                case 1: match = removeAccents(pc.getTenPhong().toLowerCase()).contains(normalizedKey); break;
                case 2: match = pc.getLoaiPhong().equalsIgnoreCase(keyword); break;
                case 3: match = String.valueOf(pc.getSoHang()).contains(keyword); break;
                case 4: match = String.valueOf(pc.getSoGheMoiHang()).contains(keyword); break;
            }
            if (match) filtered.add(pc);
        }
        parent.loadDataToTable(filtered);
    }

    public static void openAdvancedSearchDialog(FormPhongChieu parent) {
        Window owner = SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(owner, "Lọc Nâng Cao", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(520, 420);
        dialog.setLocationRelativeTo(owner);

        JPanel panelCenter = new JPanel(new GridLayout(5, 2, 10, 15));
        panelCenter.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- BỌC LẠI Ô MÃ PHÒNG ---
        panelCenter.add(parent.createLabel("Mã Phòng:"));
        JTextField txtMa = parent.createStyledTextField("", 15);
        JPanel pnlMa = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlMa.add(txtMa);
        panelCenter.add(pnlMa);

        // --- BỌC LẠI Ô TÊN PHÒNG ---
        panelCenter.add(parent.createLabel("Tên Phòng:"));
        JTextField txtTen = parent.createStyledTextField("", 15);
        JPanel pnlTen = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTen.add(txtTen);
        panelCenter.add(pnlTen);

        panelCenter.add(parent.createLabel("Loại Phòng:"));
        JPanel cbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JCheckBox chk2D = new JCheckBox("2D"); JCheckBox chk3D = new JCheckBox("3D");
        JCheckBox chk4DX = new JCheckBox("4DX"); JCheckBox chkIMAX = new JCheckBox("IMAX");
        cbPanel.add(chk2D); cbPanel.add(chk3D); cbPanel.add(chk4DX); cbPanel.add(chkIMAX);
        panelCenter.add(cbPanel);

        panelCenter.add(parent.createLabel("Số Hàng:"));
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        // Giới hạn 4 cột cho độ rộng nhỏ
        JTextField txtRMin = parent.createStyledTextField("", 4); JTextField txtRMax = parent.createStyledTextField("", 4);
        rowPanel.add(txtRMin); rowPanel.add(new JLabel("-")); rowPanel.add(txtRMax); panelCenter.add(rowPanel);

        panelCenter.add(parent.createLabel("Ghế/Hàng:"));
        JPanel seatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JTextField txtSMin = parent.createStyledTextField("", 4); JTextField txtSMax = parent.createStyledTextField("", 4);
        seatPanel.add(txtSMin); seatPanel.add(new JLabel("-")); seatPanel.add(txtSMax); panelCenter.add(seatPanel);

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLoc = parent.createStyledButton("Lọc", new Color(40, 167, 69), Color.WHITE);
        panelBottom.add(btnLoc);

        btnLoc.addActionListener(e -> {
            String inputMa = txtMa.getText().trim();

            // ---> VALIDATION: Chặn gõ chữ vào ô Mã <---
            if (!inputMa.isEmpty() && !inputMa.matches("\\d+")) {
                JOptionPane.showMessageDialog(dialog, "Mã phòng chỉ được phép nhập số!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                return; // Dừng lại không lọc tiếp
            }

            List<String> selectedTypes = new ArrayList<>();
            if (chk2D.isSelected()) selectedTypes.add("2D");
            if (chk3D.isSelected()) selectedTypes.add("3D");
            if (chk4DX.isSelected()) selectedTypes.add("4DX");
            if (chkIMAX.isSelected()) selectedTypes.add("IMAX");

            int rMin = parseIntOrDefault(txtRMin.getText(), 0), rMax = parseIntOrDefault(txtRMax.getText(), Integer.MAX_VALUE);
            int sMin = parseIntOrDefault(txtSMin.getText(), 0), sMax = parseIntOrDefault(txtSMax.getText(), Integer.MAX_VALUE);
            String inputTen = removeAccents(txtTen.getText().trim().toLowerCase());

            List<PhongChieuDTO> filtered = new ArrayList<>();
            for (PhongChieuDTO pc : parent.getPcBUS().getList()) {
                boolean matchMa = inputMa.isEmpty() || String.valueOf(pc.getMaPhong()).contains(inputMa);
                boolean matchTen = inputTen.isEmpty() || removeAccents(pc.getTenPhong().toLowerCase()).contains(inputTen);
                boolean matchLoai = selectedTypes.isEmpty() || selectedTypes.contains(pc.getLoaiPhong());
                boolean matchRow = pc.getSoHang() >= rMin && pc.getSoHang() <= rMax;
                boolean matchSeat = pc.getSoGheMoiHang() >= sMin && pc.getSoGheMoiHang() <= sMax;

                if (matchMa && matchTen && matchLoai && matchRow && matchSeat) filtered.add(pc);
            }
            parent.loadDataToTable(filtered); dialog.dispose();
        });

        dialog.add(panelCenter, BorderLayout.CENTER); dialog.add(panelBottom, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}