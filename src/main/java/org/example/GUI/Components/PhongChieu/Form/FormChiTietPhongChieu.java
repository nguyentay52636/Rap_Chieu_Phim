package org.example.GUI.Components.PhongChieu.Form;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * FormChiTietPhongChieu - Hiển thị layout ghế theo grid (A1, A2, B1, ...)
 * Mở form này khi click dòng trong JTable danh sách phòng (bấm "Chọn")
 */
public class FormChiTietPhongChieu extends JPanel {

    private int maPhong;
    private String tenPhong;
    private int soHang;
    private int soGheMoiHang;
    private JPanel gridPanel;
    
    private Map<String, JButton> mapGheButtons = new HashMap<>(); // key = "A1", value = button

    // Giả sử bạn có class kết nối DB (thay đổi theo project của bạn)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bookticket";
    private static final String DB_USER = "root";
    private static final String DB_PASS = ""; // ← thay bằng pass thật của bạn

    public FormChiTietPhongChieu(int maPhong) 
    {
        this.maPhong = maPhong;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Quản lý ghế của phòng"));

        // Panel header
        JLabel lblTitle = new JLabel("Layout ghế phòng", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitle, BorderLayout.NORTH);

        gridPanel = new JPanel();
        add(gridPanel, BorderLayout.CENTER);

        // Nút đóng / quay lại (tùy theo bạn dùng Dialog hay JFrame)
        JButton btnDong = new JButton("Đóng");
        btnDong.addActionListener(e -> {
            // Nếu dùng trong JDialog thì gọi dispose(), hoặc setVisible(false)
            SwingUtilities.getWindowAncestor(this).dispose();
        });
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnDong);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load dữ liệu ghế
        loadPhongChieuInfo();
        createSeatGrid();
    }



    /**
     * Lấy thông tin phòng (số hàng, số ghế mỗi hàng, tên phòng)
     */
    private void loadPhongChieuInfo() {
        String sql = "SELECT TenPhong, SoHang, SoGheMoiHang FROM PhongChieu WHERE MaPhong = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maPhong);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tenPhong = rs.getString("TenPhong");
                soHang = rs.getInt("SoHang");
                soGheMoiHang = rs.getInt("SoGheMoiHang");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load phòng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            // fallback giá trị demo
            soHang = 3;
            soGheMoiHang = 4;
            tenPhong = "PC01";
        }
    }

    /**
     * Tạo grid ghế động theo số hàng + số ghế mỗi hàng
     * Ghế được lấy từ bảng Ghe (có TrangThai)
     */
    private void createSeatGrid() {
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(soHang, soGheMoiHang, 8, 8)); // khoảng cách giữa các ghế
        mapGheButtons.clear();

        // Lấy danh sách ghế thật từ DB
        Map<String, String> trangThaiGhe = loadTrangThaiGhe();

        char hang = 'A';
        for (int i = 0; i < soHang; i++) {
            for (int j = 1; j <= soGheMoiHang; j++) {
                String maGheLabel = String.valueOf(hang) + j; // A1, A2, ...

                JButton btnGhe = new JButton(maGheLabel);
                btnGhe.setFont(new Font("Arial", Font.BOLD, 14));
                btnGhe.setPreferredSize(new Dimension(70, 70));

                // Màu theo trạng thái
                String tt = trangThaiGhe.getOrDefault(maGheLabel, "Trống");
                if (tt.equals("Đã đặt") || tt.equals("Đang giữ")) {
                    btnGhe.setBackground(Color.RED);
                    btnGhe.setForeground(Color.WHITE);
                } else if (tt.equals("VIP")) {
                    btnGhe.setBackground(new Color(255, 215, 0)); // vàng
                } else {
                    btnGhe.setBackground(new Color(0, 200, 0)); // xanh lá
                }

                // Click ghế → hiện thông tin chi tiết (có thể mở form chỉnh sửa sau)
                btnGhe.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(FormChiTietPhongChieu.this,
                                "Ghế: " + maGheLabel +
                                        "\nTrạng thái: " + tt +
                                        "\nPhòng: " + tenPhong,
                                "Thông tin ghế", JOptionPane.INFORMATION_MESSAGE);
                    }
                });

                gridPanel.add(btnGhe);
                mapGheButtons.put(maGheLabel, btnGhe);
            }
            hang++;
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    /**
     * Lấy trạng thái ghế từ bảng Ghe theo MaPhong
     */
    private Map<String, String> loadTrangThaiGhe() {
        Map<String, String> map = new HashMap<>();
        String sql = "SELECT HangGhe, soThuTu, TrangThai FROM Ghe WHERE MaPhong = ? ORDER BY HangGhe, soThuTu";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maPhong);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String hang = rs.getString("HangGhe");
                int so = rs.getInt("soThuTu");
                String tt = rs.getString("TrangThai");

                String label = hang + so;
                map.put(label, tt != null ? tt : "Trống");
            }
        } catch (SQLException e) {
            // Nếu chưa có dữ liệu ghế, dùng trạng thái mặc định
            System.out.println("Chưa có ghế trong DB, dùng demo");
        }
        return map;
    }

    // ====================== CÁCH SỬ DỤNG ======================
    // Trong form danh sách phòng (JTable), khi double-click hoặc bấm nút "Chọn":
    /*
    table.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int row = table.getSelectedRow();
                int maPhong = (int) table.getValueAt(row, 0); // cột MaPhong
                JFrame frame = new JFrame("Chi tiết phòng " + maPhong);
                frame.setContentPane(new FormChiTietPhongChieu(maPhong));
                frame.setSize(700, 500);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        }
    });
    */
}