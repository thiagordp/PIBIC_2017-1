package br.ufsc.pibic.recstore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import br.ufsc.pibic.recstore.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.btn_login);
        final EditText edtEmail = (EditText) findViewById(R.id.edtEmail);
        final EditText edtPassword = (EditText) findViewById(R.id.edtPassword);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    if (edtEmail.getText().toString().equals("pibic@gmail.com") &&
//                            edtPassword.getText().toString().equals("pibic123")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Login ou senha incorretos", Toast.LENGTH_SHORT).show();
//                    }

                } catch (Exception e) {
                    Log.e("DEBUG", e.getMessage());
                }
            }
        });
    }
}
