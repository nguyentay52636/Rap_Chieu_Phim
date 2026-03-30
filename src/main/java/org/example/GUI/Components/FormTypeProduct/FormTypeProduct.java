package org.example.GUI.Components.FormTypeProduct;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import org.example.BUS.TheLoaiPhimBUS;
import org.example.DTO.TheLoaiPhimDTO;

public class FormTypeProduct extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private final TheLoaiPhimBUS theLoaiPhimBUS = new TheLoaiPhimBUS();
    private JTextField txtSearch;
    private JComboBox<String> cbSearch;

    public FormTypeProduct() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        JPanel pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.setBackground(Color.WHITE);
        pnlNorth.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Quản Lý Thể Loại Phim", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)));

        // 1. Dàn nút chức năng bên trái
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlButtons.setBackground(Color.WHITE);

        JButton btnView = createButton("Xem", new Color(0, 123, 255), Color.WHITE);
        JButton btnEdit = createButton("Sửa", new Color(230, 230, 230), Color.BLACK);
        JButton btnDelete = createButton("Xóa", new Color(220, 53, 69), Color.WHITE);
        JButton btnAdd = createButton("Thêm", new Color(40, 167, 69), Color.WHITE);

        pnlButtons.add(btnView);
        pnlButtons.add(btnEdit);
        pnlButtons.add(btnDelete);
        pnlButtons.add(btnAdd);

        // 2. Khu vực tìm kiếm bên phải
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlSearch.setBackground(Color.WHITE);

        cbSearch = new JComboBox<>(new String[] { "Tất cả", "Mã Thể Loại", "Tên Thể Loại" });
        cbSearch.setPreferredSize(new Dimension(130, 35));
        cbSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 35));

        JButton btnRefresh = createButton("Làm mới", new Color(230, 230, 230), Color.BLACK);

        pnlSearch.add(cbSearch);
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnRefresh);

        pnlNorth.add(pnlButtons, BorderLayout.WEST);
        pnlNorth.add(pnlSearch, BorderLayout.EAST);

        // ================== KHU VỰC BẢNG (TABLE) ==================
        String[] columns = { "Mã Thể Loại Phim", "Tên Thể Loại Phim" };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(66, 103, 178));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(35);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(pnlNorth, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // ================== XỬ LÝ SỰ KIỆN ==================
        loadData();

        btnAdd.addActionListener(e -> addType());
        btnEdit.addActionListener(e -> editType());
        btnDelete.addActionListener(e -> deleteType());
        btnView.addActionListener(e -> viewDetails());
        btnRefresh.addActionListener(e -> {
            theLoaiPhimBUS.refreshList();
            loadData();
        });

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchType();
            }
        });
    }

    private void loadData() {
        model.setRowCount(0);
        List<TheLoaiPhimDTO> list = theLoaiPhimBUS.getList();
        for (TheLoaiPhimDTO tl : list) {
            model.addRow(new Object[] { tl.getMaLoaiPhim(), tl.getTenLoaiPhim() });
        }
    }

    private void addType() {
        String name = JOptionPane.showInputDialog(this, "Nhập tên thể loại phim mới:", "Thêm Thể Loại",
                JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            TheLoaiPhimDTO tl = new TheLoaiPhimDTO();
            tl.setTenLoaiPhim(name.trim());
            if (theLoaiPhimBUS.add(tl)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editType() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thể loại cần sửa!");
            return;
        }
        int id = (int) table.getValueAt(row, 0);
        String oldName = (String) table.getValueAt(row, 1);

        String newName = JOptionPane.showInputDialog(this, "Đổi tên thể loại:", oldName);
        if (newName != null && !newName.trim().isEmpty() && !newName.equals(oldName)) {
            TheLoaiPhimDTO tl = new TheLoaiPhimDTO(id, newName.trim());
            if (theLoaiPhimBUS.update(tl)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteType() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thể loại cần xóa!");
            return;
        }
        int id = (int) table.getValueAt(row, 0);
        String name = (String) table.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa thể loại: " + name + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (theLoaiPhimBUS.delete(id)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Thể loại này có thể đang được sử dụng.", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewDetails() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thể loại để xem!");
            return;
        }
        Object id = table.getValueAt(row, 0);
        Object name = table.getValueAt(row, 1);
        JOptionPane.showMessageDialog(this, "Mã Thể Loại: " + id + "\nTên Thể Loại: " + name, "Chi Tiết Thể Loại",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchType() {
        String keyword = txtSearch.getText().toLowerCase().trim();
        String criteria = (String) cbSearch.getSelectedItem();
        model.setRowCount(0);

        List<TheLoaiPhimDTO> list = theLoaiPhimBUS.getList();
        for (TheLoaiPhimDTO tl : list) {
            boolean match = false;
            if (criteria.equals("Tất cả")) {
                match = String.valueOf(tl.getMaLoaiPhim()).contains(keyword)
                        || tl.getTenLoaiPhim().toLowerCase().contains(keyword);
            } else if (criteria.equals("Mã Thể Loại")) {
                match = String.valueOf(tl.getMaLoaiPhim()).contains(keyword);
            } else {
                match = tl.getTenLoaiPhim().toLowerCase().contains(keyword);
            }

            if (match) {
                model.addRow(new Object[] { tl.getMaLoaiPhim(), tl.getTenLoaiPhim() });
            }
        }
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        return btn;
    }
}