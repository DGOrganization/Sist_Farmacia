/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Unidad;
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
public class Unidad_modelo {

    public List<Unidad> ListarUnidades() {
        List<Unidad> lista = new ArrayList<>();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenerunidades() }")) {
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        Unidad unidad = new Unidad();
                        unidad.setId(resultado.getInt("codigo"));
                        unidad.setNombre(resultado.getString("unidad"));
                        unidad.setAbreviado(resultado.getString("abrev"));
                        lista.add(unidad);
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return lista;
    }

    public Unidad ListarUnidad(Unidad pUnidad) {
        Unidad unidad = new Unidad();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenerunidad(?) }")) {
            cmd.setInt(1, pUnidad.getId());
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        unidad.setId(resultado.getInt("codigo"));
                        unidad.setNombre(resultado.getString("unidad"));
                        unidad.setAbreviado(resultado.getString("abrev"));
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return unidad;
    }

}