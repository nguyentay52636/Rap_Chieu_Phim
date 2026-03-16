package org.example.BUS;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import org.example.DAO.EmployeeDAO;
import org.example.DTO.EmployeeDTO;

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
        if (listEmployee == null) {
            docDanhSach();
        }
        return listEmployee;
    }

    public boolean themNhanVien(EmployeeDTO emp) {
        if (emp.getHoTen().trim().isEmpty() || emp.getLuongCoBan() < 0) {
            return false;
        }
        
        if (employeeDAO.insert(emp)) {
            docDanhSach(); 
            return true;
        }
        return false;
    }
    
    public int importExcel(File file) {
    int count = 0;
    try (FileInputStream fis = new FileInputStream(file);
         Workbook workbook = new XSSFWorkbook(fis)) {
        
        Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Bỏ qua dòng tiêu đề (Header)

            // Đọc dữ liệu từ các cột: Tên, Ngày Sinh, Ngày Vào Làm, Lương
            String hoTen = row.getCell(0).getStringCellValue();
            java.util.Date ngaySinhUtil = row.getCell(1).getDateCellValue();
            java.util.Date ngayVaoLamUtil = row.getCell(2).getDateCellValue();
            double luong = row.getCell(3).getNumericCellValue();

            // Chuyển sang SQL Date
            java.sql.Date ngaySinh = new java.sql.Date(ngaySinhUtil.getTime());
            java.sql.Date ngayVaoLam = new java.sql.Date(ngayVaoLamUtil.getTime());

            EmployeeDTO emp = new EmployeeDTO(0, hoTen, ngaySinh, ngayVaoLam, luong);
            if (employeeDAO.insert(emp)) {
                count++;
            }
        }
        docDanhSach(); // Cập nhật lại danh sách sau khi import
    } catch (Exception e) {
        e.printStackTrace();
    }
    return count;
}

    public boolean suaNhanVien(EmployeeDTO emp) {
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
}