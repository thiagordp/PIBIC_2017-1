package br.ufsc.pibic.recstore.util;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Thiago Raulino Dal Pont Definições das operações e dos dispositivos
 */
public class InteractionDefinition {

    /**
     * Indicador de requisição de lista de produtos comprados
     */
    public static final int TYPE_URL_PURCHASE = 1;

    /**
     * Indicador de requisição de registro
     */
    public static final int TYPE_URL_RECORD = 2;

    /**
     * Indicador de requisição de lista de produtos vistos
     */
    public static final int TYPE_URL_SEEN = 3;

    /**
     * Indicador de requisição de recomendações
     */
    public static final int TYPE_URL_OFFER = 4;

    /**
     * Indicador de requisição de login
     */
    public static final int TYPE_URL_LOGIN = 5;

    /**
     *
     */
    public static final int DEVICE_BEACON = 1;

    /**
     *
     */
    public static final int DEVICE_NFC = 2;

    public static String ip_server = getIp();

    public static int timeBLE = getTime();

    public static String URL_PREFIX = "http://" + ip_server + ":8080/RecomendacaoServer?";

    /**
     * Gravação em arquivo do tempo mínimo de aproximação do beacon
     *
     * @param time Novo valor de tempo em ms
     */
    public static void setTime(Integer time) {
        try {
            FileInputStream fileInputStream = new FileInputStream(Util.CONFIG_PATH);

            byte[] dados = new byte[fileInputStream.available()];

            fileInputStream.read(dados, 0, dados.length);
            fileInputStream.close();

            String text = new String(dados, 0, dados.length, "UTF-8");
            JSONObject json = new JSONObject(text);
            Log.d("JSON", json.toString());

            json.put("time", time);
            timeBLE = time;

            FileOutputStream fileOutputStream = new FileOutputStream(Util.CONFIG_PATH);
            dados = json.toString().getBytes();
            fileOutputStream.write(dados, 0, dados.length);


        } catch (JSONException e) {
            //Toast.makeText(v.getContext(), "Erro ao abrir o arquivo.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            //Toast.makeText(v.getContext(), "Erro ao abrir o arquivo.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Leitura do tempo mínimo de aproximação do beacon.
     *
     * @return Tempo em milissegundos
     */
    public static int getTime() {
        try {
            FileInputStream fileInputStream = new FileInputStream(Util.CONFIG_PATH);

            byte[] dados = new byte[fileInputStream.available()];

            fileInputStream.read(dados, 0, dados.length);
            fileInputStream.close();

            String text = new String(dados, 0, dados.length, "UTF-8");
            JSONObject json = new JSONObject(text);
            Log.d("JSON", json.toString());

            return json.getInt("time");

        } catch (JSONException e) {
            //   Toast.makeText(v.getContext(), "Erro ao abrir o arquivo.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            //  Toast.makeText(v.getContext(), "Erro ao abrir o arquivo.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return 5;
    }

    /**
     * Gravação em arquivo do novo IP do servidor da aplicação
     *
     * @param ip Novo IP
     */
    public static void setIp(String ip) {
        try {
            FileInputStream fileInputStream = new FileInputStream(Util.CONFIG_PATH);

            byte[] dados = new byte[fileInputStream.available()];

            fileInputStream.read(dados, 0, dados.length);
            fileInputStream.close();

            String text = new String(dados, 0, dados.length, "UTF-8");
            JSONObject json = new JSONObject(text);
            Log.d("JSON", json.toString());

            json.put("ipserver", ip);
            ip_server = ip;

            FileOutputStream fileOutputStream = new FileOutputStream(Util.CONFIG_PATH);
            dados = json.toString().getBytes();
            fileOutputStream.write(dados, 0, dados.length);


        } catch (JSONException e) {
            //   Toast.makeText(v.getContext(), "Erro ao abrir o arquivo.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            //  Toast.makeText(v.getContext(), "Erro ao abrir o arquivo.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Leitura do IP do servidor
     *
     * @return IP do servidor
     */
    public static String getIp() {

        try {
            FileInputStream fileInputStream = new FileInputStream(Util.CONFIG_PATH);

            byte[] dados = new byte[fileInputStream.available()];

            fileInputStream.read(dados, 0, dados.length);
            fileInputStream.close();

            String text = new String(dados, 0, dados.length, "UTF-8");
            JSONObject json = new JSONObject(text);
            Log.d("JSON", json.toString());

            return json.getString("ipserver");

        } catch (JSONException e) {
            //   Toast.makeText(v.getContext(), "Erro ao abrir o arquivo.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            //   Toast.makeText(v.getContext(), "Erro ao abrir o arquivo.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return "10.0.0.106";
    }

    /**
     * Construir URL a partir dos parâmetros informados
     *
     * @param type   Tipo de URL (compra, registro, etc.)
     * @param userId Identificação do usuário
     * @return URL para requisição ao servidor
     */
    public static String buildURL(int type, int userId) {

        String path = URL_PREFIX;

        try {
            String strType = URLEncoder.encode(String.valueOf(type), "UTF-8");
            String strUser = URLEncoder.encode(String.valueOf(userId), "UTF-8");

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(path);
            stringBuilder.append("type=");
            stringBuilder.append(type);
            stringBuilder.append("&user_id=");
            stringBuilder.append(strUser);

            return stringBuilder.toString();

        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Construir URL a partir dos parâmetros informados específicos para login
     *
     * @param type     Tipo de URL login
     * @param login    Login de usuário
     * @param password Senha do usuário
     * @return URL para requisição de login ao servidor
     */
    public static String buildURL(int type, String login, String password) {

        String path = URL_PREFIX;

        try {
            String strType = URLEncoder.encode(String.valueOf(type), "UTF-8");
            String strLogin = URLEncoder.encode(String.valueOf(login), "UTF-8");
            String strPassword = URLEncoder.encode(String.valueOf(password), "UTF-8");

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(path);
            stringBuilder.append("type=");
            stringBuilder.append(strType);

            stringBuilder.append("&user_login=");
            stringBuilder.append(strLogin);
            stringBuilder.append("&user_password=");
            stringBuilder.append(strPassword);

            return stringBuilder.toString();

        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Construir URL a partir dos parâmetros informados
     *
     * @param userId          Identificação do usuário
     * @param interactionType Tipo de Interação
     * @param deviceType      Tipo de dispositivo
     * @param deviceMac       MAC do dispositivo
     * @return URL
     */
    public static String buildURL(int userId, int interactionType, int deviceType, String deviceMac) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(InteractionDefinition.URL_PREFIX);

        stringBuilder.append("user_id=");
        stringBuilder.append(userId);

        stringBuilder.append("&type=");
        stringBuilder.append(interactionType);

        stringBuilder.append("&device_tech=");
        stringBuilder.append(deviceType);

        stringBuilder.append("&device_mac=");
        try {
            stringBuilder.append(URLEncoder.encode(deviceMac, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}


