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

public class FormEmployee extends JPanel 
{

    private DefaultTableModel model;
    private JTable table;
    private JTextField txtSearch;
    private JComboBox<String> cbSearch;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh, btnView, btnExcel, btnExportExcel;
    
    private EmployeeBUS employeeBUS = new EmployeeBUS();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public FormEmployee(String title) 
    {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        initComponents();
        initEvents(); 
        loadDataToTable();
    }

    private void initComponents() 
    {
        // --- KHU VỰC PHÍA TRÊN ---
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

        pnlButtons.add(btnView); pnlButtons.add(btnEdit); pnlButtons.add(btnDelete);
        pnlButtons.add(btnAdd); pnlButtons.add(btnExcel); pnlButtons.add(btnExportExcel);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP));

        cbSearch = new JComboBox<>(new String[]{"Mã NV", "Họ Tên"});
        cbSearch.setPreferredSize(new Dimension(100, 35));
        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(200, 35));
        btnRefresh = createButton("Làm mới", new Color(91, 192, 222));

        pnlSearch.add(cbSearch); pnlSearch.add(txtSearch); pnlSearch.add(btnRefresh);
        pnlNorth.add(pnlButtons, BorderLayout.NORTH); pnlNorth.add(pnlSearch, BorderLayout.CENTER);

        // --- KHU VỰC BẢNG ---
        String[] columns = {"Mã NV", "Họ Tên", "Ngày Sinh", "Ngày Vào Làm", "Lương Cơ Bản"};
        model = new DefaultTableModel(columns, 0) // Override để làm cho bảng không cho phép chỉnh sửa trực tiếp
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            { return false; }
        };
        
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(66, 103, 178));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(35);

        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void initEvents() 
    {
        // Nút Làm mới sẽ xóa ô tìm kiếm và load lại toàn bộ dữ liệu từ database
        btnRefresh.addActionListener(e -> 
            {
                txtSearch.setText("");
                employeeBUS.docDanhSach();
                loadDataToTable();
            });
        // Sự kiện tìm kiếm theo thời gian thực khi gõ vào ô tìm kiếm
        txtSearch.addKeyListener(new KeyAdapter() // Sử dụng KeyAdapter để chỉ override phương thức cần thiết
        {
            @Override
            public void keyReleased(KeyEvent e) { performSearch(); }
        });

        // Nút Thêm sẽ mở dialog thêm nhân viên mới
        btnAdd.addActionListener(e -> new AddEmployeeDialog(this, employeeBUS).setVisible(true));


        // Nút XÓA sẽ xóa nhân viên đã chọn với xử lý ngoại lệ khi nhân viên đã lập hóa đơn
        btnDelete.addActionListener(e -> 
        {   
            EmployeeDTO emp = getSelectedEmployee("Xóa");//lấy thông tin nhân viên được chọn để xóa
            if (emp == null) 
                return;

            if (JOptionPane.showConfirmDialog(this, "Xóa nhân viên " + emp.getMaNV() + "?", "Xác nhận", 0) == 0) //hiện hộp thoại xác nhận trước khi xóa, nếu chọn "Có" thì tiếp tục thực hiện xóa
                {
                try {
                        if (employeeBUS.xoaNhanVien(emp.getMaNV())) 
                        {
                            JOptionPane.showMessageDialog(this, "Xóa thành công!");
                            loadDataToTable();
                        } 
                        else JOptionPane.showMessageDialog(this, "Không thể xóa nhân viên này!");
                    } 
                catch (Exception ex) 
                {
                    JOptionPane.showMessageDialog(this, "KHÔNG THỂ XÓA: Nhân viên đã lập hóa đơn!");
                }
            }
        });

//--- Sự kiện Sửa sẽ mở dialog sửa nhân viên với thông tin của nhân viên được chọn
        btnEdit.addActionListener(e -> 
            {
                EmployeeDTO emp = getSelectedEmployee("Sửa");
                if (emp != null) 
                    new EditEmployeeDialog(this, employeeBUS, emp).setVisible(true);//mở dialog sửa nhân viên với thông tin của nhân viên được chọn
            });

        btnView.addActionListener(e -> 
        {
            EmployeeDTO emp = getSelectedEmployee("Xem");
            if (emp != null) 
            new DetailEmployeeDialog((JFrame) SwingUtilities.getWindowAncestor(this), emp).setVisible(true);//mở dialog xem chi tiết nhân viên với thông tin của nhân viên được chọn
        });

//--- Sự kiện nhập Excel 
        btnExcel.addActionListener(e -> 
        {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)//hiện hộp thoại chọn file, nếu chọn file và nhấn "Mở" thì tiếp tục thực hiện nhập Excel
            {
                int count = employeeBUS.importExcel(fc.getSelectedFile());
                if (count > 0) {
                    JOptionPane.showMessageDialog(this, "Đã nhập thành công " + count + " dòng!");
                    loadDataToTable();
                } else JOptionPane.showMessageDialog(this, "Nhập thất bại!");
            }
        });

//--- Sự kiện xuất Excel
        btnExportExcel.addActionListener(e -> 
        {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("DanhSachNhanVien.xlsx"));
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) 
            {
                String path = fc.getSelectedFile().getAbsolutePath();
                if (!path.endsWith(".xlsx")) path += ".xlsx";
                JOptionPane.showMessageDialog(this, employeeBUS.exportExcel(path) ? "Xuất thành công!" : "Lỗi xuất file!");
            }
        });
    }

    // =========================================================
    // CÁC HÀM XỬ LÝ PHỤ TRỢ - GIÚP RÚT GỌN CODE
    // =========================================================

    private void performSearch() // Hàm này sẽ được gọi mỗi khi người dùng gõ vào ô tìm kiếm để thực hiện tìm kiếm theo thời gian thực

    {
        String keyword = txtSearch.getText().toLowerCase().trim();
        boolean isMaNV = cbSearch.getSelectedItem().equals("Mã NV");
        ArrayList<EmployeeDTO> result = new ArrayList<>();//danh sách kết quả tìm kiếm sẽ được lưu vào đây

        for (EmployeeDTO emp : employeeBUS.getDanhSach()) 
        {
            String target = isMaNV ? String.valueOf(emp.getMaNV()) : emp.getHoTen().toLowerCase();
            if (target.contains(keyword)) 
                result.add(emp);
        }
        fillTable(result); //cập nhật lại bảng với danh sách kết quả tìm kiếm
    }

    public void loadDataToTable() 
    {
        fillTable(employeeBUS.getDanhSach()); // Tái sử dụng hàm vẽ bảng
    }

    // 1. Hàm gom chung logic Đổ dữ liệu lên bảng
    private void fillTable(ArrayList<EmployeeDTO> list) 
    {
        model.setRowCount(0);
        for (EmployeeDTO emp : list) 
        {
            model.addRow(new Object[]//định dạng lại dữ liệu khi hiển thị lên bảng, đặc biệt là ngày tháng và lương
            {
                emp.getMaNV(), emp.getHoTen(), 
                emp.getNgaySinh() != null ? sdf.format(emp.getNgaySinh()) : "", 
                emp.getNgayVaoLam() != null ? sdf.format(emp.getNgayVaoLam()) : "", 
                String.format("%,.0f VNĐ", emp.getLuongCoBan())
            });
        }
    }

    // 2. Hàm gom chung logic "Kiểm tra chọn dòng và Lấy thông tin nhân viên"
    private EmployeeDTO getSelectedEmployee(String actionName) 
    {
        if (table.getSelectedRowCount() != 1) 
        {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 nhân viên để " + actionName + "!");
            return null;
        }
        int maNV = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
        for (EmployeeDTO emp : employeeBUS.getDanhSach()) 
        {
            if (emp.getMaNV() == maNV) 
                return emp;
        }
        return null;
    }
// 3. Hàm tạo nút với kiểu dáng đồng nhất trên toàn bộ form
    private JButton createButton(String text, Color bg) // Hàm giúp tạo nút với màu sắc và kiểu dáng đồng nhất trên toàn bộ form
    {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        return btn;
    }
}