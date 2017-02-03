/**
 * 
 */
package model;

/**
 * @author trdp
 *
 */
public class Product {

	private String productUrl;

	/**
	 * 
	 */
	private Integer productId;

	/**
	 * 
	 */
	private String productName;

	/**
	 * 
	 */
	private Integer productQuantity;

	/**
	 * 
	 */
	private Float productValue;

	// -------------------- GETTERS AND SETTERS -------------------- //

	public String getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}

	public Float getProductValue() {
		return productValue;
	}

	public void setProductValue(Float productValue) {
		this.productValue = productValue;
	}
}
