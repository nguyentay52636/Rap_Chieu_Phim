package org.example.GUI.Components.FormCustomer;

import org.example.BUS.KhachHangBUS;
import org.example.DTO.KhachHangDTO;
import org.example.UltisTable.TableUtils;
import org.example.UtilsDate.FormattedDatePicker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;

public class EditCustomerDialog extends JDialog {
    private JTextField txtHoTen, txtSDT;
    private FormattedDatePicker datePickerNgaySinh;

    private JTextField txtMaKH, txtDiem, txtHang;

    private JButton btnLuu, btnHuy;

    private FormCustomer parentForm;
    private KhachHangBUS khBUS;
    private KhachHangDTO khachHangGoc;

    Font font = new Font("Segoe UI", Font.PLAIN, 14);

    public EditCustomerDialog(JFrame owner, FormCustomer parentForm, KhachHangBUS khBUS) {
        super(owner, "Cập nhật thông tin khách hàng", true); // Từ khóa 'true' biến nó thành cửa sổ Modal
        this.parentForm = parentForm;
        this.khBUS = khBUS;
        init(owner);
    }

    public void init(JFrame owner) {
        Dimension parentSize = owner.getSize();
        int canh = (int) (parentSize.height * 0.6);
        canh = Math.max(canh, 650);
        this.setSize(canh, canh);

        JPanel panelCenter=new JPanel(new GridLayout(6,2,10,20));
        panelCenter.setBorder(new EmptyBorder(30, 20, 20, 20));

        // Lấy màu nền động cho panel chính
        panelCenter.setBackground(UIManager.getColor("Panel.background"));

        //Mã khách hàng
        panelCenter.add(createLabel("Mã Khách Hàng:"));
        txtMaKH = new JTextField();
        txtMaKH.setFont(font);
        setReadOnlyStyle(txtMaKH); // Gọi hàm làm mờ ô nhập
        panelCenter.add(txtMaKH);
        //Họ và tên
        panelCenter.add(createLabel("Họ và tên:"));
        txtHoTen=new JTextField();
        txtHoTen.setFont(font);
        panelCenter.add(txtHoTen);
        //Số điện thoại
        panelCenter.add(createLabel("Số điện thoại:"));
        txtSDT = new JTextField();
        txtSDT.setFont(font);
        panelCenter.add(txtSDT);
        //Ngày sinh
        panelCenter.add(createLabel("Ngày sinh (dd/MM/yyyy):"));
        datePickerNgaySinh = new FormattedDatePicker(null);
        datePickerNgaySinh.getJFormattedTextField().setFont(font);
        panelCenter.add(datePickerNgaySinh);
        //DiemTichLuy
        panelCenter.add(createLabel("Điểm tích lũy:"));
        txtDiem = new JTextField();
        txtDiem.setFont(font);
        setReadOnlyStyle(txtDiem);
        panelCenter.add(txtDiem);
        //HangThanhVien
        panelCenter.add(createLabel("Hạng thành viên:"));
        txtHang = new JTextField();
        txtHang.setFont(font);
        setReadOnlyStyle(txtHang);
        panelCenter.add(txtHang);

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));

        // Lấy màu nền động cho panel dưới
        panelBottom.setBackground(UIManager.getColor("Panel.background"));
        panelBottom.setBorder(new EmptyBorder(0, 0, 10, 10));

        btnHuy = new JButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(100, 35));
        btnHuy.setFocusPainted(false);
        // Nút Hủy thường để màu mặc định nên không ép màu tĩnh

        btnLuu = new JButton("Cập Nhật");
        btnLuu.setPreferredSize(new Dimension(120, 35));
        // Giữ lại màu vàng điểm nhấn cho nút Cập Nhật
        btnLuu.setBackground(new Color(255, 193, 7));
        // Thay vì dùng Color.BLACK, dùng màu đen của FlatLaf để đảm bảo hiển thị đúng trên nền vàng
        //btnLuu.setForeground(UIManager.getColor("Button.foreground"));
        btnLuu.setForeground(Color.BLACK);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLuu.setFocusPainted(false);

        panelBottom.add(btnLuu);
        panelBottom.add(btnHuy);

        JPanel wrapperPanel = new JPanel(new BorderLayout());

        // Lấy màu nền động cho wrapper
        wrapperPanel.setBackground(UIManager.getColor("Panel.background"));
        wrapperPanel.add(panelCenter, BorderLayout.NORTH);

        this.add(wrapperPanel, BorderLayout.CENTER);
        this.add(panelBottom, BorderLayout.SOUTH);
        this.setLocationRelativeTo(owner);
        this.setResizable(false);

        btnHuy.addActionListener(e -> setVisible(false));
        btnLuu.addActionListener(e -> handleUpdateCustomer());

        // ========================================================
        // TÍCH HỢP ĐIỀU HƯỚNG BÀN PHÍM (Sử dụng FormUtils)
        // ========================================================
        // 1. Từ ô Họ Tên -> Ấn mũi tên xuống -> Sang ô SDT
        org.example.Utils.FormUtils.setupFocus(txtHoTen, null, txtSDT);

        // 2. Từ ô SDT -> Ấn mũi tên xuống -> Nhảy vào Ngày Sinh VÀ Bung lịch ra
        org.example.Utils.FormUtils.setupFocusToDatePicker(
                txtSDT,
                txtHoTen,
                datePickerNgaySinh.getJFormattedTextField(),
                datePickerNgaySinh
        );

        // 3. Đang ở trong Lịch -> Lên (Tắt lịch, về SDT) hoặc Enter (Tắt lịch, bấm Lưu)
        org.example.Utils.FormUtils.setupDatePickerExit(
                datePickerNgaySinh.getJFormattedTextField(),
                txtSDT,
                btnLuu,
                datePickerNgaySinh
        );
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        return lbl;
    }

    // ĐÃ SỬA LẠI HÀM NÀY ĐỂ TỰ ĐỘNG CHUYỂN MÀU
    private void setReadOnlyStyle(JTextField txt) {
        txt.setEditable(false);
        // Lấy màu nền mặc định của TextField bị khóa (disabled) trong FlatLaf
        txt.setBackground(UIManager.getColor("TextField.disabledBackground"));
        // Lấy màu chữ mặc định của TextField bị khóa trong FlatLaf
        txt.setForeground(UIManager.getColor("TextField.disabledForeground"));
        txt.setFocusable(false); // Không cho nháy con trỏ chuột vào luôn
    }

    public void loadData(KhachHangDTO kh) {
        this.khachHangGoc = kh; // Lưu lại để lát so sánh SĐT

        txtMaKH.setText(String.valueOf(kh.getMaKH()));
        txtHoTen.setText(kh.getHoTen());
        txtSDT.setText(kh.getSDT());

        // Đổ ngày sinh từ LocalDate sang java.util.Date cho JDatePicker
        java.util.Date date = java.util.Date.from(kh.getNgaySinh().atStartOfDay(ZoneId.systemDefault()).toInstant());
        datePickerNgaySinh.setDate(date);

        txtDiem.setText(String.valueOf(kh.getDiemTichLuy()));
        txtHang.setText(kh.getHangThanhVien());

        // Reset lại viền đỏ nếu lần trước mở form có báo lỗi
        txtHoTen.putClientProperty("JComponent.outline", null);
        txtSDT.putClientProperty("JComponent.outline", null);
    }

    private void handleUpdateCustomer() {
        String hoTen = txtHoTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        java.util.Date selectedDate = datePickerNgaySinh.getDate();

        txtHoTen.putClientProperty("JComponent.outline", null);
        txtSDT.putClientProperty("JComponent.outline", null);

        boolean isOk = true;
        StringBuilder errorMsg = new StringBuilder();

        if(hoTen.isEmpty()){
            txtHoTen.putClientProperty("JComponent.outline", "error");
            errorMsg.append("- Họ tên không được để trống.\n");
            isOk = false;
        } else if (!hoTen.matches("^[\\p{L}\\s]+$")) {
            txtHoTen.putClientProperty("JComponent.outline", "error");
            errorMsg.append("- Họ tên chỉ được chứa chữ cái và khoảng trắng.\n");
            isOk = false;
        }

        if (sdt.isEmpty()) {
            txtSDT.putClientProperty("JComponent.outline", "error");
            errorMsg.append("- Số điện thoại không được để trống.\n");
            isOk = false;
        } else if (!sdt.matches("^0\\d{9}$")) {
            txtSDT.putClientProperty("JComponent.outline", "error");
            errorMsg.append("- Số điện thoại phải gồm 10 chữ số và bắt đầu bằng số 0.\n");
            isOk = false;
        } else {
            // TUYỆT CHIÊU Ở ĐÂY: Chỉ kiểm tra trùng SĐT nếu số mới KHÁC số cũ!
            if (!sdt.equals(khachHangGoc.getSDT()) && khBUS.checkTrungSDT(sdt)) {
                txtSDT.putClientProperty("JComponent.outline", "error");
                errorMsg.append("- Số điện thoại này đã thuộc về người khác.\n");
                isOk = false;
            }
        }

        if (selectedDate == null) {
            datePickerNgaySinh.getJFormattedTextField().putClientProperty("JComponent.outline", "error");
            errorMsg.append("- Vui lòng chọn ngày sinh hợp lệ.\n");
            isOk = false;
        } else if (selectedDate.after(new java.util.Date())) {
            datePickerNgaySinh.getJFormattedTextField().putClientProperty("JComponent.outline", "error");
            errorMsg.append("- Ngày sinh không hợp lệ (thuộc về tương lai).\n");
            isOk = false;
        }

        if (!isOk) {
            JOptionPane.showMessageDialog(this, errorMsg.toString(), "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate ngaySinh = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Tạo DTO mới chứa thông tin đã cập nhật (Nhớ lấy Mã, Điểm, Hạng từ khachHangGoc)
        KhachHangDTO khMoi = new KhachHangDTO(
                khachHangGoc.getMaKH(),
                hoTen, sdt, ngaySinh,
                khachHangGoc.getDiemTichLuy(),
                khachHangGoc.getHangThanhVien()
        );

        // --- TẠO BẢNG XEM TRƯỚC (PREVIEW) Y HỆT HÀM THÊM ---
        String[] columns = {"Mã KH", "Họ và Tên", "Số điện thoại", "Ngày sinh", "Điểm tích lũy", "Hạng Thành Viên"};
        Object[][] data = {
                {khMoi.getMaKH(), khMoi.getHoTen(), khMoi.getSDT(), datePickerNgaySinh.getJFormattedTextField().getText(), khMoi.getDiemTichLuy(), khMoi.getHangThanhVien()}
        };

        JTable previewTable = new JTable(data, columns);
        previewTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        previewTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Thay vì dùng Color tĩnh, dùng màu của hệ thống cho Preview Table
        previewTable.getTableHeader().setBackground(UIManager.getColor("Table.selectionBackground"));
        previewTable.getTableHeader().setForeground(UIManager.getColor("Table.selectionForeground"));

        previewTable.setRowHeight(35);
        previewTable.setEnabled(false);
        previewTable.getTableHeader().setReorderingAllowed(false);
        //previewTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableUtils.autoResizeColumns(previewTable);

        JScrollPane scrollPreview = new JScrollPane(previewTable);
        scrollPreview.setPreferredSize(new Dimension(650, 85));

        JPanel panelConfirm = new JPanel(new BorderLayout(0, 10));
        JLabel lblConfirm = new JLabel("Xác nhận thay đổi thông tin Khách hàng này?");
        lblConfirm.setFont(new Font("Segoe UI", Font.ITALIC, 14));

        // Giữ màu đỏ cảnh báo, nhưng có thể điều chỉnh theo hệ thống nếu muốn đồng bộ hơn
        //lblConfirm.setForeground(UIManager.getColor("Component.error.focusedBorderColor"));
        lblConfirm.setForeground(Color.RED);

        panelConfirm.add(lblConfirm, BorderLayout.NORTH);
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.add(scrollPreview);
        panelConfirm.add(wrapper, BorderLayout.CENTER);

        int confirm = JOptionPane.showConfirmDialog(
                this, panelConfirm, "Lưu thay đổi",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );

        // --- GỌI BUS UPDATE ---
        if (confirm == JOptionPane.YES_OPTION) {
            if (khBUS.update(khMoi)) { // Lệnh update dưới BUS
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                parentForm.loadDataToTable();
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại. Lỗi CSDL.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}