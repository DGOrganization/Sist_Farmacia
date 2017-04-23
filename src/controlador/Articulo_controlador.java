/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Articulo;
import modelo.Articulo_modelo;

/**
 *
 * @author dakrpastiursSennin
 */
public class Articulo_controlador {
    public Articulo Obtener(Articulo pArticulo){
        return new Articulo_modelo().ListarArticulo(pArticulo);
    }
}
