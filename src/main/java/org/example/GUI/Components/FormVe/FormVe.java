package org.example.GUI.Components.FormVe;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.example.BUS.VeBUS;

public class FormVe extends JPanel 
{
    private VeBUS veBUS = new VeBUS();
    private JTable table;
    private DefaultTableModel model;
    
    // Khai báo công cụ Tìm kiếm (Của tui)
    private JTextField txtTimKiem;
    private JComboBox<String> cbCriteria, cbTrangThai;
    
    // Khai báo dàn Nút bấm
    private JButton btnXem, btnThemVe, btnHuyVe, btnLamMoi, btnXuatExcel, btnTimKiem;

    public FormVe() 
    {
        setLayout(new BorderLayout(10, 10)); 
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        initComponents(); 
        loadDataToTable(); 
        initEvents();      
    }

    private void initComponents() 
    {
        // ==========================================
        // KHU VỰC PHÍA TRÊN: XẾP CHỒNG 2 TẦNG (Không bao giờ bị khuất)
        // ==========================================
        JPanel pnlNorth = new JPanel();
        pnlNorth.setLayout(new BoxLayout(pnlNorth, BoxLayout.Y_AXIS));
        pnlNorth.setBackground(Color.WHITE);

        // --- TẦNG 1: Dàn Nút bấm (FlowLayout.LEFT) ---
        JPanel pnlAction = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlAction.setBackground(Color.WHITE);
        pnlAction.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), "Quản Lý Vé", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14)
        ));

        // Nút bấm theo style màu mè rõ ràng
        btnXem = createButton("Xem Chi Tiết", new Color(91, 192, 222)); // Light Blue
        btnThemVe = createButton("+ Thêm Vé Mới", new Color(40, 167, 69)); // Green
        btnHuyVe = createButton("- Hủy Vé", new Color(220, 53, 69)); // Red
        btnLamMoi = createButton("Làm Mới", new Color(108, 117, 125)); // Grey
        btnXuatExcel = createButton("Xuất Excel", new Color(138, 43, 226)); // Purple

        pnlAction.add(btnXem);
        pnlAction.add(btnThemVe);
        pnlAction.add(btnHuyVe);
        pnlAction.add(btnLamMoi);
        pnlAction.add(btnXuatExcel);

        // --- TẦNG 2: Khung Tìm kiếm (GIỮ NGUYÊN KIỂU CỦA TUI) ---
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), "Tìm Kiếm Nhanh", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14)
        ));

        // 1. Tiêu chí tìm kiếm
        cbCriteria = new JComboBox<>(new String[]{"Mã Vé", "Tên Phim", "SĐT Khách"});
        cbCriteria.setPreferredSize(new Dimension(120, 35));
        cbCriteria.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbCriteria.putClientProperty(FlatClientProperties.STYLE, "arc:8");
        
        // 2. Ô nhập từ khóa
        txtTimKiem = new JTextField(15);
        txtTimKiem.setPreferredSize(new Dimension(250, 35));
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Nhập từ khóa cần tìm...");
        txtTimKiem.putClientProperty(FlatClientProperties.STYLE, "focusWidth:2; arc:8");

        // 3. Lọc theo trạng thái
        cbTrangThai = new JComboBox<>(new String[]{"Tất cả trạng thái", "Đã thanh toán", "Đã check-in", "Đã hủy"});
        cbTrangThai.setPreferredSize(new Dimension(160, 35));
        cbTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbTrangThai.putClientProperty(FlatClientProperties.STYLE, "arc:8");

        // 4. Nút bấm Tìm kiếm
        btnTimKiem = createButton("Tìm Kiếm", new Color(2, 117, 216)); // Màu Xanh dương
        btnTimKiem.setPreferredSize(new Dimension(120, 35));

        // Ráp vô khay Tìm kiếm
        pnlSearch.add(new JLabel("Tìm theo:"));
        pnlSearch.add(cbCriteria);
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(new JLabel("  Trạng thái:"));
        pnlSearch.add(cbTrangThai);
        pnlSearch.add(btnTimKiem);

        // Gắn 2 tầng vào khối North
        pnlNorth.add(pnlAction);
        pnlNorth.add(Box.createVerticalStrut(5)); // Khoảng hở nhẹ giữa 2 tầng
        pnlNorth.add(pnlSearch);

        add(pnlNorth, BorderLayout.NORTH);

        // ==========================================
        // KHU VỰC BẢNG DỮ LIỆU JTABLE
        // ==========================================
        String[] columns = {"Mã Vé", "Khách Hàng", "Tên Phim", "Phòng", "Ghế", "Ngày Chiếu", "Giờ", "Giá Tiền", "Trạng Thái"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(model);
        table.setRowHeight(35); 
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 

        // Căn giữa các cột cho đẹp (Trừ Tên Khách và Tên Phim)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 1 && i != 2) { 
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        // Tùy chỉnh Header
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(66, 103, 178)); 
        table.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220))); 

        add(scrollPane, BorderLayout.CENTER);
    }

    // --- Hàm tiện ích: Tạo nút bấm siêu tốc ---
    private JButton createButton(String text, Color bgColor) 
    {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(140, 40)); 
        btn.putClientProperty(FlatClientProperties.STYLE, "arc:10"); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        return btn;
    }

    public void loadDataToTable() 
    {
        // 1. Dọn dẹp bảng sạch sẽ trước khi đổ data mới
        model.setRowCount(0); 
        
        System.out.println("--- ĐANG GỌI BUS ĐỂ KÉO DỮ LIỆU ---");
        
        // 2. Lôi đống data từ DB lên thông qua BUS
        java.util.ArrayList<Object[]> danhSachVe = veBUS.getDanhSachVeTable();
        
        // 3. Quăng từng dòng lên JTable
        for (Object[] row : danhSachVe) {
            model.addRow(row);
        }
        
        // 4. Ép giao diện update lại
        model.fireTableDataChanged(); 
        System.out.println("Kết quả: Đã đổ thành công " + danhSachVe.size() + " vé lên bảng!");
    }

    private void initEvents() 
    {
        btnThemVe.addActionListener(e -> {
            new ThemVeDialog(this, veBUS).setVisible(true);
        });

        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            cbCriteria.setSelectedIndex(0);
            cbTrangThai.setSelectedIndex(0);
            loadDataToTable(); // Kéo lại toàn bộ dữ liệu
        });

        // Ân sẽ code sự kiện Tìm kiếm, Hủy vé ở đây
        btnTimKiem.addActionListener(e -> {
            String criteria = (String) cbCriteria.getSelectedItem();
            String keyword = txtTimKiem.getText().trim();
            String trangThai = (String) cbTrangThai.getSelectedItem();
            System.out.println("Tìm kiếm theo: " + criteria + " | Từ khóa: " + keyword + " | Trạng thái: " + trangThai);
        });
    }
}