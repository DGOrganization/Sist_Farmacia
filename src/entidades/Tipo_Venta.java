/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

/**
 *
 * @author dakrpastiursSennin
 */
public class Tipo_Venta {
    private int _id;
    private String _nombre;

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getNombre() {
        return _nombre;
    }

    public void setNombre(String _nombre) {
        this._nombre = _nombre;
    }

    public Tipo_Venta() {
    }

    public Tipo_Venta(int _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return _nombre;
    }
  
}
