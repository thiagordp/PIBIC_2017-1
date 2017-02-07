/**
 * 
 */
package util;

import java.util.List;

import org.bson.Document;

import model.Product;

/**
 * @author trdp
 *
 */
public class JsonConverter {

	/**
	 * 
	 * @param productList
	 * @return
	 */
	// Juntar os JSONs vindos do banco
	public static String productListToJson(List<Document> productList) {
		String json = "products : [";

		for (int i = 0; productList != null && i < productList.size(); i++) {
			Document document = productList.get(i);

			json += document.toJson();

			// Caso não seja o último, adiciona vírgula
			if (i < (productList.size() - 1)) {
				json += ", ";
			}
		}
		json += " ]";

		return json;
	}

	/**
	 * 
	 * @param product
	 * @return
	 */
	// Não é necessário
	public static String productToJson(Product product) {

		String json = "{ ";

		json.concat("\"product_id\":" + product.getProductId() + ", ");
		json.concat("\"product_name\":" + product.getProductName() + ", ");
		json.concat("\"product_value\":" + product.getProductValue() + ", ");
		json.concat("\"product_url\":" + product.getProductUrl() + " }");

		return json;
	}
}
