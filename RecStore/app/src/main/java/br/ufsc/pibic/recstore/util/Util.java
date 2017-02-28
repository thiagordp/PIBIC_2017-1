package br.ufsc.pibic.recstore.util;

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


    /**
     * @param nfcContent
     * @param userId
     */
    public static String createURLNFC(String nfcContent, int userId) {
        /*
         Dados:
            user_id
            type = record
            device_tech
            device_mac
         */
        String user_id = String.valueOf(userId);
        String type = InteractionDefinition.ACTION_SEEN;
        String device_tech = String.valueOf(InteractionDefinition.DEVICE_NFC);
        String device_mac = nfcContent;

        Log.d("DEBUG", "Montando URL...");

        try {
            user_id = URLEncoder.encode(user_id, "UTF-8");
            type = URLEncoder.encode(type, "UTF-8");
            device_tech = URLEncoder.encode(device_tech, "UTF-8");
            device_mac = URLEncoder.encode(device_mac, "UTF-8");

        } catch (Exception e) {

        }

        String stringBuilder = new String();

        // TODO: Pegar o caminho certo para o servidor depois.
        String path = "http://localhost:8080/RecomendacaoServer";

        stringBuilder.concat(path);
        stringBuilder.concat("?user_id=");
        stringBuilder.concat(user_id);
        stringBuilder.concat("&type=");
        stringBuilder.concat(type);
        stringBuilder.concat("&device_tech=");
        stringBuilder.concat(device_tech);
        stringBuilder.concat("&device_mac=");
        stringBuilder.concat(device_mac);

        return stringBuilder;
    }
}
