package org.example.GUI.Components.FormEmployee;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Date;
import org.example.BUS.EmployeeBUS;
import org.example.DTO.EmployeeDTO;

public class EditEmployeeDialog extends JDialog 
{
    private JTextField txtName, txtDob, txtJoinDate, txtSalary;
    private JButton btnSave, btnCancel;
    private EmployeeBUS employeeBUS;
    private FormEmployee parent;
    private EmployeeDTO currentEmp; // Giữ thông tin nhân viên đang sửa

    public EditEmployeeDialog(FormEmployee parent, EmployeeBUS bus, EmployeeDTO emp) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "CẬP NHẬT NHÂN VIÊN", true);
        this.parent = parent;
        this.employeeBUS = bus;
        this.currentEmp = emp;
        
        setLayout(new BorderLayout());
        setSize(450, 500); 
        setLocationRelativeTo(parent);
        
        initComponents();
        loadDataToForm(); // Load dữ liệu cũ lên form
        initEvents();
    }

    private void initComponents() 
    {
        // --- PANEL TIÊU ĐỀ ---
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(240, 173, 78)); // Màu cam cho hành động Sửa
        JLabel lblTitle = new JLabel("SỬA THÔNG TIN NHÂN VIÊN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(15, 0, 15, 0));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // --- PANEL NHẬP LIỆU ---
        JPanel pnlContent = new JPanel(new GridLayout(4, 1, 15, 15));
        pnlContent.setBorder(new EmptyBorder(25, 40, 25, 40));
        pnlContent.setBackground(Color.WHITE);

        txtName = createModernField("Họ và Tên", "Nhập tên nhân viên...");
        txtDob = createModernField("Ngày Sinh", "yyyy-MM-dd");
        txtJoinDate = createModernField("Ngày Vào Làm", "yyyy-MM-dd");
        txtSalary = createModernField("Lương Cơ Bản", "Nhập số tiền lương...");

        pnlContent.add(createFieldWrapper("Họ và Tên nhân viên:", txtName));
        pnlContent.add(createFieldWrapper("Ngày sinh (Năm-Tháng-Ngày):", txtDob));
        pnlContent.add(createFieldWrapper("Ngày vào làm (Năm-Tháng-Ngày):", txtJoinDate));
        pnlContent.add(createFieldWrapper("Mức lương cơ bản (VNĐ):", txtSalary));

        add(pnlContent, BorderLayout.CENTER);

        // --- PANEL NÚT BẤM ---
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        pnlButtons.setBackground(new Color(248, 249, 250));
        
        btnCancel = new JButton("Hủy bỏ");
        btnCancel.putClientProperty(FlatClientProperties.STYLE, "arc:10; background:#6c757d; foreground:#ffffff");
        btnCancel.setPreferredSize(new Dimension(100, 40));

        btnSave = new JButton("Lưu thay đổi");
        btnSave.putClientProperty(FlatClientProperties.STYLE, "arc:10; background:#28a745; foreground:#ffffff");
        btnSave.setPreferredSize(new Dimension(150, 40));
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        pnlButtons.add(btnCancel);
        pnlButtons.add(btnSave);
        add(pnlButtons, BorderLayout.SOUTH);
    }

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

    // --- HÀM LOAD DỮ LIỆU CŨ LÊN FORM ---
    private void loadDataToForm() 
    {
        if (currentEmp != null) {
            txtName.setText(currentEmp.getHoTen());
            txtDob.setText(currentEmp.getNgaySinh() != null ? currentEmp.getNgaySinh().toString() : "");
            txtJoinDate.setText(currentEmp.getNgayVaoLam() != null ? currentEmp.getNgayVaoLam().toString() : "");
            // Bỏ phần thập phân .0 khi hiển thị lương
            txtSalary.setText(String.format("%.0f", currentEmp.getLuongCoBan())); 
        }
    }

    private void initEvents() 
    {
        // Sự kiện cho nút "Hủy bỏ" để đóng dialog
        btnCancel.addActionListener(e -> dispose());
        // Sự kiện cho nút "Lưu thay đổi" để cập nhật thông tin nhân viên
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

                // Cập nhật thông tin vào đối tượng hiện tại (Giữ nguyên Mã NV)
                currentEmp.setHoTen(name);
                currentEmp.setNgaySinh(dob);
                currentEmp.setNgayVaoLam(joinDate);
                currentEmp.setLuongCoBan(salary);
                
                // GỌI BUS ĐỂ KIỂM TRA LOGIC VÀ LƯU
                if (employeeBUS.suaNhanVien(currentEmp)) 
                { 
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    parent.loadDataToTable();
                    dispose();
                } 
                else 
                {
                    JOptionPane.showMessageDialog(this, 
                        "Dữ liệu không hợp lệ! Vui lòng kiểm tra lại:\n" +
                        "- Mức lương phải lớn hơn hoặc bằng 0.\n" +
                        "- Ngày vào làm phải sau ngày sinh và ngày sinh phải sau năm 1961.\n" +
                        "- Độ tuổi lao động phải từ 15 tuổi trở lên.", 
                        "Từ chối sửa dữ liệu", 
                        JOptionPane.ERROR_MESSAGE);
                }

            } 
            catch (IllegalArgumentException ex) 
            {
                JOptionPane.showMessageDialog(this, 
                    "Sai định dạng ngày tháng!\nVui lòng nhập theo mẫu: Năm-Tháng-Ngày (VD: 2000-01-25)", 
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