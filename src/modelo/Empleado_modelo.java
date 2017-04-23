/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Empleado;
import entidades.Persona;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author dakrpastiursSennin
 */
public class Empleado_modelo {

    public Empleado ListarEmpleado(Empleado pEmpleado) {
        Empleado empleado = new Empleado();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenerempleado(?) }")) {
            cmd.setInt(1, pEmpleado.getId());
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        empleado.setId(resultado.getInt("codigo"));
                        empleado.setNombre(resultado.getString("nombre"));
                        empleado.setApellidoPaterno(resultado.getString("apellido1"));
                        empleado.setApellidoMaterno(resultado.getObject("apellido2") == null ? "" : resultado.getString("apellido2"));
                        empleado.setDui(resultado.getString("dui"));
                        empleado.setNit(resultado.getString("nit"));
                        empleado.setSexo(resultado.getString("genero").equals("F") ? "Femenino" : "Masculino");
                        empleado.setNacimiento(resultado.getDate("nacimiento"));
                        empleado.setDireccion(resultado.getString("direccion"));
                        empleado.setEmail(resultado.getObject("correo") == null ? "" : resultado.getString("correo"));
                        empleado.setEstado(resultado.getBoolean("est"));
                        empleado.setMunicipio(new Municipio_modelo().ListarMunicipio(new Persona(resultado.getInt("codpersona"))));
                        empleado.setTelefono(new Telefono_modelo().ListarTelefonos(new Persona(resultado.getInt("codpersona"))));
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
        return empleado;
    }

}
