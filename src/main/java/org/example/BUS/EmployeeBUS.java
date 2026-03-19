package org.example.BUS;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.DAO.EmployeeDAO;
import org.example.DTO.EmployeeDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class EmployeeBUS {
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private ArrayList<EmployeeDTO> listEmployee = null;

    public EmployeeBUS() {
        docDanhSach();
    }

    public void docDanhSach() {
        this.listEmployee = employeeDAO.selectAll();
    }

    public ArrayList<EmployeeDTO> getDanhSach() {
        if (listEmployee == null) docDanhSach();
        return listEmployee;
    }

    public boolean themNhanVien(EmployeeDTO emp) {
        if (!validate(emp)) return false;
        if (employeeDAO.insert(emp)) {
            docDanhSach();
            return true;
        }
        return false;
    }

    public boolean suaNhanVien(EmployeeDTO emp) {
        if (!validate(emp)) return false;
        if (employeeDAO.update(emp)) {
            for (int i = 0; i < listEmployee.size(); i++) {
                if (listEmployee.get(i).getMaNV() == emp.getMaNV()) {
                    listEmployee.set(i, emp);
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public boolean xoaNhanVien(int maNV) {
        if (employeeDAO.delete(maNV)) {
            listEmployee.removeIf(emp -> emp.getMaNV() == maNV);
            return true;
        }
        return false;
    }

    public boolean validate(EmployeeDTO emp) {
    // 1. Kiểm tra rỗng và âm
    if (emp.getHoTen() == null || emp.getHoTen().trim().isEmpty()) return false;
    if (emp.getLuongCoBan() < 0) return false;

    // 2. Logic Ngày vào làm phải SAU ngày sinh
    if (emp.getNgayVaoLam().before(emp.getNgaySinh())) return false;

    // 3. Logic ĐỘ TUỔI (Lấy năm trừ năm)
    int namSinh = emp.getNgaySinh().toLocalDate().getYear();
    int namVaoLam = emp.getNgayVaoLam().toLocalDate().getYear();
    
    int soTuoi = namVaoLam - namSinh;

    if (soTuoi < 15) {
        return false; // Chưa đủ 15 tuổi
    }

    return true;
}

    public ArrayList<EmployeeDTO> timKiem(String text, String type) {
        ArrayList<EmployeeDTO> result = new ArrayList<>();
        String query = text.toLowerCase().trim();
        for (EmployeeDTO emp : listEmployee) {
            boolean match = false;
            switch (type) {
                case "Mã NV": if (String.valueOf(emp.getMaNV()).contains(query)) match = true; break;
                case "Họ Tên": if (emp.getHoTen().toLowerCase().contains(query)) match = true; break;
                default: if (String.valueOf(emp.getMaNV()).contains(query) || emp.getHoTen().toLowerCase().contains(query)) match = true; break;
            }
            if (match) result.add(emp);
        }
        return result;
    }

    public boolean exportExcel(String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("NhanVien");
            Row header = sheet.createRow(0);
            String[] cols = {"Mã NV", "Họ Tên", "Ngày Sinh", "Ngày Vào Làm", "Lương"};
            for(int i=0; i<cols.length; i++) header.createCell(i).setCellValue(cols[i]);

            int rowIdx = 1;
            for (EmployeeDTO emp : listEmployee) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(emp.getMaNV());
                row.createCell(1).setCellValue(emp.getHoTen());
                row.createCell(2).setCellValue(emp.getNgaySinh().toString());
                row.createCell(3).setCellValue(emp.getNgayVaoLam().toString());
                row.createCell(4).setCellValue(emp.getLuongCoBan());
            }
            try (FileOutputStream out = new FileOutputStream(filePath)) { workbook.write(out); }
            return true;
        } catch (Exception e) { return false; }
    }

    public int importExcel(File file) {
        int count = 0;
        try (FileInputStream fis = new FileInputStream(file); Workbook wb = new XSSFWorkbook(fis)) {
            Sheet sheet = wb.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                String ten = row.getCell(0).getStringCellValue();
                java.util.Date ns = row.getCell(1).getDateCellValue();
                java.util.Date nvl = row.getCell(2).getDateCellValue();
                double luong = row.getCell(3).getNumericCellValue();
                EmployeeDTO emp = new EmployeeDTO(0, ten, new java.sql.Date(ns.getTime()), new java.sql.Date(nvl.getTime()), luong);
                if (validate(emp) && employeeDAO.insert(emp)) count++;
            }
            docDanhSach();
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }
}