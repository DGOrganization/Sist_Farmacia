/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Bodega;
import entidades.Categoria;
import entidades.Inventario;
import entidades.Marca;
import entidades.Producto;
import entidades.Unidad;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author dakrpastiursSennin
 */
public class Inventario_controlador {
    
    public List<Inventario> Obtener(){
        List<Inventario> lista = new ArrayList<>();
        lista.add(
                new Inventario(
                        1, 
                        new Producto(1, "Paracetamol", "Para dolores", true),
                        new Marca(1, "Bayer", true),
                        new Categoria(1, "A", true),
                        new Bodega(1, "Casa Matriz", "Frente a la catedral", true),
                        new Unidad(1, "Pastilla", "Past.", true),
                        new BigDecimal(55.00),
                        new BigDecimal(10),
                        new BigDecimal(200),
                        new BigDecimal(0.75),
                        true
                )
        );
        lista.add(
                new Inventario(
                        2, 
                        new Producto(2, "Acetaminofen", "Para dolores", true),
                        new Marca(2, "Laboratorios Lopez", true),
                        new Categoria(2, "B", true),
                        new Bodega(2, "San Miguel", "Frente a UNAB San Miguel", true),
                        new Unidad(2, "Pastilla", "Past.", true),
                        new BigDecimal(55.00),
                        new BigDecimal(10),
                        new BigDecimal(200),
                        new BigDecimal(0.75),
                        true
                )
        );
        Collections.sort(lista, (Inventario inv1, Inventario inv2) -> {
            return inv1.toString().compareTo(inv2.toString());
        });
        return lista;
    }
    
}
