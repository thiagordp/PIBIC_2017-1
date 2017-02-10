package br.ufsc.pibic.recstore.util;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Thiago Raulino Dal Pont Definições das operações e dos dispositivos
 */
public class InteractionDefinition {

    /**
     * Indicador de requisição de compra
     */
    public static final int TYPE_URL_PURCHASE = 1;

    /**
     * Indicador de requisição de registro
     */
    public static final int TYPE_URL_RECORD = 2;

    /**
     * Indicador de requisição de visto
     */
    public static final int TYPE_URL_SEEN = 3;

    /**
     *
     */
    public static final int TYPE_URL_OFFER = 4;

    /**
     *
     */
    public static final String ACTION_SEEN = "seen";

    /**
     *
     */
    public static final String ACTION_PURCHASE = "purchase";

    /**
     *
     */
    public static final int DEVICE_BEACON = 1;

    /**
     *
     */
    public static final int DEVICE_NFC = 2;

    /**
     *
     */
    public static final String USER_COLLECTION_NAME = "user";

    /**
     *
     */
    public static final String DEVICE_COLLECTION_NAME = "device";

    /**
     *
     */
    public static final String PRODUCT_COLLECTION_NAME = "product";

    /**
     *
     */
    public static final String INTERACTION_COLLECTION_NAME = "interaction";


    public static final String URL_PREFIX = "http://192.168.1.29:8080/RecomendacaoServer?";

    public static List<String> getCollectionList() {

        List<String> lista = new ArrayList<>();

        lista.add(InteractionDefinition.PRODUCT_COLLECTION_NAME);
        lista.add(InteractionDefinition.USER_COLLECTION_NAME);
        lista.add(InteractionDefinition.DEVICE_COLLECTION_NAME);
        lista.add(InteractionDefinition.INTERACTION_COLLECTION_NAME);

        return lista;
    }


    public static String buildURL(int type, int userId) {

        // TODO: trocar pela URL correta
        String path = URL_PREFIX;

        try {
            String strType = URLEncoder.encode(String.valueOf(type), "UTF-8");
            String strUser = URLEncoder.encode(String.valueOf(userId), "UTF-8");

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(path);
            stringBuilder.append("type=");
            stringBuilder.append(strType);
            stringBuilder.append("&user_id=");
            stringBuilder.append(strUser);

            return stringBuilder.toString();

        } catch (Exception e) {
            return null;
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


