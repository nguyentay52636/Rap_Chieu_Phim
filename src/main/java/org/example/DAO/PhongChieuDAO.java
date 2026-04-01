package org.example.DAO;


import org.example.Connection.UtilsJDBC;
import org.example.DTO.GheDTO;
import org.example.DTO.PhongChieuDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhongChieuDAO {

    // Lấy toàn bộ phòng chiếu từ DB
    public ArrayList<PhongChieuDTO> selectAll() {
        ArrayList<PhongChieuDTO> list = new ArrayList<>();
        String sql = "SELECT MaPhong, TenPhong, LoaiPhong, SoHang, SoGheMoiHang FROM PhongChieu";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                PhongChieuDTO pc = new PhongChieuDTO(
                        rs.getInt("MaPhong"),
                        rs.getString("TenPhong"),
                        rs.getString("LoaiPhong"),
                        rs.getInt("SoHang"),
                        rs.getInt("SoGheMoiHang")
                );
                list.add(pc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi lấy danh sách Phòng Chiếu!");
        }
        return list;
    }


    public boolean update(PhongChieuDTO pc) {
        String sql = "UPDATE PhongChieu SET TenPhong = ?, LoaiPhong = ?, SoHang = ?, SoGheMoiHang = ? WHERE MaPhong = ?";
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pc.getTenPhong());
            ps.setString(2, pc.getLoaiPhong());
            ps.setInt(3, pc.getSoHang());
            ps.setInt(4, pc.getSoGheMoiHang());
            ps.setInt(5, pc.getMaPhong());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Xóa Chi Tiết Hóa Đơn Vé dựa trên mã phòng (Xóa tầng sâu nhất)
    public boolean deleteBill(Connection con, int maPhong) throws SQLException {
        String sql = "DELETE c FROM ChiTietHoaDonVe c " +
                "JOIN Ve v ON c.MaVe = v.MaVe " +
                "JOIN Ghe g ON v.MaGhe = g.MaGhe " +
                "WHERE g.MaPhong = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            return ps.executeUpdate() >= 0;
        }
    }

    // Xóa Vé dựa trên mã phòng (Xóa tầng thứ 2)
    public boolean deleteTicket(Connection con, int maPhong) throws SQLException {
        String sql = "DELETE v FROM Ve v " +
                "JOIN Ghe g ON v.MaGhe = g.MaGhe " +
                "WHERE g.MaPhong = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            return ps.executeUpdate() >= 0;
        }
    }

    // Xóa Ghế dựa trên mã phòng (Xóa tầng thứ 3)
    public boolean deleteChair(Connection con, int maPhong) throws SQLException {
        String sql = "DELETE FROM Ghe WHERE MaPhong = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            return ps.executeUpdate() >= 0;
        }
    }

    public boolean deleteShowtime(Connection con, int maPhong) throws SQLException {
        String sql = "DELETE FROM SuatChieu WHERE MaPhong = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            return ps.executeUpdate() >= 0;
        }
    }

    // Hàm xóa phòng chiếu: Quản lý Connection và Transaction
    public boolean delete(int maPhong) {
        String sql = "DELETE FROM PhongChieu WHERE MaPhong = ?";
        Connection con = null;

        try {
            con = UtilsJDBC.getConnectDB();
            con.setAutoCommit(false); // Bắt đầu Transaction

            // Thực thi xóa theo thứ tự từ dưới lên trên để không vi phạm khóa ngoại
            // 1. Xóa Chi Tiết Hóa Đơn
            deleteBill(con, maPhong);

            // 2. Xóa Vé
            deleteTicket(con, maPhong);

            // 3. Xóa Ghế
            deleteChair(con, maPhong);

            // 4. Xóa Suất Chiếu (Bổ sung vào đây)
            deleteShowtime(con, maPhong);

            // 5. Xóa phòng chiếu (Tầng cao nhất)
            boolean isRoomDeleted = false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, maPhong);
                isRoomDeleted = ps.executeUpdate() > 0;
            }

            con.commit(); // Xác nhận lưu xuống DB
            return isRoomDeleted;

        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback(); // Hoàn tác nếu có lỗi ở bất kỳ bước nào
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // Lấy danh sách ghế theo mã phòng
    public ArrayList<GheDTO> getListGheTheoPhong(int maPhong) {
        ArrayList<GheDTO> listGhe = new ArrayList<>();
        String sql = "SELECT MaGhe, MaPhong, MaLoaiGhe, HangGhe, SoGhe FROM Ghe WHERE MaPhong = ?";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, maPhong);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    GheDTO ghe = new GheDTO(
                            rs.getInt("MaGhe"),
                            rs.getInt("MaPhong"),
                            rs.getInt("MaLoaiGhe"),
                            rs.getString("HangGhe"),
                            rs.getInt("SoGhe")
                    );
                    listGhe.add(ghe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi lấy danh sách Ghế cho Phòng: " + maPhong);
        }
        return listGhe;
    }

    // Lấy chi tiết 1 phòng chiếu (BAO GỒM CẢ DANH SÁCH GHẾ)
    public PhongChieuDTO getPhongChieuById(int maPhong) {
        PhongChieuDTO room = null;
        String sql = "SELECT MaPhong, TenPhong, LoaiPhong, SoHang, SoGheMoiHang FROM PhongChieu WHERE MaPhong = ?";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maPhong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 1. Lấy thông tin cơ bản của phòng
                    room = new PhongChieuDTO(
                            rs.getInt("MaPhong"),
                            rs.getString("TenPhong"),
                            rs.getString("LoaiPhong"),
                            rs.getInt("SoHang"),
                            rs.getInt("SoGheMoiHang")
                    );

                    // 2. Lấy danh sách ghế và gắn vào phòng
                    ArrayList<GheDTO> listGhe = getListGheTheoPhong(maPhong);
                    room.setGheList(listGhe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return room;
    }

    public boolean insert(PhongChieuDTO pc) {
        String sql = "INSERT INTO PhongChieu (TenPhong, LoaiPhong, SoHang, SoGheMoiHang) VALUES (?, ?, ?, ?)";
        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pc.getTenPhong());
            ps.setString(2, pc.getLoaiPhong());
            ps.setInt(3, pc.getSoHang());
            ps.setInt(4, pc.getSoGheMoiHang());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật loại ghế cho một danh sách các ghế được chọn
    public boolean updateChairTypeBatch(int maPhong, List<String> selectedSeats, int maLoaiGheMoi) {
        String sql = "UPDATE Ghe SET MaLoaiGhe = ? WHERE MaPhong = ? AND HangGhe = ? AND SoGhe = ?";

        try (Connection con = UtilsJDBC.getConnectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            con.setAutoCommit(false); // Bắt đầu Transaction

            for (String seatCode : selectedSeats) {
                // Tách "A01" -> HangGhe là mã ASCII của 'A' (65), SoGhe là 1
                String hangGheDb = String.valueOf(seatCode.charAt(0));
                int soGheDb = Integer.parseInt(seatCode.substring(1));

                ps.setInt(1, maLoaiGheMoi);
                ps.setInt(2, maPhong);
                ps.setString(3, hangGheDb);
                ps.setInt(4, soGheDb);
                ps.addBatch();
            }

            ps.executeBatch();
            con.commit();
            return true;

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật phòng và sơ đồ ghế thông minh (Bảo vệ dữ liệu lịch sử)
    public boolean updateRoomAndSeatsTransaction(PhongChieuDTO pc) {
        String updateRoomSql = "UPDATE PhongChieu SET SoHang = ?, SoGheMoiHang = ? WHERE MaPhong = ?";

        // Tính toán ký tự hàng lớn nhất (VD: Phòng có 5 hàng thì max là 'E')
        String maxHangGhe = String.valueOf((char) ('A' + pc.getSoHang() - 1));

        // CÂU LỆNH QUAN TRỌNG: Chỉ xóa những ghế nằm ngoài ranh giới kích thước mới
        String deleteOutdatedSeatsSql = "DELETE FROM Ghe WHERE MaPhong = ? AND (HangGhe > ? OR SoGhe > ?)";

        // Lệnh Cập nhật và Thêm mới
        String updateSeatSql = "UPDATE Ghe SET MaLoaiGhe = ? WHERE MaPhong = ? AND HangGhe = ? AND SoGhe = ?";
        String insertSeatSql = "INSERT INTO Ghe (MaPhong, MaLoaiGhe, HangGhe, SoGhe) VALUES (?, ?, ?, ?)";

        try (Connection con = UtilsJDBC.getConnectDB()) {
            con.setAutoCommit(false);

            // 1. Cập nhật thông số kích thước phòng
            try (PreparedStatement psRoom = con.prepareStatement(updateRoomSql)) {
                psRoom.setInt(1, pc.getSoHang());
                psRoom.setInt(2, pc.getSoGheMoiHang());
                psRoom.setInt(3, pc.getMaPhong());
                psRoom.executeUpdate();
            }

            // 2. Dọn dẹp các ghế bị cắt bỏ (nếu bạn thu nhỏ phòng)
            try (PreparedStatement psDelSeats = con.prepareStatement(deleteOutdatedSeatsSql)) {
                psDelSeats.setInt(1, pc.getMaPhong());
                psDelSeats.setString(2, maxHangGhe);
                psDelSeats.setInt(3, pc.getSoGheMoiHang());
                psDelSeats.executeUpdate();
            }

            // 3. Cập nhật ghế cũ HOẶC thêm ghế mới (Upsert Logic)
            try (PreparedStatement psUpdateSeat = con.prepareStatement(updateSeatSql);
                 PreparedStatement psInsertSeat = con.prepareStatement(insertSeatSql)) {

                for (GheDTO ghe : pc.getGheList()) {
                    // Thử cập nhật ghế trước
                    psUpdateSeat.setInt(1, ghe.getMaLoaiGhe());
                    psUpdateSeat.setInt(2, pc.getMaPhong());
                    psUpdateSeat.setString(3, ghe.getHangGhe());
                    psUpdateSeat.setInt(4, ghe.getSoGhe());

                    int updatedRows = psUpdateSeat.executeUpdate();

                    // Nếu updatedRows = 0, nghĩa là ghế này chưa tồn tại trong DB -> Cần Insert
                    if (updatedRows == 0) {
                        psInsertSeat.setInt(1, pc.getMaPhong());
                        psInsertSeat.setInt(2, ghe.getMaLoaiGhe());
                        psInsertSeat.setString(3, ghe.getHangGhe());
                        psInsertSeat.setInt(4, ghe.getSoGhe());
                        psInsertSeat.executeUpdate();
                    }
                }
            }

            con.commit();
            return true;

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            System.err.println("Lỗi: Không thể thu nhỏ phòng vì các ghế bị cắt bỏ đã có người đặt vé!");
            // Rollback tự động xảy ra nếu lỗi
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Thêm phòng mới VÀ tự động sinh ghế mặc định (Dùng Transaction)
    public boolean insertRoomAndSeatsTransaction(PhongChieuDTO pc, int defaultMaLoaiGhe) {
        String insertRoomSql = "INSERT INTO PhongChieu (TenPhong, LoaiPhong, SoHang, SoGheMoiHang) VALUES (?, ?, ?, ?)";
        String insertSeatSql = "INSERT INTO Ghe (MaPhong, MaLoaiGhe, HangGhe, SoGhe) VALUES (?, ?, ?, ?)";

        try (Connection con = UtilsJDBC.getConnectDB()) {
            con.setAutoCommit(false); // Bắt đầu Transaction
            int newRoomId;

            // 1. Thêm phòng chiếu và yêu cầu CSDL trả về ID vừa được tự động tạo
            try (PreparedStatement psRoom = con.prepareStatement(insertRoomSql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                psRoom.setString(1, pc.getTenPhong());
                psRoom.setString(2, pc.getLoaiPhong());
                psRoom.setInt(3, pc.getSoHang());
                psRoom.setInt(4, pc.getSoGheMoiHang());

                int affectedRows = psRoom.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Tạo phòng chiếu thất bại.");
                }

                // Lấy ID tự động tăng
                try (ResultSet generatedKeys = psRoom.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newRoomId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Tạo phòng chiếu thất bại, không lấy được ID.");
                    }
                }
            }

            // 2. Dùng ID vừa lấy được để tự động tạo toàn bộ ghế
            try (PreparedStatement psSeats = con.prepareStatement(insertSeatSql)) {
                for (int i = 0; i < pc.getSoHang(); i++) {
                    String hangGhe = String.valueOf((char) ('A' + i));
                    for (int j = 1; j <= pc.getSoGheMoiHang(); j++) {
                        psSeats.setInt(1, newRoomId);
                        psSeats.setInt(2, defaultMaLoaiGhe);
                        psSeats.setString(3, hangGhe);
                        psSeats.setInt(4, j);
                        psSeats.addBatch(); // Cho vào danh sách chờ
                    }
                }
                psSeats.executeBatch(); // Đẩy toàn bộ ghế lên CSDL trong 1 lần
            }

            con.commit(); // Hoàn tất Transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}