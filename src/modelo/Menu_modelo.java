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
        Conexion conn = new Conexion();
        try {
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call cargarpermisos(?) }");
                cmd.setInt(1, pNivel.getId());
                if (cmd.execute()) {
                    ResultSet lector = cmd.getResultSet();
                    while (lector.next()) {
                        Menu menu = new Menu();
                        menu.setId(lector.getInt("codigo"));
                        menu.setNombre(lector.getString("menu"));
                        menu.setPermiso(lector.getBoolean("permiso"));
                        menus.add(menu);
                    }
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    "Sistema de Compras y Ventas - Menu",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            conn.Desconectar();
        }
        return menus;
    }
}
