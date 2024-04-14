package com.comidas.objetos;

import java.sql.Timestamp;
import java.util.Date;

public class ListaIngredientes {

        private Integer idIngrediente;
        private String ingrediente;
        private String descripcionIngrediente;
        private boolean tieneIngrediente;
        private Timestamp fechaRegistro;
        private Integer idUsuario;

    public Integer getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(Integer idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(String ingrediente) {
        this.ingrediente = ingrediente;
    }

    public String getDescripcionIngrediente() {
        return descripcionIngrediente;
    }

    public void setDescripcionIngrediente(String descripcionIngrediente) {
        this.descripcionIngrediente = descripcionIngrediente;
    }

    public boolean isTieneIngrediente() {
        return tieneIngrediente;
    }

    public void setTieneIngrediente(boolean tieneIngrediente) {
        this.tieneIngrediente = tieneIngrediente;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}
