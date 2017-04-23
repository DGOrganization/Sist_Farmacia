/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Departamento;
import entidades.Municipio;
import entidades.Persona;
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
public class Municipio_modelo {

    public List<Municipio> ListarMunicipios() {
        List<Municipio> lista = new ArrayList<>();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenermunicipios() }")) {
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        Municipio municipio = new Municipio();
                        municipio.setId(resultado.getInt("codigo"));
                        municipio.setNombre(resultado.getString("nom"));
                        municipio.setEstado(resultado.getBoolean("est"));
                        lista.add(municipio);
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
        return lista;
    }

    public List<Municipio> ListarMunicipio(Departamento pDepartamento) {
        List<Municipio> lista = new ArrayList<>();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenermunicipiodepto(?) }")) {
            cmd.setInt(1, pDepartamento.getId());
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        Municipio municipio = new Municipio();
                        municipio.setId(resultado.getInt("codigo"));
                        municipio.setNombre(resultado.getString("nom"));
                        municipio.setEstado(resultado.getBoolean("est"));
                        lista.add(municipio);
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
        return lista;
    }

    public Municipio ListarMunicipio(Persona pPersona) {
        Municipio municipio = new Municipio();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenermunicipiopersona(?) }")) {
            cmd.setInt(1, pPersona.getIdPersona());
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    if (resultado.next()) {
                        municipio.setId(resultado.getInt("codigo"));
                        municipio.setNombre(resultado.getString("nom"));
                        municipio.setEstado(resultado.getBoolean("est"));
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
        return municipio;
    }
}
