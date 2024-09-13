package ofosFrontend.model;

public class Product {

    private String productName;
    private double price;
    private String prodcutDesc;
    private Integer productId;

    public Product( Integer productId,String productName, double price, String prodcutDesc) {
        this.productName = productName;
        this.price = price;
        this.prodcutDesc = prodcutDesc;
        this.productId = productId;

    }

    public String getProductName() {
        return productName;
    }

    public void setProduwctName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProdcutDesc() {
        return prodcutDesc;
    }

    public void setProdcutDesc(String prodcutDesc) {
        this.prodcutDesc = prodcutDesc;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }


}
