package org.example.UltisTable;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

public class TableUtils {

    /**
     * Thuật toán tự động đo và căn chỉnh chiều rộng các cột trong JTable
     * dựa trên độ dài của Tiêu đề (Header) và Nội dung (Cells).
     */
    public static void autoResizeColumns(JTable table) {
        for (int column = 0; column < table.getColumnCount(); column++) {
            int maxWidth = 50; // Chiều rộng tối thiểu

            // 1. Đo Header
            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            Object headerValue = table.getColumnModel().getColumn(column).getHeaderValue();
            Component headerComp = headerRenderer.getTableCellRendererComponent(table, headerValue, false, false, 0, column);
            maxWidth = Math.max(headerComp.getPreferredSize().width, maxWidth);

            // 2. Đo Data của tất cả các dòng
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component cellComp = table.prepareRenderer(cellRenderer, row, column);
                maxWidth = Math.max(cellComp.getPreferredSize().width, maxWidth);
            }

            // 3. Chốt kích thước cột (Cộng thêm 15px padding cho thoáng)
            table.getColumnModel().getColumn(column).setPreferredWidth(maxWidth + 15);
        }
    }
}
