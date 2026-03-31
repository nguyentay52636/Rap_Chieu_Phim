package org.example.GUI.Components.FormPhongChieu;

import javax.swing.*;
import java.awt.*;

public class DeleteRoomHandler {
    public static void execute(FormPhongChieu parent) {
        JTable table = parent.getTablePhongChieu();
        int[] selectedRows = table.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(parent, "Vui lòng chọn ít nhất 1 phòng chiếu để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] columns = {"Mã Phòng", "Tên Phòng", "Loại Phòng", "Số Hàng", "Ghế/Hàng"};
        Object[][] data = new Object[selectedRows.length][5];

        for (int i = 0; i < selectedRows.length; i++) {
            int modelRow = table.convertRowIndexToModel(selectedRows[i]);
            for (int j = 0; j < 5; j++) data[i][j] = parent.getModel().getValueAt(modelRow, j);
        }

        JTable previewTable = new JTable(data, columns);
        previewTable.getTableHeader().setBackground(new Color(220, 53, 69));
        previewTable.getTableHeader().setForeground(Color.WHITE);
        previewTable.setEnabled(false);

        previewTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int column = 0; column < previewTable.getColumnCount(); column++) {
            int maxWidth = 50;
            // Đo Header
            javax.swing.table.TableCellRenderer headerRenderer = previewTable.getTableHeader().getDefaultRenderer();
            Object headerValue = previewTable.getColumnModel().getColumn(column).getHeaderValue();
            Component headerComp = headerRenderer.getTableCellRendererComponent(previewTable, headerValue, false, false, 0, column);
            maxWidth = Math.max(headerComp.getPreferredSize().width, maxWidth);
            // Đo Data
            for (int row = 0; row < previewTable.getRowCount(); row++) {
                javax.swing.table.TableCellRenderer cellRenderer = previewTable.getCellRenderer(row, column);
                Component cellComp = previewTable.prepareRenderer(cellRenderer, row, column);
                maxWidth = Math.max(cellComp.getPreferredSize().width, maxWidth);
            }
            previewTable.getColumnModel().getColumn(column).setPreferredWidth(maxWidth + 15);
        }

        int tableHeight = Math.min(selectedRows.length * 35 + 35, 250) + 20;
        JScrollPane scrollPreview = new JScrollPane(previewTable);
        scrollPreview.setPreferredSize(new Dimension(600, tableHeight));

        JPanel panelConfirm = new JPanel(new BorderLayout(0, 10));
        JLabel lblConfirm = new JLabel("CẢNH BÁO: Bạn có chắc muốn XÓA VĨNH VIỄN " + selectedRows.length + " phòng chiếu này?");
        lblConfirm.setForeground(Color.RED);
        panelConfirm.add(lblConfirm, BorderLayout.NORTH);
        panelConfirm.add(scrollPreview, BorderLayout.CENTER);

        int confirm = JOptionPane.showConfirmDialog(parent, panelConfirm, "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            int successCount = 0;
            for (int i = 0; i < selectedRows.length; i++) {
                int modelRow = table.convertRowIndexToModel(selectedRows[i]);
                int maPC = (int) parent.getModel().getValueAt(modelRow, 0);
                if (parent.getPcBUS().delete(maPC)) successCount++;
            }
            JOptionPane.showMessageDialog(parent, "Đã xóa thành công " + successCount + " Phòng Chiếu!");
            parent.loadDataToTable();
        }
    }
}