package br.ufsc.pibic.recstore.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;

import br.ufsc.pibic.recstore.R;

/**
 * Created by trdp on 2/7/17.
 */

public class AsyncTaskImage extends AsyncTask<String, Void, Bitmap> {
    private Context context;
    private View view;

    public AsyncTaskImage(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap imagem = null;
        try {
            ///////////////////
            String url = params[0];

            Log.d("DEBUG_ASYC", "before: " + url);


            URL Url = new URL(url);
            Log.d("DEBUG_ASYC", "befor2");
            HttpURLConnection urlConnection = (HttpURLConnection) Url.openConnection();
            Log.d("DEBUG_ASYC", "befor3");

            Log.d("URL", urlConnection.toString());
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d("DEBUG_ASYC", "befor4");
                imagem = BitmapFactory.decodeStream(urlConnection.getInputStream());
                Log.d("DEBUG_ASYC", "after");

                Log.d("DEBUG_ASYC", "yeah");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imagem;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (bitmap != null) {
            Log.d("TASK_IMG", bitmap.toString());
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
        }
    }

}
