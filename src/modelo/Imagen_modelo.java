/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Imagen;
import entidades.Inventario;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author dakrpastiursSennin
 */
public class Imagen_modelo {

    public Imagen ListarImagen(Inventario pInventario) {
        Imagen imagen = new Imagen();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenerimagen(?) }")) {
            cmd.setInt(1, pInventario.getId());
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        imagen.setId(resultado.getInt("codigo"));
                        imagen.setUrl(resultado.getString("url"));
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre") + " - " + this.getClass().getName(),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return imagen;
    }
}