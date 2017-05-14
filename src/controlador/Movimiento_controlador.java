/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Movimiento;
import java.util.List;
import java.util.stream.Collectors;
import modelo.Movimiento_modelo;

/**
 *
 * @author dakrpastiursSennin
 */
public class Movimiento_controlador {
    
    public List<Movimiento> Obtener(){
        List<Movimiento> lista = new Movimiento_modelo().ListarMovimientos();
        lista =lista.stream().sorted((Movimiento m1, Movimiento m2) -> {
            return Long.compare(m1.getId(), m2.getId());
        }).collect(Collectors.toList());
        return lista;
    }
    
}
