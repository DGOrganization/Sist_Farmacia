/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import entidades.Menu;
import entidades.Nivel;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author dakrpastiursSennin
 */
public class Menu_modelo {

    public List<Menu> obtenerMenus(Nivel pNivel) {
        List<Menu> menus = new ArrayList<>();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call cargarpermisos(?) }")) {
            cmd.setInt(1, pNivel.getId());
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        Menu menu = new Menu();
                        menu.setId(resultado.getInt("codigo"));
                        menu.setNombre(resultado.getString("menu"));
                        menu.setPermiso(resultado.getBoolean("permiso"));
                        menus.add(menu);
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    "Sistema de Compras y Ventas - Menu",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return menus;
    }
}
