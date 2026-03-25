package org.example.GUI.Components.FormEmployee;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Date;
import org.example.BUS.EmployeeBUS;
import org.example.DTO.EmployeeDTO;

public class AddEmployeeDialog extends JDialog 
{
    private JTextField txtName, txtDob, txtJoinDate, txtSalary;
    private JButton btnSave, btnCancel;
    private EmployeeBUS employeeBUS;//biến này để gọi hàm themNhanVien() trong EmployeeBUS để thêm nhân viên mới vào database và kiểm tra logic dữ liệu trước khi thêm vào database
    private FormEmployee parent;//biến này để gọi hàm loadDataToTable() trong FormEmployee sau khi thêm thành công để cập nhật lại bảng hiển thị dữ liệu mới nhất từ database

    public AddEmployeeDialog(FormEmployee parent, EmployeeBUS bus) 
    {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "QUẢN LÝ NHÂN VIÊN", true);
        this.parent = parent;
        this.employeeBUS = bus;
        
        setLayout(new BorderLayout());
        setSize(450, 500); 
        setLocationRelativeTo(parent);
        
        initComponents();
        initEvents();
    }

    // Hàm này để khởi tạo giao diện người dùng, tạo các thành phần như JTextField, JButton và sắp xếp chúng trên dialog
    private void initComponents() 
    {
        // --- PANEL TIÊU ĐỀ ---
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(66, 103, 178));
        JLabel lblTitle = new JLabel("THÊM NHÂN VIÊN MỚI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(15, 0, 15, 0));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // --- PANEL NHẬP LIỆU ---
        JPanel pnlContent = new JPanel(new GridLayout(4, 1, 15, 15));
        pnlContent.setBorder(new EmptyBorder(25, 40, 25, 40));
        pnlContent.setBackground(Color.WHITE);

        //Hàm tiện ích createModernField() để tạo JTextField với placeholder và kiểu dáng hiện đại, giúp code gọn gàng hơn và dễ bảo trì hơn, tránh việc lặp lại nhiều lần cùng một đoạn code tạo JTextField với placeholder và kiểu dáng giống nhau.
        txtName = createModernField("Họ và Tên", "Nhập tên nhân viên...");
        txtDob = createModernField("Ngày Sinh", "yyyy-MM-dd (VD: 2000-01-01)");
        txtJoinDate = createModernField("Ngày Vào Làm", "yyyy-MM-dd (VD: 2026-03-16)");
        txtSalary = createModernField("Lương Cơ Bản", "Nhập số tiền lương...");

        // Hàm tiện ích createFieldWrapper() để tạo JPanel chứa JLabel và JTextField, giúp code gọn gàng hơn và dễ bảo trì hơn, tránh việc lặp lại nhiều lần cùng một đoạn code tạo JPanel chứa JLabel và JTextField với kiểu dáng giống nhau.
        pnlContent.add(createFieldWrapper("Họ và Tên nhân viên:", txtName));
        pnlContent.add(createFieldWrapper("Ngày sinh:", txtDob));
        pnlContent.add(createFieldWrapper("Ngày vào làm:", txtJoinDate));
        pnlContent.add(createFieldWrapper("Mức lương cơ bản:", txtSalary));

        add(pnlContent, BorderLayout.CENTER);

        // --- PANEL NÚT BẤM ---
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        pnlButtons.setBackground(new Color(248, 249, 250));
        
        btnCancel = new JButton("Hủy bỏ");
        btnCancel.putClientProperty(FlatClientProperties.STYLE, "arc:10; background:#6c757d; foreground:#ffffff");
        btnCancel.setPreferredSize(new Dimension(100, 40));

        btnSave = new JButton("Xác nhận lưu");
        btnSave.putClientProperty(FlatClientProperties.STYLE, "arc:10; background:#28a745; foreground:#ffffff");
        btnSave.setPreferredSize(new Dimension(150, 40));
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        pnlButtons.add(btnCancel);
        pnlButtons.add(btnSave);
        add(pnlButtons, BorderLayout.SOUTH);
    }

    // Hàm tiện ích để tạo JTextField với placeholder và kiểu dáng hiện đại
    private JTextField createModernField(String title, String placeholder) 
    {
        JTextField txt = new JTextField();
        txt.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        txt.putClientProperty(FlatClientProperties.STYLE, "arc:8; focusWidth:2");
        return txt;
    }

    private JPanel createFieldWrapper(String labelText, JTextField field) 
    {
        JPanel pnl = new JPanel(new BorderLayout(5, 5));
        pnl.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnl.add(lbl, BorderLayout.NORTH);
        pnl.add(field, BorderLayout.CENTER);
        return pnl;
    }

    // Hàm initEvents() để thiết lập các sự kiện cho nút "Hủy bỏ" và "Xác nhận lưu". Khi người dùng nhấn "Hủy bỏ", dialog sẽ đóng lại. Khi nhấn "Xác nhận lưu", hàm sẽ thực hiện các bước sau:
    private void initEvents() 
    {
        // Sự kiện cho nút "Hủy bỏ" để đóng dialog
        btnCancel.addActionListener(e -> dispose());

        //Sự kiện cho nút lưu   
        btnSave.addActionListener(e -> {
            try 
            {
                String name = txtName.getText().trim();
                if (name.isEmpty()) 
                {
                    JOptionPane.showMessageDialog(this, "Tên nhân viên không được để trống!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                
                Date dob = Date.valueOf(txtDob.getText().trim());
                Date joinDate = Date.valueOf(txtJoinDate.getText().trim());

                
                
                double salary;
                try 
                {
                    salary = Double.parseDouble(txtSalary.getText().trim());
                } 
                catch (NumberFormatException ex) 
                {
                    JOptionPane.showMessageDialog(this, "Lương cơ bản phải là số hợp lệ!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                EmployeeDTO emp = new EmployeeDTO(0, name, dob, joinDate, salary);
                
                // KIỂM TRA LOGIC THÔNG QUA BUS (Tuổi, Lương âm, Ngày tháng...) kiểm tra sau khi dữ liệu đầu vào hợp lệ về định dạng
                if (employeeBUS.themNhanVien(emp)) 
                { 
                    JOptionPane.showMessageDialog(this, "Thành công! Đã thêm nhân viên " + name, "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    parent.loadDataToTable();
                    dispose();
                } 
                
                else 
                {
                    // Nếu BUS trả về false, hiện bảng thông báo chi tiết này
                    JOptionPane.showMessageDialog(this, 
                        "Dữ liệu không hợp lệ! Vui lòng kiểm tra lại những thứ sau đây:\n" +
                        "- Mức lương phải lớn hơn hoặc bằng 0.\n" +
                        "- Ngày vào làm phải sau ngày sinh và ngày sinh phải sau năm 1961.\n" +
                        "- Độ tuổi lao động phải từ 15 tuổi trở lên.", 
                        "Từ chối thêm dữ liệu", 
                        JOptionPane.ERROR_MESSAGE);
                }

            } 
            // Bắt lỗi khi người dùng nhập sai định dạng số (VD: nhập chữ vào trường lương)
            catch (IllegalArgumentException ex) 
            {
                // Lỗi khi người dùng gõ sai định dạng ngày tháng (VD: 2026/03/16 thay vì 2026-03-16)
                JOptionPane.showMessageDialog(this, 
                    "Sai định dạng ngày tháng!\nVui lòng nhập theo mẫu: Năm-Tháng-Ngày (VD: 2006-05-29)", 
                    "Lỗi định dạng", 
                    JOptionPane.WARNING_MESSAGE);
            } 
            catch (Exception ex) 
            {
                JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}