package org.example.DAO;

import org.example.Connection.UtilsJDBC;
import org.example.DTO.EmployeeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeDAO {

    public ArrayList<EmployeeDTO> selectAll() {
        ArrayList<EmployeeDTO> list = new ArrayList<>();
        String sql = "SELECT MaNV, HoTen, NgaySinh, NgayVaoLam, LuongCoBan FROM NhanVien";
        
        // Lấy Connection nhưng KHÔNG tự động đóng nó
        Connection con = UtilsJDBC.getConnectDB(); 

        try (PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                EmployeeDTO nv = new EmployeeDTO(
                        rs.getInt("MaNV"),
                        rs.getString("HoTen"),
                        rs.getDate("NgaySinh"),
                        rs.getDate("NgayVaoLam"),
                        rs.getDouble("LuongCoBan")
                );
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi lấy danh sách Nhân Viên!");
        }
        return list;
    }

    public boolean insert(EmployeeDTO nv) {
        Connection con = UtilsJDBC.getConnectDB();
        String sql = "INSERT INTO NhanVien (HoTen, NgaySinh, NgayVaoLam, LuongCoBan) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nv.getHoTen());
            pst.setDate(2, nv.getNgaySinh());
            pst.setDate(3, nv.getNgayVaoLam());
            pst.setDouble(4, nv.getLuongCoBan());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(EmployeeDTO nv) {
        Connection con = UtilsJDBC.getConnectDB();
        String sql = "UPDATE NhanVien SET HoTen = ?, NgaySinh = ?, NgayVaoLam = ?, LuongCoBan = ? WHERE MaNV = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nv.getHoTen());
            pst.setDate(2, nv.getNgaySinh());
            pst.setDate(3, nv.getNgayVaoLam());
            pst.setDouble(4, nv.getLuongCoBan());
            pst.setInt(5, nv.getMaNV());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int maNV) {
        Connection con = UtilsJDBC.getConnectDB();
        String sql = "DELETE FROM NhanVien WHERE MaNV = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, maNV);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}