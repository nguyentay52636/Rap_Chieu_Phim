package org.example.GUI.Components.FormEmployee;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Date;
import org.example.BUS.EmployeeBUS;
import org.example.DTO.EmployeeDTO;

public class EditEmployeeDialog extends JDialog {
    private JTextField txtMaNV, txtName, txtDob, txtJoinDate, txtSalary;
    private JButton btnSave, btnCancel;
    private EmployeeBUS employeeBUS;
    private FormEmployee parent;
    private EmployeeDTO currentEmp;

    public EditEmployeeDialog(FormEmployee parent, EmployeeBUS bus, EmployeeDTO emp) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "CẬP NHẬT NHÂN VIÊN", true);
        this.parent = parent;
        this.employeeBUS = bus;
        this.currentEmp = emp;
        
        setLayout(new BorderLayout());
        setSize(450, 550);
        setLocationRelativeTo(parent);
        
        initComponents();
        fillData(); // Đổ dữ liệu cũ vào form
        initEvents();
    }

    private void initComponents() {
        // --- HEADER ---
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(240, 173, 78)); // Màu vàng cam cho nút Sửa
        JLabel lblTitle = new JLabel("CHỈNH SỬA THÔNG TIN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(15, 0, 15, 0));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // --- CONTENT ---
        JPanel pnlContent = new JPanel(new GridLayout(5, 1, 10, 10));
        pnlContent.setBorder(new EmptyBorder(20, 40, 20, 40));
        pnlContent.setBackground(Color.WHITE);

        txtMaNV = new JTextField();
        txtMaNV.setEditable(false); // Khóa không cho sửa mã
        txtMaNV.setBackground(new Color(240, 240, 240));
        
        txtName = createField("Họ và Tên...");
        txtDob = createField("yyyy-MM-dd");
        txtJoinDate = createField("yyyy-MM-dd");
        txtSalary = createField("Mức lương...");

        pnlContent.add(createWrapper("Mã nhân viên (Không được sửa):", txtMaNV));
        pnlContent.add(createWrapper("Họ và Tên:", txtName));
        pnlContent.add(createWrapper("Ngày sinh:", txtDob));
        pnlContent.add(createWrapper("Ngày vào làm:", txtJoinDate));
        pnlContent.add(createWrapper("Lương cơ bản:", txtSalary));

        add(pnlContent, BorderLayout.CENTER);

        // --- BUTTONS ---
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        btnCancel = new JButton("Hủy");
        btnSave = new JButton("Lưu thay đổi");
        btnSave.setBackground(new Color(240, 173, 78));
        btnSave.setForeground(Color.WHITE);
        
        pnlButtons.add(btnCancel);
        pnlButtons.add(btnSave);
        add(pnlButtons, BorderLayout.SOUTH);
    }

    private void fillData() {
        txtMaNV.setText(String.valueOf(currentEmp.getMaNV()));
        txtName.setText(currentEmp.getHoTen());
        txtDob.setText(currentEmp.getNgaySinh().toString());
        txtJoinDate.setText(currentEmp.getNgayVaoLam().toString());
        txtSalary.setText(String.valueOf(currentEmp.getLuongCoBan()));
    }

    private JTextField createField(String placeholder) {
        JTextField t = new JTextField();
        t.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        t.putClientProperty(FlatClientProperties.STYLE, "arc:8");
        return t;
    }

    private JPanel createWrapper(String label, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(5, 2));
        p.setBackground(Color.WHITE);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        p.add(l, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private void initEvents() {
        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> {
            try {
                currentEmp.setHoTen(txtName.getText().trim());
                currentEmp.setNgaySinh(Date.valueOf(txtDob.getText().trim()));
                currentEmp.setNgayVaoLam(Date.valueOf(txtJoinDate.getText().trim()));
                currentEmp.setLuongCoBan(Double.parseDouble(txtSalary.getText().trim()));

                if (employeeBUS.suaNhanVien(currentEmp)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    parent.loadDataToTable();
                    dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + ex.getMessage());
            }
        });
    }
}