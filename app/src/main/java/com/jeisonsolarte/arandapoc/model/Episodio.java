package com.jeisonsolarte.arandapoc.model;

/**
 * Created by Jeison Solarte on 29/06/2016.
 */
public class Episodio {

    //modelo de Episodio, tabla en la base de datos

    String episodio;
    int num_temporada;

    public String getEpisodio() {
        return episodio;
    }

    public void setEpisodio(String episodio) {
        this.episodio = episodio;
    }

    public int getNum_temporada() {
        return num_temporada;
    }

    public void setNum_temporada(int num_temporada) {
        this.num_temporada = num_temporada;
    }
}
