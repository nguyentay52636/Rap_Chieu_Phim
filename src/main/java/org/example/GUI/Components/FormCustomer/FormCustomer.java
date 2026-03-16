package org.example.GUI.Components.FormCustomer;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.BUS.KhachHangBUS;
import org.example.DTO.KhachHangDTO;

import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class FormCustomer extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField txtTimKiem;
    private JTable table;

    private DefaultTableModel model;
    private KhachHangBUS khBUS = new KhachHangBUS();

    private AddCustomerDialog addDialog;
    private AdvancedSearchCustomerDialog filterDialog;
    private EditCustomerDialog editDialog;

    private JButton btnFilter;
    private JButton btnView;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnAdd;
    private JButton btnNhap;
    private JButton btnXuat;
    private JComboBox<String> comboBox;
    private JButton btnLamMoi;
    private JComboBox<String> cbHienThi;

    public FormCustomer() {
        init();
    }

    public FormCustomer(String title) {
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 250));

        JPanel up = new JPanel();
        up.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        up.setBackground(new Color(245, 245, 250));
        up.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), "Quản Lý Khách Hàng", TitledBorder.LEFT, TitledBorder.TOP));

        btnView = createStyledButton("Xem", new Color(102, 187, 106), Color.WHITE, "/org/example/GUI/resources/images/view.png");
        btnView.addActionListener(e -> {int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 khách hàng để xem hồ sơ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (selectedRows.length > 1) {
                JOptionPane.showMessageDialog(this, "Chỉ được xem hồ sơ của 1 khách hàng mỗi lần!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Phiên dịch vị trí (Chống lỗi khi bảng đang Sort)
            int modelRow = table.convertRowIndexToModel(selectedRows[0]);

            // Rút trích dữ liệu từ Bảng đóng gói vào DTO
            int maKH = (int) model.getValueAt(modelRow, 0);
            String hoTen = (String) model.getValueAt(modelRow, 1);
            String sdt = (String) model.getValueAt(modelRow, 2);
            java.time.LocalDate ngaySinh = (java.time.LocalDate) model.getValueAt(modelRow, 3);
            int diem = (int) model.getValueAt(modelRow, 4);
            String hang = (String) model.getValueAt(modelRow, 5);

            KhachHangDTO kh = new KhachHangDTO(maKH, hoTen, sdt, ngaySinh, diem, hang);

            // Bật Dialog Xem Hồ Sơ lên (Tạo mới luôn để nạp dữ liệu khách hàng vừa chọn)
            JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
            ViewCustomerDialog viewDialog = new ViewCustomerDialog(owner, kh);
            viewDialog.setVisible(true);});
        up.add(btnView);

        btnEdit = createStyledButton("Sửa", new Color(255, 193, 7), Color.WHITE, "/org/example/GUI/resources/images/editing.png");
        btnEdit.addActionListener(e -> {int[] selectedRows = table.getSelectedRows();

            // Xử lý chuẩn theo logic của bạn: Phải chọn ĐÚNG 1 DÒNG!
            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 khách hàng để sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (selectedRows.length > 1) {
                JOptionPane.showMessageDialog(this, "Chỉ được chọn 1 khách hàng để sửa mỗi lần!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Phiên dịch vị trí (Chống lỗi Sort)
            int modelRow = table.convertRowIndexToModel(selectedRows[0]);

            // Rút trích dữ liệu từ Bảng đóng gói vào DTO
            int maKH = (int) model.getValueAt(modelRow, 0);
            String hoTen = (String) model.getValueAt(modelRow, 1);
            String sdt = (String) model.getValueAt(modelRow, 2);
            LocalDate ngaySinh = (LocalDate) model.getValueAt(modelRow, 3);
            int diem = (int) model.getValueAt(modelRow, 4);
            String hang = (String) model.getValueAt(modelRow, 5);

            KhachHangDTO kh = new KhachHangDTO(maKH, hoTen, sdt, ngaySinh, diem, hang);

            // Bật Dialog Sửa lên
            if (editDialog == null) {
                JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
                editDialog = new EditCustomerDialog(owner, this, khBUS);
            }

            // Truyền dữ liệu vào form và hiển thị
            editDialog.loadData(kh);
            editDialog.setVisible(true);});
        up.add(btnEdit);

        btnDelete = createStyledButton("Xóa", new Color(220, 53, 69), Color.WHITE, "/org/example/GUI/resources/images/bin.png");
        btnDelete.addActionListener(e -> {
            // 1. Lấy danh sách các dòng đang được chọn
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất 1 khách hàng để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. TẠO BẢNG PREVIEW
            String[] columns = {"Mã KH", "Họ và Tên", "Số điện thoại", "Điểm", "Hạng"};
            Object[][] data = new Object[selectedRows.length][5];

            for (int i = 0; i < selectedRows.length; i++) {
                int modelRow = table.convertRowIndexToModel(selectedRows[i]);
                data[i][0] = model.getValueAt(modelRow, 0);
                data[i][1] = model.getValueAt(modelRow, 1);
                data[i][2] = model.getValueAt(modelRow, 2);
                data[i][3] = model.getValueAt(modelRow, 4);
                data[i][4] = model.getValueAt(modelRow, 5);
            }

            JTable previewTable = new JTable(data, columns);
            previewTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            previewTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            previewTable.getTableHeader().setBackground(new Color(220, 53, 69)); // Nền đỏ cảnh báo
            previewTable.getTableHeader().setForeground(Color.WHITE);
            previewTable.setRowHeight(35);
            previewTable.setEnabled(false);
            previewTable.getTableHeader().setReorderingAllowed(false);
            //previewTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            // ---> THUẬT TOÁN TỰ ĐỘNG ĐO VÀ CO GIÃN CỘT (GIỐNG HÀM ADD) <---
            for (int column = 0; column < previewTable.getColumnCount(); column++) {
                int maxWidth = 50;

                // Đo Header
                javax.swing.table.TableCellRenderer headerRenderer = previewTable.getTableHeader().getDefaultRenderer();
                Object headerValue = previewTable.getColumnModel().getColumn(column).getHeaderValue();
                Component headerComp = headerRenderer.getTableCellRendererComponent(previewTable, headerValue, false, false, 0, column);
                maxWidth = Math.max(headerComp.getPreferredSize().width, maxWidth);

                // Đo Data của tất cả các dòng sắp xóa
                for (int row = 0; row < previewTable.getRowCount(); row++) {
                    javax.swing.table.TableCellRenderer cellRenderer = previewTable.getCellRenderer(row, column);
                    Component cellComp = previewTable.prepareRenderer(cellRenderer, row, column);
                    maxWidth = Math.max(cellComp.getPreferredSize().width, maxWidth);
                }

                // Chốt kích thước cột
                previewTable.getColumnModel().getColumn(column).setPreferredWidth(maxWidth + 15);
            }

            // --- TÍNH TOÁN CHIỀU CAO LINH HOẠT CHO SCROLLPANE ---
            // Nếu xóa 1 người thì bảng thấp, xóa nhiều người thì bảng cao (Max 250px)
            // Cộng thêm 20px để chừa chỗ cho thanh cuộn ngang (Horizontal Scrollbar)
            int tableHeight = Math.min(selectedRows.length * 35 + 35, 250) + 20;

            JScrollPane scrollPreview = new JScrollPane(previewTable);
            scrollPreview.setPreferredSize(new Dimension(600, tableHeight));

            JPanel panelConfirm = new JPanel(new BorderLayout(0, 10));
            JLabel lblConfirm = new JLabel("CẢNH BÁO: Bạn có chắc chắn muốn XÓA VĨNH VIỄN " + selectedRows.length + " khách hàng này không?");
            lblConfirm.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblConfirm.setForeground(Color.RED);

            panelConfirm.add(lblConfirm, BorderLayout.NORTH);
            JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            wrapper.add(scrollPreview);
            panelConfirm.add(wrapper, BorderLayout.CENTER);

            // 3. HIỂN THỊ XÁC NHẬN
            int confirm = JOptionPane.showConfirmDialog(
                    this, panelConfirm, "Xác nhận xóa dữ liệu",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
            );

            // 4. THỰC THI XÓA XUỐNG CSDL
            if (confirm == JOptionPane.YES_OPTION) {
                int successCount = 0;
                for (int i = 0; i < selectedRows.length; i++) {
                    int modelRow = table.convertRowIndexToModel(selectedRows[i]);
                    int maKH = (int) model.getValueAt(modelRow, 0);

                    if (khBUS.delete(maKH)) {
                        successCount++;
                    }
                }
                JOptionPane.showMessageDialog(this, "Đã xóa thành công " + successCount + " khách hàng!");
                loadDataToTable(); // Cập nhật lại giao diện chính
            }
        });
        up.add(btnDelete);

        btnAdd = createStyledButton("Thêm", new Color(0, 123, 255), Color.WHITE, "/org/example/GUI/resources/images/plus.png");
        btnAdd.addActionListener(e -> {
            // Kiểm tra: Nếu chưa có thì khởi tạo 1 lần duy nhất
            if (addDialog == null) {
                JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
                addDialog = new AddCustomerDialog(owner, this, khBUS);
            }

            // Dọn dẹp tàn dư cũ trước khi cho hiện hình
            addDialog.clearData();
            addDialog.setVisible(true);});
        up.add(btnAdd);

        btnNhap = createStyledButton("Nhập Excel", new Color(153, 102, 255), Color.WHITE, "/org/example/GUI/resources/images/icons8_ms_excel_30px.png");
        btnNhap.addActionListener(e -> nhapExcel());
        up.add(btnNhap);

        btnXuat = createStyledButton("Xuất Excel", new Color(153, 102, 255), Color.WHITE, "/org/example/GUI/resources/images/icons8_ms_excel_30px.png");
        btnXuat.addActionListener(e -> xuatExcel());
        up.add(btnXuat);

        JPanel timkiem = new JPanel();
        timkiem.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        timkiem.setBackground(new Color(245, 245, 250));

//        cbHienThi = new JComboBox<>(new String[] { "Đang hoạt động", "Đã xóa" });
//        cbHienThi.setPreferredSize(new Dimension(150, 40));
//        cbHienThi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        cbHienThi.setBackground(Color.WHITE);
//        cbHienThi.addActionListener(e -> { /* chỉ giao diện */ });
//        timkiem.add(cbHienThi);

        String[] options = { "Mã KH", "Tên KH", "SĐT" };
        comboBox = new JComboBox<>(options);
        comboBox.setPreferredSize(new Dimension(100, 40));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.addActionListener(e -> txtTimKiem.setText(""));

        txtTimKiem = new JTextField(20);
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Nhập từ khóa cần tìm...");
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTimKiem.setPreferredSize(new Dimension(200, 40));
        txtTimKiem.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        txtTimKiem.setBackground(Color.WHITE);
        txtTimKiem.setHorizontalAlignment(SwingConstants.LEFT);

        txtTimKiem.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { performSearch(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { performSearch(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { performSearch(); }

            private void performSearch() {
                String tieuChiUI = comboBox.getSelectedItem().toString();

                // Lấy từ khóa (đừng dùng trim() ở đây vì nó sẽ cắt mất khoảng trắng khi bạn đang gõ dở chữ "Nguyễn ")
                String tuKhoa = txtTimKiem.getText();

                // PHIÊN DỊCH: Chuyển tiếng Việt trên UI thành Tên cột trong Database
                String tieuChiDB = "";
                switch (tieuChiUI) {
                    case "Mã KH":
                        tieuChiDB = "MaKH";
                        break;
                    case "Tên KH":
                        tieuChiDB = "HoTen";
                        break;
                    case "SĐT":
                        tieuChiDB = "SDT";
                        break;
                }

                // Gọi BUS tìm kiếm và in ra bảng
                List<KhachHangDTO> ketQua = khBUS.search(tieuChiDB, tuKhoa);
                loadDataToTable(ketQua);
            }
        });

        JPanel comTk = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        comTk.setBackground(new Color(245, 245, 250));
        comTk.add(comboBox);
        comTk.add(txtTimKiem);

        btnFilter=createSquareButton("/org/example/GUI/resources/images/filter_icon1.png", 40, new Color(255, 255, 255));
        btnFilter.addActionListener(e -> {
            // Kiểm tra: Nếu chưa có thì khởi tạo 1 lần duy nhất
            if (filterDialog == null) {
                JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
                filterDialog = new AdvancedSearchCustomerDialog(owner, this, khBUS);
            }

            // Dọn dẹp tàn dư cũ trước khi cho hiện hình
            filterDialog.clearData();
            filterDialog.setVisible(true);});
        comTk.add(btnFilter);

        comTk.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), "Tìm kiếm"));
        timkiem.add(comTk);

        btnLamMoi = createStyledButton("Làm mới", new Color(100, 181, 246), Color.WHITE, "/org/example/GUI/resources/images/icons8_data_backup_30px.png");
        btnLamMoi.addActionListener(e ->{
            txtTimKiem.setText("");
            comboBox.setSelectedIndex(0);

            if(filterDialog != null){
                filterDialog.clearData();
            }

            if (table.getRowSorter() != null) {
                table.getRowSorter().setSortKeys(null);
            }

            loadDataToTable();
        });
        timkiem.add(btnLamMoi);

        JPanel chucNang = new JPanel(new GridLayout(2, 1, 0, 10));
        chucNang.setBackground(new Color(245, 245, 250));
        chucNang.add(up);
        chucNang.add(timkiem);

        table = new JTable() {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                }
                if (!isRowSelected(row)) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(240, 240, 245));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getRowCount() == 0) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    // Bật khử răng cưa cho chữ mịn màng
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 28));
                    g2d.setColor(new Color(180, 180, 180)); // Màu xám nhạt

                    String text = "DANH SÁCH TRỐNG";
                    FontMetrics fm = g2d.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(text)) / 2;
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

                    g2d.drawString(text, x, y);
                    g2d.dispose();
                }
            }
        };

        table.getTableHeader().setReorderingAllowed(false);

        table.setFillsViewportHeight(true);

        Font font = new Font("Segoe UI", Font.PLAIN, 16);
        Font fontHeader = new Font("Segoe UI", Font.BOLD, 16);
        table.setFont(font);
        table.getTableHeader().setFont(fontHeader);
        table.getTableHeader().setBackground(new Color(66, 103, 178));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(40);
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);

        // --- NÂNG CẤP MODEL ĐỂ NHẬN DIỆN ĐÚNG KIỂU DỮ LIỆU ---
        model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Integer.class; // Mã KH
                    case 3: return java.time.LocalDate.class; // Ngày sinh
                    case 4: return Integer.class; // Điểm tích lũy
                    default: return String.class; // Tên, SĐT, Hạng
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Khóa bảng không cho gõ bậy
            }
        };

        model.addColumn("Mã KH");
        model.addColumn("Tên KH");
        model.addColumn("SĐT");
        model.addColumn("Ngày sinh");
        model.addColumn("Điểm tích lũy");
        model.addColumn("Hạng thành viên");
        table.setModel(model);

        // ---> KÍCH HOẠT TÍNH NĂNG CLICK VÀO HEADER ĐỂ SẮP XẾP <---
        javax.swing.table.TableRowSorter<DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // ---> ÉP TOÀN BỘ CỘT CĂN LỀ TRÁI (LEFT ALIGNMENT) <---
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        // --- ĐÃ ĐỔI MÀU TÔ ĐẬM GIỐNG TRONG ẢNH ---
        table.setSelectionBackground(new Color(66, 103, 178)); // Màu xanh đậm giống thanh tiêu đề bảng
        table.setSelectionForeground(Color.WHITE); // Chữ màu trắng
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String maKH = table.getValueAt(row, 0).toString();
                    String tenKH = table.getValueAt(row, 1).toString();
                    System.out.println("Bạn vừa chọn Khách hàng: " + maKH + " - " + tenKH);
                }
            }
        });
        // ----------------------------------------------------------------

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(chucNang, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadDataToTable();
    }

    private JButton createStyledButton(String text, Color bg, Color fg, String iconPath) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(165, 44));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        if (iconPath != null) {
            button.setIcon(loadImageIcon(iconPath));
        }
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bg.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bg);
            }
        });
        return button;
    }

    private ImageIcon loadImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Không thể tải hình ảnh: " + path);
            return null;
        }
    }

    public void loadDataToTable(List<KhachHangDTO> danhSachKH) {
        model.setRowCount(0); // Xóa sạch bảng cũ
        for (KhachHangDTO kh : danhSachKH) {
            Object[] row = new Object[]{
                    kh.getMaKH(),
                    kh.getHoTen(),
                    kh.getSDT(),
                    kh.getNgaySinh(),
                    kh.getDiemTichLuy(),
                    kh.getHangThanhVien()
            };
            model.addRow(row);
        }
    }

    public void loadDataToTable() {
        khBUS.refreshList(); // Cập nhật lại từ DB
        loadDataToTable(khBUS.getListKhachHang()); // Gọi hàm số 1 ở trên
    }

    private JButton createSquareButton(String iconPath, int size, Color bgColor) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(size, size));
        btn.setMinimumSize(new Dimension(size, size));
        btn.setMaximumSize(new Dimension(size, size));

        // Gắn icon và ÉP KÍCH THƯỚC
        if (iconPath != null) {
            java.net.URL imgURL = getClass().getResource(iconPath);
            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);

                // Thu nhỏ ảnh xuống còn 24x24 pixel (Thuật toán SCALE_SMOOTH giúp ảnh giữ độ nét, không bị răng cưa)
                int iconSize = 24;
                Image scaledImage = originalIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);

                btn.setIcon(new ImageIcon(scaledImage));
            }
        }

        // Căn chỉnh giao diện
        btn.setBackground(bgColor);
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);

        // Hiệu ứng hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    private void xuatExcel() {
        // 1. Hiển thị hộp thoại cho người dùng chọn nơi lưu file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file Excel");

        // Chỉ cho phép lưu định dạng .xlsx
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Tự động thêm đuôi .xlsx nếu người dùng quên gõ
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            // 2. Bắt đầu tạo file Excel bằng Apache POI
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("DanhSachKhachHang");

                // Tạo dòng đầu tiên (Header)
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < table.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(table.getColumnName(i));
                }

                // 3. Đổ dữ liệu từ JTable vào các dòng tiếp theo
                for (int i = 0; i < table.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1); // +1 vì dòng 0 là Header rồi
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        Cell cell = row.createCell(j);
                        Object value = table.getValueAt(i, j);
                        if (value != null) {
                            cell.setCellValue(value.toString()); // Ép tất cả về chuỗi cho an toàn
                        }
                    }
                }

                // Tự động co giãn cột cho đẹp (Auto-size)
                for (int i = 0; i < table.getColumnCount(); i++) {
                    sheet.autoSizeColumn(i);
                }

                // 4. Ghi ra file vật lý
                try (FileOutputStream out = new FileOutputStream(filePath)) {
                    workbook.write(out);
                }

                JOptionPane.showMessageDialog(this, "Xuất dữ liệu ra file Excel thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi xuất file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void nhapExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file Excel để nhập dữ liệu");

        // Hỗ trợ cả 2 định dạng .xls và .xlsx
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", "xls", "xlsx");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();

            int successCount = 0;
            int errorCount = 0;
            StringBuilder errorDetails = new StringBuilder();

            // Sử dụng WorkbookFactory để tự động nhận diện xls hay xlsx
            try (Workbook workbook = WorkbookFactory.create(fileToOpen)) {
                Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên

                // Vòng lặp đọc từng dòng (Bắt đầu từ 1 để bỏ qua dòng Header tiêu đề)
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue; // Bỏ qua dòng trống

                    try {
                        // Giả định cấu trúc file Excel giống file Xuất:
                        // Cột 0: Mã KH (Bỏ qua vì Database tự tăng)
                        // Cột 1: Tên KH | Cột 2: SĐT | Cột 3: Ngày sinh | Cột 4: Điểm | Cột 5: Hạng

                        String hoTen = getCellValueAsString(row.getCell(1));
                        String sdt = getCellValueAsString(row.getCell(2));
                        String ngaySinhStr = getCellValueAsString(row.getCell(3));

                        // Nếu dòng này không có tên hoặc SĐT thì bỏ qua
                        if (hoTen.isEmpty() || sdt.isEmpty()) {
                            continue;
                        }

                        // Kiểm tra trùng Số Điện Thoại trước khi thêm
                        if (khBUS.checkTrungSDT(sdt)) {
                            errorCount++;
                            errorDetails.append("- Dòng ").append(i + 1).append(": SĐT ").append(sdt).append(" đã tồn tại.\n");
                            continue; // Bỏ qua dòng này, đọc dòng tiếp theo
                        }

                        // Xử lý Ngày sinh (Bắt lỗi nếu nhập sai định dạng)
                        LocalDate ngaySinh = LocalDate.now(); // Mặc định là hôm nay nếu lỗi
                        try {
                            // Cố gắng parse ngày sinh (Hỗ trợ định dạng yyyy-MM-dd)
                            ngaySinh = LocalDate.parse(ngaySinhStr);
                        } catch (Exception ex) {
                            System.out.println("Dòng " + (i+1) + " sai định dạng ngày, lấy ngày mặc định.");
                        }

                        // Tạo đối tượng DTO và gọi BUS thêm vào DB
                        KhachHangDTO kh = new KhachHangDTO(0, hoTen, sdt, ngaySinh, 0, "Thành viên mới");
                        if (khBUS.add(kh)) {
                            successCount++;
                        } else {
                            errorCount++;
                        }

                    } catch (Exception ex) {
                        errorCount++;
                        System.out.println("Lỗi đọc dòng " + (i+1) + ": " + ex.getMessage());
                    }
                }

                // Load lại bảng sau khi nhập xong
                loadDataToTable();

                // Hiển thị thông báo tổng kết
                String thongBao = "Nhập dữ liệu hoàn tất!\n"
                        + "- Thành công: " + successCount + " khách hàng.\n"
                        + "- Thất bại / Bị trùng: " + errorCount + " khách hàng.\n";
                if (errorCount > 0) {
                    thongBao += "\nChi tiết lỗi:\n" + errorDetails.toString();
                    JOptionPane.showMessageDialog(this, thongBao, "Kết quả nhập Excel", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, thongBao, "Thành công", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Không thể đọc file Excel. Lỗi: " + ex.getMessage(), "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Hàm tiện ích: Ép kiểu mọi ô trong Excel về Chuỗi (String) để không bị lỗi
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC:
                // Xử lý nếu file Excel lưu SĐT dưới dạng số (ví dụ 912345678 -> 0912345678)
                long num = (long) cell.getNumericCellValue();
                return "0" + String.valueOf(num);
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }

    public JPanel getPanel() {
        return this;
    }

    public JPanel getPanelDisable() {
        return this;
    }
}