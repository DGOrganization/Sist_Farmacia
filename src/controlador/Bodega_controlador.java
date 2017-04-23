/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Bodega;
import java.util.Collections;
import java.util.List;
import modelo.Bodega_modelo;

/**
 *
 * @author dakrpastiursSennin
 */
public class Bodega_controlador {
    public List<Bodega> Obtener(){
        List<Bodega> lista = new Bodega_modelo().ListarBodegas();       
        Collections.sort(lista, (Bodega inv1, Bodega inv2) -> {
            return inv1.toString().compareTo(inv2.toString());
        });
        return lista;
    }
    
    public Bodega Obtener(Bodega pBodega){
        return new Bodega_modelo().ListarBodega(pBodega);
    }
}
