package com.comidas;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author CRoberto
 */
//import com.sybase.jdbc3.jdbc.SybDriver;
import com.comidas.objetos.ParametrosGen;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class OperacionesDB {

    Statement stmt;
    CallableStatement cstmt;
    PreparedStatement pstmt;
    ResultSet rs;
    
    Logger log = Logger.getLogger(this.getClass());
    
    public boolean conectado = false;        
       
    public Connection connectDB(ParametrosGen paramGen) {
        Connection con = null;
        conectado = false;
        //cargaPropiedades();
        try {
         
            Class.forName(paramGen.getDriverDB());           
            con = DriverManager.getConnection(paramGen.getUrlDB(), paramGen.getUserDB(), paramGen.getPassDB());
           
        } catch (ClassNotFoundException | SQLException ex) {

            log.error("ERROR En Coneccion Base de Datos, Consulte Atenciön al cliente " + ex.getMessage());
            conectado = false;
            return con;
        }
        conectado = true;
        return con;
    }
    
    public CallableStatement ejecutaProcedimiento(ArrayList datos, String sp, Connection cnn) {
        //rs = null;
        cstmt = null;
        
        String query = preparaSP(sp, datos.size());
        
        try {           
            cstmt = cnn.prepareCall(query);
            cstmt.clearParameters();
                        
            for (int i = 0; i < datos.size(); i++) {
                String[] valores = (String[]) datos.get(i);         //Obtenemos los valores
                if (valores[0].equalsIgnoreCase("int")) {           //Se evalua el tipo de dato
                    if (valores[2].equalsIgnoreCase("IN")){         //Se evalua que tipo de parametros es
                        cstmt.setInt(i + 1, Integer.parseInt(valores[1]));
                    }else{                                                                        //Parametros de salida
                        cstmt.registerOutParameter(i + 1, Types.INTEGER);
                        //cstmt.registerOutParameter(i + 1, -10);
                    }
                }else if (valores[0].equalsIgnoreCase("short")) {
                    if (valores[2].equalsIgnoreCase("IN")){
                        cstmt.setShort(i + 1, Short.parseShort(valores[1]));
                    }else{
                        cstmt.registerOutParameter(i + 1, Types.INTEGER);   
                        //cstmt.registerOutParameter(i + 1, -10);
                    }
                }else if (valores[0].equalsIgnoreCase("double")) {
                    if (valores[2].equalsIgnoreCase("IN")){
                        cstmt.setDouble(i + 1, Double.parseDouble(valores[1]));
                    }else{
                        cstmt.registerOutParameter(i + 1, Types.DECIMAL);
                        //cstmt.registerOutParameter(i + 1, -10);
                    }
                }else if (valores[0].equalsIgnoreCase("long")) {
                    if (valores[2].equalsIgnoreCase("IN")){
                        cstmt.setLong(i + 1, Long.parseLong(valores[1])); 
                    }else{
                        cstmt.registerOutParameter(i + 1, Types.BIGINT);
                        //cstmt.registerOutParameter(i + 1, -10);
                    }
                }else if (valores[0].equalsIgnoreCase("string")) {
                    if (valores[2].equalsIgnoreCase("IN")){
                        cstmt.setString(i + 1, valores[1]);
                    }else{
                        cstmt.registerOutParameter(i + 1, Types.VARCHAR);
                    }
                }else if (valores[0].equalsIgnoreCase("bool")){
                    if (valores[2].equalsIgnoreCase("IN")){
                        if (valores[1].equalsIgnoreCase("true") || valores[1].equalsIgnoreCase("1")){
                             cstmt.setBoolean(i + 1, true);
                        }else
                             cstmt.setBoolean(i + 1, false);
                    }else{
                        cstmt.registerOutParameter(i + 1, Types.BOOLEAN);
                    }
                }           
            }
            
            cstmt.execute();
            
        } catch (SQLException ex) {
            log.error("Error ejecutando procedimiento... ["+ query + "] " + ex.getMessage() );            
        }
        
        return cstmt;
    }

    public ResultSet ejecutaQuery(String sql, ArrayList datos, Connection cnn) {
        rs = null;
       
        try {
            //pstmt = cnn.prepareCall(sql);
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            log.debug("sql "+ sql);
            
            for (int i=0; i< datos.size(); i++){
                String[] valores = (String[]) datos.get(i);
                log.debug("Tipo Valor : "+ valores[0]);
                log.debug("Valor : "+ valores[1]);
                
                if (valores[0].equalsIgnoreCase("int")){
                    pstmt.setInt(i + 1, Integer.parseInt(valores[1]));
                }else if (valores[0].equalsIgnoreCase("short")) {
                    pstmt.setShort(i + 1, Short.parseShort(valores[1]));
                }else if (valores[0].equalsIgnoreCase("double")) {
                    pstmt.setDouble(i + 1, Double.parseDouble(valores[1]));
                }else if (valores[0].equalsIgnoreCase("long")) {
                    pstmt.setLong(i + 1, Long.parseLong(valores[1])); 
                }else if (valores[0].equalsIgnoreCase("string")) {
                    pstmt.setString(i + 1, valores[1]);
                }else if (valores[0].equalsIgnoreCase("bool")){
                     if (valores[1].equalsIgnoreCase("true") || valores[1].equalsIgnoreCase("1")){
                         pstmt.setBoolean(i + 1, true);
                    }else
                         pstmt.setBoolean(i + 1, false);
                }              
            }
            
            rs = pstmt.executeQuery();              
        } catch (SQLException ex) {
            log.error("No se puedo ejecutar el query " + ex.getMessage());
            //log.fatal("No se pudo ejecutar el Query - ["+ sql + "] " + ex.getMessage());
        }
        return rs;
    }        
    
    private String preparaSP(String sp, Integer tamano){
        String sql = "";        
        
        log.info("Preparando SP .. "+ sp);
        sql = "{call " + sp + "(";
        for (int i = 1; i <= tamano; i++) {
            if (tamano == 1) {
                sql = sql + "?";
                break;
            }
            if (i < tamano) {
                sql = sql + "?,";
            } else {
                sql = sql + "?";
            }
        }
        sql = sql + ")}";
        
        return sql;
     }

}
