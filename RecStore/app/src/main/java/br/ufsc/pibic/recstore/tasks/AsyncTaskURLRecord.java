package br.ufsc.pibic.recstore.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

import br.ufsc.pibic.recstore.R;
import br.ufsc.pibic.recstore.util.Util;


/**
 * Created by trdp on 2/4/17.
 */

public class AsyncTaskURLRecord extends AsyncTask<String, Void, JSONObject> {

    private Context context;
    private View view;

    public AsyncTaskURLRecord(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected JSONObject doInBackground(String... urls) {

        // TODO: Executar a URL
        for (String strUrl : urls) {
            try {
                URL url = new URL(strUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String response = Util.convertStreamToString(urlConnection.getInputStream());

                    Log.d("ASYNC_TASK_BLE", "resposta: " + response);
                    JSONObject object = new JSONObject(response);
                    return object;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("JSON_PRE", e.getMessage());
            }
        }


        return null;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        try {
            TextView edtCode, edtName, edtValue;


            edtCode = (TextView) view.findViewById(R.id.edtCode);
            edtName = (TextView) view.findViewById(R.id.edtName);
            edtValue = (TextView) view.findViewById(R.id.edtValue);


            String code = json.get("product_id").toString();
            String name = json.get("product_name").toString();
            String value = json.get("product_price").toString();
            String url = json.get("product_url").toString();

            edtCode.setText(code);
            edtName.setText(name);
            edtValue.setText(value);

            AsyncTaskImage asyncTaskImage = new AsyncTaskImage(context, view);
            asyncTaskImage.execute(url);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON_PON", "-> " + e.getMessage());

        }
    }
}
