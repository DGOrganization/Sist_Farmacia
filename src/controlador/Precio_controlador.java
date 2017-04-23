/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Inventario;
import entidades.Precio;
import java.util.List;
import modelo.Precio_modelo;

/**
 *
 * @author dakrpastiursSennin
 */
public class Precio_controlador {
    public List<Precio> Obtener(Inventario pInventario){
        return new Precio_modelo().ListarPrecioInv(pInventario);
    }
}
