package br.ufsc.pibic.recstore.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

import br.ufsc.pibic.recstore.activities.MainActivity;
import br.ufsc.pibic.recstore.util.Util;

/**
 * Created by trdp on 2/27/17.
 */

public class AsyncTaskLogin extends AsyncTask<String, Void, JSONObject> {

    private final String TAG = "ASYNC_TASK_LOGIN";
    private Context context;
    private View view;

    public AsyncTaskLogin(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected JSONObject doInBackground(String... urls) {

        for (String strUrl : urls) {
            try {
                Log.d(TAG, "Conectando com url: " + strUrl);

                URL url = new URL(strUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                Log.d(TAG, "Verificando código de resposta");

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String response = Util.convertStreamToString(urlConnection.getInputStream());

                    Log.d(TAG, "Resposta: " + response);
                    http:
//localhost:8080/RecomendacaoServer?user_id=2&type=4
                    return new JSONObject(response);
                }


            } catch (Exception e) {
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        try {
            if (jsonObject != null) Log.d(TAG, jsonObject.getString("result"));

            if (jsonObject != null && jsonObject.getString("result").equals("[\"granted\"]")) {
                Log.d(TAG, "Access granted");
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Login e/ou senha inválidos!\nTente novamente!", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Log.d(TAG, "Json: " + jsonObject);
            e.printStackTrace();
        }
    }
}
