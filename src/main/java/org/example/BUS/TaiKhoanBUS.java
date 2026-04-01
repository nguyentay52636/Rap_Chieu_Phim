package org.example.BUS;

import org.example.DAO.TaiKhoanDAO;
import org.example.DTO.TaiKhoanDTO;

public class TaiKhoanBUS {
    
    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    private static TaiKhoanDTO currentAccount;

    public TaiKhoanDTO login(String tenDangNhap, String matKhau) {
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) return null;
        if (matKhau == null || matKhau.trim().isEmpty()) return null;
        
        TaiKhoanDTO tk = taiKhoanDAO.checkLogin(tenDangNhap, matKhau);
        if (tk != null) {
            currentAccount = tk; // Lưu thông tin người dùng hiện tại
        }
        return tk;
    }

    public static TaiKhoanDTO getCurrentAccount() {
        return currentAccount;
    }

    public boolean updatePassword(String username, String newPassword) {
        if (username == null || username.trim().isEmpty()) return false;
        if (newPassword == null || newPassword.isEmpty()) return false;
        return taiKhoanDAO.updatePasswordByUserName(username, newPassword);
    }

    public static void logout() {
        currentAccount = null;
    }
}
