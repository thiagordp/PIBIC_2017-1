/**
 * 
 */
package util;

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
	public static final int TYPE_URL_LOGIN = 5;

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

	public static List<String> getCollectionList() {

		List<String> lista = new ArrayList<>();

		lista.add(InteractionDefinition.PRODUCT_COLLECTION_NAME);
		lista.add(InteractionDefinition.USER_COLLECTION_NAME);
		lista.add(InteractionDefinition.DEVICE_COLLECTION_NAME);
		lista.add(InteractionDefinition.INTERACTION_COLLECTION_NAME);

		return lista;
	}

}
