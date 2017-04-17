/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Compra;
import modelo.Compra_modelo;

/**
 *
 * @author dakrpastiursSennin
 */
public class Compra_controlador {
 
    public boolean Registrar(Compra pCompra){
        return new Compra_modelo().Registrar(pCompra);
    }
    
}
