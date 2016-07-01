package com.jeisonsolarte.arandapoc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jeisonsolarte.arandapoc.R;
import com.jeisonsolarte.arandapoc.model.Episodio;

import java.util.List;

/**
 * Created by Jeison Solarte on 30/06/2016.
 */
public class Adapter_ListaEpisodios extends BaseAdapter {

    /*
    Utilizo un adaptador para mostrar de forma ordenada cada episodio en la actividad según una lista
    tipo Episodio (clase en el paquete model)
     */

    Context context;
    LayoutInflater inflater;
    List<Episodio> listEpisodios;

    public Adapter_ListaEpisodios(List<Episodio> listEpisodios, Context context) {
        this.listEpisodios = listEpisodios;
        this.context = context;
        inflater=LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return listEpisodios.size();
    }

    @Override
    public Object getItem(int i) {
        return listEpisodios.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class viewHolder{
        TextView nombreEpisodio;
        TextView numeroEpisodio;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        viewHolder holder;

        if (view==null){
            view=inflater.inflate(R.layout.item_lista_episodios,null);

            holder=new viewHolder();
            holder.nombreEpisodio= (TextView) view.findViewById(R.id.item_listEpisodios_nombre);
            holder.numeroEpisodio= (TextView) view.findViewById(R.id.item_listEpisodios_numero);

            view.setTag(holder);
        }else {
            holder= (viewHolder) view.getTag();
        }

        holder.nombreEpisodio.setText(listEpisodios.get(i).getEpisodio());
        holder.numeroEpisodio.setText(String.valueOf(i));

        return view;
    }
}
