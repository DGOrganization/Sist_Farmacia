/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Municipio;
import entidades.Persona;
import modelo.Municipio_modelo;

/**
 *
 * @author dakrpastiursSennin
 */
public class Municipio_controlador {
    
    public Municipio Obtener(Persona pPersona){
        return new Municipio_modelo().ListarMunicipio(pPersona);
    }
    
}
