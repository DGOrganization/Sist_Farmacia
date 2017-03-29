/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Usuario;
import java.util.Collections;
import java.util.List;
import modelo.Usuario_modelo;

/**
 *
 * @author dakrpastiursSennin
 */
public class Usuario_controlador {
    
    public boolean Login(Usuario data){
        return new Usuario_modelo().Login(data);
    }
    
    public List<Usuario> Obtener(){
        List<Usuario> lista = new Usuario_modelo().ListarUsuarios();
        Collections.sort(lista, (Usuario p1, Usuario p2) ->{
            return (p1.getEmpleado().getApellidoPaterno() + " " + p1.getEmpleado().getApellidoMaterno()).compareTo(p2.getEmpleado().getApellidoPaterno() + " " + p2.getEmpleado().getApellidoMaterno());
        });
        return lista;
    }
    
    public Usuario Obtener(Usuario pUsuario){
        return new Usuario_modelo().ListarUsuario(pUsuario);
    }
    
    public boolean Registrar(Usuario pUsuario){
        return new Usuario_modelo().Registrar(pUsuario);
    }
    
    public boolean Editar(Usuario pUsuario){
        return new Usuario_modelo().Eliminar(pUsuario);
    }
    
    public boolean Eliminar(Usuario pUsuario){
        return new Usuario_modelo().Eliminar(pUsuario);
    }
}
