package control;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.bson.Document;

import com.sun.research.ws.wadl.Doc;

import util.InteractionDefinition;
import util.JsonConverter;

@Path("/")
public class Servidor {

	/*
	 * URL http://localhost:8080/RecomendacaoServer?user=2
	 * 
	 * Sem o caminho de contexto nem o do método (direto).
	 */
	@GET
	@Path("/")
	public String recordRequest(@QueryParam("user_id") Integer userId, @QueryParam("type") Integer type,
						@QueryParam("device_tech") Integer deviceTech, @QueryParam("device_mac") String deviceMac,
						@QueryParam("beacon_minor") Integer beaconMinor, @QueryParam("beacon_major") Integer beaconMajor,
						@QueryParam("beacon_rssi") Integer beaconRssi) {

		if (type == null) {
			return "{}";
		}

		///////////////////////////////////
		/////// ÁREA DE TESTE /////////////
		///////////////////////////////////
		//
		// Document teste = new Document("teste", "123");
		// try {
		//
		//
		// Mongo mongodb = new Mongo("pibic", getCollectionList());
		// mongodb.removeTodos(InteractionDefinition.DEVICE_COLLECTION_NAME);
		// mongodb.removeTodos(InteractionDefinition.INTERACTION_COLLECTION_NAME);
		// mongodb.removeTodos(InteractionDefinition.PRODUCT_COLLECTION_NAME);
		// mongodb.removeTodos(InteractionDefinition.USER_COLLECTION_NAME);
		//
		// return mongodb.listaRegistros(InteractionDefinition.INTERACTION_COLLECTION_NAME).toString();
		//
		// mongodb.insere(teste, InteractionDefinition.DEVICE_COLLECTION_NAME);
		// mongodb.insere(teste,
		// InteractionDefinition.INTERACTION_COLLECTION_NAME);
		// mongodb.insere(teste, InteractionDefinition.PRODUCT_COLLECTION_NAME);
		// mongodb.insere(teste, InteractionDefinition.USER_COLLECTION_NAME);
		//
		// return
		// mongodb.listaRegistros(InteractionDefinition.DEVICE_COLLECTION_NAME).toString();
		// } catch (Exception e) {
		// return e.getMessage();
		// }

		// PopulateDatabase.insertProducts();
		// PopulateDatabase.insertDevices();
		//
		// return ((new Mongo("pibic", getCollectionList())).listaRegistros(InteractionDefinition.DEVICE_COLLECTION_NAME)).toString();

		////////////////////////////////////////////////////////////////////////////////////

		switch (type) {
		case InteractionDefinition.TYPE_URL_PURCHASE:
			return purchaseRequest(userId);
		case InteractionDefinition.TYPE_URL_RECORD:
			return recordRequest(userId, deviceTech, deviceMac, beaconMinor, beaconMajor, beaconRssi);
		case InteractionDefinition.TYPE_URL_SEEN:
			return seenRequest(userId);
		case InteractionDefinition.TYPE_URL_OFFER:
			return offerRequest(userId);
		default:
			return "{}";
		}

	}

	private List<String> getCollectionList() {

		List<String> lista = new ArrayList<>();

		lista.add(InteractionDefinition.PRODUCT_COLLECTION_NAME);
		lista.add(InteractionDefinition.USER_COLLECTION_NAME);
		lista.add(InteractionDefinition.DEVICE_COLLECTION_NAME);
		lista.add(InteractionDefinition.INTERACTION_COLLECTION_NAME);

		return lista;
	}

	public String offerRequest(Integer user_id) {

		Recomendation recomendation = new Recomendation();

		return recomendation.generateRecomendation(user_id);
	}

	/**
	 *
	 * @param user_id
	 * @return
	 */
	public String purchaseRequest(Integer user_id) {
		Mongo mongodb = new Mongo("pibic", getCollectionList());

		// Pesquisar o produto relacionado ao device

		Document docSearchFilter = new Document();
		docSearchFilter.append("type", InteractionDefinition.ACTION_PURCHASE);
		docSearchFilter.append("user_id", user_id);

		List<Document> listSeenInteraction = mongodb.procura(docSearchFilter, InteractionDefinition.INTERACTION_COLLECTION_NAME);

		if (listSeenInteraction.size() == 0) {
			return "{\"nopurchase\":0}";
		}

		List<Document> listSeenProduct = new ArrayList<>();

		for (Document docInteraction : listSeenInteraction) {

			Integer productId = Integer.valueOf(docInteraction.get("product_id").toString());
			List<Document> listProduct = mongodb.procura("product_id", productId, InteractionDefinition.PRODUCT_COLLECTION_NAME);

			for (Document docProduct : listProduct) {
				docProduct.append("timestamp", docInteraction.get("timestamp")); // Adcionar o timestamp do momento da interação.
			}
			listSeenProduct.addAll(listProduct);
		}

		// this.removeDuplicate(listSeenProduct); // Talvez seja bom mostrar todas as interações, mesmo se repetidas.
		mongodb.fechaConexao();

		// Retorna o JSON
		return JsonConverter.productListToJson(listSeenProduct);
	}

	/**
	 *
	 * @param user_id
	 * @return Lista onde cada célula tem informações sobre um produto e o momento de interação com este.
	 */
	public String seenRequest(Integer user_id) {
		Mongo mongodb = new Mongo("pibic", getCollectionList());

		// Pesquisar o produto relacionado ao device

		Document docSearchFilter = new Document();
		docSearchFilter.append("type", InteractionDefinition.ACTION_SEEN);
		docSearchFilter.append("user_id", user_id);

		List<Document> listSeenInteraction = mongodb.procura(docSearchFilter, InteractionDefinition.INTERACTION_COLLECTION_NAME);

		if (listSeenInteraction.size() == 0) {
			return "{\"nointeraction\":0}";
		}

		List<Document> listSeenProduct = new ArrayList<>();

		for (Document docInteraction : listSeenInteraction) {

			Integer productId = Integer.valueOf(docInteraction.get("product_id").toString());
			List<Document> listProduct = mongodb.procura("product_id", productId, InteractionDefinition.PRODUCT_COLLECTION_NAME);

			for (Document docProduct : listProduct) {
				docProduct.append("timestamp", docInteraction.get("timestamp")); // Adcionar o timestamp do momento da interação.
			}
			listSeenProduct.addAll(listProduct);
		}

		// this.removeDuplicate(listSeenProduct); // Talvez seja bom mostrar todas as interações, mesmo se repetidas.
		mongodb.fechaConexao();

		// Retorna o JSON
		return JsonConverter.productListToJson(listSeenProduct);
	}

	private List<Document> removeDuplicate(List<Document> listDocument) {

		for (int i = 0; listDocument != null && i < listDocument.size(); i++) {
			Document document = listDocument.get(i);

			for (int j = 1; j < listDocument.size(); j++) {
				Document doc2 = listDocument.get(j);

				// Se o hash ou o product_id forem iguais remove.
				if (document.get("_id").toString().equals(doc2.get("_id").toString())
									|| document.get("product_id").toString().equals(doc2.get("product_id").toString())) {
					listDocument.remove(j);
				}
			}
		}

		return listDocument;
	}

	/**
	 *
	 * @param user_id
	 * @param deviceTech
	 * @param deviceMac
	 * @param beaconMinor
	 * @param beaconMajor
	 * @param beaconRssi
	 * @return JSON com info do produto destacado
	 */
	public String recordRequest(Integer user_id, Integer deviceTech, String deviceMac, Integer beaconMinor, Integer beaconMajor,
						Integer beaconRssi) {

		/////////////////////////////////////////
		// MAC DISPONÍVEIS
		// EA:C9:0F:74:E0:1D
		// 03:9D:AA:F8:66:94
		/////////////////////////////////////////

		// Instanciação do banco
		Mongo mongodb = new Mongo("pibic", getCollectionList());

		// Pesquisar o produto relacionado ao device
		List<Document> listDevProd = mongodb.procura("device_mac", deviceMac, InteractionDefinition.DEVICE_COLLECTION_NAME);
		List<Document> listProd = new ArrayList<Document>();

		// Para cada produto na lista, pega os dados do original
		for (int i = 0; i < listDevProd.size(); i++) {
			Document document = listDevProd.get(i);

			listProd.addAll(mongodb.procura("product_id", (Integer) document.get("product_id"),
								InteractionDefinition.PRODUCT_COLLECTION_NAME));
		}

		//////////////////////////////////////
		// Document doc = new Document();
		//
		// doc.append("product_id", 123);
		// doc.append("product_name", "Mouse");
		// doc.append("product_price", 1.50);
		// doc.append("product_quantity", 30);
		// doc.append("product_url", "http://www.google.com");
		//
		// listProd.add(doc);
		// ////////////////////////////////////////

		// Selecionar toda a info do produto

		if (listProd.size() == 0) {
			return "{\"noproduct\":0}";
		}

		Document produto = listProd.get(0);

		// Transformar todos os dados em um registro de interação
		Document record = new Document(); // Registro da interação

		Calendar calendar = Calendar.getInstance();
		Date currentTimestamp = new Timestamp(calendar.getTime().getTime());
		record.append("timestamp", currentTimestamp.toString());
		record.append("user_id", user_id);
		record.append("type", InteractionDefinition.ACTION_SEEN); // pq o produto foi visualizado no registro.

		Document deviceDoc = new Document(); // Registro do dispositivo que é anexado ao registro da interação.
		deviceDoc.append("device_tech", deviceTech);
		deviceDoc.append("device_mac", deviceMac);
		if (deviceTech == InteractionDefinition.DEVICE_BEACON) {
			deviceDoc.append("beacon_major", beaconMajor);
			deviceDoc.append("beacon_minor", beaconMinor);
			deviceDoc.append("beacon_rssi", beaconRssi);
		}
		record.append("product_id", Integer.valueOf(produto.get("product_id").toString()));
		record.append("device", deviceDoc);

		// inserir no banco a interação.
		mongodb.insere(record, InteractionDefinition.INTERACTION_COLLECTION_NAME);

		// Retorna o JSON
		return produto.toJson();
	}
}
