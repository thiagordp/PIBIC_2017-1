package br.ufsc.pibic.recstore.util;

/**
 * Created by trdp on 2/7/17.
 */

public class Product {

    private Integer productId;
    private String productTimestamp;
    private Double productValue;
    private String productName;
    private String productURL;

    public Product(Integer productId, String productName, String productTimestamp, String productURL, Double productValue) {
        this.productId = productId;
        this.productName = productName;
        this.productTimestamp = productTimestamp;
        this.productURL = productURL;
        this.productValue = productValue;
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

    public String getProductTimestamp() {
        return productTimestamp;
    }

    public void setProductTimestamp(String productTimestamp) {
        this.productTimestamp = productTimestamp;
    }

    public String getProductURL() {
        return productURL;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public Double getProductValue() {
        return productValue;
    }

    public void setProductValue(Double productValue) {
        this.productValue = productValue;
    }
}
