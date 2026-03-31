package org.example.GUI.Components.FormShowTime;

import org.example.DTO.TrangThaiGheDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ChiTietGheDialog extends JDialog {

    public ChiTietGheDialog(Frame parent, int maSC, int maPhong, List<TrangThaiGheDTO> listGhe) {
        super(parent, "Bản đồ ghế - Suất chiếu " + maSC + " (Phòng " + maPhong + ")", true);
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // 1. CHÚ THÍCH TRẠNG THÁI (Phía trên cùng)
        JPanel pnlLegend = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlLegend.add(createLegendItem("Ghế Trống", new Color(66, 103, 178))); // Màu xanh giống bạn kia
        pnlLegend.add(createLegendItem("Đã Đặt", new Color(217, 83, 79)));    // Màu đỏ
        add(pnlLegend, BorderLayout.NORTH);

        // 2. KHU VỰC BẢN ĐỒ GHẾ
        JPanel pnlMapContainer = new JPanel(new BorderLayout(0, 20));
        pnlMapContainer.setBorder(new EmptyBorder(10, 30, 30, 30));

        // Label Màn Hình
        JLabel lblScreen = new JLabel("MÀN HÌNH", SwingConstants.CENTER);
        lblScreen.setOpaque(true);
        lblScreen.setBackground(new Color(40, 40, 40));
        lblScreen.setForeground(Color.WHITE);
        lblScreen.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblScreen.setPreferredSize(new Dimension(0, 40));
        pnlMapContainer.add(lblScreen, BorderLayout.NORTH);

        // Tính toán số dòng, số cột để vẽ Grid
        int maxCol = 0;
        int maxRow = 0;
        String currentHang = "";
        for (TrangThaiGheDTO ghe : listGhe) {
            if (ghe.getSoGhe() > maxCol) maxCol = ghe.getSoGhe();
            if (!ghe.getHangGhe().equals(currentHang)) {
                maxRow++;
                currentHang = ghe.getHangGhe();
            }
        }

        // Vẽ ghế
        JPanel pnlGrid = new JPanel(new GridLayout(maxRow, maxCol, 10, 10));
        for (TrangThaiGheDTO ghe : listGhe) {
            String tenGhe = ghe.getHangGhe() + String.format("%02d", ghe.getSoGhe()); // Ví dụ: A01, B10
            JButton btnGhe = new JButton(tenGhe);
            btnGhe.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnGhe.setFocusPainted(false);

            // Tô màu theo trạng thái
            if (ghe.getTrangThai().equalsIgnoreCase("Trong")|| ghe.getTrangThai().equalsIgnoreCase("Đã Hủy")) {
                btnGhe.setBackground(new Color(66, 103, 178)); // Màu xanh
                btnGhe.setForeground(Color.WHITE);
            } else {
                btnGhe.setBackground(new Color(217, 83, 79)); // Màu đỏ
                btnGhe.setForeground(Color.WHITE);
            }
            pnlGrid.add(btnGhe);
        }

        pnlMapContainer.add(pnlGrid, BorderLayout.CENTER);

        // Cho vào ScrollPane đề phòng phòng chiếu quá bự
        JScrollPane scrollPane = new JScrollPane(pnlMapContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    // Hàm tạo chú thích màu sắc
    private JPanel createLegendItem(String text, Color color) {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel lblColor = new JLabel();
        lblColor.setOpaque(true);
        lblColor.setBackground(color);
        lblColor.setPreferredSize(new Dimension(20, 20));

        JLabel lblText = new JLabel(text);
        lblText.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        pnl.add(lblColor);
        pnl.add(lblText);
        return pnl;
    }
}