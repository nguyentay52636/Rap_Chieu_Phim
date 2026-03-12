package org.example.GUI.Components.FormBooking.components;

import org.example.BUS.SuatChieuPhimBUS;
import org.example.BUS.VeBUS;
import org.example.BUS.HoaDonBUS;
import org.example.DTO.VeDTO;
import org.example.DTO.HoaDonDTO;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;

public class DatVeDialog extends JDialog {

    private final SuatChieuPhimBUS suatChieuPhimBUS = new SuatChieuPhimBUS();
    private final VeBUS veBUS = new VeBUS();
    private final HoaDonBUS hoaDonBUS = new HoaDonBUS();
    private final int maKH;
    private final int maNV;
    private final int maPhim;
    private int currentMaSuatChieuPhim = -1;
    private int selectedMaGhe = -1;

    public DatVeDialog(int maKH, int maNV, int maPhim) {
        this.maKH = maKH;
        this.maNV = maNV;
        this.maPhim = maPhim;
        setTitle("Đặt Vé Phim");
        setSize(600, 500);
        setModal(true);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 2, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel lblPhong = createStyledLabel("Phòng chiếu:", labelFont);
        JComboBox<Integer> cbPhong = new JComboBox<>();
        cbPhong.setFont(textFont);
        cbPhong.setBackground(Color.WHITE);

        JLabel lblSuatChieu = createStyledLabel("Suất chiếu:", labelFont);
        JComboBox<Integer> cbSuatChieu = new JComboBox<>();
        cbSuatChieu.setFont(textFont);
        cbSuatChieu.setBackground(Color.WHITE);

        JLabel lblNgayChieu = createStyledLabel("Ngày chiếu:", labelFont);
        SpinnerDateModel dateModel =
                new SpinnerDateModel(new java.util.Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        dateSpinner.setFont(textFont);

        JButton btnTimGhe = createStyledButton("Tìm ghế trống", new Color(0, 123, 255), Color.WHITE, null);

        JLabel lblGheDaChon = createStyledLabel("Ghế đã chọn:", labelFont);
        JTextField txtGheDaChon = new JTextField();
        txtGheDaChon.setFont(textFont);
        txtGheDaChon.setEnabled(false);
        txtGheDaChon.setBackground(new Color(248, 249, 250));
        txtGheDaChon.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));

        JLabel lblGiaVe = createStyledLabel("Giá vé:", labelFont);
        JTextField txtGiaVe = new JTextField();
        txtGiaVe.setFont(textFont);
        txtGiaVe.setEnabled(false);
        txtGiaVe.setBackground(new Color(248, 249, 250));
        txtGiaVe.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));

        loadPhongVaSuat(maPhim, cbPhong, cbSuatChieu);

        btnTimGhe.addActionListener(e -> {
            int maPhong = (int) cbPhong.getSelectedItem();
            int maSuatChieu = (int) cbSuatChieu.getSelectedItem();
            java.util.Date utilDate = (java.util.Date) dateSpinner.getValue();
            Date ngayChieu = new Date(utilDate.getTime());
            currentMaSuatChieuPhim = suatChieuPhimBUS.getMaSuatChieuPhim(
                    maPhim, maPhong, maSuatChieu, ngayChieu);
            if (currentMaSuatChieuPhim != -1) {
                int giaVeGoc = suatChieuPhimBUS.getGiaVeGoc(currentMaSuatChieuPhim);
                txtGiaVe.setText(String.valueOf(giaVeGoc));

                SeatSelectionDialog seatDialog = new SeatSelectionDialog(currentMaSuatChieuPhim, veBUS);
                seatDialog.setVisible(true);
                int maGheChon = seatDialog.getSelectedMaGhe();
                if (maGheChon != -1) {
                    selectedMaGhe = maGheChon;
                    String seatLabel = seatDialog.getSelectedSeatLabel();
                    txtGheDaChon.setText(seatLabel != null ? seatLabel : String.valueOf(selectedMaGhe));
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Không tìm thấy suất chiếu",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        panel.add(lblPhong);
        panel.add(cbPhong);
        panel.add(lblSuatChieu);
        panel.add(cbSuatChieu);
        panel.add(lblNgayChieu);
        panel.add(dateSpinner);
        panel.add(btnTimGhe);
        panel.add(new JLabel());          // chừa trống bên cạnh nút
        panel.add(lblGheDaChon);
        panel.add(txtGheDaChon);
        panel.add(lblGiaVe);
        panel.add(txtGiaVe);

        JButton btnXacNhan = createStyledButton("Xác nhận đặt vé", new Color(40, 167, 69), Color.WHITE, null);
        btnXacNhan.addActionListener(e -> {
            if (currentMaSuatChieuPhim == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn suất chiếu và ngày chiếu", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (selectedMaGhe == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng bấm 'Tìm ghế trống' và chọn 1 ghế", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int giaVe = Integer.parseInt(txtGiaVe.getText());

            VeDTO ve = new VeDTO(0, selectedMaGhe, currentMaSuatChieuPhim, giaVe, "Đã bán");
            int maVe = veBUS.add(ve);

            HoaDonDTO hoaDon = new HoaDonDTO(
                    0, maKH, maNV,
                    new Date(System.currentTimeMillis()),
                    1, giaVe, 0, null, 0, giaVe
            );
            int maHoaDon = hoaDonBUS.add(hoaDon);

            hoaDonBUS.addCTHDVe(maHoaDon, maVe, giaVe);

            JOptionPane.showMessageDialog(this, "Đặt vé thành công!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        panel.add(new JLabel());
        panel.add(btnXacNhan);

        add(panel);
    }

    private JLabel createStyledLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(new Color(33, 37, 41));
        return label;
    }

    private JButton createStyledButton(String text, Color bg, Color fg, String iconPath) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        if (iconPath != null) {
            button.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconPath)));
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

    private void loadPhongVaSuat(int maPhim,
                                 JComboBox<Integer> cbPhong,
                                 JComboBox<Integer> cbSuatChieu) {
        ArrayList<Integer> listPhong = suatChieuPhimBUS.getListPhongByPhim(maPhim);
        for (int phong : listPhong) {
            cbPhong.addItem(phong);
        }
        ArrayList<Integer> listSuat = suatChieuPhimBUS.getListSuatByPhim(maPhim);
        for (int suat : listSuat) {
            cbSuatChieu.addItem(suat);
        }
    }

}

