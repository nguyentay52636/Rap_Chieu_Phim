package org.example.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FormUtils {

    // Hàm tiện ích duy nhất: Tìm và "bấm hộ" nút Lịch
    private static void clickCalendarButton(Container datePickerContainer) {
        if (datePickerContainer != null) {
            SwingUtilities.invokeLater(() -> {
                for (Component c : datePickerContainer.getComponents()) {
                    if (c instanceof JButton) {
                        ((JButton) c).doClick();
                        break;
                    }
                }
            });
        }
    }

    /**
     * 1. HÀM CHUYỂN Ô CƠ BẢN (Text -> Text)
     * Dùng cho: txtHoTen <-> txtSDT
     */
    public static void setupFocus(JComponent current, JComponent previous, JComponent next) {
        InputMap im = current.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = current.getActionMap();

        if (previous != null) {
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "goPrevious");
            am.put("goPrevious", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    previous.requestFocusInWindow();
                }
            });
        }

        if (next != null) {
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "goNext");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "goNext");
            am.put("goNext", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    next.requestFocusInWindow();
                }
            });
        }
    }

    /**
     * 2. HÀM ĐI VÀO LỊCH (Text -> DatePicker)
     * Dùng cho: txtSDT -> datePickerNgaySinh (Ấn xuống là BUNG LỊCH)
     */
    public static void setupFocusToDatePicker(JComponent current, JComponent previous, JComponent dateTextField, Container datePickerContainer) {
        setupFocus(current, previous, dateTextField);

        Action goNextAction = current.getActionMap().get("goNext");
        if (goNextAction != null) {
            current.getActionMap().put("goNext", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dateTextField.requestFocusInWindow();
                    clickCalendarButton(datePickerContainer); // Bật Lịch
                }
            });
        }
    }

    /**
     * 3. HÀM TỪ LỊCH RA NÚT LƯU (DatePicker -> Button)
     * Dùng cho: datePickerNgaySinh -> btnThem / btnLuu
     */
    public static void setupDatePickerExit(JComponent dateTextField, JComponent previous, JButton buttonToClick, Container datePickerContainer) {
        InputMap im = dateTextField.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = dateTextField.getActionMap();

        // Đang ở Lịch, ấn Mũi tên LÊN
        if (previous != null) {
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "goPrevious");
            am.put("goPrevious", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clickCalendarButton(datePickerContainer); // Tắt Lịch
                    previous.requestFocusInWindow();          // Nhảy lên ô trên
                }
            });
        }

        // Đang ở Lịch, ấn ENTER
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "clickButton");
        am.put("clickButton", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickCalendarButton(datePickerContainer); // Tắt Lịch
                buttonToClick.doClick();                  // Bấm nút Lưu
            }
        });
    }
}