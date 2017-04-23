/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import controlador.Inventario_controlador;
import controlador.Unidad_controlador;
import entidades.Compra;
import entidades.DetalleCompra;
import entidades.Inventario;
import entidades.Unidad;
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
public class DetalleCompra_modelo {
    
    public List<DetalleCompra> ObtenerDetalle(Compra pCompra){
        List<DetalleCompra> lista = new ArrayList<>();
        try (
            java.sql.Connection conn = new Conexion().getConnection();
            CallableStatement cmd = conn.prepareCall("{ call obtenerdetallescompra(?) }")){
                cmd.setLong(1, pCompra.getId());
                if(cmd.execute()){
                    try (ResultSet resultado = cmd.getResultSet()){
                    while(resultado.next()){
                        DetalleCompra detalle = new DetalleCompra();
                        detalle.setInventario(new Inventario_controlador().Obtener(new Inventario(resultado.getInt("codinv"))));
                        detalle.setCantidad(resultado.getBigDecimal("cant"));
                        detalle.setUnidad(new Unidad_controlador().Obtener(new Unidad(resultado.getInt("unidad"))));
                        detalle.setPrecio(resultado.getBigDecimal("precio"));
                        detalle.setImporte(resultado.getBigDecimal("imp"));
                        lista.add(detalle);
                    }
                }
                }
        } catch(SQLException ex){
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
    
}
