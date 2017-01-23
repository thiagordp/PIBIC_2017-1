package br.ufsc.pibic.nfcrw;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        // Mostrar o ícone do app na ActionBar
        try {
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        } catch (NullPointerException e) {
            Log.e(TAG, "Config icon error: " + e.getMessage());
        }
*/
        // Configuração da ação para o click no botão de leitura
        FloatingActionButton btnRead = (FloatingActionButton) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent readIntent = new Intent(MainActivity.this, NFCReadActivity.class);
                MainActivity.this.startActivity(readIntent);
            }
        });

        // Configuração da ação para o click no botão de escrita
        FloatingActionButton btnWrite = (FloatingActionButton) findViewById(R.id.btnWrite);
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent readIntent = new Intent(MainActivity.this, NFCWriteActivity.class);
                MainActivity.this.startActivity(readIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        try {
            if (getIntent().getExtras().getBoolean("WSUC", false)) {
                Toast.makeText(this, "Gravação efetuada!", Toast.LENGTH_SHORT).show();
                getIntent().removeExtra("WSUC");
            }
        } catch (NullPointerException e) {
            Log.e("LOG", e.getMessage());
        }


        super.onResume();
    }
}
