package com.jeisonsolarte.arandapoc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jeisonsolarte.arandapoc.model.Episodio;
import com.jeisonsolarte.arandapoc.model.Serie;
import com.jeisonsolarte.arandapoc.model.Temporada;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeison Solarte on 29/06/2016.
 */
public class DataResourse {

    /*
    Clase que gestiona las funciones realizadas sobre la base de datos, desde insertar valores a las tablas
    hasta realizar consultas en las mismas, según requerimientos.
     */

    Context context;
    SQLiteDatabase sqLiteDatabase;
    Data data;
    public DataResourse(Context context) {
        this.context = context;

        data=new Data(context,"POC_DB",null,1);
        sqLiteDatabase=data.getWritableDatabase();

    }


    public long insertDataSerie(Serie serie){
        ContentValues cV=new ContentValues();
        cV.put("Serie_Nombre",serie.getNombre());
        cV.put("Serie_Img",serie.getImg());
        cV.put("Serie_Genero",serie.getGenero());
        cV.put("Serie_Actores",serie.getActores());
        cV.put("Serie_Temporadas",serie.getNum_temporada());
        long res=sqLiteDatabase.insert("Series",null,cV);
        return res;
    }

    public long insertDataTemporada(Temporada temporada){
        ContentValues cV=new ContentValues();
        cV.put("Serie_Temporada",temporada.getNum_temporada());
        cV.put("Temp_Nombre",temporada.getNombre_temp());
        cV.put("Temp_Episodio",temporada.getEpisodio());
        long res=sqLiteDatabase.insert("Temporada",null,cV);
        return res;
    }

    public long insertDataEpisodio(Episodio episodio){
        ContentValues cV=new ContentValues();
        cV.put("Epi_Nombre",episodio.getEpisodio());
        cV.put("Serie_Temporad",episodio.getNum_temporada());
        long res=sqLiteDatabase.insert("Episodio",null,cV);
        return res;
    }

    public List<Serie> consultarSeries(String clave){
        List<Serie> listaSerie=null;
        String sql="Select * from Series where Serie_Nombre like '%"+clave+"%'";
        Cursor cursor=sqLiteDatabase.rawQuery(sql,null);
        if (cursor.getCount()>0){
            listaSerie=new ArrayList<Serie>();
            while (cursor.moveToNext()){
                Serie serie=new Serie();
                serie.setNombre(cursor.getString(0));
                serie.setImg(cursor.getString(1));
                serie.setGenero(cursor.getString(2));
                serie.setActores(cursor.getString(3));
                serie.setNum_temporada(cursor.getInt(4));
                listaSerie.add(serie);
            }
        }
        return listaSerie;
    }

    public List<Temporada> consultarTemporadas(int numTemporada){
        List<Temporada> listTemporada=null;
        String clave=String.valueOf(numTemporada);
        String sql="Select * from Temporada where Serie_Temporada like '%"+clave+"%'";
        Cursor cursor=sqLiteDatabase.rawQuery(sql,null);
        if (cursor.getCount()>0){
            listTemporada=new ArrayList<Temporada>();
            while (cursor.moveToNext()){
                Temporada temporada=new Temporada();
                temporada.setNum_temporada(cursor.getInt(0));
                temporada.setNombre_temp(cursor.getString(1));
                temporada.setEpisodio(cursor.getString(2));
                listTemporada.add(temporada);
            }
        }
        return listTemporada;
    }

    public List<Episodio> consultarEpisodios(int numTemporada){
        List<Episodio>  listEpisodios=null;
        String clave=String.valueOf(numTemporada);
        String sql="Select * from Episodio where Serie_Temporad like '%"+clave+"%'";
        Cursor cursor=sqLiteDatabase.rawQuery(sql,null);
        if (cursor.getCount()>0){
            listEpisodios=new ArrayList<Episodio>();
            while (cursor.moveToNext()){
                Episodio episodio=new Episodio();
                episodio.setEpisodio(cursor.getString(0));
                episodio.setNum_temporada(cursor.getInt(1));
                listEpisodios.add(episodio);
            }
        }
        return listEpisodios;
    }

    public void borrarDatos(){
        data.onUpgrade(sqLiteDatabase,0,0);
    }
}
