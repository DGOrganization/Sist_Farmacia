/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Bodega;
import entidades.Inventario;
import entidades.Precio;
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
public class Precio_modelo {
    
    public List<Precio> ListarPrecioInv(Inventario pInventario){
        List<Precio> lista = new ArrayList<>();
        Conexion conn = new Conexion();
        try{
            if(conn.Conectar()){
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenerprecioinv(?) }");
                cmd.setInt(1, pInventario.getId());
                if(cmd.execute()){
                    ResultSet resultado = cmd.getResultSet();
                    while(resultado.next()){
                        Precio precio = new Precio();
                        precio.setId(resultado.getInt("codigo"));
                        precio.setCantidad(resultado.getBigDecimal("cant"));
                        precio.setTipo(resultado.getString("tipo"));
                        precio.setEstado(resultado.getBoolean("est"));
                        lista.add(precio);
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
    
}
