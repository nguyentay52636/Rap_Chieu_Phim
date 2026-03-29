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
    
    private EmployeeBUS employeeBUS = new EmployeeBUS();//tạo đối tượng EmployeeBUS để quản lý dữ liệu nhân viên và thực hiện các thao tác liên quan đến nhân viên như thêm/sửa/xóa/tìm kiếm/nhập xuất Excel
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");//định dạng ngày tháng khi hiển thị lên bảng, giúp dễ đọc hơn

    // Biến toàn cục để giữ thông tin nhân viên đang được chọn trong bảng, giúp các dialog con có thể truy cập và sử dụng thông tin này
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

        //Panel tìm kiếm
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));//tạo khung tìm kiếm
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), "Tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP));

       // Cập nhật JComboBox gồm Tất cả, Ngày Sinh, Ngày Vào Làm, Lương Cơ Bản
        cbSearch = new JComboBox<>(new String[]{"Tất cả", "Mã NV", "Họ Tên", "Ngày Sinh", "Ngày Vào Làm", "Lương Cơ Bản"});
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
            public boolean isCellEditable(int row, int column) //tất cả các ô trong bảng sẽ không cho phép chỉnh sửa trực tiếp, giúp tránh lỗi khi người dùng cố tình hoặc vô tình sửa dữ liệu trực tiếp trên bảng mà không thông qua dialog sửa
            { return false; }
        };
        
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//chỉ cho phép chọn 1 dòng trong bảng để tránh lỗi khi sửa hoặc xóa
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(66, 103, 178));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(35);

        add(pnlNorth, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    // Hàm này sẽ được gọi trong sự kiện tìm kiếm để cập nhật lại bảng với danh sách kết quả tìm kiếm
    private void initEvents() 
    {
        // Nút Làm mới sẽ xóa ô tìm kiếm và load lại toàn bộ dữ liệu từ database
        btnRefresh.addActionListener(e -> 
            {
                txtSearch.setText("");
                employeeBUS.docDanhSach();//đọc lại danh sách từ database để đảm bảo dữ liệu mới nhất được hiển thị, đặc biệt là sau khi thêm/sửa/xóa
                loadDataToTable();
            });
        
            // Sự kiện tìm kiếm theo thời gian thực khi gõ vào ô tìm kiếm
        txtSearch.addKeyListener(new KeyAdapter() // Sử dụng KeyAdapter để chỉ override phương thức cần thiết
        {
            @Override
            public void keyReleased(KeyEvent e) { performSearch(); }
        });

        // Sự kiện tìm kiếm theo thời gian thực khi chọn ô tìm kiếm
        cbSearch.addActionListener(e -> performSearch());

        // Nút Thêm sẽ mở dialog thêm nhân viên mới
        btnAdd.addActionListener(e -> new AddEmployeeDialog(this, employeeBUS).setVisible(true));


        // Nút XÓA sẽ xóa nhân viên đã chọn với xử lý ngoại lệ khi nhân viên đã lập hóa đơn
        btnDelete.addActionListener(e -> 
        {   
            EmployeeDTO emp = getSelectedEmployee("Xóa");//lấy thông tin nhân viên được chọn để xóa
            if (emp == null) 
                return;

            if (JOptionPane.showConfirmDialog(this, "Xóa nhân viên " + emp.getMaNV() + "?", "Xác nhận", 0) == 0) //hiện hộp thoại xác nhận trước khi xóa, nếu người dùng chọn "Có" thì tiếp tục thực hiện xóa, nếu chọn "Không" thì hủy bỏ thao tác xóa
                {
                try {
                        if (employeeBUS.xoaNhanVien(emp.getMaNV())) 
                        {
                            JOptionPane.showMessageDialog(this, "Xóa thành công!");
                            loadDataToTable();//load lại dữ liệu mới nhất từ database
                        } 
                        else 
                            JOptionPane.showMessageDialog(this, "Không thể xóa nhân viên này!");
                    } 
                catch (Exception ex) 
                {
                    JOptionPane.showMessageDialog(this, "KHÔNG THỂ XÓA: Nhân viên đã lập hóa đơn!");
                }
            }
        });

        //Nút Sửa sẽ mở dialog sửa nhân viên với thông tin của nhân viên được chọn
        btnEdit.addActionListener(e -> 
            {
                EmployeeDTO emp = getSelectedEmployee("Sửa");//lấy thông tin nhân viên được chọn để sửa
                if (emp != null) 
                    new EditEmployeeDialog(this, employeeBUS, emp).setVisible(true);//mở dialog sửa nhân viên với thông tin của nhân viên được chọn
            });

        //Nút Xem sẽ mở dialog xem chi tiết nhân viên với thông tin của nhân viên được chọn
        btnView.addActionListener(e -> 
        {
            EmployeeDTO emp = getSelectedEmployee("Xem");
            if (emp != null) 
            new DetailEmployeeDialog((JFrame) SwingUtilities.getWindowAncestor(this), emp).setVisible(true);//mở dialog xem chi tiết nhân viên với thông tin của nhân viên được chọn và truyền vào parent là JFrame chứa form này để dialog có thể căn giữa trên form cha
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

    // Hàm này sẽ được gọi trong sự kiện tìm kiếm để cập nhật lại bảng với danh sách kết quả tìm kiếm
  
    private void performSearch() 
    {
        // 1. Lấy thông tin khách nhập
        String keyword = txtSearch.getText().trim();
        String type = cbSearch.getSelectedItem().toString(); 
        
       // 2. Tìm kiếm với keyword và type
        ArrayList<EmployeeDTO> result = employeeBUS.timKiem(keyword, type);
        
        // 3. Đem kết quả bưng lên Bảng
        fillTable(result); 
    }

    //Hàm này sẽ được gọi sau khi thêm/sửa/xóa để load lại dữ liệu mới nhất từ database và hiển thị lên bảng
    private void fillTable(ArrayList<EmployeeDTO> list) 
    {
        model.setRowCount(0);//xoa tat ca cac dong trong bang

        for (EmployeeDTO emp : list) 
        {
            model.addRow(new Object[]//thêm một dòng mới vào bảng với các cột tương ứng là mã nhân viên, họ tên, ngày sinh, ngày vào làm và lương cơ bản, trong đó ngày sinh và ngày vào làm được định dạng lại cho dễ đọc hơn, còn lương cơ bản được định dạng với dấu phẩy phân cách hàng nghìn và thêm đơn vị VNĐ
            {
                emp.getMaNV(), emp.getHoTen(), 
                emp.getNgaySinh() != null ? sdf.format(emp.getNgaySinh()) : "", 
                emp.getNgayVaoLam() != null ? sdf.format(emp.getNgayVaoLam()) : "", 
                String.format("%,.0f VNĐ", emp.getLuongCoBan())//định dạng lương v ới dấu phẩy phân cách hàng nghìn và thêm đơn vị VNĐ, nếu lương là null thì hiển thị chuỗi rỗng
            });
        }
    }
    
    
    // Hàm này sẽ được gọi sau khi thêm/sửa/xóa để load lại dữ liệu mới nhất từ database và hiển thị lên bảng
    public void loadDataToTable() 
    {
        fillTable(employeeBUS.getDanhSach()); // Tái sử dụng hàm vẽ bảng
    }

    

    // Hàm gom chung logic "Kiểm tra chọn dòng và Lấy thông tin nhân viên"
    private EmployeeDTO getSelectedEmployee(String actionName) 
    {
        if (table.getSelectedRowCount() != 1) 
        {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 nhân viên để " + actionName + "!");
            return null;
        }
        int maNV = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());//Lấy mã nhân viên của dòng chọn
        
        for (EmployeeDTO emp : employeeBUS.getDanhSach()) 
        {
            if (emp.getMaNV() == maNV) 
                return emp;
        }
        return null;
    }
    //Hàm tạo nút với kiểu dáng đồng nhất trên toàn bộ form
    private JButton createButton(String text, Color bg) 
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