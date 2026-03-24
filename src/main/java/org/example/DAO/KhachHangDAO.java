package org.example.DAO;

import org.example.Connection.UtilsJDBC;
import org.example.DTO.KhachHangDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    public List<KhachHangDTO> selectAll() {
        List<KhachHangDTO> list = new ArrayList<>();
        String sql = "SELECT MaKH, HoTen, SDT, NgaySinh, DiemTichLuy, HangThanhVien FROM KhachHang";

        // Try-with-resources: Tự động quản lý đóng Connection, Statement, ResultSet
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Đọc an toàn: Kiểm tra NULL trước khi ép sang LocalDate
                java.sql.Date sqlDate = rs.getDate("NgaySinh");
                LocalDate ngaySinh = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                KhachHangDTO kh = new KhachHangDTO(
                        rs.getInt("MaKH"),
                        rs.getString("HoTen"),
                        rs.getString("SDT"),
                        ngaySinh,
                        rs.getInt("DiemTichLuy"),
                        rs.getString("HangThanhVien")
                );
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("ĐÃ LẤY ĐƯỢC: " + list.size() + " KHÁCH HÀNG TỪ CSDL");
        return list;
    }

    public boolean insert(KhachHangDTO kh) {
        String sql = "INSERT INTO KhachHang (HoTen, SDT, NgaySinh, DiemTichLuy, HangThanhVien) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getSDT());
            ps.setObject(3, kh.getNgaySinh());
            ps.setInt(4, kh.getDiemTichLuy());
            ps.setString(5, kh.getHangThanhVien());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(KhachHangDTO kh) {
        String sql = "UPDATE KhachHang SET HoTen = ?, SDT = ?, NgaySinh = ?, DiemTichLuy = ?, HangThanhVien = ? WHERE MaKH = ?";
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getSDT());
            ps.setObject(3, kh.getNgaySinh());
            ps.setInt(4, kh.getDiemTichLuy());
            ps.setString(5, kh.getHangThanhVien());
            ps.setInt(6, kh.getMaKH());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int maKH) {
        String sql = "DELETE FROM KhachHang WHERE MaKH = ?";
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkTrungSDT(String sdt) {
        boolean isDuplicate = false;
        String sql = "SELECT 1 FROM KhachHang WHERE SDT = ?";
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, sdt);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    isDuplicate = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDuplicate;
    }

    public List<KhachHangDTO> search(String tieuChi, String tuKhoa) {
        List<KhachHangDTO> list = new ArrayList<>();
        String sql = "";

        switch (tieuChi) {
            case "MaKH": sql = "SELECT * FROM KhachHang WHERE MaKH LIKE ?"; break;
            case "HoTen": sql = "SELECT * FROM KhachHang WHERE HoTen LIKE ?"; break;
            case "SDT": sql = "SELECT * FROM KhachHang WHERE SDT LIKE ?"; break;
            default: return list;
        }

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + tuKhoa + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Đọc an toàn: Kiểm tra NULL
                    java.sql.Date sqlDate = rs.getDate("NgaySinh");
                    LocalDate ngaySinh = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                    KhachHangDTO kh = new KhachHangDTO(
                            rs.getInt("MaKH"), rs.getString("HoTen"), rs.getString("SDT"),
                            ngaySinh, rs.getInt("DiemTichLuy"), rs.getString("HangThanhVien")
                    );
                    list.add(kh);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<KhachHangDTO> advancedSearch(String hoTen, String sdt, java.util.Date tuNgay, java.util.Date denNgay, String phepTinhNgay, java.util.Date ngayToanTu, String diemTu, String diemDen, String phepTinh, String diemToanTu, List<String> selectedHangs) {
        List<KhachHangDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM KhachHang WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (hoTen != null && !hoTen.trim().isEmpty()) {
            sql.append(" AND HoTen LIKE ? "); params.add("%" + hoTen.trim() + "%");
        }
        if (sdt != null && !sdt.trim().isEmpty()) {
            sql.append(" AND SDT LIKE ? "); params.add("%" + sdt.trim() + "%");
        }

        // 1. So sánh Khoảng ngày sinh
        if (tuNgay != null) {
            sql.append(" AND NgaySinh >= ? "); params.add(new java.sql.Date(tuNgay.getTime()));
        }
        if (denNgay != null) {
            sql.append(" AND NgaySinh <= ? "); params.add(new java.sql.Date(denNgay.getTime()));
        }

        // 2. So sánh Toán tử ngày sinh (MỚI)
        if (!phepTinhNgay.equals("(Bỏ qua)") && ngayToanTu != null) {
            sql.append(" AND NgaySinh ").append(phepTinhNgay).append(" ? ");
            params.add(new java.sql.Date(ngayToanTu.getTime()));
        }

        // 3. So sánh Khoảng điểm
        if (diemTu != null && !diemTu.trim().isEmpty()) {
            sql.append(" AND DiemTichLuy >= ? "); params.add(Integer.parseInt(diemTu.trim()));
        }
        if (diemDen != null && !diemDen.trim().isEmpty()) {
            sql.append(" AND DiemTichLuy <= ? "); params.add(Integer.parseInt(diemDen.trim()));
        }

        // 4. So sánh Toán tử điểm
        if (!phepTinh.equals("(Bỏ qua)") && diemToanTu != null && !diemToanTu.trim().isEmpty()) {
            sql.append(" AND DiemTichLuy ").append(phepTinh).append(" ? ");
            params.add(Integer.parseInt(diemToanTu.trim()));
        }

        // 5. Lọc nhiều hạng
        if (selectedHangs != null && !selectedHangs.isEmpty()) {
            sql.append(" AND HangThanhVien IN (");
            for (int i = 0; i < selectedHangs.size(); i++) {
                sql.append("?");
                if (i < selectedHangs.size() - 1) sql.append(",");
                params.add(selectedHangs.get(i));
            }
            sql.append(") ");
        }

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Đọc an toàn: Kiểm tra NULL
                    java.sql.Date sqlDate = rs.getDate("NgaySinh");
                    LocalDate ngaySinh = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                    KhachHangDTO kh = new KhachHangDTO(
                            rs.getInt("MaKH"), rs.getString("HoTen"), rs.getString("SDT"),
                            ngaySinh, rs.getInt("DiemTichLuy"), rs.getString("HangThanhVien")
                    );
                    list.add(kh);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}