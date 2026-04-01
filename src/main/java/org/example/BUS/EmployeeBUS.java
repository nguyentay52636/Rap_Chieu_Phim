package org.example.BUS;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.DAO.EmployeeDAO;
import org.example.DTO.EmployeeDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class EmployeeBUS 
{
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private ArrayList<EmployeeDTO> listEmployee = null;

    public void docDanhSach() 
    {
        listEmployee = employeeDAO.selectAll();//listEmployee là biến toàn cục, khi gọi hàm này sẽ cập nhật lại dữ liệu mới nhất từ database vào listEmployee
    }

    public EmployeeBUS()
    {
        docDanhSach(); //khi khởi tạo BUS thì sẽ tự động gọi hàm này để lấy dữ liệu từ database vào listEmployee, giúp tránh lỗi null khi gọi getDanhSach() mà chưa có dữ liệu
    }

    public ArrayList<EmployeeDTO> getDanhSach()//hàm này sẽ được gọi trong FormEmployee để lấy danh sách nhân viên, nếu listEmployee chưa có dữ liệu thì sẽ gọi docDanhSach() để lấy dữ liệu từ database, nếu đã có dữ liệu thì trả về luôn để tránh việc gọi database nhiều lần không cần thiết
    {
        if (listEmployee == null) 
            docDanhSach();
        return listEmployee;
    }


    //---VALIDATE DỮ LIỆU NHÂN VIÊN (Dùng chung cho Thêm và Sửa)
    public boolean validate (EmployeeDTO emp) 
    {
        // 1. Kiểm tra rỗng và âm
        if (emp.getHoTen() == null || emp.getHoTen().trim().isEmpty()) 
            return false;
        if (emp.getLuongCoBan() < 0) 
            return false;

        // 2. Logic Ngày vào làm phải SAU ngày sinh và ngày sinh phải hợp lý (sau 1920 để tránh lỗi tuổi âm do sai định dạng ngày tháng)
        if (emp.getNgayVaoLam().before(emp.getNgaySinh()))
            return false;
        if (emp.getNgaySinh().before(java.sql.Date.valueOf("1961-01-01"))) 
            return false;

        // 3. Logic ĐỘ TUỔI (Lấy năm trừ năm)
        int namSinh = emp.getNgaySinh().toLocalDate().getYear();
        int namVaoLam = emp.getNgayVaoLam().toLocalDate().getYear();
        
        int soTuoi = namVaoLam - namSinh;

        
        if (soTuoi < 15) 
            return false; 
        return true;
    }

    // --- TÌM KIẾM NHÂN VIÊN (Lọc hoàn toàn bằng SQL dưới Database)
    public ArrayList<EmployeeDTO> timKiem(String keyword, String type) 
    {
        keyword = keyword.trim();
        
        // Nếu người dùng xóa hết từ khóa (để trống), trả về toàn bộ danh sách gốc
        if (keyword.isEmpty()) 
        {
            return getDanhSach(); 
        }

        // Gọi thẳng xuống DAO để lấy kết quả truy vấn từ Database
        return employeeDAO.timKiemNhanVienDAO(keyword, type);
    }
    
    //thêm nhân viên với validate
    public boolean themNhanVien(EmployeeDTO emp) 
    {
        if (!validate(emp)) 
            return false;
        
        if (employeeDAO.insert(emp))  //nếu thêm thành công vào database thì mới thêm vào listEmployee để đồng bộ với database, nếu không thêm vào database thì không thêm vào listEmployee để tránh lỗi dữ liệu không đồng bộ
        {
            docDanhSach();//cập nhật lại dữ liệu mới nhất từ database vào listEmployee sau khi thêm thông tin nhân viên
            return true;
        }
        return false;
    }

    //sửa nhân viên với validate 
    public boolean suaNhanVien(EmployeeDTO emp) 
    {
        if (!validate(emp)) 
            return false;
        if (employeeDAO.update(emp)) 
        {
            docDanhSach(); //cập nhật lại dữ liệu mới nhất từ database vào listEmployee sau khi sửa thành công, giúp đồng bộ với database
            return true;
        }
        return false;
    }

    //xóa nhân viên với xử lý ngoại lệ khi nhân viên đã lập hóa đơn
    public boolean xoaNhanVien(int maNV) 
    {
        if (employeeDAO.delete(maNV)) 
        {
            docDanhSach();
            return true;
        }
        return false;
    }


    //--- XUẤT EXCEL (Dùng thư viện Apache POI)
    public boolean exportExcel(String filePath) 
    {
        try (Workbook workbook = new XSSFWorkbook()) 
        {
            Sheet sheet = workbook.createSheet("NhanVien");
            Row header = sheet.createRow(0);
            String[] cols = {"Mã NV", "Họ Tên", "Ngày Sinh", "Ngày Vào Làm", "Lương"};
            for(int i=0; i<cols.length; i++) 
                header.createCell(i).setCellValue(cols[i]);

            int rowIdx = 1;
            for (EmployeeDTO emp : listEmployee) 
            {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(emp.getMaNV());
                row.createCell(1).setCellValue(emp.getHoTen());
                row.createCell(2).setCellValue(emp.getNgaySinh().toString());
                row.createCell(3).setCellValue(emp.getNgayVaoLam().toString());
                row.createCell(4).setCellValue(emp.getLuongCoBan());
            }
            try (FileOutputStream out = new FileOutputStream(filePath)) 
            { 
                workbook.write(out); 
            }
            return true;
        } 
        
        catch (Exception e) 
        { 
            return false; 
        }
    }

   
    //--- NHẬP EXCEL (Đã sửa lỗi lệch cột)
    public int importExcel(File file) 
    {
        int count = 0;
        try (FileInputStream fis = new FileInputStream(file); Workbook wb = new XSSFWorkbook(fis)) 
        {
            Sheet sheet = wb.getSheetAt(0);
            for (Row row : sheet) 
            {
                if (row.getRowNum() == 0) // Bỏ qua dòng tiêu đề
                    continue;
                
               
                String ten = row.getCell(1).getStringCellValue();
                java.util.Date ns = row.getCell(2).getDateCellValue();
                java.util.Date nvl = row.getCell(3).getDateCellValue();
                double luong = row.getCell(4).getNumericCellValue();
                
                EmployeeDTO emp = new EmployeeDTO(0, ten, new java.sql.Date(ns.getTime()), new java.sql.Date(nvl.getTime()), luong);
                
                // Chỉ thêm nếu vượt qua ải Validate và Insert DB thành công
                if (validate(emp) && employeeDAO.insert(emp)) 
                    count++;
            }
            
            docDanhSach(); // Cập nhật lại list sau khi nhập xong 1 loạt
        } 
        catch (Exception e) 
        { 
            e.printStackTrace(); 
        }
        return count;
    }
}