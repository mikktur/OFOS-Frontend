package ofosFrontend.model;

public class OrderItem {
    private int quantity;
    private int productID;
    private int deliveryAddressID;
    private int restaurantID;

    // Constructor
    public OrderItem(int quantity, int productID, int deliveryAddressID, int restaurantID) {
        this.quantity = quantity;
        this.productID = productID;
        this.deliveryAddressID = deliveryAddressID;
        this.restaurantID = restaurantID;
    }

    // Getters and Setters
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getDeliveryAddressID() {
        return deliveryAddressID;
    }

    public void setDeliveryAddressID(int deliveryAddressID) {
        this.deliveryAddressID = deliveryAddressID;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }
}
