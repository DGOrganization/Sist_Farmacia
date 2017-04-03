/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Unidad;
import java.util.Collections;
import java.util.List;
import modelo.Unidad_modelo;

/**
 *
 * @author dakrpastiursSennin
 */
public class Unidad_controlador {
    public List<Unidad> Obtener() {
        List<Unidad> lista = new Unidad_modelo().ListarUnidades();
        Collections.sort(lista, (Unidad inv1, Unidad inv2) -> {
            return inv1.toString().compareTo(inv2.toString());
        });
        return lista;
    }
}
