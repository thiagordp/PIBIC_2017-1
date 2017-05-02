package br.ufsc.pibic.recstore.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import br.ufsc.pibic.recstore.R;
import br.ufsc.pibic.recstore.tasks.AsyncTaskURLOffer;
import br.ufsc.pibic.recstore.tasks.AsyncTaskURLPurchaseSeen;
import br.ufsc.pibic.recstore.util.InteractionDefinition;


/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseFragment extends Fragment {

    private ListView listView;
    private int user_id;
    private View view = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getContext(), "resume", Toast.LENGTH_SHORT).show();

        if (this.view == null) {
            Toast.makeText(getContext(), "Erro", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        AsyncTaskURLPurchaseSeen taskURLPurchase = new AsyncTaskURLPurchaseSeen(getContext(), this.view);
        Log.d("PURCHASE", "user: " + this.user_id);
        String buildURL = InteractionDefinition.buildURL(InteractionDefinition.TYPE_URL_PURCHASE, this.user_id);

        if (!buildURL.equals("")) {
            taskURLPurchase.execute(buildURL);
        }

        Toast.makeText(getContext(), "yeah!!!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_purchase_seen, container, false);

        this.user_id = getArguments().getInt("user_id");
        this.view = v;

        return v;
    }
}
