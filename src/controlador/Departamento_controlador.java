/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Departamento;
import java.util.List;
import modelo.Departamento_modelo;

/**
 *
 * @author dakrpastiursSennin
 */
public class Departamento_controlador {
    public List<Departamento> Obtener(){
        List<Departamento> lista = new Departamento_modelo().ListarDepartamentos();
        return lista;
    }
}
