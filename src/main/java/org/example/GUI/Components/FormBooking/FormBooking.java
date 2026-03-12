package org.example.GUI.Components.FormBooking;

import org.example.BUS.PhimBUS;
import org.example.DTO.PhimDTO;
import org.example.GUI.Components.FormBooking.components.ChiTietPhimDialog;
import org.example.GUI.Components.FormBooking.components.DatVeDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class FormBooking extends JPanel {

    private static final long serialVersionUID = 1L;
    private JComboBox<String> cbTheLoaiPhim;
    private JTable tablePhim;
    private DefaultTableModel modelPhim;
    private final PhimBUS phimBUS = new PhimBUS();
    private ArrayList<PhimDTO> listPhim;
    private JButton btnXemChiTiet;
    private JButton btnDatVe;
    private JButton btnLamMoi;
    private int selectedMaPhim = -1;
    private final int maKH;
    private final int maNV;

    public FormBooking(int maKH, int maNV) {
        this.maKH = maKH;
        this.maNV = maNV;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 250));

        JPanel up = new JPanel();
        up.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        up.setBackground(new Color(245, 245, 250));
        up.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        "Chọn Phim",
                        TitledBorder.LEFT,
                        TitledBorder.TOP
                )
        );

        JLabel lblTheLoai = new JLabel("Thể loại phim:");
        lblTheLoai.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTheLoai.setForeground(new Color(50, 50, 50));
        cbTheLoaiPhim = new JComboBox<>();
        cbTheLoaiPhim.setPreferredSize(new Dimension(200, 30));
        cbTheLoaiPhim.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbTheLoaiPhim.setBackground(Color.WHITE);
        loadTheLoaiPhim();
        cbTheLoaiPhim.addActionListener(e -> loadDanhSachPhim());

        up.add(lblTheLoai);
        up.add(cbTheLoaiPhim);

        btnLamMoi = createStyledButton("Làm mới", new Color(100, 181, 246), Color.WHITE,
                "/org/example/GUI/resources/images/icons8_data_backup_30px.png");
        btnLamMoi.addActionListener(e -> loadDanhSachPhim());
        up.add(btnLamMoi);

        btnXemChiTiet = createStyledButton("Xem chi tiết", new Color(102, 187, 106), Color.WHITE,
                "/org/example/GUI/resources/images/view.png");
        btnXemChiTiet.addActionListener(e -> {
            int selectedRow = tablePhim.getSelectedRow();
            if (selectedRow != -1) {
                selectedMaPhim = Integer.parseInt(tablePhim.getValueAt(selectedRow, 0).toString());
                new ChiTietPhimDialog(selectedMaPhim).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phim để xem chi tiết", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        up.add(btnXemChiTiet);

        btnDatVe = createStyledButton("Đặt vé", new Color(255, 152, 0), Color.WHITE,
                "/org/example/GUI/resources/images/plus.png");
        btnDatVe.addActionListener(e -> {
            int selectedRow = tablePhim.getSelectedRow();
            if (selectedRow != -1) {
                selectedMaPhim = Integer.parseInt(tablePhim.getValueAt(selectedRow, 0).toString());
                new DatVeDialog(maKH, maNV, selectedMaPhim).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phim để đặt vé", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        up.add(btnDatVe);

        tablePhim = new JTable() {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent jc) {
                    jc.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                }
                if (row % 2 == 0) {
                    c.setBackground(new Color(240, 240, 245));
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        };
        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontHeader = new Font("Segoe UI", Font.BOLD, 14);
        tablePhim.setFont(font);
        tablePhim.getTableHeader().setFont(fontHeader);
        tablePhim.getTableHeader().setBackground(new Color(66, 103, 178));
        tablePhim.getTableHeader().setForeground(Color.WHITE);
        tablePhim.setRowHeight(40);
        tablePhim.setGridColor(new Color(220, 220, 220));
        tablePhim.setShowGrid(true);

        modelPhim = new DefaultTableModel();
        modelPhim.addColumn("Mã Phim");
        modelPhim.addColumn("Tên Phim");
        modelPhim.addColumn("Thời Lượng");
        modelPhim.addColumn("Đạo Diễn");
        modelPhim.addColumn("Năm Sản Xuất");
        modelPhim.addColumn("Giới Hạn Tuổi");

        loadDanhSachPhim();
        tablePhim.setModel(modelPhim);

        JScrollPane scrollPane = new JScrollPane(tablePhim);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(up, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color bg, Color fg, String iconPath) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(140, 40));
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

    private void loadTheLoaiPhim() {
        ArrayList<String> listTheLoai = phimBUS.getListTheLoai();
        for (String theLoai : listTheLoai) {
            cbTheLoaiPhim.addItem(theLoai);
        }
    }

    private void loadDanhSachPhim() {
        modelPhim.setRowCount(0);
        String theLoai = (String) cbTheLoaiPhim.getSelectedItem();
        listPhim = phimBUS.getListByTheLoai(theLoai);
        for (PhimDTO phim : listPhim) {
            Object[] row = {
                    phim.getMaPhim(),
                    phim.getTenPhim(),
                    phim.getThoiLuong(),
                    phim.getDaoDien(),
                    phim.getNamSanXuat(),
                    phim.getGioiHanTuoi()
            };
            modelPhim.addRow(row);
        }
    }
}

