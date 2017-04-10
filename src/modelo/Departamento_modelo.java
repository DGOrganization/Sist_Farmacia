/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Departamento;
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
public class Departamento_modelo {
    public List<Departamento> ListarDepartamentos(){
        List<Departamento> lista = new ArrayList<>();
        Conexion conn = new Conexion();
        try{
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenerdepartamentos() }");
                if(cmd.execute()){
                    ResultSet resultado = cmd.getResultSet();
                    while(resultado.next()){
                        Departamento depto = new Departamento();
                        depto.setId(resultado.getInt("codigo"));
                        depto.setNombre(resultado.getString("nom"));
                        depto.setEstado(resultado.getBoolean("est"));
                        depto.setMunicipios(new Municipio_modelo().ListarMunicipio(depto));
                        lista.add(depto);
                    }
                }
            }
        } catch(SQLException ex){
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre") + " - " + this.getClass().getName(),
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            conn.Desconectar();
        }
        return lista;
    }
    
    public Departamento ListarDepartamento(Departamento pDepartamento){
        Departamento depto = new Departamento();
        Conexion conn = new Conexion();
        try{
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenerdepartamento(?) }");
                cmd.setInt(1, pDepartamento.getId());
                if(cmd.execute()){
                    ResultSet resultado = cmd.getResultSet();
                    while(resultado.next()){
                        depto.setId(resultado.getInt("codigo"));
                        depto.setNombre(resultado.getString("nom"));
                        depto.setEstado(resultado.getBoolean("est"));
                        depto.setMunicipios(new Municipio_modelo().ListarMunicipio(depto));
                    }
                }
            }
        } catch(SQLException ex){
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre") + " - " + this.getClass().getName(),
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            conn.Desconectar();
        }
        return depto;
    }
}
