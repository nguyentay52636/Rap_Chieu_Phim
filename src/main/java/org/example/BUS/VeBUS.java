package org.example.BUS;

import org.example.DTO.VeDTO;

import java.util.ArrayList;
import java.util.List;

public class VeBUS {

    private final List<VeDTO> list = new ArrayList<>();
    private int nextId = 1;

    public ArrayList<String> getGheTrong(int maSuatChieuPhim) {
        ArrayList<String> result = new ArrayList<>();
        // Một vài ghế mẫu
        result.add("Ghế A-1 (MaGhe: 1)");
        result.add("Ghế A-2 (MaGhe: 2)");
        result.add("Ghế B-1 (MaGhe: 3)");
        return result;
    }

    public int add(VeDTO ve) {
        VeDTO stored = new VeDTO(nextId, ve.getMaGhe(),
                ve.getMaSuatChieuPhim(), ve.getGiaVe(), ve.getTrangThai());
        list.add(stored);
        return nextId++;
    }
}

