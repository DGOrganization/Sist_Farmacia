/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Persona;
import entidades.Telefono;
import java.util.List;
import modelo.Telefono_modelo;

/**
 *
 * @author dakrpastiursSennin
 */
public class Telefono_controlador {
    public List<Telefono> Obtener(Persona pPersona){
        return new Telefono_modelo().ListarTelefonos(pPersona);
    }
}
