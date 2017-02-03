package control;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Application;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.bson.Document;

import model.Product;
import util.InteractionDefinition;
import util.JsonConverter;

@Path("/")
public class Servidor {

	// private Mongo mongodb = new Mongo("pibic", null);

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

		// Document teste = new Document("teste", "123");
		// try {
		//
		// mongodb.removeTodos(InteractionDefinition.DEVICE_COLLECTION_NAME);
		// mongodb.removeTodos(InteractionDefinition.INTERACTION_COLLECTION_NAME);
		// mongodb.removeTodos(InteractionDefinition.PRODUCT_COLLECTION_NAME);
		// mongodb.removeTodos(InteractionDefinition.USER_COLLECTION_NAME);
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

		switch (type) {

		case InteractionDefinition.TYPE_INTERACTION_PURCHASE:

			return purchaseRequest(userId);
		case InteractionDefinition.TYPE_INTERACTION_RECORD:

			return recordRequest(userId, deviceTech, deviceMac, beaconMinor, beaconMajor, beaconRssi);
		case

		InteractionDefinition.TYPE_INTERACTION_SEEN:

			return seenRequest(userId);
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

	/**
	 *
	 * @param user_id
	 * @return
	 */
	public String purchaseRequest(Integer user_id) {
		Mongo mongodb = new Mongo("pibic", getCollectionList());

		// Pesquisa no 'registro' as operações de compra do user com pesquisa()

		Document parametrosDeBusca = new Document();
		parametrosDeBusca.append("user_id", user_id);

		List<Document> interactUserList = mongodb.procura(parametrosDeBusca,
				InteractionDefinition.INTERACTION_COLLECTION_NAME);

		// Pegar o id do produto de cada registro e o timestamp
		// Pesquisar em 'produtos' cada produto e pegar nome, qtd e valor.
		// Colocar tudo em um JSON master
		// Retornar
		return "";
	}

	/**
	 *
	 * @param user_id
	 * @return
	 */
	public String seenRequest(Integer user_id) {
		Mongo mongodb = new Mongo("pibic", getCollectionList());

		return "";
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
	public String recordRequest(Integer user_id, Integer deviceTech, String deviceMac, Integer beaconMinor,
			Integer beaconMajor, Integer beaconRssi) {

		Mongo mongodb = new Mongo("pibic", getCollectionList());

		// Pesquisar o produto relacionado ao device
		List<Document> listDevProd = mongodb.procura("device_mac", deviceMac,
				InteractionDefinition.DEVICE_COLLECTION_NAME);

		List<Document> listProd = new ArrayList<Document>();
		// Para cada produto na lista, pega os dados do original
		for (int i = 0; i < listDevProd.size(); i++) {
			Document document = listDevProd.get(i);

			listProd.addAll(mongodb.procura("product_id", document.getString("product_id"),
					InteractionDefinition.PRODUCT_COLLECTION_NAME));
		}

		//////////////////////////////////////
		Document doc = new Document();
		doc.append("product_id", 123);
		doc.append("product_name", "Mouse");
		doc.append("product_price", 1.50);
		doc.append("product_quantity", 30);
		doc.append("product_url", "http://www.google.com");

		listProd.add(doc);
		////////////////////////////////////////
		// Selecionar toda a info do produto
		if (listProd.size() == 0) {
			return "{\"noproduct\":0}";
		}

		Document produto = listProd.get(0);

		// Transformar todos os dados em um registro de interação
		Document record = new Document();

		Calendar calendar = Calendar.getInstance();
		Date currentTimestamp = new Timestamp(calendar.getTime().getTime());
		record.append("timestamp", currentTimestamp.toString());
		record.append("user_id", user_id);
		record.append("type", "seen"); // pq o produto foi visualizado no registro.

		Document deviceDoc = new Document();
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
