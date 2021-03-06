package br.ufsc.pibic.recstore.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import br.ufsc.pibic.recstore.R;
import br.ufsc.pibic.recstore.util.CustomList;
import br.ufsc.pibic.recstore.util.Util;

/**
 * Created by trdp on 2/7/17.
 */

public class AsyncTaskURLOffer extends AsyncTask<String, Void, JSONArray> {

    private Context context;
    private View view;

    public AsyncTaskURLOffer(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected JSONArray doInBackground(String... urls) {

        for (String strUrl : urls) {

            try {
                Log.d("ASYNC_OFFER", "Conectando com url");

                URL url = new URL(strUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                Log.d("ASYNC_OFFER", "Verificando código de resposta");

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String response = Util.convertStreamToString(urlConnection.getInputStream());

                    Log.d("ASYNC_OFFER", "Resposta: " + response);

                    JSONObject json = new JSONObject(response);
                    Log.d("ASYNC_OFFER", "JSON: " + json.toString());
                    JSONArray jsonArray = json.getJSONArray("products");

                    Log.d("ASYNC_OFFER", "JSON_Array: " + jsonArray.toString());

                    return jsonArray;
                } else {
                    Log.d("ASYNC_OFFER", "Não foi possível conectar com o servidor.");

                }
            } catch (IOException e) {

                e.printStackTrace();
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONArray objects) {
        super.onPostExecute(objects);

        if (objects != null) {
            Integer length = objects.length();
            Log.d("ASYNC_OFFER", "Length: " + length);

            String[] nome = new String[length];
            Long id[] = new Long[length];
            String data[] = new String[length];
            String url[] = new String[length];

            for (int i = 0; i < length; i++) {
                JSONObject json;

                try {
                    json = objects.getJSONObject(i);

                    nome[i] = json.getString("product_name");
                    id[i] = json.getLong("product_id");
                    url[i] = json.getString("product_url");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            ListView listView;
            Log.d("ASYNC_OFFER", "View: " + view.toString());
            listView = (ListView) view.findViewById(R.id.lvOffers);
            Log.d("ASYNC_OFFER", "ListView: " + listView);
            CustomList customList = new CustomList((Activity) context, nome, id, data, url);
            listView.setAdapter(customList);
        }
    }
}
