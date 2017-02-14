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
import br.ufsc.pibic.recstore.tasks.AsyncTaskURLPurchaseSeen;
import br.ufsc.pibic.recstore.util.InteractionDefinition;


/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseFragment extends Fragment {

    private ListView listView;
    private int user_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_purchase_seen, container, false);
        this.user_id = getArguments().getInt("user_id");

        AsyncTaskURLPurchaseSeen taskURLPurchase = new AsyncTaskURLPurchaseSeen(getContext(), v);
        Log.d("PURCHASE", "user: " + user_id);
        String buildURL = InteractionDefinition.buildURL(InteractionDefinition.TYPE_URL_PURCHASE, user_id);

        if (!buildURL.equals("")) {
            taskURLPurchase.execute(buildURL);
        }
        return v;
    }
}
