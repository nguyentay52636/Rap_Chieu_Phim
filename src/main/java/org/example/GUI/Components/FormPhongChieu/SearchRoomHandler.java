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
        try {
            return (text == null || text.trim().isEmpty()) ? defaultValue : Integer.parseInt(text.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static void searchBasic(FormPhongChieu parent, String keyword, int searchType) {
        keyword = keyword.trim();
        if (keyword.isEmpty() && searchType != 2) {
            parent.loadDataToTable();
            return;
        }

        if ((searchType == 0 || searchType == 3 || searchType == 4) && !keyword.matches("\\d+")) {
            JOptionPane.showMessageDialog(parent, "Vui lòng chỉ nhập số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<PhongChieuDTO> allRooms = parent.getPcBUS().getList();
        List<PhongChieuDTO> filtered = new ArrayList<>();
        String normalizedKey = removeAccents(keyword.toLowerCase());

        for (PhongChieuDTO pc : allRooms) {
            boolean match = false;
            switch (searchType) {
                case 0:
                    match = String.valueOf(pc.getMaPhong()).contains(keyword);
                    break;
                case 1:
                    match = removeAccents(pc.getTenPhong().toLowerCase()).contains(normalizedKey);
                    break;
                case 2:
                    match = pc.getLoaiPhong().equalsIgnoreCase(keyword);
                    break;
                case 3:
                    match = String.valueOf(pc.getSoHang()).contains(keyword);
                    break;
                case 4:
                    match = String.valueOf(pc.getSoGheMoiHang()).contains(keyword);
                    break;
            }
            if (match) filtered.add(pc);
        }
        parent.loadDataToTable(filtered);
    }

    public static void openAdvancedSearchDialog(FormPhongChieu parent) {
        Window owner = SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(owner, "Lọc Nâng Cao", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(600, 420);
        dialog.setLocationRelativeTo(owner);

        JPanel panelCenter = new JPanel(new GridLayout(5, 2, 10, 15));
        panelCenter.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] numOptions = {"Khoảng", ">", ">=", "<", "<=", "=", "<>"};

        // --- BỌC LẠI Ô MÃ PHÒNG ---
        panelCenter.add(parent.createLabel("Mã Phòng:"));
        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JComboBox<String> cbIDMode = parent.createStyledComboBox(numOptions);
        JTextField txtMa = parent.createStyledTextField("", 4);
        JLabel lblIDDash = new JLabel("-");
        JTextField txtID2 = parent.createStyledTextField("", 4);
        idPanel.add(cbIDMode);
        idPanel.add(txtMa);
        idPanel.add(lblIDDash);
        idPanel.add(txtID2);
        panelCenter.add(idPanel);

        // --- BỌC LẠI Ô TÊN PHÒNG ---
        panelCenter.add(parent.createLabel("Tên Phòng:"));
        JTextField txtTen = parent.createStyledTextField("", 20);
        JPanel pnlTen = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTen.add(txtTen);
        panelCenter.add(pnlTen);

        panelCenter.add(parent.createLabel("Loại Phòng:"));
        JPanel cbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JCheckBox chk2D = new JCheckBox("2D");
        JCheckBox chk3D = new JCheckBox("3D");
        JCheckBox chk4DX = new JCheckBox("4DX");
        JCheckBox chkIMAX = new JCheckBox("IMAX");
        cbPanel.add(chk2D);
        cbPanel.add(chk3D);
        cbPanel.add(chk4DX);
        cbPanel.add(chkIMAX);
        panelCenter.add(cbPanel);

        panelCenter.add(parent.createLabel("Số Hàng:"));
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JComboBox<String> cbRowMode = parent.createStyledComboBox(numOptions);
        JTextField txtR1 = parent.createStyledTextField("", 4);
        JLabel lblRDash = new JLabel("-");
        JTextField txtR2 = parent.createStyledTextField("", 4);
        rowPanel.add(cbRowMode);
        rowPanel.add(txtR1);
        rowPanel.add(lblRDash);
        rowPanel.add(txtR2);
        panelCenter.add(rowPanel);

        panelCenter.add(parent.createLabel("Ghế/Hàng:"));
        JPanel seatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JComboBox<String> cbSeatMode = parent.createStyledComboBox(numOptions);
        JTextField txtS1 = parent.createStyledTextField("", 4);
        JLabel lblSDash = new JLabel("-");
        JTextField txtS2 = parent.createStyledTextField("", 4);
        seatPanel.add(cbSeatMode);
        seatPanel.add(txtS1);
        seatPanel.add(lblSDash);
        seatPanel.add(txtS2);
        panelCenter.add(seatPanel);

        cbIDMode.addActionListener(e -> {
            boolean isRange = cbIDMode.getSelectedItem().equals("Khoảng");
            lblIDDash.setVisible(isRange);
            txtID2.setVisible(isRange);
        });

        cbRowMode.addActionListener(e -> {
            boolean isRange = cbRowMode.getSelectedItem().equals("Khoảng");
            lblRDash.setVisible(isRange);
            txtR2.setVisible(isRange);
        });

        cbSeatMode.addActionListener(e -> {
            boolean isRange = cbSeatMode.getSelectedItem().equals("Khoảng");
            lblSDash.setVisible(isRange);
            txtS2.setVisible(isRange);
        });

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLoc = parent.createStyledButton("Lọc", new Color(40, 167, 69), Color.WHITE);
        panelBottom.add(btnLoc);

        btnLoc.addActionListener(e -> {
            String inputMa = txtMa.getText().trim();
            String r1 = txtR1.getText().trim();
            String r2 = txtR2.getText().trim();
            String s1 = txtS1.getText().trim();
            String s2 = txtS2.getText().trim();

            // ---> VALIDATION TỔNG HỢP: Chặn gõ chữ vào TẤT CẢ các ô yêu cầu SỐ <---
            boolean isMaInvalid = !inputMa.isEmpty() && !inputMa.matches("\\d+");
            boolean isR1Invalid = !r1.isEmpty() && !r1.matches("\\d+");
            boolean isR2Invalid = !r2.isEmpty() && !r2.matches("\\d+");
            boolean isS1Invalid = !s1.isEmpty() && !s1.matches("\\d+");
            boolean isS2Invalid = !s2.isEmpty() && !s2.matches("\\d+");

            if (isMaInvalid || isR1Invalid || isR2Invalid || isS1Invalid || isS2Invalid) {
                JOptionPane.showMessageDialog(dialog,
                        "Các ô Mã Phòng, Số Hàng và Ghế/Hàng chỉ được phép nhập số nguyên!",
                        "Lỗi nhập liệu",
                        JOptionPane.ERROR_MESSAGE);
                return; // Dừng lại ngay lập tức, không lọc tiếp
            }

            // Lấy danh sách loại phòng đã chọn
            List<String> selectedTypes = new ArrayList<>();
            if (chk2D.isSelected()) selectedTypes.add("2D");
            if (chk3D.isSelected()) selectedTypes.add("3D");
            if (chk4DX.isSelected()) selectedTypes.add("4DX");
            if (chkIMAX.isSelected()) selectedTypes.add("IMAX");

            String inputTen = removeAccents(txtTen.getText().trim().toLowerCase());

            List<PhongChieuDTO> filtered = new ArrayList<>();
            for (PhongChieuDTO pc : parent.getPcBUS().getList()) {
                boolean matchMa = inputMa.isEmpty() || String.valueOf(pc.getMaPhong()).contains(inputMa);
                boolean matchTen = inputTen.isEmpty() || removeAccents(pc.getTenPhong().toLowerCase()).contains(inputTen);
                boolean matchLoai = selectedTypes.isEmpty() || selectedTypes.contains(pc.getLoaiPhong());

                // --- KIỂM TRA ĐIỀU KIỆN SỐ ---
                boolean matchRow = checkNumberCondition(pc.getSoHang(), (String) cbRowMode.getSelectedItem(), r1, r2);
                boolean matchSeat = checkNumberCondition(pc.getSoGheMoiHang(), (String) cbSeatMode.getSelectedItem(), s1, s2);

                if (matchMa && matchTen && matchLoai && matchRow && matchSeat) filtered.add(pc);
            }
            parent.loadDataToTable(filtered);
            dialog.dispose();
        });

        dialog.add(panelCenter, BorderLayout.CENTER);
        dialog.add(panelBottom, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private static boolean checkNumberCondition(int dbVal, String mode, String txt1, String txt2) {
        if (mode.equals("Khoảng")) {
            int min = parseIntOrDefault(txt1, 0);
            int max = parseIntOrDefault(txt2, Integer.MAX_VALUE);
            return dbVal >= min && dbVal <= max;
        } else {
            if (txt1.trim().isEmpty()) return true;

            int val = parseIntOrDefault(txt1, -1);
            if (val == -1) return true;

            switch (mode) {
                case ">":
                    return dbVal > val;
                case ">=":
                    return dbVal >= val;
                case "<":
                    return dbVal < val;
                case "<=":
                    return dbVal <= val;
                case "=":
                    return dbVal == val;
                case "<>":
                    return dbVal != val;
                default:
                    return true;
            }
        }
    }
}