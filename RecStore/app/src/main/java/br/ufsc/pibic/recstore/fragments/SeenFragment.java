package br.ufsc.pibic.recstore.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.ufsc.pibic.recstore.R;
import br.ufsc.pibic.recstore.tasks.AsyncTaskURLPurchaseSeen;
import br.ufsc.pibic.recstore.util.InteractionDefinition;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeenFragment extends Fragment {
    private Integer user_id;
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

        // Instanciação da tarefa que faz a requisição da lista de produtos visualizados.
        AsyncTaskURLPurchaseSeen taskURLSeen = new AsyncTaskURLPurchaseSeen(getContext(), this.view);
        // Construção da URL
        String buildURL = InteractionDefinition.buildURL(InteractionDefinition.TYPE_URL_SEEN, user_id);
        Log.d("BUILD_URL", buildURL);

        if (!buildURL.equals("")) {             // Caso a URL tenha sido construída com sucesso.
            taskURLSeen.execute(buildURL);      // Executa a tarefa.
        }

        Toast.makeText(getContext(), "yeah!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_purchase_seen, container, false); //
        this.view = v;
        this.user_id = getArguments().getInt("user_id"); // Extração do parâmetro

        return v;
    }
}
