package com.jeisonsolarte.arandapoc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeisonsolarte.arandapoc.R;
import com.jeisonsolarte.arandapoc.model.Serie;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by Jeison Solarte on 30/06/2016.
 */
public class Adapter_ListaSeries extends BaseAdapter {

    /*
    Utilizo un adaptador personalizado para cargar de forma óptima las imagenes en la lista presentada
    en la actividad Busqueda y de forma ordenada
     */

    Context context;
    List<Serie> serieList;
    LayoutInflater inflater;

    public Adapter_ListaSeries(Context context, List<Serie> serieList) {
        this.context = context;
        this.serieList = serieList;
        inflater=LayoutInflater.from(this.context);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
                .cacheOnDisk(true)
        .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
        .defaultDisplayImageOptions(defaultOptions)
        .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public int getCount() {
        return serieList.size();
    }

    @Override
    public Object getItem(int i) {
        return serieList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder{
        TextView textView;
        ImageView imageView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if (view==null){
            view=inflater.inflate(R.layout.item_lista_busqueda,null);

            holder=new ViewHolder();
            holder.textView= (TextView) view.findViewById(R.id.item_list_txt);
            holder.imageView= (ImageView) view.findViewById(R.id.item_list_img);

            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }

        holder.textView.setText(serieList.get(i).getNombre());
        ImageLoader.getInstance().displayImage(serieList.get(i).getImg(), holder.imageView);

        return view;
    }
}
