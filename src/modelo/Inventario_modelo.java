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
import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.util.PGobject;

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
                        inventario.setArticulo(new Articulo_modelo().ListarArticulo(new Articulo(resultado.getInt("articulo"))));
                        inventario.setCategoria(new Categoria_modelo().ListarCategoria(new Categoria(resultado.getInt("categoria"))));
                        inventario.setUnidad(new Unidad_modelo().ListarUnidad(new Unidad(resultado.getInt("unidad"))));
                        inventario.setEstado(resultado.getBoolean("est"));
                        inventario.setStock(resultado.getBigDecimal("stck"));
                        inventario.setStockMin(resultado.getInt("stckmin"));
                        inventario.setStockMax(resultado.getInt("stckmax"));
                        inventario.setPrecio(new Precio_modelo().ListarPrecioInv(inventario));
                        inventario.setVencimiento(resultado.getDate("vencimiento"));
                        inventario.setBodega(new Bodega_modelo().ListarBodega(new Bodega(resultado.getInt("bodega"))));
                        inventario.setImagen(new Imagen_modelo().ListarImagen(inventario));
                        lista.add(inventario);
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
    
    public boolean Registrar(Inventario pInventario){
        boolean exito = false;
        Conexion conn = new Conexion();
        try{
            if(conn.Conectar()){
                CallableStatement cmd = conn.getConnection().prepareCall("{ call registrarinventario(?,?,?,?,?,?,?,?,?,?) }");
                cmd.setString(1, pInventario.getArticulo().getNombre());
                cmd.setString(2, pInventario.getArticulo().getDescripcion());
                cmd.setInt(3, pInventario.getUnidad().getId());
                cmd.setInt(4, pInventario.getCategoria().getId());
                cmd.setInt(5, pInventario.getBodega().getId());
                cmd.setInt(6, pInventario.getStockMin());
                cmd.setInt(7, pInventario.getStockMax());
                cmd.setDate(8, pInventario.getVencimiento());
                JSONArray precios = new JSONArray();
                pInventario.getPrecio().stream().forEach(datos ->{
                    JSONObject precio = new JSONObject();
                    precio.put("cant", datos.getCantidad());
                    precios.put(precio);
                });
                PGobject invprecios = new PGobject();
                invprecios.setType("json");
                invprecios.setValue(precios.toString());
                cmd.setObject(9, invprecios);
                cmd.setString(10, pInventario.getImagen().getUrl());
                exito = cmd.execute();
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
        return exito;
    }
    
}
