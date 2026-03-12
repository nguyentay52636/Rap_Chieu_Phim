package org.example.GUI.Components.FormProduct;

import org.example.DTO.SanPhamDTO;

import java.awt.Dialog;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class DetailProductDialog extends JDialog {

    private final DecimalFormat priceFormatter = new DecimalFormat("#,##0");

    public DetailProductDialog(FormProduct parent, SanPhamDTO sp) {
        super(javax.swing.SwingUtilities.getWindowAncestor(parent), "Chi Tiết Sản Phẩm", Dialog.ModalityType.APPLICATION_MODAL);
        setSize(650, 360);
        setLocationRelativeTo(parent);
        setBackground(Color.WHITE);

        JPanel root = new JPanel(new BorderLayout(15, 10));
        root.setBorder(new EmptyBorder(15, 15, 15, 15));
        root.setBackground(Color.WHITE);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(220, 220));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        ImageIcon icon = loadProductImageIcon(parent, sp.getHinhAnh());
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } else {
            imageLabel.setText("Chưa có ảnh");
        }

        JPanel infoPanel = new JPanel(new GridLayout(6, 2, 10, 8));
        infoPanel.setBackground(Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 14);

        infoPanel.add(createLabel("Mã sản phẩm:", labelFont));
        infoPanel.add(createField(String.valueOf(sp.getMaSanPham()), textFont));

        infoPanel.add(createLabel("Tên sản phẩm:", labelFont));
        infoPanel.add(createField(nullToEmpty(sp.getTenSanPham()), textFont));

        infoPanel.add(createLabel("Giá bán:", labelFont));
        infoPanel.add(createField(priceFormatter.format(sp.getGiaBan()) + " VND", textFont));

        infoPanel.add(createLabel("Kích thước:", labelFont));
        infoPanel.add(createField(nullToEmpty(sp.getKichThuoc()), textFont));

        infoPanel.add(createLabel("Số lượng:", labelFont));
        infoPanel.add(createField(String.valueOf(sp.getSoLuong()), textFont));

        infoPanel.add(createLabel("Trạng thái:", labelFont));
        infoPanel.add(createField(nullToEmpty(sp.getTrangThai()), textFont));

        JButton btnClose = new JButton("Đóng");
        btnClose.setBackground(new Color(220, 53, 69));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnClose);

        root.add(imagePanel, BorderLayout.WEST);
        root.add(infoPanel, BorderLayout.CENTER);
        add(root, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private static JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(new Color(33, 37, 41));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    private static JTextField createField(String text, Font font) {
        JTextField field = new JTextField(text);
        field.setFont(font);
        field.setEnabled(false);
        field.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        field.setBackground(new Color(248, 249, 250));
        return field;
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private static ImageIcon loadProductImageIcon(FormProduct parent, String fileAnh) {
        if (fileAnh == null || fileAnh.isEmpty()) return null;
        java.net.URL imgURL = parent.getClass().getResource("/org/example/GUI/resources/imageTopic/" + fileAnh);
        if (imgURL != null) return new ImageIcon(imgURL);
        File file = new File(System.getProperty("user.dir"), "imageTopic" + File.separator + fileAnh);
        if (file.isFile()) return new ImageIcon(file.getAbsolutePath());
        return null;
    }
}

