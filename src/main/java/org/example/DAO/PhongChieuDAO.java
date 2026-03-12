<<<<<<< HEAD
package org.example.DAO;


import org.example.Connection.UtilsJDBC;
import org.example.DTO.PhongChieuDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PhongChieuDAO {
    
    // Hàm này sẽ gom toàn bộ Phòng Chiếu dưới Database bỏ vào một cái Danh sách (ArrayList)
    public ArrayList<PhongChieuDTO> selectAll() {
        ArrayList<PhongChieuDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM PhongChieu"; // Câu lệnh SQL quen thuộc
        
        try {
            Connection con = UtilsJDBC.getConnectDB();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(); // Chạy lệnh và hứng kết quả vào rs
            
            // Vòng lặp: Cứ mỗi dòng trong MySQL, mình tạo 1 đối tượng DTO và nhét vào list
            while (rs.next()) {
                PhongChieuDTO pc = new PhongChieuDTO(
                    rs.getInt("MaPhong"),
                    rs.getString("TenPhong"),
                    rs.getString("LoaiPhong")
                );
                list.add(pc);
            }
            // Xong việc thì đóng cửa lại cho an toàn
            rs.close();
            pst.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi lấy danh sách Phòng Chiếu!");
        }
        return list;
    }
} 
    

=======
public class PhongChieuDAO { 
    
} 
>>>>>>> 56f360b (feat : new PhongChieu Layer)
