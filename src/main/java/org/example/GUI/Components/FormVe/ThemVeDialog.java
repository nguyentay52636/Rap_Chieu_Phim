package org.example.GUI.Components.FormVe;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import org.example.BUS.VeBUS;
import org.example.DTO.VeDTO;

public class ThemVeDialog extends JDialog 
{
    // ==========================================
    // KHAI BÁO BIẾN DÙNG CHUNG
    // ==========================================
    private VeBUS veBUS;
    private FormVe parentForm; // Lưu form cha để lát gọi hàm làm mới bảng
    
    // Các ô nhập liệu trên giao diện
    private JComboBox<String> cbPhim;
    private JComboBox<String> cbSuatChieu;
    private JComboBox<String> cbGhe;
    private JTextField txtGiaVe;
    
    // Nút bấm
    private JButton btnXacNhanVe, btnHuyBo;

    // --- HÀM KHỞI TẠO (CHẠY ĐẦU TIÊN KHI MỞ FORM) ---
    public ThemVeDialog(FormVe parent, VeBUS bus) 
    {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "THÊM VÉ", true);
        this.parentForm = parent;
        this.veBUS = bus;
        
        // Thiết lập kích thước và canh giữa màn hình
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Lệnh bo góc khung cửa sổ (Dùng FlatLaf)
        getRootPane().putClientProperty(FlatClientProperties.STYLE, "arc:10");

        initComponents();      // 1. Bách vẽ giao diện
        loadDataToComboBox();  // 2. Ân đổ dữ liệu vào ô chọn
        initEvents();          // 3. Ân gắn sự kiện click chuột
    }

    // ==========================================
    // LÃNH THỔ CỦA BÁCH (VẼ GIAO DIỆN)
    // Code đơn giản, dùng GridLayout chia dòng cho dễ nhìn
    // ==========================================
    private void initComponents() 
    {
        // --- 1. PHẦN KHUNG CHÍNH Ở GIỮA ---
        JPanel pnlCenter = new JPanel();
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.setBorder(new EmptyBorder(15, 20, 15, 20)); // Căn lề 4 góc
        
        // Tạo khung viền có chữ "THÔNG TIN VÉ MỚI"
        TitledBorder vienKhung = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            "THÔNG TIN VÉ MỚI", 
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14)
        );
        pnlCenter.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), vienKhung));
        
        // Chia lưới: 4 hàng, 2 cột, khoảng cách ngang 10, dọc 20
        pnlCenter.setLayout(new GridLayout(4, 2, 10, 20)); 

        // -- Tạo các thành phần --
        // Ô chọn Phim
        cbPhim = new JComboBox<>();
        cbPhim.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Ô chọn Suất
        cbSuatChieu = new JComboBox<>();
        cbSuatChieu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Ô chọn Ghế
        cbGhe = new JComboBox<>();
        cbGhe.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Ô nhập Giá vé (Khóa lại không cho gõ)
        txtGiaVe = new JTextField();
        txtGiaVe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtGiaVe.setEditable(false); 
        txtGiaVe.putClientProperty(FlatClientProperties.STYLE, "focusWidth:2");

        // Gắn vào khung (Cứ 1 nhãn đi kèm 1 ô nhập)
        pnlCenter.add(new JLabel("Chọn Phim:"));    pnlCenter.add(cbPhim);
        pnlCenter.add(new JLabel("Suất Chiếu:"));   pnlCenter.add(cbSuatChieu);
        pnlCenter.add(new JLabel("Ghế Trống:"));    pnlCenter.add(cbGhe);
        pnlCenter.add(new JLabel("Giá Vé (VNĐ):")); pnlCenter.add(txtGiaVe);

        add(pnlCenter, BorderLayout.CENTER); // Đẩy khung này vào giữa cửa sổ

        // --- 2. PHẦN NÚT BẤM Ở DƯỚI ĐÁY ---
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlSouth.setBackground(Color.WHITE);

        // Nút Hủy
        btnHuyBo = new JButton("Hủy Bỏ");
        btnHuyBo.setPreferredSize(new Dimension(100, 40));
        btnHuyBo.setBackground(new Color(108, 117, 125)); // Màu xám
        btnHuyBo.setForeground(Color.WHITE);
        btnHuyBo.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Nút Xác nhận
        btnXacNhanVe = new JButton("Xác Nhận Thêm");
        btnXacNhanVe.setPreferredSize(new Dimension(150, 40));
        btnXacNhanVe.setBackground(new Color(40, 167, 69)); // Màu xanh lá
        btnXacNhanVe.setForeground(Color.WHITE);
        btnXacNhanVe.setFont(new Font("Segoe UI", Font.BOLD, 14));

        pnlSouth.add(btnHuyBo);
        pnlSouth.add(btnXacNhanVe);

        add(pnlSouth, BorderLayout.SOUTH); // Đẩy phần nút xuống đáy
    }

    // ==========================================
    // LÃNH THỔ CỦA ÂN (DATABASE & SỰ KIỆN)
    // ==========================================
    
    // Hàm này Ân sẽ móc Database đưa tên Phim, Suất, Ghế vào ComboBox
    private void loadDataToComboBox()
    {
        // Ân code ở đây nha! Tạm thời để trống cho giao diện lên hình đã.
        cbPhim.addItem("-- Dữ liệu đang tải --"); 
    }

    // Hàm này Ân bắt sự kiện click chuột
    private void initEvents() 
    {
        // 1. Tắt cửa sổ khi bấm Hủy
        btnHuyBo.addActionListener(e -> dispose()); 

        // 2. Chốt đơn khi bấm Xác nhận
        btnXacNhanVe.addActionListener(e -> {
            
            // Ân viết logic bắt lỗi để trống và gọi DAO thêm vé ở đây nhé!
            
            // Gợi ý cấu trúc của Ân:
            /*
            VeDTO ve = new VeDTO();
            ve.setMaSuatChieu(...);
            if (veBUS.themVe(ve)) {
                JOptionPane.showMessageDialog(this, "Thêm vé thành công!");
                parentForm.loadDataToTable(); // Làm mới bảng JTable ngoài màn hình chính
                dispose(); // Đóng hộp thoại
            }
            */
        });
    }
}