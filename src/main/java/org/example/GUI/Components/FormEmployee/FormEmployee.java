package org.example.GUI.Components.FormEmployee;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;

import org.example.BUS.EmployeeBUS;
import org.example.DTO.EmployeeDTO;

public class FormEmployee extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private JTextField txtSearch;
    private JComboBox<String> cbSearch;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh, btnView, btnExcel, btnExportExcel;
    
    private EmployeeBUS employeeBUS = new EmployeeBUS();

    public FormEmployee(String title) {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        initComponents();
        initEvents(); 
        loadDataToTable();
    }

    private void initComponents() {
        // ================== KHU VỰC PHÍA TRÊN ==================
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));
        pnlNorth.setBackground(Color.WHITE);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlButtons.setBackground(Color.WHITE);
        
        btnView = createButton("Xem", new Color(92, 184, 92));
        btnEdit = createButton("Sửa", new Color(240, 173, 78));
        btnDelete = createButton("Xóa", new Color(217, 83, 79));
        btnAdd = createButton("Thêm", new Color(2, 117, 216));
        btnExcel = createButton("Nhập Excel", new Color(138, 43, 226));
        btnExportExcel = createButton("Xuất Excel", new Color(40, 167, 69));

        pnlButtons.add(btnView);
        pnlButtons.add(btnEdit);
        pnlButtons.add(btnDelete);
        pnlButtons.add(btnAdd);
        pnlButtons.add(btnExcel);
        pnlButtons.add(btnExportExcel);

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

        // ================== KHU VỰC BẢNG ==================
        String[] columns = {"Mã NV", "Họ Tên", "Ngày Sinh", "Ngày Vào Làm", "Lương Cơ Bản"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        
        // CHỐT CHẶN: Chỉ cho phép chọn 1 dòng duy nhất
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        // --- 1. Làm mới ---
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            employeeBUS.docDanhSach();
            loadDataToTable();
        });

        // --- 2. Xóa (Đã dọn dẹp để không bị lặp sự kiện) ---
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Phải chọn 1 dòng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
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
                        JOptionPane.showMessageDialog(this, "Không thể xóa nhân viên này!");
                    }
                } catch (Exception ex) {
                    if (ex.getMessage().contains("foreign key")) {
                        JOptionPane.showMessageDialog(this, "KHÔNG THỂ XÓA: Nhân viên đã lập hóa đơn!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                    }
                }
            }
        });

        // --- 3. Tìm kiếm Real-time ---
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { performSearch(); }
        });

        // --- 4. Thêm ---
        btnAdd.addActionListener(e -> {
            new AddEmployeeDialog(this, employeeBUS).setVisible(true);
        });

        // --- 5. Sửa (Chặn sửa khi chọn nhiều dòng hoặc không chọn) ---
        btnEdit.addActionListener(e -> {
            if (table.getSelectedRowCount() != 1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 nhân viên để sửa!");
                return;
            }
            int row = table.getSelectedRow();
            int maNV = Integer.parseInt(table.getValueAt(row, 0).toString());
            
            EmployeeDTO selectedEmp = null;
            for (EmployeeDTO emp : employeeBUS.getDanhSach()) {
                if (emp.getMaNV() == maNV) { selectedEmp = emp; break; }
            }

            if (selectedEmp != null) {
                new EditEmployeeDialog(this, employeeBUS, selectedEmp).setVisible(true);
            }
        });

        // --- 6. Xem chi tiết ---
        btnView.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 nhân viên để xem!");
                return;
            }
            int maNV = Integer.parseInt(table.getValueAt(row, 0).toString());
            EmployeeDTO selectedEmp = null;
            for (EmployeeDTO emp : employeeBUS.getDanhSach()) {
                if (emp.getMaNV() == maNV) { selectedEmp = emp; break; }
            }
            if (selectedEmp != null) {
                new DetailEmployeeDialog((JFrame) SwingUtilities.getWindowAncestor(this), selectedEmp).setVisible(true);
            }
        });

        // --- 7. Nhập Excel (Đã chặn thoát nhây) ---
        btnExcel.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                int count = employeeBUS.importExcel(fc.getSelectedFile());
                if (count > 0) {
                    JOptionPane.showMessageDialog(this, "Đã nhập thành công " + count + " nhân viên!");
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Nhập Excel thất bại hoặc dữ liệu không hợp lệ!");
                }
            }
        });

        // --- 8. Xuất Excel ---
        btnExportExcel.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("DanhSachNhanVien.xlsx"));
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getAbsolutePath();
                if (!path.toLowerCase().endsWith(".xlsx")) path += ".xlsx";
                
                if (employeeBUS.exportExcel(path)) {
                    JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xuất file!");
                }
            }
        });
    }
    // --- HÀM TÌM KIẾM RIÊNG BIỆT ĐỂ GIẢI QUYẾT VẤN ĐỀ TÌM KIẾM THEO LOẠI ---

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
                emp.getMaNV(), emp.getHoTen(), 
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