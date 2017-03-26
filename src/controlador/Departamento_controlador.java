/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Departamento;
import entidades.Municipio;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dakrpastiursSennin
 */
public class Departamento_controlador {
    public List<Departamento> Obtener(){
        List<Departamento> lista = new ArrayList<>();
        List<Municipio> lista2 = new ArrayList<>();
        lista2.add(
                new Municipio(1, "Chalatenango", true)
        );
        lista2.add(
                new Municipio(2, "Nueva Concepcion", true)
        );
        lista2.add(
                new Municipio(3, "Agua Caliente", true)
        );
        lista.add(
                new Departamento(1, "Chalatenango", lista2, true)
        );
        return lista;
    }
}
