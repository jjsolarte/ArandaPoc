package com.jeisonsolarte.arandapoc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements Runnable{

    //Esta clase es muy simple, solo permite al usuario la visualización de una ventana de bienvenida
    /*
    Cargo después de 2 segundos, la ventana principal de la aplicación, en la cual el usuario podrá realizar
    la búsqueda, se llama Busqueda
     */

    int TIEMPO_ESPERA=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread=new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(TIEMPO_ESPERA);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent=new Intent(this,Busqueda.class);
        startActivity(intent);
        finish();
    }
}
