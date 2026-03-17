package org.example.BUS;

import org.example.DTO.PhimDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Stub implementation using in-memory data so that FormBooking can run
 * without a real database. Replace with JDBC logic later.
 */
public class PhimBUS {

    private final List<PhimDTO> list = new ArrayList<>();
    private int nextId = 3;

    public PhimBUS() {
        // Dữ liệu mẫu
        list.add(new PhimDTO(1, 1, "Avengers: Endgame", 181, "Anthony Russo",
                2019, 13, "https://picsum.photos/id/1015/800/1200"));
        list.add(new PhimDTO(2, 2, "Your Name", 106, "Makoto Shinkai",
                2016, 13, "https://picsum.photos/id/201/800/1200"));
    }

    public ArrayList<PhimDTO> getList() {
        return new ArrayList<>(list);
    }

    public ArrayList<String> getListTheLoai() {
        ArrayList<String> result = new ArrayList<>();
        result.add("Tất cả");
        result.add("Hành động");
        result.add("Tình cảm");
        return result;
    }

    public ArrayList<PhimDTO> getListByTheLoai(String theLoai) {
        if (theLoai == null || theLoai.equals("Tất cả")) {
            return new ArrayList<>(list);
        }
        // Ở đây chỉ lọc đơn giản theo mã thể loại
        int maTheLoai = theLoai.equals("Hành động") ? 1 : 2;
        return list.stream()
                .filter(p -> p.getMaTheLoaiPhim() == maTheLoai)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public PhimDTO getById(int maPhim) {
        return list.stream()
                .filter(p -> p.getMaPhim() == maPhim)
                .findFirst()
                .orElse(null);
    }

    public String getTenTheLoai(int maTheLoai) {
        return switch (maTheLoai) {
            case 1 -> "Hành động";
            case 2 -> "Tình cảm";
            default -> "Khác";
        };
    }

    public boolean isMatched(PhimDTO phim, String field, String txt) {
        if (txt == null || txt.trim().isEmpty()) {
            return true;
        }
        String search = txt.trim().toLowerCase();
        return switch (field) {
            case "MaPhim" -> String.valueOf(phim.getMaPhim()).contains(search);
            case "TenPhim" -> phim.getTenPhim() != null
                    && phim.getTenPhim().toLowerCase().contains(search);
            case "DaoDien" -> phim.getDaoDien() != null
                    && phim.getDaoDien().toLowerCase().contains(search);
            case "NamSanXuat" -> String.valueOf(phim.getNamSanXuat()).contains(search);
            default -> true;
        };
    }

    public ArrayList<Integer> getListMaTheLoai() {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(1);
        result.add(2);
        return result;
    }

    public void add(PhimDTO phim) {
        PhimDTO stored = new PhimDTO(nextId++, phim.getMaTheLoaiPhim(), phim.getTenPhim(),
                phim.getThoiLuong(), phim.getDaoDien(), phim.getNamSanXuat(),
                phim.getGioiHanTuoi(), phim.getPosterURL());
        list.add(stored);
    }

    public void delete(int maPhim) {
        list.removeIf(p -> p.getMaPhim() == maPhim);
    }

    public void update(PhimDTO phim) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaPhim() == phim.getMaPhim()) {
                list.set(i, phim);
                return;
            }
        }
    }
}

