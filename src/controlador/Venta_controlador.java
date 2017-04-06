/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Venta;
import modelo.Venta_modelo;

/**
 *
 * @author dakrpastiursSennin
 */
public class Venta_controlador {
    
    public boolean Registrar(Venta pVenta){
        return new Venta_modelo().Registrar(pVenta);
    }
}
