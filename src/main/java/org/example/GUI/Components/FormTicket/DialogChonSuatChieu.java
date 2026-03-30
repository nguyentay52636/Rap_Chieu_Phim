package org.example.GUI.Components.FormTicket;

import org.example.BUS.SuatChieuPhimBUS;
import org.example.DAO.PhimDAO;
import org.example.DTO.PhimDTO;
import org.example.DTO.SuatChieuPhimDTO;
import org.example.UltisTable.TableUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class DialogChonSuatChieu extends JDialog 
{
    private JComboBox<ComboItemPhim> cbPhim;
    private JComboBox<String> cbNgay;
    private JComboBox<String> cbGio;

    private JTable tableSuatChieu;
    private DefaultTableModel modelSuatChieu;
    private JButton btnTim, btnLamMoi, btnTiepTuc, btnHuy;

    // Các biến lưu trữ kết quả để truyền sang Form Chọn Ghế
    private int maSuatChieuDuocChon = -1;
    private int maPhongDuocChon = -1;
    private int giaVeGocDuocChon = 0;

    // Khởi tạo DAO, BUS và Data
    private PhimDAO phimDAO = new PhimDAO();
    private SuatChieuPhimBUS suatChieuBUS = new SuatChieuPhimBUS();

    // Lưu toàn bộ danh sách suất chiếu & phim để lọc In-Memory cực nhanh
    private List<SuatChieuPhimDTO> allSuatChieu;
    private List<PhimDTO> allPhim;

    private boolean isUpdatingCombos = false; // Cờ chặn sự kiện lặp vòng

    Font font = new Font("Segoe UI", Font.PLAIN, 14);

    public DialogChonSuatChieu(JFrame owner) 
    {
        super(owner, "Bước 1: Chọn Suất Chiếu Phim", true);

        // Tải dữ liệu 1 lần duy nhất khi mở Form
        allSuatChieu = suatChieuBUS.getListSuatChieu();
        allPhim = phimDAO.selectAll();

        init(owner);
    }

    private void init(JFrame owner) 
    {
        Dimension parentSize = owner.getSize();
        int width = (int) (parentSize.width * 0.85);
        int height = (int) (parentSize.height * 0.75);
        width = Math.max(width, 1000);
        height = Math.max(height, 600);
        this.setSize(width, height);
        this.setLocationRelativeTo(owner);
        this.setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(UIManager.getColor("Panel.background"));

        // ================= TOP: BỘ LỌC TÌM KIẾM LIÊN HOÀN =================
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlFilter.setBackground(UIManager.getColor("Panel.background"));
        pnlFilter.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")),
                "Lọc Suất Chiếu Nhanh", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), new Color(0, 123, 255)));

        // 1. Chọn Phim
        pnlFilter.add(new JLabel("Phim:"));
        cbPhim = new JComboBox<>();
        cbPhim.setFont(font);
        cbPhim.setPreferredSize(new Dimension(220, 35));
        pnlFilter.add(cbPhim);

        // 2. Chọn Ngày
        pnlFilter.add(new JLabel("Ngày:"));
        cbNgay = new JComboBox<>();
        cbNgay.setFont(font);
        cbNgay.setPreferredSize(new Dimension(150, 35));
        pnlFilter.add(cbNgay);

        // 3. Chọn Giờ
        pnlFilter.add(new JLabel("Giờ:"));
        cbGio = new JComboBox<>();
        cbGio.setFont(font);
        cbGio.setPreferredSize(new Dimension(120, 35));
        pnlFilter.add(cbGio);

        // 4. Các Nút Bấm
        btnTim = new JButton("Tìm kiếm");
        btnTim.setPreferredSize(new Dimension(110, 35));
        btnTim.setBackground(new Color(0, 123, 255));
        btnTim.setForeground(Color.WHITE);
        btnTim.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTim.setFocusPainted(false);
        btnTim.addActionListener(e -> timSuatChieu());
        pnlFilter.add(btnTim);

        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setPreferredSize(new Dimension(100, 35));
        btnLamMoi.setBackground(new Color(108, 117, 125));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.addActionListener(e -> lamMoiBoLoc());
        pnlFilter.add(btnLamMoi);

        mainPanel.add(pnlFilter, BorderLayout.NORTH);

        // ================= CENTER: BẢNG KẾT QUẢ =================
        String[] columns = {"Mã SC", "Tên Phim", "Ngày Chiếu", "Giờ Bắt Đầu", "Phòng Chiếu", "Giá Vé"};
        modelSuatChieu = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableSuatChieu = new JTable(modelSuatChieu);
        tableSuatChieu.setRowHeight(40);
        tableSuatChieu.setFont(font);
        tableSuatChieu.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableSuatChieu.getTableHeader().setBackground(new Color(66, 103, 178));
        tableSuatChieu.getTableHeader().setForeground(Color.WHITE);
        tableSuatChieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tableSuatChieu);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ================= BOTTOM: NÚT ĐIỀU HƯỚNG =================
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        pnlBottom.setBackground(UIManager.getColor("Panel.background"));

        btnHuy = new JButton("Hủy Bỏ");
        btnHuy.setPreferredSize(new Dimension(100, 35));
        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnHuy.addActionListener(e -> setVisible(false));

        btnTiepTuc = new JButton("Tiếp Tục Chọn Ghế ->");
        btnTiepTuc.setPreferredSize(new Dimension(200, 35));
        btnTiepTuc.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTiepTuc.setBackground(new Color(40, 167, 69));
        btnTiepTuc.setForeground(Color.WHITE);
        btnTiepTuc.addActionListener(e -> actionTiepTuc((JFrame) owner));

        pnlBottom.add(btnHuy);
        pnlBottom.add(btnTiepTuc);
        mainPanel.add(pnlBottom, BorderLayout.SOUTH);

        this.add(mainPanel);

        // ================= GẮN SỰ KIỆN CHO COMBOBOX =================
        setupComboBoxListeners();

        // Chạy lần đầu
        loadPhim();
    }

    // =========================================================================
    // LOGIC "ĐỔ MỒI" (CASCADING COMBOBOXES)
    // =========================================================================

    private void setupComboBoxListeners() 
    {
        cbPhim.addActionListener(e -> {
            if (!isUpdatingCombos) updateComboBoxNgay();
        });

        cbNgay.addActionListener(e -> {
            if (!isUpdatingCombos) updateComboBoxGio();
        });
    }

    private void loadPhim() 
    {
        isUpdatingCombos = true;
        cbPhim.removeAllItems();
        cbPhim.addItem(new ComboItemPhim(-1, "-- Chọn Phim --"));

        // Lọc ra các phim đang chiếu
        for (PhimDTO p : allPhim) {
            if ("DangChieu".equalsIgnoreCase(p.getTrangThai())) {
                cbPhim.addItem(new ComboItemPhim(p.getMaPhim(), p.getTenPhim()));
            }
        }
        isUpdatingCombos = false;

        updateComboBoxNgay(); // Kéo theo cập nhật Ngày
        timSuatChieu(); // Load sẵn toàn bộ bảng
    }

    private void updateComboBoxNgay() 
    {
        isUpdatingCombos = true;
        cbNgay.removeAllItems();
        cbNgay.addItem("-- Tất cả ngày --");

        ComboItemPhim selectedPhim = (ComboItemPhim) cbPhim.getSelectedItem();
        if (selectedPhim != null && selectedPhim.maPhim != -1) {
            Set<LocalDate> uniqueDates = new TreeSet<>();
            for (SuatChieuPhimDTO sc : allSuatChieu) {
                if (sc.getMaPhim() == selectedPhim.maPhim) {
                    uniqueDates.add(sc.getGioBatDau().toLocalDate());
                }
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (LocalDate date : uniqueDates) {
                cbNgay.addItem(date.format(dtf));
            }
        }
        isUpdatingCombos = false; 
        updateComboBoxGio(); 
    }

    private void updateComboBoxGio() 
    {
        isUpdatingCombos = true;
        cbGio.removeAllItems();
        cbGio.addItem("-- Tất cả giờ --");

        ComboItemPhim selectedPhim = (ComboItemPhim) cbPhim.getSelectedItem();
        String selectedNgay = (String) cbNgay.getSelectedItem();

        if (selectedPhim != null && selectedPhim.maPhim != -1 && selectedNgay != null && !selectedNgay.equals("-- Tất cả ngày --")) {
            Set<LocalTime> uniqueTimes = new TreeSet<>();
            DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate targetDate = LocalDate.parse(selectedNgay, dtfDate);

            for (SuatChieuPhimDTO sc : allSuatChieu) {
                if (sc.getMaPhim() == selectedPhim.maPhim && sc.getGioBatDau().toLocalDate().equals(targetDate)) {
                    uniqueTimes.add(sc.getGioBatDau().toLocalTime());
                }
            }
            DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm");
            for (LocalTime time : uniqueTimes) {
                cbGio.addItem(time.format(dtfTime));
            }
        }
        isUpdatingCombos = false; 
    }

    private void lamMoiBoLoc() 
    {
        suatChieuBUS.refreshList(); 
        allSuatChieu = suatChieuBUS.getListSuatChieu();

        isUpdatingCombos = true;
        cbPhim.setSelectedIndex(0);
        isUpdatingCombos = false;

        updateComboBoxNgay();
        timSuatChieu();
    }

    // =========================================================================
    // LOGIC TÌM KIẾM & ĐỔ BẢNG
    // =========================================================================

    private void timSuatChieu() 
    {
        ComboItemPhim selectedPhim = (ComboItemPhim) cbPhim.getSelectedItem();
        String selectedNgayStr = (String) cbNgay.getSelectedItem();
        String selectedGioStr = (String) cbGio.getSelectedItem();

        int maPhim = (selectedPhim != null) ? selectedPhim.maPhim : -1;

        modelSuatChieu.setRowCount(0);

        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm");

        List<SuatChieuPhimDTO> filteredList = allSuatChieu.stream().filter(sc -> {
            boolean matchPhim = (maPhim == -1) || (sc.getMaPhim() == maPhim);
            boolean matchNgay = selectedNgayStr == null || selectedNgayStr.equals("-- Tất cả ngày --")
                    || sc.getGioBatDau().toLocalDate().format(dtfDate).equals(selectedNgayStr);
            boolean matchGio = selectedGioStr == null || selectedGioStr.equals("-- Tất cả giờ --")
                    || sc.getGioBatDau().toLocalTime().format(dtfTime).equals(selectedGioStr);

            return matchPhim && matchNgay && matchGio;
        }).collect(Collectors.toList());

        for (SuatChieuPhimDTO sc : filteredList) {
            String tenPhim = getTenPhimById(sc.getMaPhim());

            modelSuatChieu.addRow(new Object[]{
                    sc.getMaSuatChieu(),
                    tenPhim,
                    sc.getGioBatDau().format(dtfDate),
                    sc.getGioBatDau().format(dtfTime),
                    "Phòng " + sc.getMaPhong(),
                    sc.getGiaVeGoc() // Cột này có thể chứa dạng "160000.0"
            });
        }
        TableUtils.autoResizeColumns(tableSuatChieu);
    }

    private String getTenPhimById(int id) 
    {
        for (PhimDTO p : allPhim) {
            if (p.getMaPhim() == id) return p.getTenPhim();
        }
        return "Unknown";
    }

    // =========================================================================
    // CHUYỂN BƯỚC 2
    // =========================================================================

    private void actionTiepTuc(JFrame owner) 
    {
        int row = tableSuatChieu.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 suất chiếu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            maSuatChieuDuocChon = Integer.parseInt(tableSuatChieu.getValueAt(row, 0).toString());
            String tenPhong = tableSuatChieu.getValueAt(row, 4).toString();
            maPhongDuocChon = Integer.parseInt(tenPhong.replace("Phòng ", "").trim());
            
            // 🔥 ĐÃ SỬA LỖI X10 GIÁ TIỀN Ở ĐÂY 🔥
            // Dùng Double.parseDouble để nó đọc được chuỗi "160000.0" rồi ép về int (160000)
            giaVeGocDuocChon = (int) Double.parseDouble(tableSuatChieu.getValueAt(row, 5).toString());

            // Tắt Popup Bước 1 trước khi mở Bước 2
            this.dispose();

            // Mở Bước 2: Sơ đồ ghế
            DialogChonGhe dialogGhe = new DialogChonGhe(owner, maSuatChieuDuocChon, maPhongDuocChon, giaVeGocDuocChon);
            dialogGhe.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy thông tin suất chiếu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private class ComboItemPhim 
    {
        int maPhim;
        String tenPhim;
        public ComboItemPhim(int maPhim, String tenPhim) {
            this.maPhim = maPhim;
            this.tenPhim = tenPhim;
        }
        @Override
        public String toString() { return tenPhim; }
    }
}