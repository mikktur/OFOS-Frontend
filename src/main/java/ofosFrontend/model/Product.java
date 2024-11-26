package ofosFrontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    private String productName;
    private double productPrice;
    private String productDesc;
    private Integer productID;
    private String picture;
    private String category;
    private boolean active;

    // Translation fields
    private String finnishTranslation;
    private String japaneseTranslation;
    private String russianTranslation;

    // Parameterized constructor with translations
    public Product(String productName, double productPrice, String productDesc, Integer productID, String picture, String category, boolean active, String finnishTranslation, String japaneseTranslation, String russianTranslation) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDesc = productDesc;
        this.productID = productID;
        this.picture = picture;
        this.category = category;
        this.active = active;
        this.finnishTranslation = finnishTranslation;
        this.japaneseTranslation = japaneseTranslation;
        this.russianTranslation = russianTranslation;
    }

    // Default constructor
    public Product() {
    }

    // Getters and setters for the original fields
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getFinnishTranslation() {
        return finnishTranslation;
    }

    public void setFinnishTranslation(String finnishTranslation) {
        this.finnishTranslation = finnishTranslation;
    }

    public String getJapaneseTranslation() {
        return japaneseTranslation;
    }

    public void setJapaneseTranslation(String japaneseTranslation) {
        this.japaneseTranslation = japaneseTranslation;
    }

    public String getRussianTranslation() {
        return russianTranslation;
    }

    public void setRussianTranslation(String russianTranslation) {
        this.russianTranslation = russianTranslation;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", price=" + productPrice +
                ", productDesc='" + productDesc + '\'' +
                ", productID=" + productID +
                ", picture='" + picture + '\'' +
                ", category='" + category + '\'' +
                ", active=" + active +
                ", finnishTranslation='" + finnishTranslation + '\'' +
                ", japaneseTranslation='" + japaneseTranslation + '\'' +
                ", russianTranslation='" + russianTranslation + '\'' +
                '}';
    }
}
