package br.ufsc.pibic.recstore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.ufsc.pibic.recstore.R;
import br.ufsc.pibic.recstore.tasks.AsyncTaskLogin;
import br.ufsc.pibic.recstore.util.InteractionDefinition;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = "LOGIN_ACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instanciação de objetos de layout
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.btn_login);
        final EditText edtEmail = (EditText) findViewById(R.id.edtEmail);
        final EditText edtPassword = (EditText) findViewById(R.id.edtPassword);
        final Button btnCfg = (Button) findViewById(R.id.btn_cfg);

        // Configuração da ação executada em função do evento Click.
        btnCfg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicializa a Activity de Configurações
                startActivity(new Intent(LoginActivity.this, ConfigActivity.class));
            }
        });

        // Configuração da ação executada em função do evento Click.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String login = edtEmail.getText().toString();
                    String password = edtPassword.getText().toString();

                    // Verificação de um login default
                    if (login.equals("labdata") && password.equals("pibic")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("user_id", 1);
                        startActivity(intent);
                    } else { // Requisição ao servidor para verificação de identidade.
                        AsyncTaskLogin asyncTaskLogin = new AsyncTaskLogin(getApplicationContext(), getWindow().getDecorView().getRootView()); // Instanciação da tarefa assíncrona
                        String url = InteractionDefinition.buildURL(InteractionDefinition.TYPE_URL_LOGIN, login, password); // Construção da URL
                        Log.d(TAG, "URL: " + url);
                        asyncTaskLogin.execute(url); // Executa a tarefa em segundo plano.
                    }

                } catch (Exception e) {
                    Log.e("DEBUG", e.getMessage());
                }

            }
        });
    }
}
