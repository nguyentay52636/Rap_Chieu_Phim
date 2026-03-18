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

