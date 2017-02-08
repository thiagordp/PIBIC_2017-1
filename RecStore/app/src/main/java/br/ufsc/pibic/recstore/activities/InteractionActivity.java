package br.ufsc.pibic.recstore.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import br.ufsc.pibic.recstore.R;
import br.ufsc.pibic.recstore.tasks.AsyncTaskURLBeacon;

public class InteractionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();

        String url = "";

        if (extras != null) {
            url = extras.getString("url");
        }
        Log.d("INT_ADT", url);
        AsyncTaskURLBeacon taskURLBeacon = new AsyncTaskURLBeacon(getApplicationContext(), getWindow().getDecorView().getRootView());

        taskURLBeacon.execute(url);
    }
}
