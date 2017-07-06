package br.ufsc.pibic.recstore.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import br.ufsc.pibic.recstore.R;
import br.ufsc.pibic.recstore.tasks.AsyncTaskURLRecord;

/**
 * Classe responsável por processar a interação com um beacon ou tag NFC.
 */
public class InteractionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interaction);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        Bundle extras = getIntent().getExtras(); // Objeto que contém os parâmetros vindos da activity principal.

        String url = "";
        if (extras != null) {
            url = extras.getString("url"); // Captura do parâmetro "url"
        }
        Log.d("INT_ADT", url);
        AsyncTaskURLRecord taskURLBeacon = new AsyncTaskURLRecord(getApplicationContext(), getWindow().getDecorView().getRootView());

        taskURLBeacon.execute(url);
    }

}
