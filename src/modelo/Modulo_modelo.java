/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import entidades.Menu;
import entidades.Modulo;
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
public class Modulo_modelo {
    
    public Modulo obtenerModulo(Menu pMenu) {
        Modulo modulo = new Modulo();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenermodulo(?) }")) {
            cmd.setInt(1, pMenu.getId());
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        modulo.setId(resultado.getInt("codigo"));
                        modulo.setNombre(resultado.getString("modulo"));
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
        return modulo;
    }
    
    public List<Modulo> obtenerModulos() {
        List<Modulo> lista = new ArrayList<>();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenermodulos() }")) {
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        Modulo modulo = new Modulo();
                        modulo.setId(resultado.getInt("codigo"));
                        modulo.setNombre(resultado.getString("modulo"));
                        lista.add(modulo);
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
        return lista;
    }
}
