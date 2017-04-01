/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Inventario;
import entidades.Articulo;
import entidades.Bodega;
import entidades.Categoria;
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
public class Inventario_modelo {
    
    public List<Inventario> ListarInventarioCompleto(){
        List<Inventario> lista = new ArrayList<>();
        Conexion conn = new Conexion();        
        try{
            if(conn.Conectar()){
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenerinventarios() }");
                if(cmd.execute()){
                    ResultSet resultado = cmd.getResultSet();
                    while(resultado.next()){
                        Inventario inventario = new Inventario();
                        inventario.setId(resultado.getInt("codigo"));
                        inventario.setProducto(new Articulo_modelo().ListarArticulo(new Articulo(resultado.getInt("articulo"))));
                        inventario.setCategoria(new Categoria_modelo().ListarCategoria(new Categoria(resultado.getInt("categoria"))));
                        inventario.setUnidad(new Unidad_modelo().ListarUnidad(new Unidad(resultado.getInt("unidad"))));
                        inventario.setEstado(resultado.getBoolean("est"));
                        inventario.setStock(resultado.getBigDecimal("stck"));
                        inventario.setStockMin(resultado.getBigDecimal("stckmin"));
                        inventario.setStockMax(resultado.getBigDecimal("stckmax"));
                        inventario.setPrecio(new Precio_modelo().ListarPrecioInv(inventario));
                        inventario.setVencimiento(resultado.getDate("vencimiento"));
                        inventario.setBodega(new Bodega_modelo().ListaBodega(new Bodega(resultado.getInt("bodega"))));
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
        } finally {
            conn.Desconectar();
        }
        
        return lista;
    }
    
}
