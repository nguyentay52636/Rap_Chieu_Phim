package org.example.GUI.Components.FormEmployee;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Date;
import org.example.BUS.EmployeeBUS;
import org.example.DTO.EmployeeDTO;

public class AddEmployeeDialog extends JDialog {
    private JTextField txtName, txtDob, txtJoinDate, txtSalary;
    private JButton btnSave, btnCancel;
    private EmployeeBUS employeeBUS;
    private FormEmployee parent;

    public AddEmployeeDialog(FormEmployee parent, EmployeeBUS bus) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "QUẢN LÝ NHÂN VIÊN", true);
        this.parent = parent;
        this.employeeBUS = bus;
        
        setLayout(new BorderLayout());
        setSize(450, 500); // Rộng hơn một chút cho thoải mái
        setLocationRelativeTo(parent);
        
        initComponents();
        initEvents();
    }

    private void initComponents() {
        // --- PANEL TIÊU ĐỀ ---
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(66, 103, 178));
        JLabel lblTitle = new JLabel("THÊM NHÂN VIÊN MỚI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(15, 0, 15, 0));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // --- PANEL NHẬP LIỆU (Giống style ảnh ông gửi nhưng đẹp hơn) ---
        JPanel pnlContent = new JPanel(new GridLayout(4, 1, 15, 15));
        pnlContent.setBorder(new EmptyBorder(25, 40, 25, 40));
        pnlContent.setBackground(Color.WHITE);

        txtName = createModernField("Họ và Tên", "Nhập tên nhân viên...");
        txtDob = createModernField("Ngày Sinh", "yyyy-MM-dd (VD: 2000-01-01)");
        txtJoinDate = createModernField("Ngày Vào Làm", "yyyy-MM-dd (VD: 2026-03-16)");
        txtSalary = createModernField("Lương Cơ Bản", "Nhập số tiền lương...");

        pnlContent.add(createFieldWrapper("Họ và Tên nhân viên:", txtName));
        pnlContent.add(createFieldWrapper("Ngày sinh:", txtDob));
        pnlContent.add(createFieldWrapper("Ngày vào làm:", txtJoinDate));
        pnlContent.add(createFieldWrapper("Mức lương cơ bản:", txtSalary));

        add(pnlContent, BorderLayout.CENTER);

        // --- PANEL NÚT BẤM (Bo góc, màu sắc giống ảnh mẫu) ---
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

    private JTextField createModernField(String title, String placeholder) {
        JTextField txt = new JTextField();
        // FlatLaf placeholder xịn xò
        txt.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        // Tạo bo góc nhẹ cho ô nhập
        txt.putClientProperty(FlatClientProperties.STYLE, "arc:8; focusWidth:2");
        return txt;
    }

    private JPanel createFieldWrapper(String labelText, JTextField field) {
        JPanel pnl = new JPanel(new BorderLayout(5, 5));
        pnl.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnl.add(lbl, BorderLayout.NORTH);
        pnl.add(field, BorderLayout.CENTER);
        return pnl;
    }

    private void initEvents() {
        btnCancel.addActionListener(e -> dispose());

        btnSave.addActionListener(e -> {
            try {
                String name = txtName.getText().trim();
                Date dob = Date.valueOf(txtDob.getText().trim());
                Date joinDate = Date.valueOf(txtJoinDate.getText().trim());
                double salary = Double.parseDouble(txtSalary.getText().trim());

                if (name.isEmpty()) throw new Exception("Tên không được trống!");

                EmployeeDTO emp = new EmployeeDTO(0, name, dob, joinDate, salary);
                if (employeeBUS.themNhanVien(emp)) {
                    JOptionPane.showMessageDialog(this, "Thành công! Đã thêm " + name);
                    parent.loadDataToTable();
                    dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage() + "\n(Lưu ý định dạng: Năm-Tháng-Ngày)");
            }
        });
    }
}