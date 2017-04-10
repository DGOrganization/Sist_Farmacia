/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Proveedor;
import java.util.List;
import modelo.Proveedor_modelo;

/**
 *
 * @author dakrpastiursSennin
 */
public class Proveedor_controlador {
    public List<Proveedor> Obtener(){
        List<Proveedor> lista = new Proveedor_modelo().ObtenerProveedores();
        lista.stream().sorted((Proveedor prov1, Proveedor prov2)-> {
            return prov1.getNombre().compareTo(prov2.getNombre());
        });
        return lista;
    }
    
    public boolean Registrar(Proveedor pProveedor){
        return new Proveedor_modelo().Registrar(pProveedor);
    }
    
    public boolean Editar(Proveedor pProveedor){
        return new Proveedor_modelo().Editar(pProveedor);
    }
    
    public boolean Eliminar(Proveedor pProveedor){
        return new Proveedor_modelo().Eliminar(pProveedor);
    }
}
