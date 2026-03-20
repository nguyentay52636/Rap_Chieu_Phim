package org.example.GUI.Components.FormEmployee;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import org.example.DTO.EmployeeDTO;

public class DetailEmployeeDialog extends JDialog 
{
    private EmployeeDTO emp;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public DetailEmployeeDialog(JFrame parent, EmployeeDTO emp) 
    {
        super(parent, "Hồ Sơ Chi Tiết Nhân Viên", true);
        this.emp = emp;
        
        setLayout(new BorderLayout(10, 10));
        setSize(750, 550);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Color.WHITE);
        initComponents();
    }

    private void initComponents() {
        // --- 1. THÔNG TIN CHUNG (Phía trên) ---
        JPanel pnlInfo = new JPanel(new GridLayout(3, 2, 20, 15));
        pnlInfo.setBackground(Color.WHITE);
        pnlInfo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Thông Tin Chung", TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 14), new Color(66, 103, 178)));

        pnlInfo.add(createLabelGroup("Mã nhân viên:", String.valueOf(emp.getMaNV())));
        pnlInfo.add(createLabelGroup("Họ và Tên:", emp.getHoTen()));
        pnlInfo.add(createLabelGroup("Ngày sinh:", emp.getNgaySinh() != null ? sdf.format(emp.getNgaySinh()) : "N/A"));
        pnlInfo.add(createLabelGroup("Ngày vào làm:", emp.getNgayVaoLam() != null ? sdf.format(emp.getNgayVaoLam()) : "N/A"));
        pnlInfo.add(createLabelGroup("Lương cơ bản:", String.format("%,.0f VNĐ", emp.getLuongCoBan())));

        // --- 2. LỊCH SỬ LẬP HÓA ĐƠN (Phía dưới) ---
        JPanel pnlHistory = new JPanel(new BorderLayout(0, 10));
        pnlHistory.setBackground(Color.WHITE);
        pnlHistory.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Lịch Sử Lập Hóa Đơn (Sắp ra mắt)", TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 14), new Color(100, 100, 100)));

        String[] cols = {"Mã Hóa Đơn", "Mã KH", "Ngày Lập", "Tổng Tiền"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        
        // Demo dữ liệu (Sau này ông gọi HoaDonBUS để lấy dữ liệu thật)
        model.addRow(new Object[]{"HD001", "KH01", "16/03/2026", "250,000 VNĐ"});
        model.addRow(new Object[]{"HD005", "KH02", "15/03/2026", "120,000 VNĐ"});

        pnlHistory.add(new JScrollPane(table), BorderLayout.CENTER);

        // --- 3. NÚT ĐÓNG ---
        JButton btnClose = new JButton("Đóng Hồ Sơ");
        btnClose.setBackground(new Color(108, 117, 125));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e -> dispose());
        
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.add(btnClose);

        // Thêm vào Dialog
        JPanel pnlMain = new JPanel(new BorderLayout(15, 15));
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlMain.add(pnlInfo, BorderLayout.NORTH);
        pnlMain.add(pnlHistory, BorderLayout.CENTER);
        pnlMain.add(pnlBottom, BorderLayout.SOUTH);

        add(pnlMain);
    }

    private JPanel createLabelGroup(String title, String value) 
    {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnl.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        if (title.contains("Lương")) lblValue.setForeground(new Color(217, 83, 79)); // Màu đỏ cho lương
        
        pnl.add(lblTitle);
        pnl.add(lblValue);
        return pnl;
    }
}