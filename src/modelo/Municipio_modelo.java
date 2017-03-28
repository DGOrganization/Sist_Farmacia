/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Municipio;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author dakrpastiursSennin
 */
public class Municipio_modelo {
    
    public List<Municipio> ListarMunicipios(){
        List<Municipio> lista = new ArrayList<>();
        Conexion conn = new Conexion();
        try{
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenermunicipios() }");
                if(cmd.execute()){
                    ResultSet resultado = cmd.getResultSet();
                    while(resultado.next()){
                        Municipio municipio = new Municipio();
                        municipio.setId(resultado.getLong("id"));
                        municipio.setNombre(resultado.getString("nombre"));;
                        municipio.setEstado(resultado.getBoolean("estado"));
                        lista.add(municipio);
                    }
                }
            }
        } catch(Exception ex){
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            conn.Desconectar();
        }
        return lista;
    }
    
    public Municipio ListarMunicipio(Municipio pMunicipio){
        Municipio municipio = new Municipio();
        Conexion conn = new Conexion();
        try{
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenermunicipio(?) }");
                cmd.setLong(1, pMunicipio.getId());
                if(cmd.execute()){
                    ResultSet resultado = cmd.getResultSet();
                    if(resultado.next()){
                        municipio.setId(resultado.getLong("id"));
                        municipio.setNombre(resultado.getString("nombre"));;
                        municipio.setEstado(resultado.getBoolean("estado"));
                    }
                }
            }
        } catch(Exception ex){
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            conn.Desconectar();
        }
        return municipio;
    }
    
}
