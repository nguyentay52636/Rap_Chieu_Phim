package org.example.GUI.Components.FormPhim.Dialog;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.Date;
import org.example.BUS.TheLoaiPhimBUS;
import org.example.DTO.TheLoaiPhimDTO;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class DialogAddPhim extends JDialog {

    private boolean saved = false;
    private final TheLoaiPhimBUS theLoaiPhimBUS = new TheLoaiPhimBUS();
    private String selectedImagePath = "";
    private final String IMAGE_PATH = "src/main/java/org/example/GUI/resources/images/ImagePhim";

    private final JTextField ten = createStyledTextField(30);
    private final JComboBox<String> theLoai = createStyledComboBox(
            theLoaiPhimBUS.getList().stream()
                    .map(TheLoaiPhimDTO::getTenLoaiPhim)
                    .toArray(String[]::new)
    );
    private final JTextField thoiLuong = createStyledTextField(10);
    private final JTextField daoDien = createStyledTextField(30);
    
    private final JDatePickerImpl pickerNam;
    private final JDatePickerImpl pickerNgayKC;
    
    private final JComboBox<String> trangThai = createStyledComboBox(new String[] {
            "Đang chiếu", "Sắp chiếu", "Ngừng chiếu"
    });
    
    private final JTextField txtPoster = createStyledTextField(25);
    private final JButton btnChoosePoster = new JButton("Chọn ảnh...");
    private JLabel lblPreview; // Khung hiển thị ảnh bên phải

    public DialogAddPhim(Window owner) {
        super(owner, "➕ Thêm Phim Mới", ModalityType.APPLICATION_MODAL);
        pickerNam = createDatePicker();
        pickerNgayKC = createDatePicker();
        txtPoster.setEditable(false);
        initUI();
    }

    private void initUI() {
        setSize(1000, 780); // Tăng kích thước chiều rộng lên 1000 để lấn sang phải
        setLocationRelativeTo(getOwner());
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 30, 30));

        JLabel titleLabel = new JLabel("THÊM PHIM MỚI", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // --- PHẦN BÊN TRÁI: FORM NHẬP LIỆU ---
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(pnlForm, gbc, 0, "Tên phim", ten, true);
        addFormRow(pnlForm, gbc, 1, "Thể loại", theLoai, false);
        addFormRow(pnlForm, gbc, 2, "Thời lượng (phút)", thoiLuong, true);
        addFormRow(pnlForm, gbc, 3, "Đạo diễn", daoDien, false);
        addFormRow(pnlForm, gbc, 4, "Năm sản xuất", pickerNam, true);
        addFormRow(pnlForm, gbc, 5, "Ngày khởi chiếu", pickerNgayKC, true);
        addFormRow(pnlForm, gbc, 6, "Trạng thái", trangThai, false);
        
        JPanel pnlPosterHelper = new JPanel(new BorderLayout(5, 0));
        pnlPosterHelper.setOpaque(false);
        pnlPosterHelper.add(txtPoster, BorderLayout.CENTER);
        pnlPosterHelper.add(btnChoosePoster, BorderLayout.EAST);
        addFormRow(pnlForm, gbc, 7, "Poster", pnlPosterHelper, true);

        btnChoosePoster.addActionListener(e -> chooseImage());

        // Nút Lưu
        JButton btnLuu = new JButton("💾 LƯU PHIM");
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLuu.setBackground(new Color(46, 204, 113));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setPreferredSize(new Dimension(0, 45));
        btnLuu.addActionListener(e -> onSave());

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        pnlForm.add(btnLuu, gbc);

        // --- PHẦN BÊN PHẢI: KHUNG XEM TRƯỚC ẢNH ---
        JPanel pnlPreview = new JPanel(new BorderLayout());
        pnlPreview.setBackground(Color.WHITE);
        pnlPreview.setPreferredSize(new Dimension(380, 0));
        pnlPreview.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                " XEM TRƯỚC POSTER ", 0, 0, new Font("Segoe UI", Font.BOLD, 13)));

        lblPreview = new JLabel("Chưa chọn ảnh", SwingConstants.CENTER);
        lblPreview.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblPreview.setForeground(Color.GRAY);
        pnlPreview.add(lblPreview, BorderLayout.CENTER);

        // Ghép 2 phần
        JPanel pnlContent = new JPanel(new BorderLayout(30, 0));
        pnlContent.setBackground(Color.WHITE);
        pnlContent.add(pnlForm, BorderLayout.CENTER);
        pnlContent.add(pnlPreview, BorderLayout.EAST);

        mainPanel.add(pnlContent, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Hình ảnh", "jpg", "png", "jpeg"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();
            txtPoster.setText(selectedFile.getName());
            
            // Hiển thị Preview
            updatePreview(selectedFile);
        }
    }

    private void updatePreview(File file) {
        try {
            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            Image img = icon.getImage();
            // Scale ảnh cho khớp khung preview (350x500)
            Image scaledImg = img.getScaledInstance(350, 500, Image.SCALE_SMOOTH);
            lblPreview.setIcon(new ImageIcon(scaledImg));
            lblPreview.setText("");
        } catch (Exception e) {
            lblPreview.setText("Lỗi hiển thị ảnh");
        }
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hôm nay");
        p.put("text.month", "Tháng");
        p.put("text.year", "Năm");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    private void onSave() {
        try {
            if (ten.getText().trim().isEmpty()) throw new Exception("Tên phim không được trống!");
            if (pickerNgayKC.getModel().getValue() == null) throw new Exception("Chọn ngày khởi chiếu!");
            if (selectedImagePath.isEmpty()) throw new Exception("Chọn poster!");

            File source = new File(selectedImagePath);
            File destDir = new File(IMAGE_PATH);
            if (!destDir.exists()) destDir.mkdirs();
            
            String fileName = System.currentTimeMillis() + "_" + source.getName();
            Path targetPath = Paths.get(IMAGE_PATH, fileName);
            Files.copy(source.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            txtPoster.setText(fileName);
            saved = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- GETTERS ---
    public String getTenPhim() { return ten.getText().trim(); }
    public String getTheLoai() { return (String) theLoai.getSelectedItem(); }
    public int getThoiLuong() { return Integer.parseInt(thoiLuong.getText().trim().isEmpty() ? "0" : thoiLuong.getText().trim()); }
    public String getDaoDien() { return daoDien.getText().trim(); }
    public int getNamSanXuat() { return pickerNam.getModel().getYear(); }
    public String getNgayKhoiChieu() { 
        Object val = pickerNgayKC.getModel().getValue();
        if (val == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (val instanceof Date) return sdf.format((Date) val);
        if (val instanceof Calendar) return sdf.format(((Calendar) val).getTime());
        return "";
    }
    public String getTrangThai() { return (String) trangThai.getSelectedItem(); }
    public String getPosterUrl() { return txtPoster.getText(); }
    public boolean isSaved() { return saved; }

    // --- STYLED HELPERS ---
    private JTextField createStyledTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204), 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        return tf;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBackground(Color.WHITE);
        cb.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1, true));
        return cb;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent component, boolean required) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        if (required) { label.setText(labelText + " *"); label.setForeground(new Color(231, 76, 60)); }
        panel.add(label, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(component, gbc);
    }

    public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        @Override
        public Object stringToValue(String text) throws ParseException { return dateFormatter.parseObject(text); }
        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) { return dateFormatter.format(((Calendar) value).getTime()); }
            return "";
        }
    }
}