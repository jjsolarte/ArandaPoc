package com.jeisonsolarte.arandapoc;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeisonsolarte.arandapoc.adapters.Adapter_ListaEpisodios;
import com.jeisonsolarte.arandapoc.database.DataResourse;
import com.jeisonsolarte.arandapoc.model.Episodio;
import com.jeisonsolarte.arandapoc.model.Serie;
import com.jeisonsolarte.arandapoc.model.Temporada;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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

public class DetalleSerie extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    /*
    1.  En primer lugar, recibo el valor de la posición del ítem de la lista de películas en la que el usuario
        realiza click en la actividad Busqueda. Esto con el fin de cargar los valores de cada película pues según
        el proveedor http://www.omdbapi.com/ debo realizar una solicitud diferente para obtener detalles de cada temporada.
    2.  Antes de solicitar los detalles de los episodios, debo confirmar la temporada seleccionada en el Spinner
        por el usuario, pues los capítulos a mostrar dependen de este valor. Así es como llamo nuevamente a una taréa JsonTask
        y además, lleno los campos de cuyo valor ya he recolectado anteriormente, como Nombre de la Serie e Imagen.
    3.  Como lo realizado anteriormente, al obtener el JSON del servicio web, lo descompongo en una lista para llenar
        listEpisodios, como su nombre lo indica, me brinda la información de cada episodio según la temporada seleccionada.
        Toda la información de Series, Temporadas y Episodios, se encuentra en la base de datos local en el paquete database.
    4.  Finalmente, finalizo la actividad cuando el usuario presiona el botón Regresar
     */

    TextView nombreSerie;
    TextView generoSerie;
    TextView actoresSerie;
    ImageView imgSerie;
    Button buttonRegresar;

    Spinner spinner;
    ListView listView;

    DataResourse dataResourse;
    Adapter_ListaEpisodios adapter_listaEpisodios;

    Serie serie;

    int posActualSpin=0;

    List<JSONObject> listaRequest;
    List<Episodio> listEpisodios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_serie);

        nombreSerie= (TextView) findViewById(R.id.detalle_nombre);
        generoSerie= (TextView) findViewById(R.id.detalle_genero);
        actoresSerie= (TextView) findViewById(R.id.detalle_actores);
        imgSerie= (ImageView) findViewById(R.id.detalle_img);
        spinner= (Spinner) findViewById(R.id.detalle_spin);
        listView= (ListView) findViewById(R.id.detalle_lista_episodios);
        buttonRegresar= (Button) findViewById(R.id.detalle_btn_regresar);

        buttonRegresar.setOnClickListener(this);

        dataResourse=new DataResourse(this);

        //1-----------------------------------------------------------------------------------------
        Bundle bundle=getIntent().getExtras();
        int aux=bundle.getInt("seleccion");
        serie=new Serie();
        serie=Busqueda.listaSerie.get(aux);

        spinner.setOnItemSelectedListener(this);
        //mostrarInformacionObtenida();
    }

    private void obtenerDatosRestantes(int pos) {
        //recibir información de los episodios según la temporada
        //2-----------------------------------------------------------------------------------------
        listEpisodios=new ArrayList<Episodio>();
        nombreSerie.setText(serie.getNombre());
        generoSerie.setText(serie.getGenero());
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
        ImageLoader.getInstance().displayImage(serie.getImg(), imgSerie);

        obtenerNombresEpisodios(pos);
    }

    private void obtenerNombresEpisodios(int pos) {
        posActualSpin=pos+1;
        new JsonTask(DetalleSerie.this).execute(serie.getNombre());
    }

    //4---------------------------------------------------------------------------------------------
    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        obtenerDatosRestantes(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

                String url_plus="http://www.omdbapi.com/?t="+serie.getNombre()+"&Season="+posActualSpin+"";

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
                    JSONArray jsonArray=jsonObject.getJSONArray("Episodes");
                    listaRequest=new ArrayList<JSONObject>();
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObjectAux=new JSONObject();
                        jsonObjectAux=jsonArray.getJSONObject(i);
                        listaRequest.add(jsonObjectAux);
                        Episodio episodio=new Episodio();
                        episodio.setEpisodio(listaRequest.get(i).getString("Title"));
                        episodio.setNum_temporada(posActualSpin);
                        listEpisodios.add(episodio);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter_listaEpisodios=new Adapter_ListaEpisodios(listEpisodios,DetalleSerie.this);
                listView.setAdapter(adapter_listaEpisodios);
            }else {
                Toast.makeText(context,"No hay restultados",Toast.LENGTH_SHORT).show();
            }

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

}
