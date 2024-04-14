/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package com.comidas.app;

import com.comidas.MD5EncriptaClave;
import com.comidas.OperacionesDB;
import com.comidas.Procesos;
import com.comidas.objetos.ListaCatalago;
import com.comidas.objetos.ListaIngredientes;
import com.comidas.objetos.ParametrosGen;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PUT;
import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author CRoberto
 */
@Path("Metodos")
@RequestScoped
public class Metodos {

    Logger log;
    Integer isWindows;
    String rutaPropiedades;

    @Context
    private ServletContext contexto;

    /**
     * Creates a new instance of Metodos
     */
    public Metodos() {
    }

    /**
     * Retrieves representation of an instance of com.comidas.Metodos
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of Metodos
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    //<editor-fold defaultstate="collapsed" desc="public method: Registro">
    @POST
    @Path("Registro")
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response registro(@QueryParam("nombreUsuario") String nombreUsuario,
            @QueryParam("clave") String clave,
            @QueryParam("correo") String correo,
            @QueryParam("telefono") String telefono,
            @QueryParam("fechaNacimiento") String fechaNacimiento,
            @QueryParam("idPais") Integer idPais,
            @QueryParam("idGenero") Integer idGenero) throws JSONException {

        Connection con = null;
        ResultSet rs = null;
        Procesos prc = new Procesos();
        OperacionesDB operDB = new OperacionesDB();

        CallableStatement cstmt;
        Integer codRet = -1;
        String msg = "";
        JSONObject jsonObj = new JSONObject();
        ParametrosGen paramGen;
        String claveEncript;

        rutaPropiedades = contexto.getRealPath("recursos");
        paramGen = prc.cargaPropiedades(rutaPropiedades);
        log = Logger.getLogger(this.getClass());
        log.info("***********  Entro al registro *****************");

        log.info("Nombre Usuario: " + nombreUsuario);
        log.info("Correo: " + correo);
        log.info("Telefono: " + telefono);
        log.info("Fecha Nac.: " + fechaNacimiento);
        log.info("Id Pais: " + idPais);
        log.info("Id Genero: " + idGenero);

        con = operDB.connectDB(paramGen);
        boolean conectado = operDB.conectado;
        if (!conectado) {
            jsonObj.put("codRet", "-1");
            jsonObj.put("mensaje", "Error de conexion a la DB");
            return Response.ok(jsonObj.toString()).build();
        }
        try {
            log.info("Encriptando clave .. ");
            log.debug("CLAVE ... " + clave);
            MD5EncriptaClave encript = new MD5EncriptaClave();
            claveEncript = encript.encripta(clave.trim());
            log.debug("EncriptPass ....." + claveEncript);

            String sqlString = "spRegistraUsuario";
            ArrayList datos = new ArrayList();
            log.info("Ejecuta ..: " + sqlString);

            datos.add(new String[]{"String", correo, "IN"});
            datos.add(new String[]{"String", nombreUsuario, "IN"});
            datos.add(new String[]{"String", telefono, "IN"});
            datos.add(new String[]{"Date", fechaNacimiento, "IN"});
            datos.add(new String[]{"String", claveEncript, "IN"});
            datos.add(new String[]{"Int", idPais.toString(), "IN"});
            datos.add(new String[]{"Int", idGenero.toString(), "IN"});
            datos.add(new String[]{"Int", "codResultado", "OUT"});
            datos.add(new String[]{"String", "mensaje", "OUT"});

            cstmt = operDB.ejecutaProcedimiento(datos, sqlString, con);
            log.info("Obteniendo resultados... ");

            codRet = cstmt.getInt(8);
            msg = cstmt.getString(9);

            con.close();
            log.info("Resultado: " + codRet + " - " + msg);
            jsonObj.put("codRet", codRet);
            jsonObj.put("mensaje", msg);

        } catch (SQLException ex) {
            log.info(" ******** SQLException ********** : " + ex.getMessage());
            jsonObj.put("codRet", "11");
            jsonObj.put("mensaje", "Ocurrio un error en la aplicación");
        }
        return Response.ok(jsonObj.toString()).build();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="public method: Receta">
    @POST
    @Path("Receta")
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response receta(@QueryParam("nombreReceta") String nombreReceta,
            @QueryParam("descripcion") String descripcion,
            @QueryParam("idUsuario") Integer idUsuario,
            @QueryParam("idCategoria") Integer idCategoria) throws JSONException {

        Connection con = null;
        ResultSet rs = null;
        Procesos prc = new Procesos();
        OperacionesDB operDB = new OperacionesDB();

        CallableStatement cstmt;
        Integer codRet = -1;
        String msg = "";
        JSONObject jsonObj = new JSONObject();
        ParametrosGen paramGen;

        rutaPropiedades = contexto.getRealPath("recursos");
        paramGen = prc.cargaPropiedades(rutaPropiedades);
        log = Logger.getLogger(this.getClass());
        log.info("***********  Entro al registro *****************");

        log.info("Nombre Receta: " + nombreReceta);
        log.info("Descripcion: " + descripcion);
        log.info("Id Usuario: " + idUsuario);
        log.info("Id Cateooria: " + idCategoria);

        con = operDB.connectDB(paramGen);
        boolean conectado = operDB.conectado;
        if (!conectado) {
            jsonObj.put("codRet", "-1");
            jsonObj.put("mensaje", "Error de conexion a la DB");
            return Response.ok(jsonObj.toString()).build();
        }
        try {
            String sqlString = "spRegistraReceta";
            ArrayList datos = new ArrayList();
            log.info("Ejecuta ..: " + sqlString);

            datos.add(new String[]{"String", nombreReceta, "IN"});
            datos.add(new String[]{"String", descripcion, "IN"});
            datos.add(new String[]{"Int", idUsuario.toString(), "IN"});
            datos.add(new String[]{"Int", idCategoria.toString(), "IN"});
            datos.add(new String[]{"Int", "codResultado", "OUT"});
            datos.add(new String[]{"String", "mensaje", "OUT"});

            cstmt = operDB.ejecutaProcedimiento(datos, sqlString, con);
            log.info("Obteniendo resultados... ");

            codRet = cstmt.getInt(5);
            msg = cstmt.getString(6);

            con.close();
            log.info("Resultado: " + codRet + " - " + msg);
            jsonObj.put("codRet", codRet);
            jsonObj.put("mensaje", msg);

        } catch (SQLException ex) {
            log.info(" ******** SQLException ********** : " + ex.getMessage());
            jsonObj.put("codRet", "11");
            jsonObj.put("mensaje", "Ocurrio un error en la aplicación");
        }

        return Response.ok(jsonObj.toString()).build();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="public method: listaCatalogo">
    @POST
    @Path("ListaCatalago")
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response ListaCatalago(@QueryParam("tabla") String tabla
    ) throws JSONException {

        Connection con = null;
        ResultSet rs = null;
        Procesos prc = new Procesos();
        OperacionesDB operDB = new OperacionesDB();        

        JSONArray jsonArray = new JSONArray();
        ParametrosGen paramGen;

        rutaPropiedades = contexto.getRealPath("recursos");
        paramGen = prc.cargaPropiedades(rutaPropiedades);
        log = Logger.getLogger(this.getClass());
        log.info("***********  Entro Lista Catalago *****************");

        log.info("Parametro: " + tabla);
        //log.info("Descripcion: "+ descripcion);
        //log.info("Id Usuario: "+ idUsuario);
        //log.info("Id Cateooria: "+ idCategoria);

        con = operDB.connectDB(paramGen);
        boolean conectado = operDB.conectado;
        if (!conectado) {
            return Response.ok(jsonArray.toString()).build();
        }
        try {
            String sqlString = "SELECT * FROM " + tabla;
            ArrayList datos = new ArrayList();
            log.info("Ejecuta ..: " + sqlString);

            rs = operDB.ejecutaQuery(sqlString, datos, con);
            //cstmt = operDB.ejecutaProcedimiento(datos, sqlString, con);
            log.info("Obteniendo resultados... ");

            while (rs.next()) {
                ListaCatalago lista = new ListaCatalago();
                lista.setId(rs.getString(1));
                lista.setNombre(rs.getString(2));
                lista.setDescripcion(rs.getString(3));
                
                jsonArray.put(lista);
            }
            
            con.close();
        } catch (SQLException ex) {
            log.info(" ******** SQLException ********** : " + ex.getMessage());
            
        }
        return Response.ok(jsonArray.toString()).build();
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public method: listaIngredientes">
    @POST
    @Path("ListaIngrediente")
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response ListaIngrediente(@QueryParam("idUsuario") String idUsuario
    ) throws JSONException {

        Connection con = null;
        ResultSet rs = null;
        Procesos prc = new Procesos();
        OperacionesDB operDB = new OperacionesDB();

        JSONArray jsonArray = new JSONArray();
        ParametrosGen paramGen;

        rutaPropiedades = contexto.getRealPath("recursos");
        paramGen = prc.cargaPropiedades(rutaPropiedades);
        log = Logger.getLogger(this.getClass());
        log.info("***********  Entro Lista Ingredientes *****************");

        log.info("Parametro: " + idUsuario);

        con = operDB.connectDB(paramGen);
        boolean conectado = operDB.conectado;
        if (!conectado) {
            return Response.ok(jsonArray.toString()).build();
        }
        try {
            String sqlString = "SELECT * FROM Ingredientes WHERE IdUsuario=" + idUsuario;
            ArrayList datos = new ArrayList();
            log.info("Ejecuta ..: " + sqlString);

            rs = operDB.ejecutaQuery(sqlString, datos, con);
            //cstmt = operDB.ejecutaProcedimiento(datos, sqlString, con);
            log.info("Obteniendo resultados... ");

            while (rs.next()) {
                ListaIngredientes lista = new ListaIngredientes();
                lista.setIdIngrediente(rs.getInt(1));
                lista.setIngrediente(rs.getString(2));
                lista.setDescripcionIngrediente(rs.getString(3));
                lista.setTieneIngrediente(rs.getBoolean(4));
                lista.setFechaRegistro(rs.getTimestamp(5));
                lista.setIdUsuario(rs.getInt(6));

                jsonArray.put(lista);
            }

            con.close();
        } catch (SQLException ex) {
            log.info(" ******** SQLException ********** : " + ex.getMessage());

        }
        return Response.ok(jsonArray.toString()).build();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="public method: agregaIngredientes">
    @POST
    @Path("AgregaIngredientes")
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response agregaIngredientes(@QueryParam("ingrediente") String ingrediente,
                           @QueryParam("descripcionIngrediente") String descripcionIngrediente,
                           @QueryParam("idUsuario") Integer idUsuario) throws JSONException {

        Connection con = null;
        ResultSet rs = null;
        Procesos prc = new Procesos();
        OperacionesDB operDB = new OperacionesDB();

        CallableStatement cstmt;
        Integer codRet = -1;
        String msg = "";
        JSONObject jsonObj = new JSONObject();
        ParametrosGen paramGen;

        rutaPropiedades = contexto.getRealPath("recursos");
        paramGen = prc.cargaPropiedades(rutaPropiedades);
        log = Logger.getLogger(this.getClass());
        log.info("***********  Entro al registro *****************");

        log.info("Nombre Ingrediente: " + ingrediente);
        log.info("Descripcion Ingrediente: " + descripcionIngrediente);
        log.info("Id Usuario: " + idUsuario);

        con = operDB.connectDB(paramGen);
        boolean conectado = operDB.conectado;
        if (!conectado) {
            jsonObj.put("codRet", "-1");
            jsonObj.put("mensaje", "Error de conexion a la DB");
            return Response.ok(jsonObj.toString()).build();
        }
        try {
            String sqlString = "spAgregaIngrediente";
            ArrayList datos = new ArrayList();
            log.info("Ejecuta ..: " + sqlString);

            datos.add(new String[]{"String", ingrediente, "IN"});
            datos.add(new String[]{"String", descripcionIngrediente, "IN"});
            datos.add(new String[]{"Int", idUsuario.toString(), "IN"});
            datos.add(new String[]{"Int", "codResultado", "OUT"});
            datos.add(new String[]{"String", "mensaje", "OUT"});

            cstmt = operDB.ejecutaProcedimiento(datos, sqlString, con);
            log.info("Obteniendo resultados... ");

            codRet = cstmt.getInt(4);
            msg = cstmt.getString(5);

            con.close();
            log.info("Resultado: " + codRet + " - " + msg);
            jsonObj.put("codRet", codRet);
            jsonObj.put("mensaje", msg);

        } catch (SQLException ex) {
            log.info(" ******** SQLException ********** : " + ex.getMessage());
            jsonObj.put("codRet", "11");
            jsonObj.put("mensaje", "Ocurrio un error en la aplicación");
        }
        return Response.ok(jsonObj.toString()).build();
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="public method: actualizaIngredientes">
    @POST
    @Path("ActualizaIngredientes")
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response actualizaIngredientes(@QueryParam("ingrediente") String ingrediente,
                                 @QueryParam("descripcionIngrediente") String descripcionIngrediente,
                                 @QueryParam("tieneIngrediente") Boolean tieneIngrediente) throws JSONException {

        Connection con = null;
        ResultSet rs = null;
        Procesos prc = new Procesos();
        OperacionesDB operDB = new OperacionesDB();

        CallableStatement cstmt;
        Integer codRet = -1;
        String msg = "";
        JSONObject jsonObj = new JSONObject();
        ParametrosGen paramGen;

        rutaPropiedades = contexto.getRealPath("recursos");
        paramGen = prc.cargaPropiedades(rutaPropiedades);
        log = Logger.getLogger(this.getClass());
        log.info("***********  Entro al registro *****************");

        log.info("Nombre Ingrediente: " + ingrediente);
        log.info("Descripcion Ingrediente: " + descripcionIngrediente);
        log.info("Tiene Ingrediente: " + tieneIngrediente);

        con = operDB.connectDB(paramGen);
        boolean conectado = operDB.conectado;
        if (!conectado) {
            jsonObj.put("codRet", "-1");
            jsonObj.put("mensaje", "Error de conexion a la DB");
            return Response.ok(jsonObj.toString()).build();
        }
        try {
            String sqlString = "spActualizaIngrediente";
            ArrayList datos = new ArrayList();
            log.info("Ejecuta ..: " + sqlString);

            datos.add(new String[]{"String", ingrediente, "IN"});
            datos.add(new String[]{"String", descripcionIngrediente, "IN"});
            datos.add(new String[]{"Bit", tieneIngrediente.toString(), "IN"});
            datos.add(new String[]{"Int", "codResultado", "OUT"});
            datos.add(new String[]{"String", "mensaje", "OUT"});

            cstmt = operDB.ejecutaProcedimiento(datos, sqlString, con);
            log.info("Obteniendo resultados... ");

            codRet = cstmt.getInt(4);
            msg = cstmt.getString(5);

            con.close();
            log.info("Resultado: " + codRet + " - " + msg);
            jsonObj.put("codRet", codRet);
            jsonObj.put("mensaje", msg);

        } catch (SQLException ex) {
            log.info(" ******** SQLException ********** : " + ex.getMessage());
            jsonObj.put("codRet", "11");
            jsonObj.put("mensaje", "Ocurrio un error en la aplicación");
        }
        return Response.ok(jsonObj.toString()).build();
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="public method: eliminaIngredientes">
    @POST
    @Path("EliminaIngredientes")
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response EliminaIngredientes(@QueryParam("idIngrediente") Integer idIngrediente) throws JSONException {

        Connection con = null;
        ResultSet rs = null;
        Procesos prc = new Procesos();
        OperacionesDB operDB = new OperacionesDB();

        CallableStatement cstmt;
        Integer codRet = -1;
        String msg = "";
        JSONObject jsonObj = new JSONObject();
        ParametrosGen paramGen;

        rutaPropiedades = contexto.getRealPath("recursos");
        paramGen = prc.cargaPropiedades(rutaPropiedades);
        log = Logger.getLogger(this.getClass());
        log.info("***********  Entro al registro *****************");

        log.info("Id Ingrediente: " + idIngrediente);

        con = operDB.connectDB(paramGen);
        boolean conectado = operDB.conectado;
        if (!conectado) {
            jsonObj.put("codRet", "-1");
            jsonObj.put("mensaje", "Error de conexion a la DB");
            return Response.ok(jsonObj.toString()).build();
        }
        try {
            String sqlString = "spEliminaIngrediente";
            ArrayList datos = new ArrayList();
            log.info("Ejecuta ..: " + sqlString);

            datos.add(new String[]{"Int", idIngrediente.toString() , "IN"});
            datos.add(new String[]{"Int", "codResultado", "OUT"});
            datos.add(new String[]{"String", "mensaje", "OUT"});

            cstmt = operDB.ejecutaProcedimiento(datos, sqlString, con);
            log.info("Obteniendo resultados... ");

            codRet = cstmt.getInt(2);
            msg = cstmt.getString(3);

            con.close();
            log.info("Resultado: " + codRet + " - " + msg);
            jsonObj.put("codRet", codRet);
            jsonObj.put("mensaje", msg);

        } catch (SQLException ex) {
            log.info(" ******** SQLException ********** : " + ex.getMessage());
            jsonObj.put("codRet", "11");
            jsonObj.put("mensaje", "Ocurrio un error en la aplicación");
        }
        return Response.ok(jsonObj.toString()).build();
    }

    //</editor-fold>

}
