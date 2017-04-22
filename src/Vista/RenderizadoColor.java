/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import entidades.Inventario;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author dakrpastiursSennin
 */
public class RenderizadoColor extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (table.getValueAt(row, column) instanceof Inventario) {
            Inventario inv = (Inventario) table.getValueAt(row, column);
            //boolean valid = inv.getStock().compareTo(BigDecimal.ZERO) == 0 || inv.getStock().compareTo(new BigDecimal(inv.getStockMin())) <= 0;
            //c.setBackground(valid ? Color.green : Color.red);
            if (inv.getStock().compareTo(BigDecimal.ZERO) == 0 || inv.getStock().compareTo(new BigDecimal(inv.getStockMin())) <= 0) {
                //System.out.println("Se cambiara de color a " + inv.toString());
                c.setForeground(Color.RED);
            } else {
                c.setForeground(Color.BLACK);
            }
        }
        return c;
    }

}
