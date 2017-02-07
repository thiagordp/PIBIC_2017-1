/**
 * 
 */
package control;

import org.bson.Document;

import util.InteractionDefinition;

/**
 * @author trdp
 *
 */
public class PopulateDatabase {

	public static void insertProducts() {

		Mongo mongo = new Mongo("pibic", InteractionDefinition.getCollectionList());

		Document product = new Document();

		// TODO: Adicionar URL da imagem

		product.append("product_id", 1);
		product.append("product_name", "Caneta");
		product.append("product_price", 2.50);

		mongo.insere(product, InteractionDefinition.PRODUCT_COLLECTION_NAME);
		product.clear();

		product.append("product_id", 2);
		product.append("product_name", "Relógio");
		product.append("product_price", 50.00);

		mongo.insere(product, InteractionDefinition.PRODUCT_COLLECTION_NAME);
		product.clear();

	}

	public static void insertUsers() {

	}

	public static void insertDevices() {
		Mongo mongo = new Mongo("pibic", InteractionDefinition.getCollectionList());

		Document device = new Document();

		device.append("device_tech", "nfc");
		device.append("device_mac", "EA:C9:0F:74:E0:1D");
		device.append("product_id", 1);

		mongo.insere(device, InteractionDefinition.DEVICE_COLLECTION_NAME);
		device.clear();

		device.append("device_tech", "nfc");
		device.append("device_mac", "03:9D:AA:F8:66:94");
		device.append("product_id", 2);

		mongo.insere(device, InteractionDefinition.DEVICE_COLLECTION_NAME);
		device.clear();
	}

}
