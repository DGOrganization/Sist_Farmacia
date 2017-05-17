/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import controlador.Inventario_controlador;
import entidades.Inventario;
import entidades.Movimiento;
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
public class Movimiento_modelo {
    public List<Movimiento> ListarMovimientos(){
        List<Movimiento> lista = new ArrayList<>();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenermovimientos() }")) {
            if(cmd.execute()){
                try(ResultSet resultado = cmd.getResultSet()){
                    while(resultado.next()){
                        Movimiento movimiento = new Movimiento();
                        movimiento.setId(resultado.getLong("codigo"));
                        movimiento.setCantidad(resultado.getBigDecimal("cantidad"));
                        movimiento.setComentario(resultado.getString("comenta"));
                        movimiento.setFecha(resultado.getDate("fech"));
                        movimiento.setInventario(new Inventario_controlador().Obtener(new Inventario(resultado.getInt("codinv"))));
                        movimiento.setEstado(resultado.getBoolean("est"));
                        lista.add(movimiento);
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
}
