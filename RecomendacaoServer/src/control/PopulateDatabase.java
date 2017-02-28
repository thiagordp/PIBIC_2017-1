/**
 * 
 */
package control;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

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
		product.append("product_name", "Smartphone Samsung");
		product.append("product_price", 2.50);
		product.append("product_url",
							"http://www.casasbahia-imagens.com.br/TelefoneseCelulares/Smartphones/Android/5409350/177974681/Smartphone-Samsung-Galaxy-J7-Duos-Preto-com-Dual-chip-Tela-5-5-4G-Camera-13MP-Android-5-1-e-Processador-Octa-Core-de-1-5-Ghz-5409350.jpg");

		mongo.insere(product, InteractionDefinition.PRODUCT_COLLECTION_NAME);
		product.clear();

		product.append("product_id", 2);
		product.append("product_name", "Relógio");
		product.append("product_price", 50.00);
		product.append("product_url", "http://www.ablogtowatch.com/wp-content/uploads/2009/10/tissot-v8-quartz-chronograph-2009-watch.jpg");

		mongo.insere(product, InteractionDefinition.PRODUCT_COLLECTION_NAME);
		product.clear();

		product.append("product_id", 3);
		product.append("product_name", "Mouse");
		product.append("product_price", 35.00);
		product.append("product_url",
							"http://compass.microsoft.com/assets/4b/d0/4bd0d69b-f194-4364-b5f7-d7bc95e795bc.jpg?n=mk_ambi_blk_large.jpg");

		mongo.insere(product, InteractionDefinition.PRODUCT_COLLECTION_NAME);
		product.clear();

		product.append("product_id", 4);
		product.append("product_name", "Xícara");
		product.append("product_price", 15.00);
		product.append("product_url", "http://vitrine.tokstok.com.br/pnv/570/c/citxch_vdam.jpg");

		mongo.insere(product, InteractionDefinition.PRODUCT_COLLECTION_NAME);
		product.clear();

		product.append("product_id", 5);
		product.append("product_name", "Telescópio");
		product.append("product_price", 1359.90);
		product.append("product_url", "http://www.universetoday.com/wp-content/uploads/2012/08/21048_2008powerseeker_large_1.jpg");

		mongo.insere(product, InteractionDefinition.PRODUCT_COLLECTION_NAME);
		product.clear();

		mongo.fechaConexao();
	}

	public static void insertPurchase() {
		Mongo mongo = new Mongo("pibic", InteractionDefinition.getCollectionList());

		Document purchase = new Document();
		Random random = new Random();

		for (int i = 0; i < 10; i++) {

			purchase.append("type", InteractionDefinition.ACTION_PURCHASE);
			purchase.append("user_id", random.nextInt(3) + 1);
			Calendar calendar = Calendar.getInstance();
			Date currentTimestamp = new Timestamp(calendar.getTime().getTime());
			purchase.append("timestamp", currentTimestamp.toString());
			purchase.append("product_id", random.nextInt(3) + 1);

			mongo.insere(purchase, InteractionDefinition.INTERACTION_COLLECTION_NAME);

			purchase.clear();
		}

		mongo.fechaConexao();
	}

	public static void insertUsers() {
		Mongo mongo = new Mongo("pibic", InteractionDefinition.getCollectionList());

		Document userDoc = new Document();

		userDoc.append("user_name", "Kevin M. Watkins");
		userDoc.append("user_login", "kevin");
		userDoc.append("user_password", "yole123");
		userDoc.append("user_id", 1);
		userDoc.append("user_address", "Rua Yole, 684");

		mongo.insere(userDoc, InteractionDefinition.USER_COLLECTION_NAME);
		userDoc.clear();

		userDoc.append("user_name", "Brenda T. Powell");
		userDoc.append("user_login", "brendaT");
		userDoc.append("user_password", "brtp");
		userDoc.append("user_id", 2);
		userDoc.append("user_address", "Rua Quatro, 1260");

		mongo.insere(userDoc, InteractionDefinition.USER_COLLECTION_NAME);
		userDoc.clear();

		userDoc.append("user_name", "Sheila A. Thompson");
		userDoc.append("user_login", "sheilathompson");
		userDoc.append("user_password", "thompson321");
		userDoc.append("user_id", 3);
		userDoc.append("user_address", "Rua Lidia Cardoso, 747");

		mongo.insere(userDoc, InteractionDefinition.USER_COLLECTION_NAME);
		userDoc.clear();
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

		device.append("device_tech", "nfc");
		device.append("device_mac", "4B:84:57:E5:BB:F2");
		device.append("product_id", 3);

		mongo.insere(device, InteractionDefinition.DEVICE_COLLECTION_NAME);
		device.clear();

		device.append("device_tech", "nfc");
		device.append("device_mac", "59:4E:E9:22:1A:D0");
		device.append("product_id", 4);

		mongo.insere(device, InteractionDefinition.DEVICE_COLLECTION_NAME);
		device.clear();

		device.append("device_tech", "beacon");
		device.append("device_mac", "20:91:48:07:08:3F");
		device.append("product_id", 5);
		device.append("beacon_major", 10006);
		device.append("beacon_minor", 48408);

		mongo.insere(device, InteractionDefinition.DEVICE_COLLECTION_NAME);
		device.clear();
		// 20:91:48:07:08:3F
		mongo.fechaConexao();
	}

}
