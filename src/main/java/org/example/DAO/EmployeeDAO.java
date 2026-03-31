package org.example.DAO;

import org.example.Connection.UtilsJDBC;
import org.example.DTO.EmployeeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeDAO 
{ 

    public ArrayList<EmployeeDTO> selectAll() 
    {
        ArrayList<EmployeeDTO> list = new ArrayList<>();
        String sql = "SELECT MaNV, HoTen, NgaySinh, NgayVaoLam, LuongCoBan FROM NhanVien";
        
        // Cho Connection vào trong cụm try() để nó TỰ ĐỘNG ĐÓNG khi dùng xong
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) 
        {
            while (rs.next()) 
            {
                EmployeeDTO nv = new EmployeeDTO
                (
                        rs.getInt("MaNV"),
                        rs.getString("HoTen"),
                        rs.getDate("NgaySinh"),
                        rs.getDate("NgayVaoLam"),
                        rs.getDouble("LuongCoBan")
                );
                list.add(nv);
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            System.err.println("Lỗi lấy danh sách Nhân Viên!");
        }
        return list;
    }

    public boolean insert(EmployeeDTO nv) 
    {
        String sql = "INSERT INTO NhanVien (HoTen, NgaySinh, NgayVaoLam, LuongCoBan) VALUES (?, ?, ?, ?)";
        
        // Gộp Connection vào cụm try() để nó TỰ ĐỘNG ĐÓNG khi dùng xong
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement pst = con.prepareStatement(sql)) 
        {
            pst.setString(1, nv.getHoTen());
            pst.setDate(2, nv.getNgaySinh());
            pst.setDate(3, nv.getNgayVaoLam());
            pst.setDouble(4, nv.getLuongCoBan());
            return pst.executeUpdate() > 0;
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(EmployeeDTO nv) 
    {
        String sql = "UPDATE NhanVien SET HoTen = ?, NgaySinh = ?, NgayVaoLam = ?, LuongCoBan = ? WHERE MaNV = ?";
        
        // Gộp Connection vào cụm try()  để nó TỰ ĐỘNG ĐÓNG khi dùng xong
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement pst = con.prepareStatement(sql)) 
        {
            pst.setString(1, nv.getHoTen());
            pst.setDate(2, nv.getNgaySinh());
            pst.setDate(3, nv.getNgayVaoLam());
            pst.setDouble(4, nv.getLuongCoBan());
            pst.setInt(5, nv.getMaNV());
            return pst.executeUpdate() > 0;
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int maNV) 
    {
        String sql = "DELETE FROM NhanVien WHERE MaNV = ?";
        
        // Gộp Connection vào cụm try() để nó TỰ ĐỘNG ĐÓNG khi dùng xong
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement pst = con.prepareStatement(sql)) 
        {
            pst.setInt(1, maNV);
            return pst.executeUpdate() > 0;
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================================
    // HÀM TÌM KIẾM NHÂN VIÊN BẰNG SQL TRỰC TIẾP TỪ DATABASE
    // =========================================================================
    public ArrayList<EmployeeDTO> timKiemNhanVienDAO(String keyword, String type) 
    {
        ArrayList<EmployeeDTO> dsKetQua = new ArrayList<>();
        String sql = "";
        
        // Định dạng từ khóa cho lệnh LIKE
        String searchPattern = "%" + keyword + "%";
        
        // Tùy biến câu SQL dựa trên lựa chọn ở ComboBox
        switch (type) 
        {
            case "Mã NV":
                sql = "SELECT MaNV, HoTen, NgaySinh, NgayVaoLam, LuongCoBan FROM NhanVien WHERE MaNV LIKE ?";
                break;
            case "Họ Tên":
                sql = "SELECT MaNV, HoTen, NgaySinh, NgayVaoLam, LuongCoBan FROM NhanVien WHERE HoTen LIKE ?";
                break;
            case "Ngày Sinh":
                sql = "SELECT MaNV, HoTen, NgaySinh, NgayVaoLam, LuongCoBan FROM NhanVien WHERE NgaySinh LIKE ?";
                break;
            case "Ngày Vào Làm":
                sql = "SELECT MaNV, HoTen, NgaySinh, NgayVaoLam, LuongCoBan FROM NhanVien WHERE NgayVaoLam LIKE ?";
                break;
            case "Lương Cơ Bản":
                sql = "SELECT MaNV, HoTen, NgaySinh, NgayVaoLam, LuongCoBan FROM NhanVien WHERE LuongCoBan LIKE ?";
                break;
            default: // "Tất cả"
                sql = "SELECT MaNV, HoTen, NgaySinh, NgayVaoLam, LuongCoBan FROM NhanVien " +
                      "WHERE MaNV LIKE ? OR HoTen LIKE ? OR NgaySinh LIKE ? OR NgayVaoLam LIKE ? OR LuongCoBan LIKE ?";
                break;
        }
        
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement pst = con.prepareStatement(sql)) 
        {
            // Nếu là "Tất cả" thì phải truyền tham số cho 5 dấu ?
            if (type.equals("Tất cả")) 
            {
                pst.setString(1, searchPattern);
                pst.setString(2, searchPattern);
                pst.setString(3, searchPattern);
                pst.setString(4, searchPattern);
                pst.setString(5, searchPattern);
            } 
            else // Nếu tìm theo 1 cột cụ thể thì chỉ có 1 dấu ?
            {
                pst.setString(1, searchPattern);
            }
            
            try (ResultSet rs = pst.executeQuery()) 
            {
                while (rs.next()) 
                {
                    EmployeeDTO nv = new EmployeeDTO
                    (
                            rs.getInt("MaNV"),
                            rs.getString("HoTen"),
                            rs.getDate("NgaySinh"),
                            rs.getDate("NgayVaoLam"),
                            rs.getDouble("LuongCoBan")
                    );
                    dsKetQua.add(nv);
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            System.err.println("Lỗi khi thực thi SQL tìm kiếm!");
        }
        
        return dsKetQua;
    }
}