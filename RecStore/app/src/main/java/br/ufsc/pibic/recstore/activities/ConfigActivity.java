package br.ufsc.pibic.recstore.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.ufsc.pibic.recstore.R;
import br.ufsc.pibic.recstore.fragments.SettingsFragment;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        // Inicializa o fragment
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new SettingsFragment()).commit(); 
    }
}
