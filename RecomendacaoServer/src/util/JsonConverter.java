/**
 * 
 */
package util;

import java.util.List;

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
	public static String productListToJson(List<Object> productList) {
		String json = "{ ";

		if (productList != null) {

			for (int i = 0; i < productList.size(); i++) {

			}
		}
		
		return "";
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
