/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Inventario;
import modelo.Inventario_modelo;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author dakrpastiursSennin
 */
public class Inventario_controlador {
    
    public List<Inventario> Obtener(){
        List<Inventario> lista = new Inventario_modelo().ListarInventarioCompleto();       
        Collections.sort(lista, (Inventario inv1, Inventario inv2) -> {
            return inv1.toString().compareTo(inv2.toString());
        });
        return lista;
    }
    
    public boolean Registrar(Inventario pInventario){
        return new Inventario_modelo().Registrar(pInventario);
    }
}
