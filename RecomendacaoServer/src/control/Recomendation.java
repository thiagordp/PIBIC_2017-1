/**
 * 
 */
package control;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bson.Document;

import util.InteractionDefinition;
import util.JsonConverter;

/**
 * @author Thiago Raulino Dal Pont
 *         Fornece recomendacao ao usuário
 */
public class Recomendation {

	public String generateRandomRecomendation(Integer user_id) {

		Mongo mongodb = new Mongo("pibic", InteractionDefinition.getCollectionList());

		List<Document> listDoc = mongodb.procura("user_id", user_id, InteractionDefinition.INTERACTION_COLLECTION_NAME);
		List<Document> listProd = new ArrayList<Document>();

		// Para cada produto na lista, pega os dados do original
		for (int i = 0; i < listDoc.size(); i++) {
			Document document = listDoc.get(i);

			listProd.addAll(mongodb.procura("product_id", (Integer) document.get("product_id"),
								InteractionDefinition.PRODUCT_COLLECTION_NAME));
		}

		removeDuplicate(listProd);

		// /////////////////////////
		// listProd.clear();
		// listProd.add(new Document().append("product_id", "Notebook").append("_id", 1));
		// listProd.add(new Document().append("product_id", "Fone").append("_id", 2));
		// listProd.add(new Document().append("product_id", "Guitarra").append("_id", 3));
		// listProd.add(new Document().append("product_id", "Chave").append("_id", 4));
		// listProd.add(new Document().append("product_id", "Cabo de rede").append("_id", 5));
		// listProd.add(new Document().append("product_id", "Cano").append("_id", 6));
		// /////////////////////

		if (listProd.size() < 5) {

			return JsonConverter.productListToJson(listProd);
		}

		Random random = new Random(Calendar.getInstance().getTimeInMillis());
		List<Document> listDocument = new ArrayList<>();

		while (listDocument.size() < 5) {

			int i = random.nextInt(listProd.size());
			for (; i < listProd.size(); i += random.nextInt(listProd.size() - i + 1)) {
				listDocument.add(listProd.get(i));
			}
		}

		removeDuplicate(listDocument);
		mongodb.fechaConexao();

		return JsonConverter.productListToJson(listDocument);
	}

	public String collaborativeFilteringRec(Integer userId) {

		String debug = "";

		debug += (":::init:::");

		try {
			Mongo mongo = new Mongo("pibic", InteractionDefinition.getCollectionList());

			debug += (":::Start:::");

			// Pesquisar se o usuário está cadastrado.
			Document testUserDoc = new Document();
			testUserDoc.append("user_id", userId);

			debug += ("Before query1:::");
			List<Document> listQuery = mongo.procura(testUserDoc, InteractionDefinition.USER_COLLECTION_NAME);

			if (listQuery.isEmpty()) {
				// return debug;
				// return "unknown user";
				return JsonConverter.productListToJson(null);
			}

			debug += ("After query1:::");

			// Cria hash from pessoa
			Map<Integer, Integer> hashPerson = createUserProductHash(userId);
			debug += hashPerson.toString();
			debug += ("After hash1:::");
			List<Double> listAngle = new ArrayList<>();

			// Selecionar todas as pessoas que têm pelo menos um produto interagido em comum
			debug += ("Before find userInt:::");
			List<Document> userIntList = mongo.procura(new Document("user_id", userId), InteractionDefinition.INTERACTION_COLLECTION_NAME);

			debug += ("Before find fullInt:::");
			List<Document> fullIntList = mongo.listaRegistros(InteractionDefinition.INTERACTION_COLLECTION_NAME);

			debug += ("Before remove commom:::");
			fullIntList.removeAll(userIntList); // Garante que só resta outros usuários nas interações.

			List<Document> resultList = new ArrayList<>();

			debug += ("Before find for:::");
			// TODO: Melhorar
			for (Document docUser : userIntList) {
				for (Document docFull : fullIntList) {

					if (docUser.get("product_id").toString().equals(docFull.get("product_id").toString())) {

						resultList.add(new Document("user_id", docFull.get("user_id")));
					}
				}
			}

			Set<Document> removeDupl = new HashSet<>();
			removeDupl.addAll(resultList);
			resultList.clear();
			resultList.addAll(removeDupl);

			debug += ("After find for:::");
			if (resultList.isEmpty()) {
				debug += ("null:::");
				return JsonConverter.productListToJson(null); // Retorna um json vazio.
			}

			debug += ("Before cosine for:::");

			// TODO: Calcular o ângulo do usuário em relação a cada uma dessas pessoas.
			for (Document resultDoc : resultList) {
				int userIdQuery = Integer.valueOf(resultDoc.getInteger("user_id"));

				listAngle.add(calculateCosine(hashPerson, createUserProductHash(userIdQuery)));
			}

			debug += "#####" + listAngle.toString() + "####";
			listAngle.sort(new Comparator<Double>() {

				@Override
				public int compare(Double o1, Double o2) {

					if (o1 < o2)
						return -1;
					if (o1 > o2)
						return 1;

					return 0;
				}
			});
			debug += ("After cosine for:::");

			// TODO: Para cada usuário que será selecionado,
			// - pegar o usuário no topo da lista;
			// - remover esse usuário da lista.

			Random random = new Random();

			int recomendationCount = Math.abs(random.nextInt(5) + 1);
			int[] userMatchs = new int[recomendationCount];
			double max = 0;
			int i, index = 0;

			debug += ("Before pick best matches for:::");

			// TODO: para cada usuário na lista final, gerar recomendações com base em subtração de conjustos
			for (int j = 0; j < listAngle.size() && j < recomendationCount; j++) {
				max = listAngle.get(0); // Inicializa com o primeiro

				index = 0;

				for (i = 1; i < listAngle.size(); i++) {
					if (max < listAngle.get(i)) {
						max = listAngle.get(i);
						index = i;
					}
				}

				userMatchs[j] = Integer.valueOf(resultList.get(index).getInteger("user_id"));

				listAngle.set(index, -1.0);
			}

			resultList = new ArrayList<>();

			debug += ("Before g for:::");
			for (int j = 0; j < listAngle.size() && j < recomendationCount; j++) {
				if (userMatchs[j] > 0) {
					List<Document> result = recommend(userId, userMatchs[j]);

					if (result != null) {
						resultList.addAll(result);
					}
				}
			}

			// TODO: se no final, não tiver nada, colocar alguns aleatórios.
			debug += "|||||||||" + resultList.toString() + "||||||||||";

			removeDupl = new HashSet<>();
			removeDupl.addAll(resultList);
			resultList.clear();
			resultList.addAll(removeDupl);

			// return debug;

			// TODO: retornar a lista com até x produtos para recomendação

			return JsonConverter.productListToJson(resultList);

			// TODO: OPT: níveis de interesse conforme o tipo de interação (beacon, nfc)
		} catch (Exception e) {
			debug += (e.getMessage() + ":::");
		}
		// return debug;
		return JsonConverter.productListToJson(null);
	}

	/**
	 * Algoritmo de recomendação que baseado em subtração de conjuntos.
	 * 
	 * @param userId1
	 * @param userId2
	 * @return
	 */
	private List<Document> recommend(Integer userId1, Integer userId2) {

		Mongo mongodb = new Mongo("pibic", InteractionDefinition.getCollectionList());
		List<Document> listProductUser1 = new ArrayList<>();

		List<Document> list = mongodb.procura("user_id", userId1, InteractionDefinition.INTERACTION_COLLECTION_NAME);

		for (Document document : list) {
			listProductUser1.add(new Document("product_id", document.get("product_id")));
		}

		// Remoção de duplicatas
		Set<Document> removeDupl = new HashSet<>();
		removeDupl.addAll(listProductUser1);
		listProductUser1.clear();
		listProductUser1.addAll(removeDupl);
		removeDupl.clear();

		list.clear();
		list = mongodb.procura("user_id", userId2, InteractionDefinition.INTERACTION_COLLECTION_NAME);
		List<Document> listProductUser2 = new ArrayList<>();

		for (Document document : list) {
			listProductUser2.add(new Document("product_id", document.get("product_id")));
		}
		removeDupl.addAll(listProductUser2);
		listProductUser2.clear();
		listProductUser2.addAll(removeDupl);
		removeDupl.clear();

		listProductUser2.removeAll(listProductUser1);

		list = new ArrayList<>();

		for (Document document : listProductUser2) {
			List<Document> listDoc = mongodb.procura("product_id", document.get("product_id"),
								InteractionDefinition.PRODUCT_COLLECTION_NAME);

			list.addAll(listDoc);
		}

		mongodb.fechaConexao();
		return list;
	}

	// TODO: revisar
	private Map<Integer, Integer> createUserProductHash(Integer userId) {
		Mongo mongodb = new Mongo("pibic", InteractionDefinition.getCollectionList());
		Map<Integer, Integer> map = new HashMap<>();

		// TODO: Fazer a consulta
		List<Document> list = mongodb.procura("user_id", userId, InteractionDefinition.INTERACTION_COLLECTION_NAME);
		// List<Document> listQueryResult = new ArrayList<>();

		if (list.isEmpty()) {
			return map;
		}

		for (Document document : list) {
			map.put(document.getInteger("product_id"), 1);
			// listQueryResult.add(new Document("product_id", document.get("product_id")));
		}

		// for (Document document : listQueryResult) {
		// map.put(document.getInteger("product_id"), 1);
		// }

		return map;
	}

	private Double calculateCosine(Map<Integer, Integer> hashP1, Map<Integer, Integer> hashP2) {

		double sumNumerator = 0; // Soma do numerador da fórmula
		double sumDenominator1 = 0, sumDenominator2 = 0; // Soma do denominador na fórmula.

		boolean contains = false;

		// Iterações para cálculo do numerador e denominador.

		for (Integer key1 : hashP1.keySet()) {
			if (hashP2.containsKey(key1)) {
				// Cálculo do produto escalar
				sumNumerator += hashP1.get(key1) * hashP2.get(key1); // produto de cada componente dos vetores
				contains = true;
			}

			// Cálculo da norma
			sumDenominator1 += Math.pow(hashP1.get(key1), 2);
		}

		if (contains) {
			for (Integer key2 : hashP2.keySet()) {
				sumDenominator2 += Math.pow(hashP2.get(key2), 2);
			}

			return (sumNumerator / Math.sqrt(sumDenominator1 * sumDenominator2));
		}

		return 0.0;
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
}
