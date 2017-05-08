/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Nivel;
import java.util.List;
import modelo.Nivel_modelo;

/**
 *
 * @author dakrpastiursSennin
 */
public class Nivel_controlador {

    public Nivel Obtener(Nivel pNivel, boolean listarMenus) {
        return new Nivel_modelo().ListarNivel(pNivel, listarMenus);
    }
    
    public List<Nivel> Obtener(boolean listarMenus) {        
        List<Nivel> lista = new Nivel_modelo().ListarNiveles(listarMenus);
        lista.sort((Nivel lvl1, Nivel lvl2)->{
            return lvl1.getNombre().compareTo(lvl2.getNombre());
        });
        return lista;
    }
    
    public boolean Asignar(Nivel pNivel){
        return new Nivel_modelo().Asignar(pNivel);
    }
    
    public boolean Registrar(Nivel pNivel){
        return new Nivel_modelo().Registrar(pNivel);
    }
    
    public boolean Editar(Nivel pNivel){
        return new Nivel_modelo().Editar(pNivel);
    }
    
    public boolean Eliminar(Nivel pNivel){
        return new Nivel_modelo().Elimnar(pNivel);
    }
}
