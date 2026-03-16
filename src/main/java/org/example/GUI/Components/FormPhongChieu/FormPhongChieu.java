package org.example.GUI.Components.FormPhongChieu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.example.BUS.PhongChieuBUS;
import org.example.DTO.PhongChieuDTO;

public class FormPhongChieu extends JPanel {

    private DefaultTableModel model;
    private JTable tablePhongChieu;
    private final PhongChieuBUS phongChieuBUS = new PhongChieuBUS();

    public FormPhongChieu() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 250));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 250));
        topPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Quản Lý Phòng Chiếu",
                TitledBorder.LEFT,
                TitledBorder.TOP));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttonPanel.setOpaque(false);

        JButton btnAdd = createStyledButton("Thêm", new Color(40, 167, 69), Color.WHITE);
        JButton btnEdit = createStyledButton("Sửa", new Color(255, 193, 7), Color.BLACK);
        JButton btnDelete = createStyledButton("Xóa", new Color(220, 53, 69), Color.WHITE);
        JButton btnView = createStyledButton("Xem", new Color(0, 123, 255), Color.WHITE);
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnView);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        searchPanel.setOpaque(false);
        
        searchPanel.add(new JLabel("Tìm mã/tên phòng:"));
        JTextField txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 35));
        searchPanel.add(txtSearch);
        
        JButton btnSearch = createStyledButton("Lọc", new Color(0, 123, 255), Color.WHITE);
        searchPanel.add(btnSearch);

        topPanel.add(buttonPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        String[] columnNames = {"Mã Phòng", "Tên Phòng", "Loại Phòng", "Số Hàng", "Ghế/Hàng"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePhongChieu = new JTable(model);

        tablePhongChieu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablePhongChieu.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablePhongChieu.getTableHeader().setBackground(new Color(66, 103, 178));
        tablePhongChieu.getTableHeader().setForeground(Color.WHITE);
        tablePhongChieu.setRowHeight(40);

        loadDataToTable();

        JScrollPane scrollPane = new JScrollPane(tablePhongChieu);
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnView.addActionListener(e -> moXemPhongDaChon());
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(80, 35));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        return button;
    }

    private void loadDataToTable() {
        model.setRowCount(0);
        for (PhongChieuDTO pc : phongChieuBUS.getList()) {
            model.addRow(new Object[]{
                    pc.getMaPhong(),
                    pc.getTenPhong(),
                    pc.getLoaiPhong(),
                    pc.getSoHang(),
                    pc.getSoGheMoiHang()
            });
        }
    }

    private void moXemPhongDaChon() {
        int viewRow = tablePhongChieu.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Chọn 1 phòng trong bảng trước!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tablePhongChieu.convertRowIndexToModel(viewRow);
        String maPhong = String.valueOf(model.getValueAt(modelRow, 0));
        String tenPhong = String.valueOf(model.getValueAt(modelRow, 1));
        String loaiPhong = String.valueOf(model.getValueAt(modelRow, 2));

        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner);
        dialog.setTitle("Xem phòng: " + tenPhong + " (" + maPhong + ")");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(createPhongPreviewPanel(maPhong, tenPhong, loaiPhong));
        dialog.setSize(980, 720);
        dialog.setLocationRelativeTo(owner);
        dialog.setModal(false);
        dialog.setVisible(true);
    }

    private JPanel createPhongPreviewPanel(String maPhong, String tenPhong, String loaiPhong) {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        root.setBackground(new Color(245, 245, 250));

        JLabel header = new JLabel("Phòng: " + tenPhong + " | Loại: " + loaiPhong + " | Mã: " + maPhong, SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setOpaque(true);
        header.setBackground(new Color(66, 103, 178));
        header.setForeground(Color.WHITE);
        header.setBorder(new EmptyBorder(10, 10, 10, 10));
        root.add(header, BorderLayout.NORTH);

        // Demo layout (có thể thay bằng dữ liệu DB: số hàng/ghế mỗi hàng theo MaPhong)
        int soHang = 6;
        int soGheMoiHang = 10;

        JPanel seatArea = new JPanel(new BorderLayout(10, 10));
        seatArea.setOpaque(false);

        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(new Color(30, 30, 35));
        screen.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel lblScreen = new JLabel("MÀN HÌNH", SwingConstants.CENTER);
        lblScreen.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblScreen.setForeground(Color.WHITE);
        screen.add(lblScreen, BorderLayout.CENTER);
        seatArea.add(screen, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(soHang, soGheMoiHang, 8, 8));
        grid.setBackground(Color.WHITE);
        grid.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Bản đồ ghế (demo)",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)));

        DefaultTableModel seatModel = new DefaultTableModel(new String[]{"Mã Ghế", "Hàng", "Số", "Trạng thái"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int i = 0; i < soHang; i++) {
            char hang = (char) ('A' + i);
            for (int j = 1; j <= soGheMoiHang; j++) {
                String maGhe = hang + String.format("%02d", j);
                JButton btn = new JButton(maGhe);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btn.setFocusPainted(false);
                btn.setBackground(new Color(40, 167, 69));
                btn.setForeground(Color.WHITE);
                btn.setPreferredSize(new Dimension(54, 44));
                grid.add(btn);

                seatModel.addRow(new Object[]{maGhe, String.valueOf(hang), j, "Trống"});
            }
        }

        JScrollPane scrollGrid = new JScrollPane(grid);
        scrollGrid.getViewport().setBackground(Color.WHITE);

        JTable seatTable = new JTable(seatModel);
        seatTable.setRowHeight(32);
        seatTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        seatTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        JScrollPane scrollTable = new JScrollPane(seatTable);
        scrollTable.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Danh sách ghế (demo)",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollGrid, scrollTable);
        split.setResizeWeight(0.62);
        split.setDividerSize(8);
        seatArea.add(split, BorderLayout.CENTER);

        root.add(seatArea, BorderLayout.CENTER);
        return root;
    }
}