package br.ufsc.pibic.recstore.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import br.ufsc.pibic.recstore.R;
import br.ufsc.pibic.recstore.tasks.AsyncTaskURLOffer;
import br.ufsc.pibic.recstore.util.InteractionDefinition;


public class OffersFragment extends Fragment {
    private ListView listView;
    private Integer user_id;
    String nome[];
    String data[];
    Long id[];
    String url[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_offers, container, false);

        this.user_id = getArguments().getInt("user_id");

        AsyncTaskURLOffer taskURLOffer = new AsyncTaskURLOffer(getContext(), v);
        Log.d("OFFER", "user: " + user_id);
        String buildURL = InteractionDefinition.buildURL(InteractionDefinition.TYPE_URL_OFFER, user_id);
        taskURLOffer.execute(buildURL);

        return v;
    }
}
