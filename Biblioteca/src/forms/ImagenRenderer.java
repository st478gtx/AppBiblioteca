package forms;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ImagenRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        setText("");

        if (value != null) {
            ImageIcon icon = new ImageIcon(value.toString());

            Image imagen = icon.getImage().getScaledInstance(
                    50, 70, Image.SCALE_SMOOTH);

            setIcon(new ImageIcon(imagen));
        } else {
            setIcon(null);
        }

        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);

        return this;
    }
}
