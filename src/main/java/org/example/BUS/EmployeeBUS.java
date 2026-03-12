package org.example.BUS;

import org.example.DTO.EmployeeDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class EmployeeBUS {

    private final List<EmployeeDTO> list = new ArrayList<>();

    public ArrayList<EmployeeDTO> getList() {
        return new ArrayList<>(list);
    }

    public void add(EmployeeDTO dto) {
        list.add(dto);
    }

    public void update(EmployeeDTO dto) {
        for (int i = 0; i < list.size(); i++) {
            if (Objects.equals(list.get(i).getMaNV(), dto.getMaNV())) {
                list.set(i, dto);
                return;
            }
        }
    }

    public void updateTrangThai(String maNV, int trangThai) {
        for (EmployeeDTO e : list) {
            if (Objects.equals(e.getMaNV(), maNV)) {
                e.setTrangThai(trangThai);
                return;
            }
        }
    }

    public boolean checkMaNV(String maNV) {
        return list.stream().anyMatch(e -> Objects.equals(e.getMaNV(), maNV));
    }

    public boolean isMatched(EmployeeDTO employee, String selectedField, String txt) {
        if (txt == null || txt.trim().isEmpty()) {
            return true;
        }
        String search = txt.trim().toLowerCase();
        switch (selectedField) {
            case "MaNV":
                return employee.getMaNV() != null && employee.getMaNV().toLowerCase().contains(search);
            case "TenNV":
                return employee.getTenNV() != null && employee.getTenNV().toLowerCase().contains(search);
            case "SDT":
                return employee.getSdt() != null && employee.getSdt().toLowerCase().contains(search);
            case "DiaChi":
                return employee.getDiaChi() != null && employee.getDiaChi().toLowerCase().contains(search);
            default:
                return true;
        }
    }
}
