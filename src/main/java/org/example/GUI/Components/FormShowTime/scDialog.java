package org.example.GUI.Components.FormShowTime;

import org.example.DTO.SuatChieuPhimDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class scDialog extends JDialog {
    private JTextField txtMaSC, txtMaPhim, txtMaPhong, txtGiaVe;
    private JSpinner spnBatDau, spnKetThuc;
    private JButton btnLuu, btnHuy;
    private boolean isUpdatingSpinner = false;

    private boolean confirmed = false;
    private SuatChieuPhimDTO suatChieu;
    private final boolean isUpdate;

    // Truyền tham số sc = null nếu là Thêm mới, truyền đối tượng sc nếu là Sửa
    public scDialog(Frame parent, String title, SuatChieuPhimDTO sc) {
        super(parent, title, true); // true = Modal (Chặn form chính)
        this.suatChieu = sc;
        this.isUpdate = (sc != null);

        initComponents();
        addEvents();

        if (isUpdate) {
            loadData(sc);
        } else {
            txtMaSC.setText("Tự động");
        }

        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel pnlCenter = new JPanel(new GridLayout(3, 4, 15, 15));
        pnlCenter.setBorder(new EmptyBorder(20, 20, 20, 20));

        txtMaSC = new JTextField();
        txtMaSC.setEditable(false); // Luôn khóa Mã SC

        txtMaPhim = new JTextField();
        txtMaPhong = new JTextField();
        txtGiaVe = new JTextField();

        spnBatDau = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorBatDau = new JSpinner.DateEditor(spnBatDau, "dd/MM/yyyy HH:mm");
        spnBatDau.setEditor(editorBatDau);

        spnKetThuc = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorKetThuc = new JSpinner.DateEditor(spnKetThuc, "dd/MM/yyyy HH:mm");
        spnKetThuc.setEditor(editorKetThuc);

        pnlCenter.add(new JLabel("Mã SC:", SwingConstants.RIGHT)); pnlCenter.add(txtMaSC);
        pnlCenter.add(new JLabel("Bắt đầu:", SwingConstants.RIGHT)); pnlCenter.add(spnBatDau);
        pnlCenter.add(new JLabel("Mã Phim:", SwingConstants.RIGHT)); pnlCenter.add(txtMaPhim);
        pnlCenter.add(new JLabel("Kết thúc:", SwingConstants.RIGHT)); pnlCenter.add(spnKetThuc);
        pnlCenter.add(new JLabel("Phòng chiếu:", SwingConstants.RIGHT)); pnlCenter.add(txtMaPhong);
        pnlCenter.add(new JLabel("Giá vé (VNĐ):", SwingConstants.RIGHT)); pnlCenter.add(txtGiaVe);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnLuu = new JButton("Lưu");
        btnLuu.setBackground(new Color(2, 117, 216));
        btnLuu.setForeground(Color.WHITE);

        btnHuy = new JButton("Hủy");
        btnHuy.setBackground(new Color(217, 83, 79));
        btnHuy.setForeground(Color.WHITE);

        pnlBottom.add(btnLuu);
        pnlBottom.add(btnHuy);

        add(pnlCenter, BorderLayout.CENTER);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    private void loadData(SuatChieuPhimDTO sc) {
        txtMaSC.setText(String.valueOf(sc.getMaSuatChieu()));
        txtMaPhim.setText(String.valueOf(sc.getMaPhim()));
        txtMaPhong.setText(String.valueOf(sc.getMaPhong()));
        txtGiaVe.setText(String.valueOf((long) sc.getGiaVeGoc())); // Ép kiểu để bỏ số thập phân nếu cần

        Date dateBatDau = Date.from(sc.getGioBatDau().atZone(ZoneId.systemDefault()).toInstant());
        spnBatDau.setValue(dateBatDau);

        Date dateKetThuc = Date.from(sc.getGioKetThuc().atZone(ZoneId.systemDefault()).toInstant());
        spnKetThuc.setValue(dateKetThuc);
    }

    private void addEvents() {
        btnHuy.addActionListener(e -> dispose());

        btnLuu.addActionListener(e -> {
            try {
                int maSC = isUpdate ? Integer.parseInt(txtMaSC.getText()) : 0;
                int maPhim = Integer.parseInt(txtMaPhim.getText().trim());
                int maPhong = Integer.parseInt(txtMaPhong.getText().trim());
                double giaVe = Double.parseDouble(txtGiaVe.getText().trim());

                // Lấy giờ và CẮT BỎ hoàn toàn phần giây (ép về 00)
                Date dateBatDau = (Date) spnBatDau.getValue();
                LocalDateTime batDau = dateBatDau.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                        .withSecond(0).withNano(0);

                // Lấy giờ và CẮT BỎ hoàn toàn phần giây (ép về 00)
                Date dateKetThuc = (Date) spnKetThuc.getValue();
                LocalDateTime ketThuc = dateKetThuc.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                        .withSecond(0).withNano(0);

                if (batDau.isAfter(ketThuc) || batDau.isEqual(ketThuc)) {
                    throw new Exception("Giờ bắt đầu phải diễn ra trước giờ kết thúc!");
                }

                suatChieu = new SuatChieuPhimDTO(maSC, maPhim, maPhong, batDau, ketThuc, giaVe);
                confirmed = true;
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã phim, phòng chiếu và giá vé phải là số hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Logic tự động tính giờ kết thúc (mang từ code cũ của bạn sang)
        // Logic tự động tính giờ kết thúc (đã fix lỗi vòng lặp)
        spnBatDau.addChangeListener(e -> {
            // Nếu đang tự động update bằng code thì bỏ qua để tránh lặp vô tận
            if (isUpdatingSpinner) return;

            Date dateBatDau = (Date) spnBatDau.getValue();
            LocalDateTime batDau = dateBatDau.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            int phut = batDau.getMinute();
            if (phut != 0 && phut != 30) {
                if (phut < 15) phut = 0;
                else if (phut < 45) phut = 30;
                else {
                    phut = 0;
                    batDau = batDau.plusHours(1);
                }

                // Bật cờ -> Set lại giờ chẵn -> Tắt cờ
                isUpdatingSpinner = true;
                batDau = batDau.withMinute(phut).withSecond(0).withNano(0);
                spnBatDau.setValue(Date.from(batDau.atZone(ZoneId.systemDefault()).toInstant()));
                isUpdatingSpinner = false;
            }

            // Tự động set Giờ kết thúc = Giờ bắt đầu + 3 tiếng
            LocalDateTime ketThuc = batDau.plusHours(3);
            spnKetThuc.setValue(Date.from(ketThuc.atZone(ZoneId.systemDefault()).toInstant()));
        });
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public SuatChieuPhimDTO getSuatChieu() {
        return suatChieu;
    }
}