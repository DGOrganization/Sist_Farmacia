/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Proveedor;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author dakrpastiursSennin
 */
public class Proveedor_modelo {

    public List<Proveedor> ObtenerProveedores() {
        List<Proveedor> lista = new ArrayList<>();
        try (
            java.sql.Connection conn = new Conexion().getConnection();
            CallableStatement cmd = conn.prepareCall("{ call obtenerproveedores() }")){
            if (cmd.execute()) {
                try(ResultSet resultado = cmd.getResultSet()){
                while (resultado.next()) {
                    Proveedor proveedor = new Proveedor();
                    proveedor.setId(resultado.getInt("cod"));
                    proveedor.setNombre(resultado.getString("nombre"));
                    proveedor.setRespresentante(resultado.getString("represent"));
                    proveedor.setNRC(resultado.getString("nrc"));
                    proveedor.setDomicilio(resultado.getString("direccion"));
                    proveedor.setNIT(resultado.getString("nit"));
                    proveedor.setTelefono(resultado.getString("telefono"));
                    proveedor.setCelular(resultado.getString("celular"));
                    proveedor.setEmail(resultado.getObject("email") == null ? "" : resultado.getString("email"));
                    proveedor.setWebsite(resultado.getObject("website") == null ? "" : resultado.getString("website"));
                    proveedor.setEstado(resultado.getBoolean("est"));
                    lista.add(proveedor);
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

    public Proveedor ObtenerProveedor(Proveedor pProveedor) {
        Proveedor proveedor = new Proveedor();
        try (
            java.sql.Connection conn = new Conexion().getConnection();
            CallableStatement cmd = conn.prepareCall("{ call obtenerproveedor(?) }")){
            cmd.setInt(1, pProveedor.getId());
            if (cmd.execute()) {
                try(ResultSet resultado = cmd.getResultSet()){
                while (resultado.next()) {
                    proveedor.setId(resultado.getInt("cod"));
                    proveedor.setNombre(resultado.getString("nombre"));
                    proveedor.setRespresentante(resultado.getString("represent"));
                    proveedor.setNRC(resultado.getString("nrc"));
                    proveedor.setDomicilio(resultado.getString("direccion"));
                    proveedor.setNIT(resultado.getString("nit"));
                    proveedor.setTelefono(resultado.getString("telefono"));
                    proveedor.setCelular(resultado.getString("celular"));
                    proveedor.setEmail(resultado.getObject("email") == null ? "" : resultado.getString("email"));
                    proveedor.setWebsite(resultado.getObject("website") == null ? "" : resultado.getString("website"));
                    proveedor.setEstado(resultado.getBoolean("est"));
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
        return proveedor;
    }

    public boolean Registrar(Proveedor pProveedor) {
        boolean exito = false;
        try (
            java.sql.Connection conn = new Conexion().getConnection();
            CallableStatement cmd = conn.prepareCall("{ call registrarproveedor(?,?,?,?,?,?,?,?,?) }")){
            cmd.setString(1, pProveedor.getNombre());
            cmd.setString(2, pProveedor.getRespresentante());
            cmd.setString(3, pProveedor.getNRC());
            cmd.setString(4, pProveedor.getDomicilio());
            cmd.setString(5, pProveedor.getNIT());
            cmd.setString(6, pProveedor.getTelefono());
            cmd.setString(7, pProveedor.getCelular());
            if (pProveedor.getEmail().isEmpty()) {
                cmd.setNull(8, Types.VARCHAR);
            } else {
                cmd.setString(8, pProveedor.getEmail());
            }

            if (pProveedor.getWebsite().isEmpty()) {
                cmd.setNull(9, Types.VARCHAR);
            } else {
                cmd.setString(9, pProveedor.getWebsite());
            }
            exito = cmd.execute();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han regitrado los datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre") + " - " + this.getClass().getName(),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return exito;
    }

    public boolean Editar(Proveedor pProveedor) {
        boolean exito = false;
        try (
            java.sql.Connection conn = new Conexion().getConnection();
            CallableStatement cmd = conn.prepareCall("{ call editarproveedor(?,?,?,?,?,?,?,?,?,?) }")){
            cmd.setString(1, pProveedor.getNombre());
            cmd.setString(2, pProveedor.getRespresentante());
            cmd.setString(3, pProveedor.getNRC());
            cmd.setString(4, pProveedor.getDomicilio());
            cmd.setString(5, pProveedor.getNIT());
            cmd.setString(6, pProveedor.getTelefono());
            cmd.setString(7, pProveedor.getCelular());
            if (pProveedor.getEmail().isEmpty()) {
                cmd.setNull(8, Types.VARCHAR);
            } else {
                cmd.setString(8, pProveedor.getEmail());
            }

            if (pProveedor.getWebsite().isEmpty()) {
                cmd.setNull(9, Types.VARCHAR);
            } else {
                cmd.setString(9, pProveedor.getWebsite());
            }

            cmd.setInt(10, pProveedor.getId());
            exito = cmd.execute();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han regitrado los datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre") + " - " + this.getClass().getName(),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return exito;
    }

    public boolean Eliminar(Proveedor pProveedor) {
        boolean exito = false;
        try (
            java.sql.Connection conn = new Conexion().getConnection();
            CallableStatement cmd = conn.prepareCall("{ call eliminarproveedor(?) }")){
            cmd.setInt(1, pProveedor.getId());
            exito = cmd.execute();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han regitrado los datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre") + " - " + this.getClass().getName(),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return exito;
    }
}
