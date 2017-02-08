package br.ufsc.pibic.recstore.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.ufsc.pibic.recstore.R;

/**
 * Created by trdp on 2/7/17.
 */

public class
CustomList extends ArrayAdapter<String> {

    private Activity context;
    private String[] names;
    private Long[] id;
    private String[] dates;
    private String[] urls;

    public CustomList(Context context, int resource, Activity context1, String[] dates, Long[] id, String[] names, String[] urls) {
        super(context, resource);
        this.context = context1;
        this.dates = dates;
        this.id = id;
        this.names = names;
        this.urls = urls;
    }


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
        imageView.setImageResource(R.drawable.ic_menu_camera);  // TODO pegar imagem correta

        tvId.setText(String.valueOf(id[position]));
        tvName.setText(names[position]);
        tvDate.setText(dates[position]);

        return rowView;
    }
}
