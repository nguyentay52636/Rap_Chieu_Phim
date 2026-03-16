package org.example.BUS;

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