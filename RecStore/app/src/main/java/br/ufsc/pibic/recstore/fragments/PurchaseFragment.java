package br.ufsc.pibic.recstore.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import br.ufsc.pibic.recstore.R;
import br.ufsc.pibic.recstore.util.CustomList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseFragment extends Fragment {

    private ListView listView;

    String nome[] = {"Compras", "Oftalmotorrinolaringologista", "Borracha", "Mouse", "Relógio", "Smartphone"};
    String data[] = {"10/02/2015", "10/03/2015", "02/02/2016", "26/05/2014", "13/05/2014", "10/02/2016"};
    Long id[] = {7899264359674L, 2L, 3L, 4L, 5L, 6L};
    String url[] = {"", "abc", "abc"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_seen, container, false);
        // Inflate the layout for this fragment

        CustomList customList = new CustomList(getActivity(), nome, id, data, url);
        listView = (ListView) v.findViewById(R.id.lvSeens);
        listView.setAdapter(customList);


        Toast.makeText(getContext(), "Yeah", Toast.LENGTH_SHORT).show();

        return v;
    }

}
