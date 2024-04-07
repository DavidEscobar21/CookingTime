/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.comidas;

import com.comidas.objetos.ParametrosGen;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author CRoberto
 */
public class Procesos {
    
    Logger log = Logger.getLogger(this.getClass());
    
    //<editor-fold defaultstate="collapsed" desc="public method: cargaPropiedades">
    public ParametrosGen cargaPropiedades(String rutaProp) {
        Integer isWindows = -1;
        File propFile;
        ParametrosGen paramGen = new ParametrosGen();
        //Logger log = null;
        try {
            if (rutaProp.startsWith("/")) {
                PropertyConfigurator.configure(rutaProp + "/wsComidasLog.properties");
                propFile = new File(rutaProp + "/wsComidas.properties");
                isWindows = 0;
            } else {
                PropertyConfigurator.configure(rutaProp + "\\wsComidasLog.properties");
                propFile = new File(rutaProp + "\\wsComidas.properties");
                isWindows = 1;
            }
            //System.out.println("Ruta de archivos... "+ rutaProp);
            log = Logger.getLogger(this.getClass());
            Properties prop = new Properties();

            //prop.load(new FileReader("ApaguizWeb.properties"));                     
            prop.load(new InputStreamReader(new FileInputStream(propFile), "UTF-8"));
            //System.out.println("Cargo archivo de propiedades... ");
            
            paramGen.setUrlDB(prop.getProperty("urlDB"));
            paramGen.setUserDB(prop.getProperty("userDB"));
            paramGen.setPassDB(prop.getProperty("passDB"));
            paramGen.setDriverDB(prop.getProperty("driverDB"));          
            paramGen.setIsWindows(isWindows.toString());            
            paramGen.setFormatoFecha(prop.getProperty("formatoFecha"));            

        } catch (IOException ex) {
            log.error("No se encontro la definicion para nombre de Cooperativa en archivo de propiedades. " + ex.getMessage());
        }

        return paramGen;
    }
    //</editor-fold>
}
