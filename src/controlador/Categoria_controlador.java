/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Categoria;
import java.util.Collections;
import java.util.List;
import modelo.Categoria_modelo;

/**
 *
 * @author dakrpastiursSennin
 */
public class Categoria_controlador {
    public List<Categoria> Obtener() {
        List<Categoria> lista = new Categoria_modelo().ListarCategorias();
        Collections.sort(lista, (Categoria inv1, Categoria inv2) -> {
            return inv1.toString().compareTo(inv2.toString());
        });
        return lista;
    }
    
    public Categoria Obtener(Categoria pCategoria) {
        return new Categoria_modelo().ListarCategoria(pCategoria);
    }
    
    public boolean Registrar(Categoria pCategoria){
        return new Categoria_modelo().Registrar(pCategoria);
    }
    
    public boolean Editar(Categoria pCategoria){
        return new Categoria_modelo().Editar(pCategoria);
    }
    
    public boolean Eliminar(Categoria pCategoria){
        return new Categoria_modelo().Eliminar(pCategoria);
    }
}
