/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Cliente;
import java.util.Collections;
import java.util.List;
import modelo.Cliente_modelo;

/**
 *
 * @author dakrpastiursSennin
 */
public class Cliente_controlador {
    
    public List<Cliente> Obtener(){
        List<Cliente> lista = new Cliente_modelo().ListarClientes();
        Collections.sort(lista, (Cliente p1, Cliente p2)->{
            return (p1.getApellidoPaterno() + " " + p1.getApellidoMaterno()).compareTo(p2.getApellidoPaterno() + " " + p2.getApellidoMaterno());
        });
        return lista;
    }
    
    public Cliente Obtener(Cliente pCliente){
        return new Cliente_modelo().ListarCliente(pCliente);
    }
    
    public boolean Registrar(Cliente pCliente){
        return new Cliente_modelo().Registrar(pCliente);
    }
    
    public boolean Editar(Cliente pCliente){
        return new Cliente_modelo().Editar(pCliente);
    }
    
    public boolean Eliminar(Cliente pCliente){
        return new Cliente_modelo().Eliminar(pCliente);
    }
    
}
