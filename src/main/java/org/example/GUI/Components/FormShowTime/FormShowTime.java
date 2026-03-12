package org.example.GUI.Components.FormShowTime;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class FormShowTime extends JPanel {

    private DefaultTableModel model;
    private JTable table;

    public FormShowTime() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 250));

        // ================== KHU VỰC TRÊN (NÚT BẤM + BỘ LỌC) ==================
        JPanel pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.setBackground(new Color(245, 245, 250));
        pnlNorth.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Quản Lý Lịch Chiếu Phim", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)));

        // 1. Dàn nút bấm (Trái)
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlButtons.setBackground(new Color(245, 245, 250));
        pnlButtons.add(createButton("Xem", new Color(0, 123, 255), Color.WHITE));
        pnlButtons.add(createButton("Sửa", new Color(255, 193, 7), Color.BLACK));
        pnlButtons.add(createButton("Xóa", new Color(220, 53, 69), Color.WHITE));
        pnlButtons.add(createButton("Thêm", new Color(40, 167, 69), Color.WHITE));

        // 2. Bộ lọc (Phải)
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlFilter.setBackground(new Color(245, 245, 250));
        
        JComboBox<String> cbTimKiem = new JComboBox<>(new String[]{"Mã SC", "Tên Phim"});
        cbTimKiem.setPreferredSize(new Dimension(100, 35));
        
        JTextField txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 35));
        
        JComboBox<String> cbPhong = new JComboBox<>(new String[]{"Tất cả phòng", "Phòng 1", "Phòng 2"});
        cbPhong.setPreferredSize(new Dimension(120, 35));
        
        JButton btnLamMoi = createButton("Làm mới", new Color(230, 230, 230), Color.BLACK);

        pnlFilter.add(cbTimKiem);
        pnlFilter.add(txtSearch);
        pnlFilter.add(cbPhong);
        pnlFilter.add(btnLamMoi);

        pnlNorth.add(pnlButtons, BorderLayout.WEST);
        pnlNorth.add(pnlFilter, BorderLayout.EAST);

        // ================== KHU VỰC GIỮA (BẢNG DỮ LIỆU) ==================
        String[] columns = {"Mã SC", "Tên Phim", "Phòng", "Ngày Chiếu", "Bắt Đầu", "Kết Thúc", "Giá Vé"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(66, 103, 178));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(35);

        // Dữ liệu demo bám sát DB
        model.addRow(new Object[]{"1", "Phim 1", "Phong 1", "2026-03-10", "09:00", "11:00", "100,000"});
        model.addRow(new Object[]{"2", "Phim 2", "Phong 2", "2026-03-11", "10:00", "12:00", "120,000"});

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // ================== KHU VỰC DƯỚI (FORM CHI TIẾT) ==================
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBackground(Color.WHITE);
        pnlSouth.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Chi tiết Suất Chiếu", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)));

        // Dùng GridLayout chia 4 dòng, 4 cột (Label1 - Txt1 - Label2 - Txt2)
        JPanel pnlForm = new JPanel(new GridLayout(4, 4, 15, 10));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(new EmptyBorder(10, 50, 10, 50)); // Thụt lề cho đẹp giống ảnh

        // Dòng 1
        pnlForm.add(createLabel("Mã SC:"));       pnlForm.add(new JTextField());
        pnlForm.add(createLabel("Giờ bắt đầu:")); pnlForm.add(new JTextField());
        // Dòng 2
        pnlForm.add(createLabel("Tên Phim:"));    pnlForm.add(new JTextField());
        pnlForm.add(createLabel("Giờ kết thúc:"));pnlForm.add(new JTextField());
        // Dòng 3
        pnlForm.add(createLabel("Phòng chiếu:")); pnlForm.add(new JTextField());
        pnlForm.add(createLabel("Giá vé (VNĐ):"));pnlForm.add(new JTextField());
        // Dòng 4
        pnlForm.add(createLabel("Ngày chiếu:"));  pnlForm.add(new JTextField());
        pnlForm.add(new JLabel(""));              pnlForm.add(new JLabel("")); // Khoảng trống góc dưới phải

        pnlSouth.add(pnlForm, BorderLayout.CENTER);

        // ================== LẮP RÁP ==================
        add(pnlNorth, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(pnlSouth, BorderLayout.SOUTH);
    }

    // Hàm tiện ích tạo Nút bấm
    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(90, 35));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        return btn;
    }

    // Hàm tiện ích tạo Nhãn (Label) căn phải cho Form chi tiết
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.RIGHT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return lbl;
    }
}