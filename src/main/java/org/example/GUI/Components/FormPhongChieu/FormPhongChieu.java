package org.example.GUI.Components.FormPhongChieu;

import org.example.BUS.LoaiGheBUS;
import org.example.BUS.PhongChieuBUS;
import org.example.DTO.PhongChieuDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class FormPhongChieu extends JPanel {
    private final PhongChieuBUS pcBUS = new PhongChieuBUS();
    private final LoaiGheBUS lgBUS = new LoaiGheBUS();
    private final DefaultTableModel model;
    private final JTable tablePhongChieu;

    public FormPhongChieu() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Quản Lý Phòng Chiếu", TitledBorder.LEFT,
                TitledBorder.TOP));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttonPanel.setOpaque(false);
        JButton btnAdd = createStyledButton("Thêm", new Color(40, 167, 69), Color.WHITE);
        JButton btnEdit = createStyledButton("Sửa", new Color(255, 193, 7), Color.BLACK);
        JButton btnDelete = createStyledButton("Xóa", new Color(220, 53, 69), Color.WHITE);
        JButton btnView = createStyledButton("Xem", new Color(0, 123, 255), Color.WHITE);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnView);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Tìm:"));

        JComboBox<String> optLoaiTimKiem = createStyledComboBox(
                new String[] { "Mã", "Tên", "Loại Phòng", "Số Hàng", "Ghế/Hàng" });
        searchPanel.add(optLoaiTimKiem);

        JPanel inputCardPanel = new JPanel(new CardLayout());
        inputCardPanel.setOpaque(false);
        JTextField txtSearch = createStyledTextField();
        inputCardPanel.add(txtSearch, "TEXT");

        JComboBox<String> optSearchLoai = createStyledComboBox(new String[] { "2D", "3D", "4DX", "IMAX" });
        optSearchLoai.setPreferredSize(new Dimension(150, 35));
        inputCardPanel.add(optSearchLoai, "COMBO");
        searchPanel.add(inputCardPanel);

        optLoaiTimKiem.addActionListener(e -> {
            CardLayout cl = (CardLayout) inputCardPanel.getLayout();
            cl.show(inputCardPanel, optLoaiTimKiem.getSelectedIndex() == 2 ? "COMBO" : "TEXT");
        });

        JButton btnSearch = createStyledButton("Tìm", new Color(24, 223, 140), Color.WHITE);
        JButton btnAdvSearch = createSquareButton("/org/example/GUI/resources/images/filter_icon1.png", 40, new Color(100, 181, 246));
        JButton btnRefresh = createSquareButton("/org/example/GUI/resources/images/reload.png", 40, new Color(100, 181, 246));
        searchPanel.add(btnSearch);
        searchPanel.add(btnAdvSearch);
        searchPanel.add(btnRefresh);

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnSearch.doClick();
                }
            }
        });

        topPanel.add(buttonPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        String[] columnNames = { "Mã Phòng", "Tên Phòng", "Loại Phòng", "Số Hàng", "Ghế/Hàng" };
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePhongChieu = new JTable(model);
        tablePhongChieu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablePhongChieu.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablePhongChieu.getTableHeader().setBackground(new Color(66, 103, 178));
        tablePhongChieu.getTableHeader().setForeground(Color.WHITE);
        tablePhongChieu.setRowHeight(40);

        JScrollPane scrollPane = new JScrollPane(tablePhongChieu);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // --- GỌI CÁC CLASS XỬ LÝ ---
        loadDataToTable();

        btnAdd.addActionListener(e -> new AddRoomDialog(this));
        btnEdit.addActionListener(e -> EditRoomDialog.open(this));
        btnView.addActionListener(e -> ViewRoomDialog.open(this));
        btnDelete.addActionListener(e -> DeleteRoomHandler.execute(this));

        btnSearch.addActionListener(e -> {
            int searchType = optLoaiTimKiem.getSelectedIndex();
            String keyword = (searchType == 2) ? (String) optSearchLoai.getSelectedItem() : txtSearch.getText();
            SearchRoomHandler.searchBasic(this, keyword, searchType);
        });
        btnAdvSearch.addActionListener(e -> SearchRoomHandler.openAdvancedSearchDialog(this));
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadDataToTable();
        });
    }

    // --- CÁC HÀM GETTER & HỖ TRỢ UI CHO CÁC CLASS KHÁC GỌI ---
    public PhongChieuBUS getPcBUS() {
        return pcBUS;
    }

    public LoaiGheBUS getLgBUS() {
        return lgBUS;
    }

    public JTable getTablePhongChieu() {
        return tablePhongChieu;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public void loadDataToTable() {
        loadDataToTable(pcBUS.getList());
    }

    public void loadDataToTable(List<PhongChieuDTO> list) {
        model.setRowCount(0);
        for (PhongChieuDTO pc : list) {
            model.addRow(new Object[] { pc.getMaPhong(), pc.getTenPhong(), pc.getLoaiPhong(), pc.getSoHang(),
                    pc.getSoGheMoiHang() });
        }
    }

    public JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(80, 35));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        return button;
    }

    public JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBackground(Color.WHITE);
        cb.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1, true));
        return cb;
    }

    public JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return lbl;
    }

    private JButton createSquareButton(String iconPath, int size, Color bgColor) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(size, size));
        if (iconPath != null) {
            java.net.URL imgURL = getClass().getResource(iconPath);
            if (imgURL != null) {
                btn.setIcon(
                        new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
            }
        }
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        return btn;
    }

    public JTextField createStyledTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBackground(Color.WHITE);
        txt.setForeground(new Color(33, 37, 41)); // Dark gray text for better readability

        // Combine a soft gray rounded border with inner padding (Margins)
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204), 1, true), // Outer: 1px rounded gray line
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Inner: 5px top/bottom, 10px left/right
        ));

        return txt;
    }

    public JTextField createStyledTextField(String text, int columns) {
        JTextField txt = new JTextField(text, columns); // Khởi tạo với giá trị truyền vào

        // Giữ nguyên toàn bộ style ở dưới
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBackground(Color.WHITE);
        txt.setForeground(new Color(33, 37, 41));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        return txt;
    }
}