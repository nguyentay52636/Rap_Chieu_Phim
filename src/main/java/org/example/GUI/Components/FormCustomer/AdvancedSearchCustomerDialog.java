package org.example.GUI.Components.FormCustomer;

import org.example.BUS.KhachHangBUS;
import org.example.DTO.KhachHangDTO;
import org.example.UtilsDate.FormattedDatePicker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AdvancedSearchCustomerDialog extends JDialog {
    private JTextField txtHoTen, txtSDT;
    private FormattedDatePicker dateTuNgay, dateDenNgay;

    // Giữ lại Khoảng điểm
    private JTextField txtDiemTu, txtDiemDen;

    // Thêm Toán tử điểm
    private JComboBox<String> cbPhepTinhDiem;
    private JTextField txtDiemToanTu;

    // Tuyệt chiêu JComboBox chọn nhiều
    private JButton btnComboHang;
    private JPopupMenu popupHang;
    private List<JCheckBoxMenuItem> chkHangList;

    private JButton btnTimKiem, btnHuy, btnLamMoi;
    private FormCustomer parentForm;
    private KhachHangBUS khBUS;

    Font font = new Font("Segoe UI", Font.PLAIN, 14);

    public AdvancedSearchCustomerDialog(JFrame owner, FormCustomer parentForm, KhachHangBUS khBUS) {
        super(owner, "Bộ lọc nâng cao", true);
        this.parentForm = parentForm;
        this.khBUS = khBUS;
        init(owner);
    }

    public void init(JFrame owner) {
        Dimension parentSize = owner.getSize();
        int canh = (int) (parentSize.height * 0.7);
        canh = Math.max(canh, 600);
        this.setSize(canh, canh);

        JPanel panelCenter = new JPanel(new GridLayout(6, 2, 10, 15));
        panelCenter.setBorder(new EmptyBorder(20, 20, 10, 20));
        panelCenter.setBackground(Color.WHITE);

        // 1. Họ Tên & SĐT
        panelCenter.add(createLabel("Họ và Tên:"));
        txtHoTen = new JTextField(); txtHoTen.setFont(font); panelCenter.add(txtHoTen);

        panelCenter.add(createLabel("Số điện thoại:"));
        txtSDT = new JTextField(); txtSDT.setFont(font); panelCenter.add(txtSDT);

        // 2. Ngày sinh
        panelCenter.add(createLabel("Ngày sinh (Từ - Đến):"));
        JPanel panelDate = new JPanel(new GridLayout(1, 2, 5, 0));
        panelDate.setBackground(Color.WHITE);
        dateTuNgay = new FormattedDatePicker(null); dateTuNgay.getJFormattedTextField().setFont(font);
        dateDenNgay = new FormattedDatePicker(null); dateDenNgay.getJFormattedTextField().setFont(font);
        panelDate.add(dateTuNgay); panelDate.add(dateDenNgay);
        panelCenter.add(panelDate);

        // 3. Khoảng điểm (Từ - Đến) - GIỮ LẠI NHƯ CŨ
        panelCenter.add(createLabel("Khoảng điểm tích lũy:"));
        JPanel jPanelKhoangDiem = new JPanel(new GridLayout(1, 2, 5, 0));
        jPanelKhoangDiem.setBackground(Color.WHITE);
        txtDiemTu = new JTextField(); txtDiemTu.setFont(font); txtDiemTu.putClientProperty("JTextField.placeholderText", "Từ điểm...");
        txtDiemDen = new JTextField(); txtDiemDen.setFont(font); txtDiemDen.putClientProperty("JTextField.placeholderText", "Đến điểm...");
        jPanelKhoangDiem.add(txtDiemTu); jPanelKhoangDiem.add(txtDiemDen);
        panelCenter.add(jPanelKhoangDiem);

        // 4. Toán tử điểm (>, <, =) - TÍNH NĂNG MỚI
        panelCenter.add(createLabel("Hoặc so sánh điểm:"));
        JPanel panelToanTu = new JPanel(new BorderLayout(5, 0));
        panelToanTu.setBackground(Color.WHITE);
        String[] operators = {"(Bỏ qua)", ">=", ">", "=", "<=", "<"};
        cbPhepTinhDiem = new JComboBox<>(operators); cbPhepTinhDiem.setFont(font); cbPhepTinhDiem.setBackground(Color.WHITE);
        txtDiemToanTu = new JTextField(); txtDiemToanTu.setFont(font);
        panelToanTu.add(cbPhepTinhDiem, BorderLayout.WEST);
        panelToanTu.add(txtDiemToanTu, BorderLayout.CENTER);
        panelCenter.add(panelToanTu);

        // 5. Hạng thành viên (Mô phỏng JComboBox Multi-Select)
        panelCenter.add(createLabel("Hạng Thành Viên:"));
        btnComboHang = new JButton("Tất cả ▼");
        btnComboHang.setFont(font);
        btnComboHang.setBackground(Color.WHITE);
        btnComboHang.setHorizontalAlignment(SwingConstants.LEFT);

        popupHang = new JPopupMenu();
        chkHangList = new ArrayList<>();
        String[] hangOptions = {"Thành viên mới", "Đồng", "Bạc", "Vàng", "VIP"};

        for (String h : hangOptions) {
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(h);
            item.setFont(font);
            chkHangList.add(item);
            popupHang.add(item);

            // Cập nhật text trên nút khi người dùng tick chọn
            item.addActionListener(e -> {
                List<String> selected = new ArrayList<>();
                for (JCheckBoxMenuItem c : chkHangList) {
                    if (c.isSelected()) selected.add(c.getText());
                }
                if (selected.isEmpty()) btnComboHang.setText("Tất cả ▼");
                else btnComboHang.setText(String.join(", ", selected) + " ▼");
            });
        }

        // Khi click vào nút thì xổ menu ra
        btnComboHang.addActionListener(e -> popupHang.show(btnComboHang, 0, btnComboHang.getHeight()));
        panelCenter.add(btnComboHang);

        // --- BUTTONS ---
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBottom.setBackground(Color.WHITE);

        btnLamMoi = new JButton("Làm mới"); btnLamMoi.setPreferredSize(new Dimension(100, 35)); btnLamMoi.setFocusPainted(false);
        btnHuy = new JButton("Hủy"); btnHuy.setPreferredSize(new Dimension(100, 35)); btnHuy.setFocusPainted(false);
        btnTimKiem = new JButton("Lọc Kết Quả"); btnTimKiem.setPreferredSize(new Dimension(130, 35));
        btnTimKiem.setBackground(new Color(0, 123, 255)); btnTimKiem.setForeground(Color.WHITE); btnTimKiem.setFocusPainted(false);

        panelBottom.add(btnLamMoi); panelBottom.add(btnTimKiem); panelBottom.add(btnHuy);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.add(panelCenter, BorderLayout.NORTH);

        this.setLayout(new BorderLayout());
        this.add(wrapperPanel, BorderLayout.CENTER);
        this.add(panelBottom, BorderLayout.SOUTH);

        this.setLocationRelativeTo(owner);
        this.setResizable(false);

        btnHuy.addActionListener(e -> setVisible(false));
        btnLamMoi.addActionListener(e -> clearData());
        btnTimKiem.addActionListener(e -> handleSearch());
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text); label.setFont(font); return label;
    }

    public void clearData() {
        txtHoTen.setText("");
        txtSDT.setText("");
        dateTuNgay.setDate(null);
        dateDenNgay.setDate(null);
        txtDiemTu.setText("");
        txtDiemDen.setText("");
        cbPhepTinhDiem.setSelectedIndex(0);
        txtDiemToanTu.setText("");
        for (JCheckBoxMenuItem chk : chkHangList) chk.setSelected(false);
        btnComboHang.setText("Tất cả ▼");
    }

    private void handleSearch() {
        String hoTen = txtHoTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        java.util.Date tuNgay = dateTuNgay.getDate();
        java.util.Date denNgay = dateDenNgay.getDate();

        String diemTu = txtDiemTu.getText().trim();
        String diemDen = txtDiemDen.getText().trim();

        String phepTinh = cbPhepTinhDiem.getSelectedItem().toString();
        String diemToanTu = txtDiemToanTu.getText().trim();

        List<String> selectedHangs = new ArrayList<>();
        for (JCheckBoxMenuItem chk : chkHangList) {
            if (chk.isSelected()) selectedHangs.add(chk.getText());
        }

        try {
            if (!diemTu.isEmpty()) Integer.parseInt(diemTu);
            if (!diemDen.isEmpty()) Integer.parseInt(diemDen);
            if (!diemToanTu.isEmpty()) Integer.parseInt(diemToanTu);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Điểm tích lũy phải là số nguyên!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Gọi hàm BUS
        List<KhachHangDTO> ketQua = khBUS.advancedSearch(hoTen, sdt, tuNgay, denNgay, diemTu, diemDen, phepTinh, diemToanTu, selectedHangs);
        parentForm.loadDataToTable(ketQua);
        dispose();
    }
}