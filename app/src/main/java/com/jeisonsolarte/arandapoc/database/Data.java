package com.jeisonsolarte.arandapoc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jeison Solarte on 29/06/2016.
 */
public class Data extends SQLiteOpenHelper{

    /*
    Clase que define la mi estructura de la base de datos
     */

    String ScripDDL_Series="CREATE TABLE Series(Serie_Nombre TEXT, Serie_Img TEXT,Serie_Genero TEXT, Serie_Actores TEXT, Serie_Temporadas INTEGER)";
    String ScripDDL_Temporada="CREATE TABLE Temporada(Serie_Temporada INTEGER, Temp_Nombre TEXT, Temp_Episodio TEXT)";
    String ScripDDL_Episodio="CREATE TABLE Episodio(Epi_Nombre TEXT, Serie_Temporad INTEGER)";

    public Data(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScripDDL_Series);
        db.execSQL(ScripDDL_Temporada);
        db.execSQL(ScripDDL_Episodio);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Series");
        db.execSQL("DROP TABLE IF EXISTS Temporada");
        db.execSQL("DROP TABLE IF EXISTS Episodio");
        db.execSQL(ScripDDL_Series);
        db.execSQL(ScripDDL_Temporada);
        db.execSQL(ScripDDL_Episodio);
    }
}
