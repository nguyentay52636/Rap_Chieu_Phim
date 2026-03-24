package org.example.GUI.Components.FormCustomer;

import com.formdev.flatlaf.FlatClientProperties;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.BUS.KhachHangBUS;
import org.example.DTO.KhachHangDTO;
import org.example.UltisTable.TableUtils;

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
        setBackground(UIManager.getColor("Panel.background"));

        JPanel up = new JPanel();
        up.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        up.setBackground(UIManager.getColor("Panel.background"));
        up.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")), "Quản Lý Khách Hàng", TitledBorder.LEFT, TitledBorder.TOP));

        btnView = createStyledButton("Xem", new Color(102, 187, 106), Color.WHITE, "/org/example/GUI/resources/images/view.png");
        btnView.addActionListener(e -> actionXemChiTiet());
        up.add(btnView);

        btnEdit = createStyledButton("Sửa", new Color(255, 193, 7), Color.WHITE, "/org/example/GUI/resources/images/editing.png");
        btnEdit.addActionListener(e -> actionSuaThongTin());
        up.add(btnEdit);

        btnDelete = createStyledButton("Xóa", new Color(220, 53, 69), Color.WHITE, "/org/example/GUI/resources/images/bin.png");
        btnDelete.addActionListener(e -> actionXoaKhachHang());
        up.add(btnDelete);

        btnAdd = createStyledButton("Thêm", new Color(0, 123, 255), Color.WHITE, "/org/example/GUI/resources/images/plus.png");
        btnAdd.addActionListener(e -> actionThemKhachHang());
        up.add(btnAdd);

        btnNhap = createStyledButton("Nhập Excel", new Color(153, 102, 255), Color.WHITE, "/org/example/GUI/resources/images/icons8_ms_excel_30px.png");
        btnNhap.addActionListener(e -> nhapExcel());
        up.add(btnNhap);

        btnXuat = createStyledButton("Xuất Excel", new Color(153, 102, 255), Color.WHITE, "/org/example/GUI/resources/images/icons8_ms_excel_30px.png");
        btnXuat.addActionListener(e -> xuatExcel());
        up.add(btnXuat);

        JPanel timkiem = new JPanel();
        timkiem.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        timkiem.setBackground(UIManager.getColor("Panel.background"));

        String[] options = { "Mã KH", "Tên KH", "SĐT" };
        comboBox = new JComboBox<>(options);
        comboBox.setPreferredSize(new Dimension(100, 40));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.addActionListener(e -> txtTimKiem.setText(""));

        txtTimKiem = new JTextField(20);
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Nhập từ khóa cần tìm...");
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTimKiem.setPreferredSize(new Dimension(200, 40));
        txtTimKiem.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
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
                String tuKhoa = txtTimKiem.getText();
                String tieuChiDB = "";
                switch (tieuChiUI) {
                    case "Mã KH": tieuChiDB = "MaKH"; break;
                    case "Tên KH": tieuChiDB = "HoTen"; break;
                    case "SĐT": tieuChiDB = "SDT"; break;
                }
                List<KhachHangDTO> ketQua = khBUS.search(tieuChiDB, tuKhoa);
                loadDataToTable(ketQua);
            }
        });

        JPanel comTk = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        comTk.setBackground(UIManager.getColor("Panel.background"));
        comTk.add(comboBox);
        comTk.add(txtTimKiem);

        btnFilter=createSquareButton("/org/example/GUI/resources/images/filter_icon1.png", 40, new Color(100, 181, 246));
        btnFilter.addActionListener(e -> {
            if (filterDialog == null) {
                JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
                filterDialog = new AdvancedSearchCustomerDialog(owner, this, khBUS);
            }
            SwingUtilities.updateComponentTreeUI(filterDialog);
            filterDialog.clearData();
            filterDialog.setVisible(true);
        });
        comTk.add(btnFilter);

        comTk.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")), "Tìm kiếm"));
        timkiem.add(comTk);

        btnLamMoi = createStyledButton("Làm mới", new Color(100, 181, 246), Color.WHITE, "/org/example/GUI/resources/images/icons8_data_backup_30px.png");
        btnLamMoi.addActionListener(e ->{
            txtTimKiem.setText("");
            comboBox.setSelectedIndex(0);
            if(filterDialog != null) filterDialog.clearData();
            if (table.getRowSorter() != null) table.getRowSorter().setSortKeys(null);
            loadDataToTable();
        });
        timkiem.add(btnLamMoi);

        JPanel chucNang = new JPanel(new GridLayout(2, 1, 0, 10));
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
                    Color bg = UIManager.getColor("Table.background");
                    if (bg != null) {
                        if (row % 2 == 0) {
                            c.setBackground(bg);
                        } else {
                            int r = bg.getRed(), g = bg.getGreen(), b = bg.getBlue();
                            int offset = (r < 128) ? 12 : -12;
                            c.setBackground(new Color(
                                    Math.max(0, Math.min(255, r + offset)),
                                    Math.max(0, Math.min(255, g + offset)),
                                    Math.max(0, Math.min(255, b + offset))
                            ));
                        }
                    }
                }
                return c;
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getRowCount() == 0) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 28));
                    g2d.setColor(new Color(180, 180, 180));
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
        table.setGridColor(UIManager.getColor("Table.gridColor"));
        table.setShowGrid(true);

        model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Integer.class;
                    case 3: return java.time.LocalDate.class;
                    case 4: return Integer.class;
                    default: return String.class;
                }
            }
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        model.addColumn("Mã KH");
        model.addColumn("Tên KH");
        model.addColumn("SĐT");
        model.addColumn("Ngày sinh");
        model.addColumn("Điểm tích lũy");
        model.addColumn("Hạng thành viên");
        table.setModel(model);

        javax.swing.table.TableRowSorter<DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(model);
        table.setRowSorter(sorter);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        table.setSelectionBackground(new Color(66, 103, 178));
        table.setSelectionForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // ========================================================
        // MENU CHUỘT PHẢI (Không Icon + Đồng bộ Sáng/Tối)
        // ========================================================
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem itemXem = new JMenuItem("Xem chi tiết");
        itemXem.addActionListener(e -> actionXemChiTiet());

        JMenuItem itemThem = new JMenuItem("Thêm khách hàng");
        itemThem.addActionListener(e -> actionThemKhachHang());

        JMenuItem itemSua = new JMenuItem("Sửa thông tin");
        itemSua.addActionListener(e -> actionSuaThongTin());

        JMenuItem itemXoa = new JMenuItem("Xóa khách hàng");
        itemXoa.addActionListener(e -> actionXoaKhachHang());

        popupMenu.add(itemXem);
        popupMenu.add(itemThem);
        popupMenu.add(itemSua);
        popupMenu.addSeparator(); // Đường phân cách
        popupMenu.add(itemXoa);

        table.addMouseListener(new MouseAdapter() {
            private void handlePopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    // ÉP CẬP NHẬT GIAO DIỆN THEO THEME SÁNG/TỐI HIỆN TẠI
                    SwingUtilities.updateComponentTreeUI(popupMenu);

                    int row = table.rowAtPoint(e.getPoint());

                    if (row >= 0 && !table.isRowSelected(row)) {
                        table.setRowSelectionInterval(row, row);
                    }

                    int selectedCount = table.getSelectedRowCount();

                    if (selectedCount == 1) {
                        itemXem.setVisible(true);
                        itemThem.setVisible(true);
                        itemSua.setVisible(true);
                        itemXoa.setVisible(true);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    } else if (selectedCount > 1) {
                        itemXem.setVisible(false);
                        itemThem.setVisible(false);
                        itemSua.setVisible(false);
                        itemXoa.setVisible(true);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    } else {
                        itemXem.setVisible(false);
                        itemSua.setVisible(false);
                        itemXoa.setVisible(false);
                        itemThem.setVisible(true);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) { handlePopup(e); }

            @Override
            public void mouseReleased(MouseEvent e) { handlePopup(e); }

            @Override
            public void mouseClicked(MouseEvent evt) {
                if(SwingUtilities.isLeftMouseButton(evt)) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        String maKH = table.getValueAt(row, 0).toString();
                        String tenKH = table.getValueAt(row, 1).toString();
                        System.out.println("Bạn vừa chọn Khách hàng: " + maKH + " - " + tenKH);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));

        add(chucNang, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadDataToTable();
    }

    // =========================================================================
    // CÁC HÀM XỬ LÝ SỰ KIỆN
    // =========================================================================

    private void actionXemChiTiet() {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 khách hàng để xem hồ sơ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        } else if (selectedRows.length > 1) {
            JOptionPane.showMessageDialog(this, "Chỉ được xem hồ sơ của 1 khách hàng mỗi lần!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRows[0]);
        int maKH = (int) model.getValueAt(modelRow, 0);
        String hoTen = (String) model.getValueAt(modelRow, 1);
        String sdt = (String) model.getValueAt(modelRow, 2);
        java.time.LocalDate ngaySinh = (java.time.LocalDate) model.getValueAt(modelRow, 3);
        int diem = (int) model.getValueAt(modelRow, 4);
        String hang = (String) model.getValueAt(modelRow, 5);

        KhachHangDTO kh = new KhachHangDTO(maKH, hoTen, sdt, ngaySinh, diem, hang);

        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        ViewCustomerDialog viewDialog = new ViewCustomerDialog(owner, kh);
        viewDialog.setVisible(true);
    }

    private void actionThemKhachHang() {
        if (addDialog == null) {
            JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
            addDialog = new AddCustomerDialog(owner, this, khBUS);
        }
        SwingUtilities.updateComponentTreeUI(addDialog);
        addDialog.clearData();
        addDialog.setVisible(true);
    }

    private void actionSuaThongTin() {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 khách hàng để sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        } else if (selectedRows.length > 1) {
            JOptionPane.showMessageDialog(this, "Chỉ được chọn 1 khách hàng để sửa mỗi lần!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRows[0]);
        int maKH = (int) model.getValueAt(modelRow, 0);
        String hoTen = (String) model.getValueAt(modelRow, 1);
        String sdt = (String) model.getValueAt(modelRow, 2);
        LocalDate ngaySinh = (LocalDate) model.getValueAt(modelRow, 3);
        int diem = (int) model.getValueAt(modelRow, 4);
        String hang = (String) model.getValueAt(modelRow, 5);

        KhachHangDTO kh = new KhachHangDTO(maKH, hoTen, sdt, ngaySinh, diem, hang);

        if (editDialog == null) {
            JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
            editDialog = new EditCustomerDialog(owner, this, khBUS);
        }
        SwingUtilities.updateComponentTreeUI(editDialog);
        editDialog.loadData(kh);
        editDialog.setVisible(true);
    }

    private void actionXoaKhachHang() {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất 1 khách hàng để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

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

        Color errorColor = UIManager.getColor("Component.error.focusedBorderColor");
        if(errorColor == null) errorColor = Color.RED;

        previewTable.getTableHeader().setBackground(errorColor);
        previewTable.getTableHeader().setForeground(Color.WHITE);
        previewTable.setRowHeight(35);
        previewTable.setEnabled(false);
        previewTable.getTableHeader().setReorderingAllowed(false);
        previewTable.putClientProperty("JTable.showAlternateRowColors", true);
        previewTable.setShowGrid(true);
        previewTable.setGridColor(UIManager.getColor("Table.gridColor"));

        TableUtils.autoResizeColumns(previewTable);
        int tableHeight = Math.min(selectedRows.length * 35 + 35, 250) + 20;

        JScrollPane scrollPreview = new JScrollPane(previewTable);
        scrollPreview.setPreferredSize(new Dimension(600, tableHeight));

        JPanel panelConfirm = new JPanel(new BorderLayout(0, 10));
        panelConfirm.setOpaque(false);

        JLabel lblConfirm = new JLabel("CẢNH BÁO: Bạn có chắc chắn muốn XÓA VĨNH VIỄN " + selectedRows.length + " khách hàng này không?");
        lblConfirm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblConfirm.setForeground(errorColor);

        panelConfirm.add(lblConfirm, BorderLayout.NORTH);
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);
        wrapper.add(scrollPreview);
        panelConfirm.add(wrapper, BorderLayout.CENTER);

        int confirm = JOptionPane.showConfirmDialog(
                this, panelConfirm, "Xác nhận xóa dữ liệu",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
        );

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
            loadDataToTable();
        }
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
        model.setRowCount(0);
        for (KhachHangDTO kh : danhSachKH) {
            Object[] row = new Object[]{
                    kh.getMaKH(), kh.getHoTen(), kh.getSDT(), kh.getNgaySinh(), kh.getDiemTichLuy(), kh.getHangThanhVien()
            };
            model.addRow(row);
        }
    }

    public void loadDataToTable() {
        khBUS.refreshList();
        loadDataToTable(khBUS.getListKhachHang());
    }

    private JButton createSquareButton(String iconPath, int size, Color bgColor) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(size, size));
        btn.setMinimumSize(new Dimension(size, size));
        btn.setMaximumSize(new Dimension(size, size));

        if (iconPath != null) {
            java.net.URL imgURL = getClass().getResource(iconPath);
            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);
                int iconSize = 24;
                Image scaledImage = originalIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(scaledImage));
            }
        }

        btn.setBackground(bgColor);
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(bgColor.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(bgColor); }
        });
        return btn;
    }

    private void xuatExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file Excel");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".xlsx")) filePath += ".xlsx";

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("DanhSachKhachHang");
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < table.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(table.getColumnName(i));
                }

                for (int i = 0; i < table.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        Cell cell = row.createCell(j);
                        Object value = table.getValueAt(i, j);
                        if (value != null) cell.setCellValue(value.toString());
                    }
                }

                for (int i = 0; i < table.getColumnCount(); i++) sheet.autoSizeColumn(i);

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
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", "xls", "xlsx");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            int successCount = 0, errorCount = 0;
            StringBuilder errorDetails = new StringBuilder();

            try (Workbook workbook = WorkbookFactory.create(fileToOpen)) {
                Sheet sheet = workbook.getSheetAt(0);
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    try {
                        String hoTen = getCellValueAsString(row.getCell(1));
                        String sdt = getCellValueAsString(row.getCell(2));
                        String ngaySinhStr = getCellValueAsString(row.getCell(3));

                        if (hoTen.isEmpty() || sdt.isEmpty()) continue;

                        if (khBUS.checkTrungSDT(sdt)) {
                            errorCount++;
                            errorDetails.append("- Dòng ").append(i + 1).append(": SĐT ").append(sdt).append(" đã tồn tại.\n");
                            continue;
                        }

                        LocalDate ngaySinh = LocalDate.now();
                        try { ngaySinh = LocalDate.parse(ngaySinhStr); }
                        catch (Exception ex) { System.out.println("Dòng " + (i+1) + " sai định dạng ngày."); }

                        KhachHangDTO kh = new KhachHangDTO(0, hoTen, sdt, ngaySinh, 0, "Thành viên mới");
                        if (khBUS.add(kh)) successCount++;
                        else errorCount++;

                    } catch (Exception ex) {
                        errorCount++;
                        System.out.println("Lỗi đọc dòng " + (i+1) + ": " + ex.getMessage());
                    }
                }

                loadDataToTable();
                String thongBao = "Nhập dữ liệu hoàn tất!\n- Thành công: " + successCount + "\n- Thất bại: " + errorCount + "\n";
                if (errorCount > 0) {
                    thongBao += "\nChi tiết lỗi:\n" + errorDetails.toString();
                    JOptionPane.showMessageDialog(this, thongBao, "Kết quả", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, thongBao, "Thành công", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Không thể đọc file Excel. Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC: return "0" + String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }

    public JPanel getPanel() { return this; }
    public JPanel getPanelDisable() { return this; }
}