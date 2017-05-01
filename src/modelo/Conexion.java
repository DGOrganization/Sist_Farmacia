/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;


/**
 *
 * @author dakrpastiursSennin
 */
public final class Conexion {    
    private static final BasicDataSource pool = new BasicDataSource();
    static {     
        pool.setDriverClassName("org.postgresql.Driver");
        pool.setUrl("jdbc:postgresql://localhost:5432/db_farmacia");        
        pool.setUsername("postgres");
        pool.setPassword("adminroot");
        /*pool.setInitialSize(1);
        pool.setMinIdle(1);
        pool.setMaxIdle(1);
        pool.setMaxTotal(1);*/
    }
    
    public Conexion(){
    }

    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }
}
