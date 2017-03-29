/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.sql.Date;
import java.util.List;

/**
 *
 * @author dakrpastiursSennin
 */
public class Cliente extends Persona{
    private int _id;
    
    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public Cliente() {
    }

    public Cliente(int _id) {
        this._id = _id;
    }

    public Cliente(int _id, String _nombre, String _apellidopaterno, String _apellidomaterno, String _dui, String _nit, String _sexo, Date _nacimiento, String _direccion, Municipio _municipio, List<Telefono> _telefono, String _movil, String _email, boolean _estado) {
        super(_id, _nombre, _apellidopaterno, _apellidomaterno, _dui, _nit, _sexo, _nacimiento, _direccion, _municipio, _telefono, _movil, _email, _estado);
    }
    
    
    
}
