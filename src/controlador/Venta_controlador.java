/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Venta;
import java.util.Collections;
import java.util.List;
import modelo.Venta_modelo;

/**
 *
 * @author dakrpastiursSennin
 */
public class Venta_controlador {
    
    public boolean Registrar(Venta pVenta){
        return new Venta_modelo().Registrar(pVenta);
    }
    
    public List<Venta> Obtener(){
        List<Venta> lista = new Venta_modelo().ListarVentas();
        Collections.sort(lista, (Venta v1, Venta v2) -> {
            return v2.getFecha().compareTo(v1.getFecha());
        });
        return lista;
    }
    
    public boolean Anular(Venta pVenta){
        return new Venta_modelo().Anular(pVenta);
    }
}
