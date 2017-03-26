/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Cliente;
import entidades.Municipio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dakrpastiursSennin
 */
public class Cliente_controlador {
    
    public List<Cliente> Obtener(){
        List<Cliente> lista = new ArrayList<>();
        
        lista.add(
                new Cliente(
                        1,
                        "Hermes Nouel",
                        "Hernandez",
                        "Serrano",
                        "12365478-9",
                        "1234-567890-123-4",
                        'M',
                        new Date(),
                        "Chalatenango",
                        new Municipio(1, "Chalatenango", true),
                        "1234-5678",
                        "7890-1234",
                        "",
                        true
                )
        );
        
        lista.add(
                new Cliente(
                        2,
                        "Leticia",
                        "Borja",
                        "Serrano",
                        "12365478-9",
                        "1234-567890-123-4",
                        'F',
                        new Date(),
                        "Nueva Concepcion",
                        new Municipio(1, "Nueva Concepcion", true),
                        "1234-5678",
                        "7890-1234",
                        "leticia@unab.edu.sv",
                        true
                )
        );
        
        return lista;
    }
    
}
