package br.ufsc.pibic.recstore.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import org.json.JSONArray;

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


        return null;
    }

    @Override
    protected void onPostExecute(JSONArray objects) {
        super.onPostExecute(objects);
    }
}
