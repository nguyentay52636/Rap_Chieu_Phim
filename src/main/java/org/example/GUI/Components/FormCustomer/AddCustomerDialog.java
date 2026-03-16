package org.example.GUI.Components.FormCustomer;

import org.example.BUS.KhachHangBUS;
import org.example.DTO.KhachHangDTO;
import org.example.UtilsDate.FormattedDatePicker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;

public class AddCustomerDialog extends JDialog {
    private JTextField txtHoTen, txtSDT;
    private FormattedDatePicker datePickerNgaySinh;
    private JButton btnThem, btnHuy;

    private FormCustomer parentForm;
    private KhachHangBUS khBUS;

    Font font=new Font("Segoe UI", Font.PLAIN, 14);
    public AddCustomerDialog(JFrame owner, FormCustomer parentForm, KhachHangBUS khBUS) {
        super(owner, "Thêm Khách Hàng Mới", true); // Từ khóa 'true' biến nó thành cửa sổ Modal
        this.parentForm = parentForm;
        this.khBUS = khBUS;
        init(owner);
    }

    public void init(JFrame owner){
        Dimension parentSize=owner.getSize();
        int canh=(int) (parentSize.height*0.5);
        canh=Math.max(canh, 450);
        this.setSize(canh, canh);

        JPanel panelCenter=new JPanel(new GridLayout(3,2,10, 30));
        panelCenter.setBorder(new EmptyBorder(30,20,20,20));

        JLabel jLabelHoTen=new JLabel("Họ và Tên: ");
        jLabelHoTen.setFont(font);
        panelCenter.add(jLabelHoTen);
        txtHoTen=new JTextField();
        txtHoTen.setFont(font);
        panelCenter.add(txtHoTen);

        JLabel jLabelSDT=new JLabel("Số điện thoại: ");
        jLabelSDT.setFont(font);
        panelCenter.add(jLabelSDT);
        txtSDT=new JTextField();
        txtSDT.setFont(font);
        panelCenter.add(txtSDT);

        JLabel jLabelNgaySinh=new JLabel("Ngày sinh (dd/MM/yyyy): ");
        jLabelNgaySinh.setFont(font);
        panelCenter.add(jLabelNgaySinh);

        datePickerNgaySinh = new FormattedDatePicker(null);
        datePickerNgaySinh.getJFormattedTextField().setFont(font);
        panelCenter.add(datePickerNgaySinh);

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBottom.setBorder(new EmptyBorder(0, 0, 10, 10));

        btnHuy = new JButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(100, 35));
        btnHuy.setFocusPainted(false);

        btnThem = new JButton("Lưu");
        btnThem.setPreferredSize(new Dimension(120, 35));
        btnThem.setBackground(new Color(0, 123, 255));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFocusPainted(false);

        panelBottom.add(btnThem);
        panelBottom.add(btnHuy);

        JPanel wrapperPanel = new JPanel(new BorderLayout());

        wrapperPanel.add(panelCenter, BorderLayout.NORTH);

        this.add(wrapperPanel, BorderLayout.CENTER);

        this.add(panelBottom, BorderLayout.SOUTH);

        this.setLocationRelativeTo(owner);
        this.setResizable(false);

        panelCenter.setBackground(Color.WHITE);
        wrapperPanel.setBackground(Color.WHITE);
        panelBottom.setBackground(Color.WHITE);

        btnHuy.addActionListener(e -> setVisible(false));
        btnThem.addActionListener(e -> handleAddCustomer());
    }

    public void clearData() {
        txtHoTen.setText("");
        txtSDT.setText("");
        datePickerNgaySinh.setDate(null);

        txtHoTen.putClientProperty("JComponent.outline", null);
        txtSDT.putClientProperty("JComponent.outline", null);
        datePickerNgaySinh.getJFormattedTextField().putClientProperty("JComponent.outline", null);

        txtHoTen.requestFocus();
    }

    private void handleAddCustomer() {
        String hoTen = txtHoTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        java.util.Date selectedDate = datePickerNgaySinh.getDate();
        // Bẫy lỗi trống
//        if (hoTen.isEmpty() || sdt.isEmpty() || selectedDate == null) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin và chọn ngày sinh!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
        txtHoTen.putClientProperty("JComponent.outline", null);
        txtSDT.putClientProperty("JComponent.outline", null);

        boolean isOk=true;
        StringBuilder errorMsg= new StringBuilder();

        if(hoTen.isEmpty()){
            txtHoTen.putClientProperty("JComponent.outline", "error"); // Đổi viền thành đỏ
            errorMsg.append("- Họ tên không được để trống.\n");
            isOk = false;
        }
        else if (!hoTen.matches("^[\\p{L}\\s]+$")) {
            // Regex \p{L} là kiểm tra chữ cái, hỗ trợ ĐẦY ĐỦ tiếng Việt có dấu
            txtHoTen.putClientProperty("JComponent.outline", "error");
            errorMsg.append("- Họ tên chỉ được chứa chữ cái và khoảng trắng, không chứa số hay ký tự đặc biệt.\n");
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
            // KIỂM TRA TRÙNG LẶP SỐ ĐIỆN THOẠI BẰNG BUS
            if (khBUS.checkTrungSDT(sdt)) { // Chỉ cần 1 dòng này thôi!
                txtSDT.putClientProperty("JComponent.outline", "error");
                errorMsg.append("- Số điện thoại này đã được đăng ký cho khách hàng khác.\n");
                isOk = false;
            }
        }

        if (selectedDate == null) {
            // Trường hợp rỗng hoặc người dùng gõ tay sai định dạng
            datePickerNgaySinh.getJFormattedTextField().putClientProperty("JComponent.outline", "error");
            errorMsg.append("- Vui lòng chọn hoặc nhập ngày sinh hợp lệ.\n");
            isOk = false;
        }
        else {
            // Trường hợp ngày sinh lớn hơn ngày hiện tại (Tương lai)
            java.util.Date currentDate = new java.util.Date(); // Lấy ngày giờ hệ thống hiện tại
            if (selectedDate.after(currentDate)) {
                datePickerNgaySinh.getJFormattedTextField().putClientProperty("JComponent.outline", "error");
                errorMsg.append("- Ngày sinh không hợp lệ (Không thể chọn ngày trong tương lai).\n");
                isOk = false;
            }
        }

        if (!isOk) {
            // Hiện 1 bảng thông báo duy nhất chứa tất cả các lỗi
            JOptionPane.showMessageDialog(this, errorMsg.toString(), "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return; // DỪNG LẠI, KHÔNG CHẠY CODE LƯU XUỐNG CSDL NỮA
        }

        // --- 1. CHUYỂN ĐỔI DỮ LIỆU ---
        LocalDate ngaySinh = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        KhachHangDTO kh = new KhachHangDTO(0, hoTen, sdt, ngaySinh, 0, "Thành viên mới");

        // --- 2. TẠO BẢNG JTABLE XEM TRƯỚC (PREVIEW TABLE) ---
        String[] columns = {"Họ và Tên", "Số điện thoại", "Ngày sinh", "Điểm tích lũy", "Hạng Thành Viên"};
        // Đưa dữ liệu của khách hàng vừa nhập vào 1 mảng 2 chiều (1 dòng)
        Object[][] data = {
                {kh.getHoTen(), kh.getSDT(), datePickerNgaySinh.getJFormattedTextField().getText(), kh.getDiemTichLuy() ,kh.getHangThanhVien()}
        };

        JTable previewTable = new JTable(data, columns);
        previewTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        previewTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        previewTable.getTableHeader().setBackground(new Color(66, 103, 178));
        previewTable.getTableHeader().setForeground(Color.WHITE);
        previewTable.setRowHeight(35);
        previewTable.setEnabled(false);
        previewTable.getTableHeader().setReorderingAllowed(false);

        //previewTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int column = 0; column < previewTable.getColumnCount(); column++) {
            int maxWidth = 50; // Độ rộng tối thiểu

            // 1. Đo chiều rộng của Tiêu đề cột (Header)
            javax.swing.table.TableCellRenderer headerRenderer = previewTable.getTableHeader().getDefaultRenderer();
            Object headerValue = previewTable.getColumnModel().getColumn(column).getHeaderValue();
            Component headerComp = headerRenderer.getTableCellRendererComponent(previewTable, headerValue, false, false, 0, column);
            maxWidth = Math.max(headerComp.getPreferredSize().width, maxWidth);

            // 2. Đo chiều rộng của dữ liệu bên trong (Data)
            for (int row = 0; row < previewTable.getRowCount(); row++) {
                javax.swing.table.TableCellRenderer cellRenderer = previewTable.getCellRenderer(row, column);
                Component cellComp = previewTable.prepareRenderer(cellRenderer, row, column);
                maxWidth = Math.max(cellComp.getPreferredSize().width, maxWidth);
            }

            // 3. Set độ rộng lớn nhất tìm được (Cộng thêm 15px lề trái/phải cho thoáng chữ)
            previewTable.getColumnModel().getColumn(column).setPreferredWidth(maxWidth + 15);

        }
        // ---> KẾT THÚC ĐOẠN TỰ ĐỘNG <---

        // 1. Tăng chiều rộng ScrollPane lên 600, chiều cao 65 (hoặc 70) là đẹp
        JScrollPane scrollPreview = new JScrollPane(previewTable);
        scrollPreview.setPreferredSize(new Dimension(600, 75));

        // 2. Tạo Panel chính
        JPanel panelConfirm = new JPanel(new BorderLayout(0, 10));
        JLabel lblConfirm = new JLabel("Vui lòng kiểm tra lại thông tin trước khi lưu xuống Cơ sở dữ liệu:");
        lblConfirm.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblConfirm.setForeground(Color.RED);

        panelConfirm.add(lblConfirm, BorderLayout.NORTH);

        // 3. Tạo áo khoác chống dãn
        JPanel tableWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tableWrapper.add(scrollPreview);

        // 4. Ném áo khoác vào Panel chính
        panelConfirm.add(tableWrapper, BorderLayout.CENTER);

        // --- 3. HIỂN THỊ HỘP THOẠI XÁC NHẬN CHỨA BẢNG ---
        int confirm = JOptionPane.showConfirmDialog(
                this,
                panelConfirm, // NHÉT NGUYÊN CÁI PANEL CHỨA BẢNG VÀO ĐÂY!
                "Xác nhận dữ liệu",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        // --- 4. KIỂM TRA QUYẾT ĐỊNH CỦA NGƯỜI DÙNG ---
        if (confirm == JOptionPane.YES_OPTION) {
            // NẾU BẤM YES -> GỌI DB ĐỂ LƯU THẬT
            if (khBUS.add(kh)) {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                parentForm.loadDataToTable(); // Bảng ở ngoài tự động reload
                setVisible(false); // Đóng Pop-up AddCustomerDialog
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại. Lỗi CSDL.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        }
        // Nếu bấm NO, hộp thoại xác nhận sẽ tự tắt, form nhập liệu vẫn giữ nguyên để họ sửa chữ tiếp
    }
}
