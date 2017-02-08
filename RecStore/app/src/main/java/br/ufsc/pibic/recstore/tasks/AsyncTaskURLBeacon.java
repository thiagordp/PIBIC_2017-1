package br.ufsc.pibic.recstore.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import br.ufsc.pibic.recstore.R;


/**
 * Created by trdp on 2/4/17.
 */

public class AsyncTaskURLBeacon extends AsyncTask<String, Void, JSONObject> {

    private Context context;
    private View view;

    public AsyncTaskURLBeacon(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected JSONObject doInBackground(String... urls) {

        // TODO: Executar a URL
//        for (String strUrl : urls) {
//            try {
//                URL url = new URL(strUrl);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    String response = Util.convertStreamToString(urlConnection.getInputStream());
//
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    // TODO: Verificar o caso vazio
//
//                    return jsonObject;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.e("JSON_PRE", e.getMessage());
//            }
//        }

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("product_id", 123);
            jsonObject.put("product_name", "Lapis");
            jsonObject.put("product_value", 2.50);
            jsonObject.put("product_url", "abc");
        } catch (Exception e) {
            /**/
        }
        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        try {
            TextView edtCode, edtName, edtValue;
            ImageView imageView;

            edtCode = (TextView) view.findViewById(R.id.edtCode);
            edtName = (TextView) view.findViewById(R.id.edtName);
            edtValue = (TextView) view.findViewById(R.id.edtValue);

            imageView = (ImageView) view.findViewById(R.id.imageView);
            // Pegar a imagem

            // Colocar os textos

            String code = json.get("product_id").toString();
            String name = json.get("product_name").toString();
            String value = json.get("product_value").toString();
            String url = json.get("product_url").toString();

            ///////////////////

            // Criar outra para a URL
            url = "http://www.casasbahia-imagens.com.br/TelefoneseCelulares/Smartphones/Android/5409350/177974681/Smartphone-Samsung-Galaxy-J7-Duos-Preto-com-Dual-chip-Tela-5-5-4G-Camera-13MP-Android-5-1-e-Processador-Octa-Core-de-1-5-Ghz-5409350.jpg";

            /////////////
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
