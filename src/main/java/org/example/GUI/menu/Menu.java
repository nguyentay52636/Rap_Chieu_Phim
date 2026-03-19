package org.example.GUI.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import org.example.GUI.menu.mode.LightDarkMode;
import org.example.GUI.menu.mode.ToolBarAccentColor;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;

public class Menu extends JPanel {
    private static final int ARC = 10;

    private final String menuItems[][] = {

            { "Quản lý Phim" },
            { "Phòng chiếu" },
            { "Sản Phẩm" },
            { "Thể Loại Phim" },
            { "Quản lý suất chiếu" },
            { "Quản lý vé" },
            { "Hóa đơn" },
            { "Khách hàng" },
            { "Nhân viên" },
            { "Thống kê" }
    };

    public boolean isMenuFull() {
        return menuFull;
    }

    public void setMenuFull(boolean menuFull) {
        this.menuFull = menuFull;
        if (menuFull) {
            header.setText(headerName);
            header.setHorizontalAlignment(JLabel.CENTER);
            // header.setHorizontalAlignment(getComponentOrientation().isLeftToRight() ?
            // JLabel.LEFT : JLabel.RIGHT);
        } else {
            header.setText("");
            header.setHorizontalAlignment(JLabel.CENTER);
        }
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem) {
                ((MenuItem) com).setFull(menuFull);
            }
        }
        lightDarkMode.setMenuFull(menuFull);
        toolBarAccentColor.setMenuFull(menuFull);
    }

    private final List<MenuEvent> events = new ArrayList<>();
    private boolean menuFull = true;
    private final String headerName = "";

    protected final boolean hideMenuTitleOnMinimum = true;
    protected final int menuTitleLeftInset = 5;
    protected final int menuTitleVgap = 5;
    protected final int menuMaxWidth = 250;
    protected final int menuMinWidth = 60;
    protected final int headerFullHgap = 5;

    public Menu() {
        init();
    }

    private void init() {
        setLayout(new MenuLayout());
        putClientProperty(FlatClientProperties.STYLE, ""
                + "border:20,2,2,2;"
                + "arc:" + ARC);
        // We paint our own background (gradient in Light mode)
        setOpaque(false);
        header = new JLabel(headerName);
        java.net.URL imgURL = getClass().getResource("/org/example/GUI/menu/logo/logojavawing.png");
        if (imgURL != null) {
            header.setIcon(new ImageIcon(imgURL));
        } else {
            System.err.println("Couldn't find file: logojavawing.png");
        }
        header.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$Menu.header.font;"
                + "foreground:$Menu.foreground");

        // Menu
        scroll = new JScrollPane();
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        panelMenu = new JPanel(new MenuItemLayout(this));
        panelMenu.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5;"
                + "background:null");
        panelMenu.setOpaque(false);

        scroll.setViewportView(panelMenu);
        scroll.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:null");
        JScrollBar vscroll = scroll.getVerticalScrollBar();
        vscroll.setUnitIncrement(10);
        // Chỉ style màu cho scrollbar để tránh các lỗi liên quan đến insets/width trên JDK 17
        vscroll.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.ScrollBar.background;"
                + "thumb:$Menu.ScrollBar.thumb");
        createMenu();
        lightDarkMode = new LightDarkMode();
        toolBarAccentColor = new ToolBarAccentColor(this);
        toolBarAccentColor.setVisible(FlatUIUtils.getUIBoolean("AccentControl.show", false));
        add(header);
        add(scroll);
        add(lightDarkMode);
        add(toolBarAccentColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int arc = UIScale.scale(ARC);
            int w = getWidth();
            int h = getHeight();
            Shape shape = new RoundRectangle2D.Float(0, 0, w, h, arc, arc);

            if (FlatLaf.isLafDark()) {
                // Keep current dark background from theme
                Color bg = FlatUIUtils.getUIColor("Menu.background", getBackground());
                g2.setColor(bg);
            } else {
                // Light mode gradient background (slightly lighter purple)
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(30, 30, 60),
                        0, h, new Color(140, 0, 120));
                g2.setPaint(gp);
            }
            g2.fill(shape);
        } finally {
            g2.dispose();
        }
        super.paintComponent(g);
    }

    private void createMenu() {
        int index = 0;
        for (int i = 0; i < menuItems.length; i++) {
            String menuName = menuItems[i][0];
            if (menuName.startsWith("~") && menuName.endsWith("~")) {
                panelMenu.add(createTitle(menuName));
            } else {
                MenuItem menuItem = new MenuItem(this, menuItems[i], index++, events);
                panelMenu.add(menuItem);
            }
        }
    }

    private JLabel createTitle(String title) {
        String menuName = title.substring(1, title.length() - 1);
        JLabel lbTitle = new JLabel(menuName);
        lbTitle.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$Menu.label.font;"
                + "foreground:$Menu.title.foreground");
        return lbTitle;
    }

    public void setSelectedMenu(int index, int subIndex) {
        runEvent(index, subIndex);
    }

    protected void setSelected(int index, int subIndex) {
        int size = panelMenu.getComponentCount();
        for (int i = 0; i < size; i++) {
            Component com = panelMenu.getComponent(i);
            if (com instanceof MenuItem) {
                MenuItem item = (MenuItem) com;
                if (item.getMenuIndex() == index) {
                    item.setSelectedIndex(subIndex);
                } else {
                    item.setSelectedIndex(-1);
                }
            }
        }
    }

    protected void runEvent(int index, int subIndex) {
        MenuAction menuAction = new MenuAction();
        for (MenuEvent event : events) {
            event.menuSelected(index, subIndex, menuAction);
        }
        if (!menuAction.isCancel()) {
            setSelected(index, subIndex);
        }
    }

    public void addMenuEvent(MenuEvent event) {
        events.add(event);
    }

    public void hideMenuItem() {
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem) {
                ((MenuItem) com).hideMenuItem();
            }
        }
        revalidate();
    }

    public boolean isHideMenuTitleOnMinimum() {
        return hideMenuTitleOnMinimum;
    }

    public int getMenuTitleLeftInset() {
        return menuTitleLeftInset;
    }

    public int getMenuTitleVgap() {
        return menuTitleVgap;
    }

    public int getMenuMaxWidth() {
        return menuMaxWidth;
    }

    public int getMenuMinWidth() {
        return menuMinWidth;
    }

    private JLabel header;
    private JScrollPane scroll;
    private JPanel panelMenu;
    private LightDarkMode lightDarkMode;
    private ToolBarAccentColor toolBarAccentColor;

    private class MenuLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(5, 5);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets insets = parent.getInsets();
                int x = insets.left;
                int y = insets.top;
                int gap = UIScale.scale(5);
                int sheaderFullHgap = UIScale.scale(headerFullHgap);
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int iconWidth = width;
                int iconHeight = header.getPreferredSize().height;
                int hgap = menuFull ? sheaderFullHgap : 0;
                int accentColorHeight = 0;
                if (toolBarAccentColor.isVisible()) {
                    accentColorHeight = toolBarAccentColor.getPreferredSize().height + gap;
                }

                header.setBounds(x + hgap, y, iconWidth - (hgap * 2), iconHeight);
                int ldgap = UIScale.scale(10);
                int ldWidth = width - ldgap * 2;
                int ldHeight = lightDarkMode.getPreferredSize().height;
                int ldx = x + ldgap;
                int ldy = y + height - ldHeight - ldgap - accentColorHeight;

                int menux = x;
                int menuy = y + iconHeight + gap;
                int menuWidth = width;
                int menuHeight = height - (iconHeight + gap) - (ldHeight + ldgap * 2) - (accentColorHeight);
                scroll.setBounds(menux, menuy, menuWidth, menuHeight);

                lightDarkMode.setBounds(ldx, ldy, ldWidth, ldHeight);

                if (toolBarAccentColor.isVisible()) {
                    int tbheight = toolBarAccentColor.getPreferredSize().height;
                    int tbwidth = Math.min(toolBarAccentColor.getPreferredSize().width, ldWidth);
                    int tby = y + height - tbheight - ldgap;
                    int tbx = ldx + ((ldWidth - tbwidth) / 2);
                    toolBarAccentColor.setBounds(tbx, tby, tbwidth, tbheight);
                }
            }
        }
    }
}
