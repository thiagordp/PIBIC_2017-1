/**
 * 
 */
package control;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.bson.Document;

import util.InteractionDefinition;
import util.JsonConverter;

/**
 * @author Thiago Raulino Dal Pont
 *         Fornece recomendacao ao usuário
 */
public class Recomendation {

	public String generateRecomendation(Integer user_id) {

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
