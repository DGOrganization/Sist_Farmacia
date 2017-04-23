/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Categoria;
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
public class Categoria_modelo {

    public List<Categoria> ListarCategorias() {
        List<Categoria> lista = new ArrayList<>();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenercategorias() }")) {
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        Categoria categoria = new Categoria();
                        categoria.setId(resultado.getInt("codigo"));
                        categoria.setNombre(resultado.getString("categoria"));
                        categoria.setEstado(resultado.getBoolean("estado"));
                        lista.add(categoria);
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

    public Categoria ListarCategoria(Categoria pCategoria) {
        Categoria categoria = new Categoria();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenercategoria(?) }")) {
            cmd.setInt(1, pCategoria.getId());
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        categoria.setId(resultado.getInt("codigo"));
                        categoria.setNombre(resultado.getString("categoria"));
                        categoria.setEstado(resultado.getBoolean("estado"));
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
        return categoria;
    }
}
