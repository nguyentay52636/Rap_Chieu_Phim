package org.example.GUI.Components.FormPhongChieu;

import org.example.DTO.LoaiGheDTO;
import org.example.DTO.PhongChieuDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class AddRoomDialog {
    public AddRoomDialog(FormPhongChieu parent) {
        Window owner = SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(owner, "Thêm Phòng Chiếu Mới", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(owner);

        JPanel panelCenter = new JPanel(new GridLayout(4, 2, 10, 20));
        panelCenter.setBorder(new EmptyBorder(20, 20, 20, 20));

        panelCenter.add(parent.createLabel("Tên Phòng: "));
        JTextField txtTenPhong = parent.createStyledTextField();
        panelCenter.add(txtTenPhong);

        panelCenter.add(parent.createLabel("Loại Phòng: "));
        JComboBox<String> optLoaiPhong = parent.createStyledComboBox(new String[]{"2D", "3D", "4DX", "IMAX"});
        panelCenter.add(optLoaiPhong);

        panelCenter.add(parent.createLabel("Số Hàng: "));
        JTextField txtSoHang = parent.createStyledTextField();
        panelCenter.add(txtSoHang);

        panelCenter.add(parent.createLabel("Số Ghế/Hàng: "));
        JTextField txtGheHang = parent.createStyledTextField();
        panelCenter.add(txtGheHang);

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton btnHuy = parent.createStyledButton("Hủy", new Color(108, 117, 125), Color.WHITE);
        JButton btnThem = parent.createStyledButton("Lưu", new Color(40, 167, 69), Color.WHITE);
        panelBottom.add(btnThem); panelBottom.add(btnHuy);

        dialog.setLayout(new BorderLayout());
        dialog.add(panelCenter, BorderLayout.CENTER);
        dialog.add(panelBottom, BorderLayout.SOUTH);

        btnHuy.addActionListener(e -> dialog.dispose());

        btnThem.addActionListener(e -> {
            String tenPhong = txtTenPhong.getText().trim();
            String loaiPhong = (String) optLoaiPhong.getSelectedItem();
            String soHangStr = txtSoHang.getText().trim();
            String soGheStr = txtGheHang.getText().trim();

            if (tenPhong.isEmpty() || soHangStr.isEmpty() || soGheStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int soHang = Integer.parseInt(soHangStr);
                int soGhe = Integer.parseInt(soGheStr);

                if (soHang <= 0 || soGhe <= 0 || soHang > 26) {
                    JOptionPane.showMessageDialog(dialog, "Số hàng (tối đa 26) và số ghế phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PhongChieuDTO pc = new PhongChieuDTO(0, tenPhong, loaiPhong, soHang, soGhe);
                List<LoaiGheDTO> listLoaiGhe = parent.getLgBUS().getList();
                int defaultMaLoai = listLoaiGhe.isEmpty() ? 1 : listLoaiGhe.get(0).getMaLoaiGhe();

                if (parent.getPcBUS().addRoomWithSeats(pc, defaultMaLoai)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm phòng chiếu thành công!");
                    parent.loadDataToTable();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Số hàng và số ghế phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.setVisible(true);
    }

}