package com.jeisonsolarte.arandapoc.model;

/**
 * Created by Jeison Solarte on 29/06/2016.
 */
public class Temporada {

    //modelo de Temporada, tabla en la base de datos

    int num_temporada;
    String nombre_temp;
    String episodio;

    public int getNum_temporada() {
        return num_temporada;
    }

    public void setNum_temporada(int num_temporada) {
        this.num_temporada = num_temporada;
    }

    public String getNombre_temp() {
        return nombre_temp;
    }

    public void setNombre_temp(String nombre_temp) {
        this.nombre_temp = nombre_temp;
    }

    public String getEpisodio() {
        return episodio;
    }

    public void setEpisodio(String episodio) {
        this.episodio = episodio;
    }
}
