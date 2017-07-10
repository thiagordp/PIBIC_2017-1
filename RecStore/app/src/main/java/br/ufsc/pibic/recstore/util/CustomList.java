package br.ufsc.pibic.recstore.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.ufsc.pibic.recstore.R;
import br.ufsc.pibic.recstore.tasks.AsyncTaskImage;

/**
 * Created by trdp on 2/7/17.
 */

public class
CustomList extends ArrayAdapter<String> {


    private Activity context;

    // Parâmetros que cada item da lista vai ter.
    private String[] names;
    private Long[] id;
    private String[] dates;
    private String[] urls;

    public CustomList(Activity context,
                      String[] names, Long[] id, String[] dates, String[] urls) {
        super(context, R.layout.list_item, names);
        this.context = context;
        this.dates = dates;
        this.id = id;
        this.names = names;
        this.urls = urls;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null, true);
        TextView tvId = (TextView) rowView.findViewById(R.id.tvId);
        TextView tvName = (TextView) rowView.findViewById(R.id.tvName);
        TextView tvDate = (TextView) rowView.findViewById(R.id.tvDate);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.image_not_available);


        // Tarefa que faz requisição da imagem de representação do produto em segundo plano.
        AsyncTaskImage taskImage = new AsyncTaskImage(getContext(), rowView);
        taskImage.execute(urls[position]);


        tvId.setText(String.valueOf(id[position]));
        tvName.setText(names[position]);
        tvDate.setText(dates[position]);

        return rowView;
    }
}
