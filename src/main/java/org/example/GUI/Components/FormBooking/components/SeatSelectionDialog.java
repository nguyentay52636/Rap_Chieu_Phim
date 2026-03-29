// package org.example.GUI.Components.FormBooking.components;

// import org.example.BUS.VeBUS;

// import java.awt.BorderLayout;
// import java.awt.Color;
// import java.awt.FlowLayout;
// import java.awt.Font;
// import java.awt.GridLayout;
// import java.util.ArrayList;
// import java.util.HashSet;
// import java.util.Set;
// import javax.swing.BorderFactory;
// import javax.swing.JButton;
// import javax.swing.JDialog;
// import javax.swing.JLabel;
// import javax.swing.JOptionPane;
// import javax.swing.JPanel;
// import javax.swing.border.EmptyBorder;

// public class SeatSelectionDialog extends JDialog {

//     private int selectedMaGhe = -1;
//     private String selectedSeatLabel;

//     public SeatSelectionDialog(int maSuatChieuPhim, VeBUS veBUS) {
//         setTitle("Chọn ghế");
//         setSize(500, 400);
//         setModal(true);
//         setLocationRelativeTo(null);
//         setBackground(Color.WHITE);

//         JPanel main = new JPanel(new BorderLayout(10, 10));
//         main.setBorder(new EmptyBorder(15, 15, 15, 15));
//         main.setBackground(Color.WHITE);

//         // Legend
//         JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
//         legend.setBackground(Color.WHITE);
//         legend.add(createLegendLabel("", new Color(76, 175, 80)));
//         legend.add(new JLabel(" Ghế trống"));
//         legend.add(createLegendLabel("", new Color(244, 67, 54)));
//         legend.add(new JLabel(" Ghế đã đặt"));
//         legend.add(createLegendLabel("", new Color(33, 150, 243)));
//         legend.add(new JLabel(" Ghế đang chọn"));

//         // Lấy danh sách ghế trống từ BUS
//         ArrayList<String> gheTrongList = veBUS.getGheTrong(maSuatChieuPhim);
//         Set<Integer> availableIds = new HashSet<>();
//         for (String s : gheTrongList) {
//             try {
//                 availableIds.add(parseMaGheFromString(s));
//             } catch (Exception ignored) {
//             }
//         }

//         int rows = 8;
//         int cols = 8;
//         JPanel grid = new JPanel(new GridLayout(rows, cols, 8, 8));
//         grid.setBackground(Color.WHITE);

//         Color AVAILABLE = new Color(76, 175, 80);
//         Color BOOKED = new Color(244, 67, 54);
//         Color SELECTED = new Color(33, 150, 243);
//         Font seatFont = new Font("Segoe UI", Font.BOLD, 12);

//         final JButton[] currentSelected = { null };

//         int seatId = 1;
//         for (int r = 0; r < rows; r++) {
//             char rowChar = (char) ('A' + r);
//             for (int c = 0; c < cols; c++) {
//                 String label = rowChar + String.valueOf(c + 1);
//                 JButton btn = new JButton(label);
//                 btn.putClientProperty("maGhe", seatId);
//                 btn.setFont(seatFont);
//                 btn.setFocusPainted(false);
//                 btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

//                 if (availableIds.contains(seatId)) {
//                     btn.setBackground(AVAILABLE);
//                     btn.setForeground(Color.WHITE);
//                     btn.setEnabled(true);
//                     btn.addActionListener(e -> {
//                         if (!btn.isEnabled()) {
//                             return;
//                         }
//                         if (currentSelected[0] != null && currentSelected[0] != btn) {
//                             currentSelected[0].setBackground(AVAILABLE);
//                         }
//                         currentSelected[0] = btn;
//                         btn.setBackground(SELECTED);
//                         Object idObj = btn.getClientProperty("maGhe");
//                         if (idObj instanceof Integer id) {
//                             selectedMaGhe = id;
//                         }
//                         selectedSeatLabel = label;
//                     });
//                 } else {
//                     btn.setBackground(BOOKED);
//                     btn.setForeground(Color.WHITE);
//                     btn.setEnabled(false);
//                 }

//                 grid.add(btn);
//                 seatId++;
//             }
//         }

//         JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
//         bottom.setBackground(Color.WHITE);
//         JButton btnCancel = new JButton("Hủy");
//         btnCancel.addActionListener(e -> {
//             selectedMaGhe = -1;
//             selectedSeatLabel = null;
//             dispose();
//         });
//         JButton btnOK = new JButton("Chọn");
//         btnOK.addActionListener(e -> {
//             if (selectedMaGhe == -1) {
//                 JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 ghế", "Thông báo",
//                         JOptionPane.WARNING_MESSAGE);
//                 return;
//             }
//             dispose();
//         });
//         bottom.add(btnCancel);
//         bottom.add(btnOK);

//         main.add(legend, BorderLayout.NORTH);
//         main.add(grid, BorderLayout.CENTER);
//         main.add(bottom, BorderLayout.SOUTH);

//         add(main);
//     }

//     private JLabel createLegendLabel(String text, Color color) {
//         JLabel label = new JLabel(text);
//         label.setOpaque(true);
//         label.setBackground(color);
//         label.setPreferredSize(new java.awt.Dimension(20, 20));
//         return label;
//     }

//     private int parseMaGheFromString(String gheStr) {
//         return Integer.parseInt(
//                 gheStr.split("\\(MaGhe: ")[1].replace(")", "")
//         );
//     }

//     public int getSelectedMaGhe() {
//         return selectedMaGhe;
//     }

//     public String getSelectedSeatLabel() {
//         return selectedSeatLabel;
//     }
// }

