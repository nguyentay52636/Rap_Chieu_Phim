package org.example.GUI.Components.FormProduct;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import org.example.BUS.SanPhamBUS;
import org.example.DTO.SanPhamDTO;

public class FormProduct extends JPanel {

    private static final long serialVersionUID = 1L;
    private final SanPhamBUS sanPhamBUS = new SanPhamBUS();
    private DefaultTableModel model;
    private JTable tableSanPham;
    // Khu vực hiển thị chi tiết
    private JLabel imageLabel;
    private JTextField txtMaSPDetail;
    private JTextField txtTenSPDetail;
    private JTextField txtGiaBanDetail;
    private JTextField txtKichThuocDetail;
    private JTextField txtSoLuongDetail;
    private JTextField txtTrangThaiDetail;
    private final DecimalFormat priceFormatter = new DecimalFormat("#,##0");
    // Search / Filter
    private JComboBox<String> comboBoxTimKiem;
    private JTextField txtTimKiem;
    private JComboBox<String> comboTrangThaiFilter;

    public FormProduct() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 250));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 250));
        topPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Quản Lý Sản Phẩm",
                TitledBorder.LEFT,
                TitledBorder.TOP));

        // Button bar (không gắn chức năng)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttonPanel.setOpaque(false);

        JButton btnView = createStyledButton("Xem");
        JButton btnEdit = createStyledButton("Sửa");
        JButton btnDelete = createStyledButton("Xóa");
        JButton btnAdd = createStyledButton("Thêm");

        // Icon và màu cho các nút (giống FormTypeProduct)
        btnAdd.setBackground(new Color(40, 167, 69)); // xanh lá
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setIcon(loadImageIcon("/org/example/GUI/resources/images/plus.png"));

        btnView.setBackground(new Color(0, 123, 255)); // xanh dương
        btnView.setForeground(Color.WHITE);
        btnView.setIcon(loadImageIcon("/org/example/GUI/resources/images/view.png"));

        btnEdit.setBackground(new Color(255, 193, 7)); // vàng/cam
        btnEdit.setForeground(Color.BLACK);
        btnEdit.setIcon(loadImageIcon("/org/example/GUI/resources/images/editing.png"));

        btnDelete.setIcon(loadImageIcon("/org/example/GUI/resources/images/bin.png"));

        btnView.addActionListener(e -> onView());
        btnEdit.addActionListener(e -> onEdit());
        btnDelete.addActionListener(e -> onDelete());

        btnAdd.addActionListener(e -> {
            AddProductDialog dlg = new AddProductDialog(FormProduct.this);
            dlg.setVisible(true);
        });

        buttonPanel.add(btnView);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnAdd);

        // Thanh tìm kiếm (canh thẳng hàng với các nút)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setOpaque(false);

        String[] options = { "Tất cả", "Mã SP", "Tên sản phẩm", "Giá bán", "Kích thước", "Số lượng", "Trạng thái" };
        comboBoxTimKiem = new JComboBox<>(options);
        comboBoxTimKiem.setPreferredSize(new Dimension(100, 40));
        comboBoxTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txtTimKiem = new JTextField(20);
        txtTimKiem.setPreferredSize(new Dimension(200, 40));
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTimKiem.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));

        comboTrangThaiFilter = new JComboBox<>(new String[] { "Tất cả", "Đang bán", "Không bán" });
        comboTrangThaiFilter.setPreferredSize(new Dimension(110, 40));
        comboTrangThaiFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnLamMoi = createStyledButton("Làm mới");
        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            comboBoxTimKiem.setSelectedIndex(0);
            comboTrangThaiFilter.setSelectedIndex(0);
            refreshTable();
        });

        searchPanel.add(comboBoxTimKiem);
        searchPanel.add(txtTimKiem);
        searchPanel.add(comboTrangThaiFilter);
        searchPanel.add(btnLamMoi);

        topPanel.add(buttonPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        // Bảng dữ liệu sản phẩm, load từ BUS
        String[] columnNames = {
                "Mã SP", "Tên sản phẩm", "Hình ảnh",
                "Giá bán", "Kích thước", "Số lượng", "Trạng thái"
        };
        model = new DefaultTableModel(columnNames, 0);
        tableSanPham = new JTable(model);

        tableSanPham.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableSanPham.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableSanPham.getTableHeader().setBackground(new Color(66, 103, 178));
        tableSanPham.getTableHeader().setForeground(Color.WHITE);
        tableSanPham.setRowHeight(40);

        // Bấm vào 1 dòng để xem chi tiết sản phẩm
        tableSanPham.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedProductDetail();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableSanPham);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Panel trung tâm chứa bảng + khu vực chi tiết ở dưới
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel chính cho phần chi tiết
        JPanel detailMainPanel = new JPanel(new BorderLayout(20, 10));
        detailMainPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Chi tiết sản phẩm",
                TitledBorder.LEFT,
                TitledBorder.TOP));
        detailMainPanel.setBackground(new Color(245, 245, 250));

        // Panel bên trái: hình ảnh sản phẩm
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(220, 220));
        imagePanel.setBackground(Color.WHITE);
        // Border dày ~1.5px màu đen
        imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // Panel bên phải: thông tin sản phẩm (label + text field)
        JPanel infoPanel = new JPanel(new GridLayout(6, 2, 8, 6));
        infoPanel.setOpaque(false);
        Font boldFont = new Font("Segoe UI", Font.BOLD, 14);

        txtMaSPDetail = createDetailField();
        txtTenSPDetail = createDetailField();
        txtGiaBanDetail = createDetailField();
        txtKichThuocDetail = createDetailField();
        txtSoLuongDetail = createDetailField();
        txtTrangThaiDetail = createDetailField();

        addDetailRow(infoPanel, "Mã sản phẩm:", txtMaSPDetail, boldFont);
        addDetailRow(infoPanel, "Tên sản phẩm:", txtTenSPDetail, boldFont);
        addDetailRow(infoPanel, "Giá bán:", txtGiaBanDetail, boldFont);
        addDetailRow(infoPanel, "Kích thước:", txtKichThuocDetail, boldFont);
        addDetailRow(infoPanel, "Số lượng:", txtSoLuongDetail, boldFont);
        addDetailRow(infoPanel, "Trạng thái:", txtTrangThaiDetail, boldFont);

        detailMainPanel.add(imagePanel, BorderLayout.WEST);
        detailMainPanel.add(infoPanel, BorderLayout.CENTER);

        centerPanel.add(detailMainPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // Load dữ liệu sản phẩm lần đầu
        refreshTable();
        installSearchHandlers();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(new Color(230, 230, 235));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
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

    public void refreshTable() {
        sanPhamBUS.refreshList();
        applyFilterAndRender();
    }

    private void loadTableData() {
        model.setRowCount(0);
        for (SanPhamDTO sp : sanPhamBUS.getList()) {
            model.addRow(new Object[]{
                    sp.getMaSanPham(),
                    sp.getTenSanPham(),
                    sp.getHinhAnh(),
                    sp.getGiaBan(),
                    sp.getKichThuoc(),
                    sp.getSoLuong(),
                    sp.getTrangThai()
            });
        }
    }

    private void installSearchHandlers() {
        if (txtTimKiem != null) {
            txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    applyFilterAndRender();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    applyFilterAndRender();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    applyFilterAndRender();
                }
            });
        }

        if (comboBoxTimKiem != null) {
            comboBoxTimKiem.addActionListener(e -> applyFilterAndRender());
        }
        if (comboTrangThaiFilter != null) {
            comboTrangThaiFilter.addActionListener(e -> applyFilterAndRender());
        }
    }

    private void applyFilterAndRender() {
        String keyword = txtTimKiem != null ? txtTimKiem.getText().trim().toLowerCase() : "";
        String field = comboBoxTimKiem != null ? String.valueOf(comboBoxTimKiem.getSelectedItem()) : "Tất cả";
        String statusFilter = comboTrangThaiFilter != null ? String.valueOf(comboTrangThaiFilter.getSelectedItem()) : "Tất cả";

        model.setRowCount(0);
        for (SanPhamDTO sp : sanPhamBUS.getList()) {
            if (!matchesStatus(sp, statusFilter)) {
                continue;
            }
            if (!matchesKeyword(sp, field, keyword)) {
                continue;
            }
            model.addRow(new Object[]{
                    sp.getMaSanPham(),
                    sp.getTenSanPham(),
                    sp.getHinhAnh(),
                    sp.getGiaBan(),
                    sp.getKichThuoc(),
                    sp.getSoLuong(),
                    sp.getTrangThai()
            });
        }
    }

    private boolean matchesStatus(SanPhamDTO sp, String statusFilter) {
        if (statusFilter == null || statusFilter.equalsIgnoreCase("Tất cả")) {
            return true;
        }
        String tt = sp.getTrangThai() == null ? "" : sp.getTrangThai().trim().toLowerCase();
        // Map: "Đang bán" -> any contains "đang", "ban"; "Không bán" -> contains "không" or "ngừng"
        if (statusFilter.equalsIgnoreCase("Đang bán")) {
            return tt.contains("đang") || tt.contains("dang") || tt.contains("bán") || tt.contains("ban");
        }
        if (statusFilter.equalsIgnoreCase("Không bán")) {
            return tt.contains("không") || tt.contains("khong") || tt.contains("ngừng") || tt.contains("ngung")
                    || tt.contains("khong ban") || tt.contains("ngung ban");
        }
        return true;
    }

    private boolean matchesKeyword(SanPhamDTO sp, String field, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return true;
        }
        String kw = keyword.toLowerCase();

        String ma = String.valueOf(sp.getMaSanPham());
        String ten = sp.getTenSanPham() == null ? "" : sp.getTenSanPham().toLowerCase();
        String hinh = sp.getHinhAnh() == null ? "" : sp.getHinhAnh().toLowerCase();
        String gia = String.valueOf(sp.getGiaBan());
        String kt = sp.getKichThuoc() == null ? "" : sp.getKichThuoc().toLowerCase();
        String sl = String.valueOf(sp.getSoLuong());
        String tt = sp.getTrangThai() == null ? "" : sp.getTrangThai().toLowerCase();

        switch (field) {
            case "Mã SP":
                return ma.contains(kw);
            case "Tên sản phẩm":
                return ten.contains(kw);
            case "Giá bán":
                return gia.contains(kw);
            case "Kích thước":
                return kt.contains(kw);
            case "Số lượng":
                return sl.contains(kw);
            case "Trạng thái":
                return tt.contains(kw);
            case "Tất cả":
            default:
                return ma.contains(kw) || ten.contains(kw) || hinh.contains(kw) || gia.contains(kw) || kt.contains(kw)
                        || sl.contains(kw) || tt.contains(kw);
        }
    }

    // Tạo text field chi tiết (chỉ đọc)
    private JTextField createDetailField() {
        JTextField tf = new JTextField();
        tf.setEditable(false);
        tf.setHorizontalAlignment(SwingConstants.LEFT);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(2, 6, 2, 6)));
        return tf;
    }

    private void addDetailRow(JPanel panel, String labelText, JTextField field, Font labelFont) {
        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setFont(labelFont);
        panel.add(label);
        panel.add(field);
    }

    // Hiển thị chi tiết sản phẩm khi chọn 1 dòng trong bảng (đổ vào khu vực bên dưới)
    private void showSelectedProductDetail() {
        int selectedRow = tableSanPham.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        Object maSpValue = tableSanPham.getValueAt(selectedRow, 0);
        if (maSpValue == null) {
            return;
        }

        int maSp;
        try {
            maSp = Integer.parseInt(maSpValue.toString());
        } catch (NumberFormatException ex) {
            return;
        }

        SanPhamDTO selected = null;
        for (SanPhamDTO sp : sanPhamBUS.getList()) {
            if (sp.getMaSanPham() == maSp) {
                selected = sp;
                break;
            }
        }

        if (selected == null) {
            clearDetailPanel();
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm.", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Cập nhật text field
        txtMaSPDetail.setText(String.valueOf(selected.getMaSanPham()));
        txtTenSPDetail.setText(selected.getTenSanPham());
        txtGiaBanDetail.setText(priceFormatter.format(selected.getGiaBan()) + " VND");
        txtKichThuocDetail.setText(selected.getKichThuoc());
        txtSoLuongDetail.setText(String.valueOf(selected.getSoLuong()));
        txtTrangThaiDetail.setText(selected.getTrangThai());

        // Cập nhật hình ảnh bên trái
        imageLabel.setIcon(null);
        imageLabel.setText("");
        if (selected.getHinhAnh() != null && !selected.getHinhAnh().isEmpty()) {
            String fileAnh = selected.getHinhAnh();
            ImageIcon icon = loadProductImageIcon(fileAnh);
            if (icon != null) {
                Image img = icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
            } else {
                imageLabel.setText("Không tìm thấy ảnh: " + fileAnh);
            }
        }
    }

    private SanPhamDTO getSelectedSanPham() {
        int selectedRow = tableSanPham.getSelectedRow();
        if (selectedRow == -1) {
            return null;
        }
        Object maSpValue = tableSanPham.getValueAt(selectedRow, 0);
        if (maSpValue == null) {
            return null;
        }
        int maSp;
        try {
            maSp = Integer.parseInt(maSpValue.toString());
        } catch (NumberFormatException ex) {
            return null;
        }
        for (SanPhamDTO sp : sanPhamBUS.getList()) {
            if (sp.getMaSanPham() == maSp) {
                return sp;
            }
        }
        return null;
    }

    private void onView() {
        SanPhamDTO sp = getSelectedSanPham();
        if (sp == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xem!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        DetailProductDialog dlg = new DetailProductDialog(this, sp);
        dlg.setVisible(true);
    }

    private void onEdit() {
        SanPhamDTO sp = getSelectedSanPham();
        if (sp == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để sửa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        EditProductDialog dlg = new EditProductDialog(this, sanPhamBUS, sp);
        dlg.setVisible(true);
    }

    private void onDelete() {
        SanPhamDTO sp = getSelectedSanPham();
        if (sp == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa sản phẩm \"" + sp.getTenSanPham() + "\" (Mã: " + sp.getMaSanPham() + ")?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean ok = sanPhamBUS.delete(sp.getMaSanPham());
        if (ok) {
            JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            clearDetailPanel();
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa sản phẩm thất bại. Kiểm tra database.", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Tải ảnh sản phẩm: ưu tiên classpath, sau đó thư mục imageTopic cạnh thư mục chạy app. */
    private ImageIcon loadProductImageIcon(String fileAnh) {
        if (fileAnh == null || fileAnh.isEmpty()) return null;
        java.net.URL imgURL = getClass().getResource("/org/example/GUI/resources/imageTopic/" + fileAnh);
        if (imgURL != null) return new ImageIcon(imgURL);
        File file = new File(System.getProperty("user.dir"), "imageTopic" + File.separator + fileAnh);
        if (file.isFile()) return new ImageIcon(file.getAbsolutePath());
        return null;
    }

    private void clearDetailPanel() {
        txtMaSPDetail.setText("");
        txtTenSPDetail.setText("");
        txtGiaBanDetail.setText("");
        txtKichThuocDetail.setText("");
        txtSoLuongDetail.setText("");
        txtTrangThaiDetail.setText("");
        imageLabel.setIcon(null);
        imageLabel.setText("");
    }
}

