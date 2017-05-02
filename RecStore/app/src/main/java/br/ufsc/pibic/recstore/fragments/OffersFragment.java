package br.ufsc.pibic.recstore.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import br.ufsc.pibic.recstore.R;
import br.ufsc.pibic.recstore.tasks.AsyncTaskURLOffer;
import br.ufsc.pibic.recstore.util.InteractionDefinition;


public class OffersFragment extends Fragment {
    private ListView listView;
    private Integer user_id;
    private View view = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        AsyncTaskURLOffer taskURLOffer = new AsyncTaskURLOffer(getContext(), this.view);
        // Log.d("OFFER", "user: " + user_id);
        String buildURL = InteractionDefinition.buildURL(InteractionDefinition.TYPE_URL_OFFER, user_id);
        Log.d("BUILD_URL", buildURL);

        if (!buildURL.equals("")) {             // Caso a URL tenha sido construída com sucesso
            taskURLOffer.execute(buildURL);
        }

        Toast.makeText(getContext(), "yeah!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_offers, container, false);

        this.view = v;
        this.user_id = getArguments().getInt("user_id"); // Recebe a identificação do usuário.

        return v;
    }
}
