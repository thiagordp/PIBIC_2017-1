package control;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.bson.Document;
import org.json.JSONObject;

import util.InteractionDefinition;
import util.JsonConverter;

@Path("/")
public class Servidor {

	/*
	 * URL http://localhost:8080/RecomendacaoServer?user=2
	 * 
	 * Sem o Rcaminho de contexto nem o do método (direto).
	 */
	@GET
	@Path("/")
	@Produces(MediaType.TEXT_PLAIN)
	public String recordRequest(@QueryParam("user_id") Integer userId, @QueryParam("type") Integer type, @QueryParam("device_tech") Integer deviceTech,
			@QueryParam("device_mac") String deviceMac, @QueryParam("beacon_minor") Integer beaconMinor, @QueryParam("beacon_major") Integer beaconMajor,
			@QueryParam("beacon_rssi") Integer beaconRssi, @QueryParam("user_login") String userLogin, @QueryParam("user_password") String userPassword) {

		if (type == null) {
			return "{}";
		}

		switch (type) {
		case InteractionDefinition.TYPE_URL_PURCHASE:
			return purchaseRequest(userId);
		case InteractionDefinition.TYPE_URL_RECORD:
			return recordRequest(userId, deviceTech, deviceMac, beaconMinor, beaconMajor, beaconRssi);
		case InteractionDefinition.TYPE_URL_SEEN:
			return seenRequest(userId);
		case InteractionDefinition.TYPE_URL_OFFER:
			return offerRequest(userId);
		case InteractionDefinition.TYPE_URL_LOGIN:
			return loginRequest(userLogin, userPassword);
		default:
			return "{}";
		}
	}

	/**
	 * Função que faz as operações relacionadas à requisição de login
	 * 
	 * @param login
	 *            Login do usuário
	 * @param password
	 *            Senha do login
	 * @return JSON com resultado da operação (concedido ou negado) e, em caso
	 *         afirmativo, a identificação do usuário
	 */
	public String loginRequest(String login, String password) {

		Mongo mongodb = new Mongo("pibic", InteractionDefinition.getCollectionList());
		Document document = new Document();
		document.append("user_login", login);
		document.append("user_password", password);

		List<Document> listUser = mongodb.procura(document, InteractionDefinition.USER_COLLECTION_NAME);

		JSONObject json = new JSONObject();
		try {
			if (listUser.size() > 0) {
				int user_id = listUser.get(0).getInteger("user_id");

				json.append("result", "granted");
				json.append("user_id", user_id);
			} else {
				json.append("result", "denied");
			}
		} catch (Exception e) {

		}

		mongodb.fechaConexao();
		return json.toString();
	}

	/**
	 * Função que faz as operações relacionadas à requisição de recomendações
	 * 
	 * @param user_id
	 *            Identificação do usuário
	 * @return JSON com uma lista de recomendações para o usuário
	 */
	public String offerRequest(Integer user_id) {

		Recomendation recomendation = new Recomendation();

		return recomendation.collaborativeFilteringRec(user_id);
	}

	/**
	 * Função que faz as operações relacionadas à requisição de compras
	 * realizadas
	 * 
	 * @param user_id
	 *            Identificação do usuário
	 * @return JSON com uma lista de produtos recomendados
	 */
	public String purchaseRequest(Integer user_id) {
		Mongo mongodb = new Mongo("pibic", InteractionDefinition.getCollectionList());

		// Pesquisar o produto relacionado ao device

		Document docSearchFilter = new Document();
		docSearchFilter.append("type", InteractionDefinition.ACTION_PURCHASE);
		docSearchFilter.append("user_id", user_id);

		List<Document> listSeenInteraction = mongodb.procura(docSearchFilter, InteractionDefinition.INTERACTION_COLLECTION_NAME);

		if (listSeenInteraction.size() == 0) {
			mongodb.fechaConexao();
			return JsonConverter.productListToJson(null);
		}

		List<Document> listSeenProduct = new ArrayList<>();

		for (Document docInteraction : listSeenInteraction) {

			Integer productId = Integer.valueOf(docInteraction.get("product_id").toString());
			List<Document> listProduct = mongodb.procura("product_id", productId, InteractionDefinition.PRODUCT_COLLECTION_NAME);

			for (Document docProduct : listProduct) {
				docProduct.append("timestamp", docInteraction.get("timestamp")); // Adcionar
																					// o
																					// timestamp
																					// do
																					// momento
																					// da
																					// interação.
			}
			listSeenProduct.addAll(listProduct);
		}

		// this.removeDuplicate(listSeenProduct); // Talvez seja bom mostrar
		// todas as interações, mesmo se repetidas.
		mongodb.fechaConexao();

		// Retorna o JSON
		return JsonConverter.productListToJson(listSeenProduct);
	}

	/**
	 * Função que faz as operações relacionadas à requisição de produtos vistos
	 * 
	 * @param user_id
	 *            Identificação do usuário
	 * @return Lista onde cada célula tem informações sobre um produto e o
	 *         momento de interação com este.
	 */
	public String seenRequest(Integer user_id) {
		Mongo mongodb = new Mongo("pibic", InteractionDefinition.getCollectionList());

		// Pesquisar o produto relacionado ao device

		Document docSearchFilter = new Document();
		docSearchFilter.append("type", InteractionDefinition.ACTION_SEEN);
		docSearchFilter.append("user_id", user_id);

		List<Document> listSeenInteraction = mongodb.procura(docSearchFilter, InteractionDefinition.INTERACTION_COLLECTION_NAME);

		if (listSeenInteraction.size() == 0) {
			mongodb.fechaConexao();
			return JsonConverter.productListToJson(null);
		}

		List<Document> listSeenProduct = new ArrayList<>();

		for (Document docInteraction : listSeenInteraction) {

			Integer productId = Integer.valueOf(docInteraction.get("product_id").toString());
			List<Document> listProduct = mongodb.procura("product_id", productId, InteractionDefinition.PRODUCT_COLLECTION_NAME);

			for (Document docProduct : listProduct) {
				docProduct.append("timestamp", docInteraction.get("timestamp")); // Adcionar
																					// o
																					// timestamp
																					// do
																					// momento
																					// da
																					// interação.
			}
			listSeenProduct.addAll(listProduct);
		}

		// this.removeDuplicate(listSeenProduct); // Talvez seja bom mostrar
		// todas as interações, mesmo se repetidas.
		mongodb.fechaConexao();

		// Retorna o JSON
		return JsonConverter.productListToJson(listSeenProduct);
	}

	/**
	 * Função que faz as operações relacionadas à requisição de registro de
	 * interação
	 * 
	 * @param user_id
	 *            Identificação do usuário
	 * @param deviceTech
	 *            Tipo de dispositivo (beacon ou tag NFC)
	 * @param deviceMac
	 *            MAC do dispositivo
	 * @param beaconMinor
	 *            Minor do beacon (caso seja NFC, é nulo)
	 * @param beaconMajor
	 *            Major do beacon (caso seja NFC, é nulo)
	 * @param beaconRssi
	 *            RSSI do beacon (caso seja NFC, é nulo)
	 * @return JSON com info do produto destacado
	 */
	public String recordRequest(Integer user_id, Integer deviceTech, String deviceMac, Integer beaconMinor, Integer beaconMajor, Integer beaconRssi) {

		/////////////////////////////////////////
		// MAC DISPONÍVEIS
		// EA:C9:0F:74:E0:1D
		// 03:9D:AA:F8:66:94
		/////////////////////////////////////////

		// Instanciação do banco
		Mongo mongodb = new Mongo("pibic", InteractionDefinition.getCollectionList());

		// Pesquisar o produto relacionado ao device
		List<Document> listDevProd = mongodb.procura("device_mac", deviceMac, InteractionDefinition.DEVICE_COLLECTION_NAME);
		List<Document> listProd = new ArrayList<Document>();

		// Para cada produto na lista, pega os dados do original
		for (int i = 0; i < listDevProd.size(); i++) {
			Document document = listDevProd.get(i);

			listProd.addAll(mongodb.procura("product_id", (Integer) document.get("product_id"), InteractionDefinition.PRODUCT_COLLECTION_NAME));
		}

		// Selecionar toda a info do produto

		if (listProd.size() == 0) {
			mongodb.fechaConexao();
			return "{}";
		}

		Document produto = listProd.get(0);

		// Transformar todos os dados em um registro de interação
		Document record = new Document(); // Registro da interação

		Calendar calendar = Calendar.getInstance();
		Date currentTimestamp = new Date(calendar.getTime().getTime());
		record.append("timestamp", currentTimestamp.toString());
		record.append("user_id", user_id);
		record.append("type", InteractionDefinition.ACTION_SEEN); // pq o
																	// produto
																	// foi
																	// visualizado
																	// no
																	// registro.

		Document deviceDoc = new Document(); // Registro do dispositivo que é
												// anexado ao registro da
												// interação.
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
		mongodb.fechaConexao();
		// Retorna o JSON
		return produto.toJson();
	}
}
