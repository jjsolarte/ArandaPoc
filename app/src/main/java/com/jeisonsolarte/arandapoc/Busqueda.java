package com.jeisonsolarte.arandapoc;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.jeisonsolarte.arandapoc.adapters.Adapter_ListaSeries;
import com.jeisonsolarte.arandapoc.database.DataResourse;
import com.jeisonsolarte.arandapoc.model.Episodio;
import com.jeisonsolarte.arandapoc.model.Serie;
import com.jeisonsolarte.arandapoc.model.Temporada;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Busqueda extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    /*
    En esta clase, el procedimiento para cargar el contenido aparte del campo de texto y el botón de búsqueda
    es el siguiente
    1. Recibo el click que el usuario realiza sobre el ImageButton, en el campo de texto (en el XML activity_busqueda.xml)
        está implícita la funcionalidad de no aceptar caractéres especiales, solo números y letras.
    2. Inmediatamente invoco la clase JsonTask que extiende de la clase AsyncTask y recibe el valor del campo de texto
        que el usuario digitó. Lo que hace esta clase es obtener la respuesta en un formato JSON de la url http://www.omdbapi.com/
        añadiendo el valor deseado para obtener la respuesta de las películas solicitadas.
    3. Una vez obtengo el valor JSON de la solicitud enviada, paso a descomponer el archivo en un arreglo que me sirve para
        llenar una lista llamada listRequest, cuya función es ser utilizada a continuación.
    4. Al finalizar el llenado de la lista listRequest, llamo al método llenarSerieEnDB, que como su nombre lo indica
        me permitirá llegar la tabla llamada Series en la base de datos local ubicada en el paquete database.
    5. La solicitud a la base de datos que realizo en este paso, es para comprobar que la base de datos funciona correctamente
        y que la búsqueda la realiza como lo exigen los requerimientos. Finalmente utilizo el adaptador creado de la clase
        Adapter_ListaSeries en el paquete adapters para visualizar la lista principal de las películas en esta actividad.
    6. Recibo finalmente el click que el usuario realiza sobre un ítem de la lista y cambio de actividad, pasándo como
        parámetro, la posición del ítem seleccionado.
     */

    public static EditText editBuscar;
    ImageButton imgBuscar;
    ListView listView;

    DataResourse dataResourse;
    public static List<Serie> listaSerie;
    Adapter_ListaSeries adapter_listaSeries;

    List<JSONObject> listaRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        dataResourse=new DataResourse(this);
        editBuscar= (EditText) findViewById(R.id.busqueda_campo);
        imgBuscar= (ImageButton) findViewById(R.id.busqueda_boton_buscar);
        listView= (ListView) findViewById(R.id.busqueda_lista);

        //1-----------------------------------------------------------------------------------------
        imgBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valorEdit;
                if (editBuscar!=null)
                    //2-----------------------------------------------------------------------------
                new JsonTask(Busqueda.this).execute(editBuscar.getText().toString());
            }
        });
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View view) {
        //quemo datos de prueba antes de implementar el consumo de IMDB


/*
        llenarObjetos();
        //buscar
        if (!editBuscar.getText().toString().isEmpty()){
            listaSerie=dataResourse.consultarSeries(editBuscar.getText().toString());
        }

        if (listaSerie!=null){
            adapter_listaSeries=new Adapter_ListaSeries(this,listaSerie);
            listView.setAdapter(adapter_listaSeries);
        }else {
            Toast.makeText(this,"No hay resultados encontrados",Toast.LENGTH_SHORT).show();
        }



        new JsonTask(this);
*/
    }


    private class JsonTask extends AsyncTask<String, Void, String>{

        Context context;

        public JsonTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection urlConnection=null;

            StringBuilder requestParams=new StringBuilder();
            try {
                requestParams.append(URLEncoder.encode("CountryName","UTF-8")).append("=");
                requestParams.append(URLEncoder.encode(String.valueOf(strings[0]),"UTF-8"));

                String url_plus="http://www.omdbapi.com/?s="+strings[0]+"&r=json&i=a";

                URL url=new URL(url_plus);
                urlConnection= (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setDoOutput(true);

                OutputStreamWriter writer=new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(strings.toString());
                writer.flush();
                urlConnection.connect();

                InputStream stream=urlConnection.getInputStream();

                int response_status=urlConnection.getResponseCode();
                if (response_status>=HttpURLConnection.HTTP_OK && response_status<HttpURLConnection.HTTP_MULT_CHOICE){
                    String result_str=getOutputRequest(stream);
                    return result_str;
                }



            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s!=null){
                try {
                    //3-----------------------------------------------------------------------------
                    JSONObject jsonObject=new JSONObject(s);
                    JSONArray jsonArray=jsonObject.getJSONArray("Search");
                    listaRequest=new ArrayList<JSONObject>();
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObjectAux=new JSONObject();
                        jsonObjectAux=jsonArray.getJSONObject(i);
                        listaRequest.add(jsonObjectAux);
                    }
                    /*
                    JSONObject jsonObjectSerie=jsonArray.getJSONObject(0);
                    asd=jsonArray.length();
                    nombreSerie=jsonObjectSerie.getString("Title");
                    tipoSerie=jsonObjectSerie.getString("Type");
                    */
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    //4.----------------------------------------------------------------------------
                    if (listaRequest!=null)
                    llenarSerieEnDB();
                    else
                        Toast.makeText(context,"No hay restultados",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(context,"No hay restultados",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void llenarSerieEnDB() throws JSONException {
        dataResourse.borrarDatos();
        for (int i=0;i<listaRequest.size();i++){
            Serie serie=new Serie();
            serie.setNombre(listaRequest.get(i).getString("Title"));
            serie.setImg(listaRequest.get(i).getString("Poster"));
            serie.setGenero(listaRequest.get(i).getString("Type"));
            serie.setActores("Actor 1, Actor 2, Actor 3");
            serie.setNum_temporada(1);
            dataResourse.insertDataSerie(serie);
        }

        //5-----------------------------------------------------------------------------------------
        listaSerie=dataResourse.consultarSeries(editBuscar.getText().toString());
        if (listaSerie!=null){
            adapter_listaSeries=new Adapter_ListaSeries(this,listaSerie);
            listView.setAdapter(adapter_listaSeries);
        }else {
            Toast.makeText(this,"No hay resultados encontrados",Toast.LENGTH_SHORT).show();
        }

    }

    private String getOutputRequest(InputStream stream) {
        StringBuffer output=new StringBuffer("");
        BufferedReader buffer=  new BufferedReader(new InputStreamReader(stream));
        String s="";
        try {
            while ((s=buffer.readLine())!=null){
                output.append(s);
            }
        }catch (IOException e1){
            e1.printStackTrace();
        }
        return output.toString();
    }

    private void llenarObjetos() {
        Serie serie=new Serie();
        serie.setNombre("Agents of Shield");
        serie.setImg("img_agents");
        serie.setGenero("Acción");
        serie.setActores("Actor 1, Actor 2, Actor 3");
        serie.setNum_temporada(1);

        Temporada temporada=new Temporada();
        temporada.setNum_temporada(1);
        temporada.setNombre_temp("Temporada 1");
        temporada.setEpisodio("Episodio 1");

        Episodio episodio=new Episodio();
        episodio.setEpisodio("Episodio 1");
        episodio.setNum_temporada(1);

        dataResourse.insertDataSerie(serie);
        dataResourse.insertDataTemporada(temporada);
        dataResourse.insertDataEpisodio(episodio);
    }

    //6.--------------------------------------------------------------------------------------------
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(this,DetalleSerie.class);
        intent.putExtra("seleccion",i);
        startActivity(intent);
    }
}
