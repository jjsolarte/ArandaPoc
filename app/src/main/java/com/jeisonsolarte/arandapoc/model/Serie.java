package com.jeisonsolarte.arandapoc.model;

/**
 * Created by Jeison Solarte on 29/06/2016.
 */
public class Serie {

    //modelo de Serie, tabla en la base de datos

    String nombre;
    String img;
    String genero;
    String actores;
    int num_temporada;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public int getNum_temporada() {
        return num_temporada;
    }

    public void setNum_temporada(int num_temporada) {
        this.num_temporada = num_temporada;
    }
}
