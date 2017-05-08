/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Nivel;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.util.PGobject;

/**
 *
 * @author dakrpastiursSennin
 */
public class Nivel_modelo {

    public Nivel ListarNivel(Nivel pNivel, boolean listarMenus) {
        Nivel nivel = new Nivel();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenernivel(?) }")) {
            cmd.setInt(1, pNivel.getId());
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        nivel.setId(resultado.getInt("codigo"));
                        nivel.setNombre(resultado.getString("nivel"));
                        nivel.setEstado(resultado.getBoolean("est"));
                        if (listarMenus) {
                            nivel.setMenus(new Menu_modelo().obtenerMenus(pNivel));
                        }
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
        return nivel;
    }

    public List<Nivel> ListarNiveles(boolean listarMenus) {
        List<Nivel> lista = new ArrayList<>();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenerniveles() }")) {
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        Nivel nivel = new Nivel();
                        nivel.setId(resultado.getInt("codigo"));
                        nivel.setNombre(resultado.getString("nivel"));
                        nivel.setEstado(resultado.getBoolean("est"));
                        if (listarMenus) {
                            nivel.setMenus(new Menu_modelo().obtenerMenus(nivel));
                        }
                        if (nivel.getMenus().isEmpty()) {
                            nivel.setMenus(new Menu_modelo().obtenerMenus());
                        }
                        lista.add(nivel);
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

    public boolean Registrar(Nivel pNivel){
        boolean exito = false;
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call registrarnivel(?) }")){
                cmd.setString(1, pNivel.getNombre());
                exito = cmd.execute();
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return exito;
    }
    
    public boolean Editar(Nivel pNivel){
        boolean exito = false;
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call editarnivel(?,?) }")){
                cmd.setString(1, pNivel.getNombre());
                cmd.setInt(2, pNivel.getId());
                exito = cmd.execute();
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return exito;
    }
    
    public boolean Elimnar(Nivel pNivel){
        boolean exito = false;
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call eliminarnivel(?) }")){
                cmd.setInt(1, pNivel.getId());
                exito = cmd.execute();
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return exito;
    }
    
    public boolean Asignar(Nivel pNivel) {
        boolean exito = false;

        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call insertarpermisos(?,?) }")) {
            cmd.setInt(1, pNivel.getId());
            JSONArray menus = new JSONArray();
            pNivel.getMenus().stream().forEach(datos -> {
                JSONObject menu = new JSONObject();
                menu.put("codmenu", datos.getId());
                menu.put("perm", datos.isPermiso());
                menus.put(menu);
            });
            PGobject menuso = new PGobject();
            menuso.setType("json");
            System.out.println("Menu \n" + menus.toString());
            menuso.setValue(menus.toString());
            cmd.setObject(2, menuso);
            exito = cmd.execute();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return exito;
    }

}
