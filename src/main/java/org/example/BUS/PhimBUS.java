package org.example.BUS;

import org.example.DAO.PhimDAO;
import org.example.DTO.PhimDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PhimBUS {

    private final List<PhimDTO> list = new ArrayList<>();
    private final PhimDAO phimDAO = new PhimDAO();

    public PhimBUS() {
        refreshList();
    }

    public void refreshList() {
        list.clear();
        list.addAll(phimDAO.selectAll());
    }

    public ArrayList<PhimDTO> getList() {
        return new ArrayList<>(list);
    }

    public ArrayList<PhimDTO> getListByTheLoai(String theLoai) {
        if (theLoai == null || theLoai.equals("Tất cả")) {
            return new ArrayList<>(list);
        }
        // Giả sử mã loại phim khớp với logic BUS cũ or bạn cần map lại từ names
        return list.stream()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public PhimDTO getById(int maPhim) {
        return list.stream()
                .filter(p -> p.getMaPhim() == maPhim)
                .findFirst()
                .orElse(null);
    }

    public ArrayList<PhimDTO> search(String keyword, String type) {
        ArrayList<PhimDTO> result = new ArrayList<>();
        if (keyword == null) {
            return new ArrayList<>(list);
        }
        String kw = keyword.trim().toLowerCase();
        if (kw.isEmpty()) {
            return new ArrayList<>(list);
        }

        for (PhimDTO p : list) {
            String maPhimStr = String.valueOf(p.getMaPhim());
            String maLoaiStr = String.valueOf(p.getMaTheLoaiPhim());
            String ten = p.getTenPhim() != null ? p.getTenPhim() : "";
            String daoDien = p.getDaoDien() != null ? p.getDaoDien() : "";
            String namSXStr = String.valueOf(p.getNamSanXuat());
            String trangThai = p.getTrangThai() != null ? p.getTrangThai() : "";

            switch (type) {
                case "Mã phim" -> {
                    if (maPhimStr.contains(kw)) result.add(p);
                }
                case "Tên phim" -> {
                    if (ten.toLowerCase().contains(kw)) result.add(p);
                }
                case "Đạo diễn" -> {
                    if (daoDien.toLowerCase().contains(kw)) result.add(p);
                }
                case "Năm sản xuất" -> {
                    if (namSXStr.contains(kw)) result.add(p);
                }
                case "Thể loại" -> {
                    if (maLoaiStr.contains(kw)) result.add(p);
                }
                case "Trạng thái" -> {
                    if (trangThai.toLowerCase().contains(kw)) result.add(p);
                }
                case "Tất cả" -> {
                    if (maPhimStr.contains(kw)
                            || maLoaiStr.contains(kw)
                            || ten.toLowerCase().contains(kw)
                            || daoDien.toLowerCase().contains(kw)
                            || namSXStr.contains(kw)
                            || trangThai.toLowerCase().contains(kw)) {
                        result.add(p);
                    }
                }
                default -> {
                    if (maPhimStr.contains(kw) || ten.toLowerCase().contains(kw)) {
                        result.add(p);
                    }
                }
            }
        }
        return result;
    }

    public boolean add(PhimDTO phim) {
        if (phimDAO.insert(phim)) {
            refreshList();
            return true;
        }
        return false;
    }

    public boolean delete(int maPhim) {
        if (phimDAO.delete(maPhim)) {
            refreshList();
            return true;
        }
        return false;
    }

    public boolean update(PhimDTO phim) {
        if (phimDAO.update(phim)) {
            refreshList();
            return true;
        }
        return false;
    }
}
