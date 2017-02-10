package br.ufsc.pibic.recstore.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import br.ufsc.pibic.recstore.R;
import br.ufsc.pibic.recstore.tasks.AsyncTaskURLPurchase;
import br.ufsc.pibic.recstore.util.InteractionDefinition;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeenFragment extends Fragment {
    private Integer user_id;
    ListView listView;

    /////////////
    String nome[] = {"Vistos", "Oftalmotorrinolaringologista", "Borracha", "Mouse", "Relógio", "Smartphone"};
    String data[] = {"10/02/2015", "10/03/2015", "02/02/2016", "26/05/2014", "13/05/2014", "10/02/2016"};
    Long id[] = {7899264359674L, 2L, 3L, 4L, 5L, 6L};
    String url[] = {"", "abc", "abc"};
    ////////////////////

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_purchase, container, false);

        this.user_id = getArguments().getInt("user_id");

        AsyncTaskURLPurchase taskURLPurchase = new AsyncTaskURLPurchase(getContext(), v);
        Log.d("PURCHASE", "user: " + user_id);
        String buildURL = InteractionDefinition.buildURL(InteractionDefinition.TYPE_URL_SEEN, user_id);
        taskURLPurchase.execute(buildURL);
        return v;
    }

}
