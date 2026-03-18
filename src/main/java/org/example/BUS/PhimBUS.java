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
        list.addAll(phimDAO.selectAll());
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

    // Tìm kiếm phim theo keyword + loại tiêu chí (Tất cả, Mã phim, Tên phim, Đạo diễn, Năm sản xuất, Thể loại, Trạng thái)
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
                    if (maPhimStr.toLowerCase().contains(kw)) result.add(p);
                }
                case "Tên phim" -> {
                    if (ten.toLowerCase().contains(kw)) result.add(p);
                }
                case "Đạo diễn" -> {
                    if (daoDien.toLowerCase().contains(kw)) result.add(p);
                }
                case "Năm sản xuất" -> {
                    if (namSXStr.toLowerCase().contains(kw)) result.add(p);
                }
                case "Thể loại" -> {
                    if (maLoaiStr.toLowerCase().contains(kw)) result.add(p);
                }
                case "Trạng thái" -> {
                    if (trangThai.toLowerCase().contains(kw)) result.add(p);
                }
                case "Tất cả" -> {
                    if (maPhimStr.toLowerCase().contains(kw)
                            || maLoaiStr.toLowerCase().contains(kw)
                            || ten.toLowerCase().contains(kw)
                            || daoDien.toLowerCase().contains(kw)
                            || namSXStr.toLowerCase().contains(kw)
                            || trangThai.toLowerCase().contains(kw)) {
                        result.add(p);
                    }
                }
                default -> {
                    // mặc định coi như "Tất cả"
                    if (maPhimStr.toLowerCase().contains(kw)
                            || maLoaiStr.toLowerCase().contains(kw)
                            || ten.toLowerCase().contains(kw)
                            || daoDien.toLowerCase().contains(kw)
                            || namSXStr.toLowerCase().contains(kw)
                            || trangThai.toLowerCase().contains(kw)) {
                        result.add(p);
                    }
                }
            }
        }
        return result;
    }

    public void add(PhimDTO phim) {
        list.add(phim);
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

