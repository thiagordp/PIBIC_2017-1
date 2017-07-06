package br.ufsc.pibic.recstore.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by trdp on 2/3/17.
 */
public class Util {

    public final static String CONFIG_PATH = Environment.getExternalStorageDirectory().toString() + "/conf.dat";

    /**
     * Captura os dados recebidos de InputStream
     *
     * @param is InputStream
     * @return String com conteúdo extraído
     */
    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static JSONArray parserJason(String json) {
        try {

            Log.d("JSON", "JSON: " + json);

            Log.d("JSON", "Criando objeto");
            JSONObject jsonObject = new JSONObject(json);

            Log.d("JSON", "Criando array");
            JSONArray jsonArray = jsonObject.getJSONArray("products");

            Log.d("JSON", "Pegando os objetos do array");
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Log.d("JSON", "Obj " + i + " || " + object.toString());
                    // Pegar os dados do bixo.

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return jsonArray;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DEBUG", e.getMessage());
        }
        return null;
    }

    public static String createURLBeacon(Beacon beacon, int userId) {
        String user_id = String.valueOf(userId);
        String type = String.valueOf(InteractionDefinition.TYPE_URL_RECORD);
        String device_tech = String.valueOf(InteractionDefinition.DEVICE_BEACON);
        String device_mac = beacon.getBluetoothAddress();
        String beacon_major = beacon.getId2().toString();
        String beacon_minor = beacon.getId3().toString();
        String beacon_rssi = String.valueOf(beacon.getRssi());

        Log.d("DEBUG", "Montando URL BEACON...");
        Log.d("DEBUG", "MAC: " + device_mac + "\tmajor: " + beacon_major + "\tminor: " + beacon_minor + "\trssi: " + beacon_rssi);

        try {
            user_id = URLEncoder.encode(user_id, "UTF-8");
            type = URLEncoder.encode(type, "UTF-8");
            device_tech = URLEncoder.encode(device_tech, "UTF-8");
            device_mac = URLEncoder.encode(device_mac, "UTF-8");

            beacon_major = URLEncoder.encode(beacon_major, "UTF-8");
            beacon_minor = URLEncoder.encode(beacon_minor, "UTF-8");
            beacon_rssi = URLEncoder.encode(beacon_rssi, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuilder stringBuilder = new StringBuilder();

        // TODO: Pegar o caminho certo para o servidor depois.
        String path = InteractionDefinition.URL_PREFIX; // Provisório

        stringBuilder.append(path);
        stringBuilder.append("user_id=");
        stringBuilder.append(user_id);
        stringBuilder.append("&type=");
        stringBuilder.append(type);
        stringBuilder.append("&device_tech=");
        stringBuilder.append(device_tech);
        stringBuilder.append("&device_mac=");
        stringBuilder.append(device_mac);
        stringBuilder.append("&beacon_major=");
        stringBuilder.append(beacon_major);
        stringBuilder.append("&beacon_minor=");
        stringBuilder.append(beacon_minor);
        stringBuilder.append("&beacon_rssi=");
        stringBuilder.append(beacon_rssi);
        Log.d("DEBUG", "URL: " + stringBuilder.toString());

        return stringBuilder.toString();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
