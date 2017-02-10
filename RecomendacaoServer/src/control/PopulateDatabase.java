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
		product.append("product_name", "Smartphone Samsung Galaxy J7");
		product.append("product_price", 2.50);
		product.append("product_url",
							"http://www.casasbahia-imagens.com.br/TelefoneseCelulares/Smartphones/Android/5409350/177974681/Smartphone-Samsung-Galaxy-J7-Duos-Preto-com-Dual-chip-Tela-5-5-4G-Camera-13MP-Android-5-1-e-Processador-Octa-Core-de-1-5-Ghz-5409350.jpg");

		mongo.insere(product, InteractionDefinition.PRODUCT_COLLECTION_NAME);
		product.clear();

		product.append("product_id", 2);
		product.append("product_name", "Relógio");
		product.append("product_price", 50.00);
		product.append("product_url",
							"http://www.casasbahia-imagens.com.br/Relogios/relogiosMasculinos/Analogicomasculino/9354589/698734970/Relogio-masculino-analogico-em-inox-9354589.jpg");

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

		mongo.fechaConexao();
	}

}
