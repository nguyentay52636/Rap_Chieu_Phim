package org.example.GUI.Components.FormEmployee;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import org.example.BUS.EmployeeBUS;
import org.example.DTO.EmployeeDTO;

public class FormEmployee extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    
    // Khai báo các thành phần điều khiển để xử lý sự kiện
    private JTextField txtSearch;
    private JComboBox<String> cbSearch;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh, btnView, btnExcel;

    // Gọi BUS ra để làm việc với Database
    private EmployeeBUS employeeBUS = new EmployeeBUS();

    public FormEmployee(String title) {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        initComponents();
        initEvents(); // Khởi tạo các sự kiện Click, Search...
        loadDataToTable(); // Load dữ liệu lần đầu
    }

    private void initComponents() {
        // ================== KHU VỰC PHÍA TRÊN (NÚT BẤM + TÌM KIẾM) ==================
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));
        pnlNorth.setBackground(Color.WHITE);

        // 1. Dàn nút chức năng
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlButtons.setBackground(Color.WHITE);
        
        btnView = createButton("Xem", new Color(92, 184, 92));
        btnEdit = createButton("Sửa", new Color(240, 173, 78));
        btnDelete = createButton("Xóa", new Color(217, 83, 79));
        btnAdd = createButton("Thêm", new Color(2, 117, 216));
        btnExcel = createButton("Nhập Excel", new Color(138, 43, 226));

        pnlButtons.add(btnView);
        pnlButtons.add(btnEdit);
        pnlButtons.add(btnDelete);
        pnlButtons.add(btnAdd);
        pnlButtons.add(btnExcel);

        // 2. Khu vực Tìm kiếm
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP));

        cbSearch = new JComboBox<>(new String[]{"Mã NV", "Họ Tên"});
        cbSearch.setPreferredSize(new Dimension(100, 35));
        
        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(200, 35));
        
        btnRefresh = createButton("Làm mới", new Color(91, 192, 222));

        pnlSearch.add(cbSearch);
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnRefresh);

        pnlNorth.add(pnlButtons, BorderLayout.NORTH);
        pnlNorth.add(pnlSearch, BorderLayout.CENTER);

        // ================== KHU VỰC BẢNG (TABLE) ==================
        String[] columns = {"Mã NV", "Họ Tên", "Ngày Sinh", "Ngày Vào Làm", "Lương Cơ Bản"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Không cho sửa trực tiếp trên bảng
        };
        table = new JTable(model);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(66, 103, 178));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(35);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(pnlNorth, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initEvents() {
        // 1. Sự kiện Làm mới
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            employeeBUS.docDanhSach(); // Đọc lại data mới nhất từ Database
            loadDataToTable();
        
        
        });

        // 2. Sự kiện Xóa
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dòng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int maNV = (int) table.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa nhân viên " + maNV + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (employeeBUS.xoaNhanVien(maNV)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDelete.addActionListener(e -> {
    int row = table.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Chọn nhân viên đã chứ!");
        return;
    }

    int maNV = Integer.parseInt(table.getValueAt(row, 0).toString());
    int confirm = JOptionPane.showConfirmDialog(this, "Xóa nhân viên " + maNV + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            if (employeeBUS.xoaNhanVien(maNV)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadDataToTable();
            } else {
                // Thường là do logic BUS trả về false
                JOptionPane.showMessageDialog(this, "Không thể xóa nhân viên này!");
            }
        } catch (Exception ex) {
            // ĐÂY LÀ CHỖ BẮT CÁI LỖI ĐỎ Ở TERMINAL NÈ
            if (ex.getMessage().contains("foreign key constraint fails")) {
                JOptionPane.showMessageDialog(this, "KHÔNG THỂ XÓA: Nhân viên này đã lập hóa đơn, không được xóa để giữ lịch sử!");
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage());
            }
        }
    }
});
        // 4. Sự kiện bấm nút Thêm
        btnAdd.addActionListener(e -> {
        AddEmployeeDialog dialog = new AddEmployeeDialog(this, employeeBUS);
        dialog.setVisible(true);
    });

    // 5. Sự kiện bấm nút Sửa (Tạm thời thông báo, mình làm sau)
    btnEdit.addActionListener(e -> {
        JOptionPane.showMessageDialog(this, "Chức năng Sửa: Chọn 1 dòng rồi nhấn Sửa để cập nhật!");
    });


        // 3. Sự kiện Tìm kiếm Real-time (Gõ đến đâu lọc đến đó)
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        String keyword = txtSearch.getText().toLowerCase().trim();
        String type = (String) cbSearch.getSelectedItem();
        ArrayList<EmployeeDTO> list = employeeBUS.getDanhSach();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        model.setRowCount(0);
        for (EmployeeDTO emp : list) {
            boolean match = false;
            if (type.equals("Mã NV")) {
                if (String.valueOf(emp.getMaNV()).contains(keyword)) match = true;
            } else {
                if (emp.getHoTen().toLowerCase().contains(keyword)) match = true;
            }

            if (match) {
                model.addRow(new Object[]{
                    emp.getMaNV(), emp.getHoTen(), 
                    emp.getNgaySinh() != null ? sdf.format(emp.getNgaySinh()) : "", 
                    emp.getNgayVaoLam() != null ? sdf.format(emp.getNgayVaoLam()) : "", 
                    String.format("%,.0f VNĐ", emp.getLuongCoBan())
                });
            }
        }
    }

    public void loadDataToTable() {
        model.setRowCount(0);
        ArrayList<EmployeeDTO> list = employeeBUS.getDanhSach();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (EmployeeDTO emp : list) {
            model.addRow(new Object[]{
                emp.getMaNV(), 
                emp.getHoTen(), 
                emp.getNgaySinh() != null ? sdf.format(emp.getNgaySinh()) : "", 
                emp.getNgayVaoLam() != null ? sdf.format(emp.getNgayVaoLam()) : "", 
                String.format("%,.0f VNĐ", emp.getLuongCoBan())
            });
        }
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        return btn;
    }
}