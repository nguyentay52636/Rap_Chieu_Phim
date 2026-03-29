package org.example.GUI.Components.FormVe;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.table.DefaultTableCellRenderer;
import org.example.BUS.VeBUS;

public class FormVe extends JPanel 
{
    private VeBUS veBUS = new VeBUS();
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTimKiem;
    private JComboBox<String> cbCriteria, cbTrangThai;
    private JButton btnThemVe, btnHuyVe, btnLamMoi, btnXuatExcel;

    public FormVe() 
    {
        // Đồng bộ hóa Layout và nền trắng như bên Nhân viên
        setLayout(new BorderLayout(10, 10)); 
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        initComponents(); // Bách thầu giao diện
        loadDataToTable(); // Ân móc logic DB
        initEvents();      // Ân móc logic sự kiện
    }

    // ==========================================
    // LÃNH THỔ CỦA BÁCH (UI ĐỒNG BỘ MODULE NHÂN VIÊN)
    // ==========================================
    private void initComponents() 
    {
        // 1. THANH CÔNG CỤ (NÚT BẤM & TÌM KIẾM) - Phía trên
        JPanel pnlNorth = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlNorth.setBackground(Color.WHITE);

        // --- Dàn Nút bấm (Đồng bộ kiểu dáng và màu sắc từ Nhân viên) ---
        btnThemVe = new JButton("+ Thêm Vé Mới");
        btnThemVe.setBackground(new Color(40, 167, 69)); // Màu Green đậm
        btnThemVe.setForeground(Color.WHITE);
        btnThemVe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnThemVe.setPreferredSize(new Dimension(140, 40));
        btnThemVe.putClientProperty(FlatClientProperties.STYLE, "arc:10"); // Bo góc

        btnHuyVe = new JButton("- Hủy Vé");
        btnHuyVe.setBackground(new Color(220, 53, 69)); // Màu Red
        btnHuyVe.setForeground(Color.WHITE);
        btnHuyVe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnHuyVe.setPreferredSize(new Dimension(110, 40));
        btnHuyVe.putClientProperty(FlatClientProperties.STYLE, "arc:10"); // Bo góc

        btnLamMoi = new JButton("Làm Mới");
        btnLamMoi.setBackground(new Color(91, 192, 222)); // Màu Light Blue
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLamMoi.setPreferredSize(new Dimension(110, 40));
        btnLamMoi.putClientProperty(FlatClientProperties.STYLE, "arc:10"); // Bo góc

        btnXuatExcel = new JButton("Xuất Excel");
        btnXuatExcel.setBackground(new Color(92, 184, 92)); // Màu Green
        btnXuatExcel.setForeground(Color.WHITE);
        btnXuatExcel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXuatExcel.setPreferredSize(new Dimension(120, 40));
        btnXuatExcel.putClientProperty(FlatClientProperties.STYLE, "arc:10"); // Bo góc

        pnlNorth.add(btnThemVe);
        pnlNorth.add(btnHuyVe);
        pnlNorth.add(btnLamMoi);
        pnlNorth.add(btnXuatExcel);

        // --- Khung Tìm kiếm (Đồng bộ TitledBorder từ Nhân viên) ---
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), "Tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14)
        ));

        cbCriteria = new JComboBox<>(new String[]{"Mã Vé", "Tên Phim", "SĐT Khách"});
        cbCriteria.setPreferredSize(new Dimension(100, 35));
        
        txtTimKiem = new JTextField(15);
        txtTimKiem.setPreferredSize(new Dimension(200, 35));
        txtTimKiem.putClientProperty(FlatClientProperties.STYLE, "focusWidth:2");

        cbTrangThai = new JComboBox<>(new String[]{"Tất cả trạng thái", "Đã thanh toán", "Đã check-in", "Đã hủy"});
        cbTrangThai.setPreferredSize(new Dimension(150, 35));
        
        // TitledBorder đã có tên, không cần gắn nút Tìm kiếm, JComboBox đã đủ rồi

        pnlSearch.add(cbCriteria);
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(new JLabel("  Trạng thái:"));
        pnlSearch.add(cbTrangThai);
        
        pnlNorth.add(pnlSearch);
        add(pnlNorth, BorderLayout.NORTH);

        // 2. BẢNG DỮ LIỆU (Đồng bộ kiểu dáng và render từ Nhân viên) ---
        // Thêm cột # (Số thứ tự) bên trái
        String[] columns = {"#", "Mã Vé", "Khách Hàng", "SĐT", "Tên Phim", "Phòng", "Ghế", "Ngày", "Giờ", "Giá Tiền", "Trạng Thái"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return false; // Khóa không cho người dùng nhấp đúp sửa
            }
        };
        
        table = new JTable(model);
        table.setRowHeight(35); // Chiều cao dòng 35px
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Font chữ nội dung 14px

       // BƯỚC 1: Tạo 1 cái "cọ" chuyên dùng để quét chữ ra giữa ô
    DefaultTableCellRenderer canhGiua = new DefaultTableCellRenderer();
    canhGiua.setHorizontalAlignment(JLabel.CENTER);

    // BƯỚC 2: Liệt kê các cột ông muốn căn giữa (Nhớ là cột đầu tiên đếm từ số 0)
    // Ở đây là: Cột STT, Mã vé, SĐT, Ngày, Giờ, Giá tiền, Trạng thái...
    int[] cacCot = {0, 1, 3, 5, 6, 7, 8, 9, 10}; 

    // BƯỚC 3: Cho vòng lặp chạy qua từng số ở trên để lấy cái cọ "quét" vào cột đó
    for (int i = 0; i < cacCot.length; i++) 
    {
        int viTriCot = cacCot[i];
        table.getColumnModel().getColumn(viTriCot).setCellRenderer(canhGiua);
    }

        // Tùy chỉnh Header (Đồng bộ màu xanh navy từ Nhân viên)
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(66, 103, 178)); // Màu Header dương
        table.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Xóa Border của ScrollPane

        add(scrollPane, BorderLayout.CENTER);
    }

    // ==========================================
    // LÃNH THỔ CỦA ÂN (LOGIC & DATABASE - Ân viết code ở đây)
    // ==========================================
    public void loadDataToTable() 
    {
        // Ân gọi BUS lấy dữ liệu, đổ vào model.addRow()
        // Chú ý định dạng tiền tệ cho cột Giá Tiền nhé Ân! (##,### VNĐ)
        System.out.println("Ân: Đang load dữ liệu vé...");
    }

    private void initEvents() 
    {
        // 1. Mở Form Thêm Vé mới (Đã nối sang ThemVeDialog bo góc của Bách)
        btnThemVe.addActionListener(e -> {
            new ThemVeDialog(this, veBUS).setVisible(true);
        });

        // 2. Logic Hủy vé (Ân code update Trạng thái)
        btnHuyVe.addActionListener(e -> {
            // ... logic gọi BUS hủy vé của Ân ...
        });

        // 3. Logic Tìm kiếm và Lọc trạng thái (Ân code filter)
        // txtTimKiem Key Released, cbTrangThai Action Listener

        // 4. Logic Làm mới và Xuất Excel (Ân code)
        btnLamMoi.addActionListener(e -> loadDataToTable());
        btnXuatExcel.addActionListener(e -> System.out.println("Ân: Xuất Excel vé..."));
    }
}