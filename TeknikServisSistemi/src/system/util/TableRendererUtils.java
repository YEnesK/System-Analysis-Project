package system.util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class TableRendererUtils {
    public static void setRenderer(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Integer.class, centerRenderer);
    }
}
