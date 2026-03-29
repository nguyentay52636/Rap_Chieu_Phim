package org.example.BUS;

import org.example.DAO.GheDAO;
import org.example.DTO.GheDTO;
import java.util.ArrayList;
import java.util.List;

public class GheBUS {
    private final GheDAO gheDAO = new GheDAO();

    // Hàm hỗ trợ tạo danh sách ghế tự động khi tạo phòng chiếu mới
    public boolean generateSeatsForRoom(int maPhong, int soHang, int soGheMoiHang, int maLoaiGheMacDinh) {
        List<GheDTO> listGheMoi = new ArrayList<>();

        for (int i = 0; i < soHang; i++) {
            // Chuyển đổi 0, 1, 2... thành chữ cái "A", "B", "C"...
            String hangGhe = String.valueOf((char) ('A' + i));

            for (int cot = 1; cot <= soGheMoiHang; cot++) {
                // Tạo ghế với mã loại ghế mặc định và HangGhe là String
                GheDTO ghe = new GheDTO(0, maPhong, maLoaiGheMacDinh, hangGhe, cot);
                listGheMoi.add(ghe);
            }
        }

        // Gọi DAO để insert một lúc (Batch)
        return gheDAO.insertBatch(listGheMoi);
    }
}