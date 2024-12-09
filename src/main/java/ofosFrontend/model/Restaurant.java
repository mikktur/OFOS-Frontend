package ofosFrontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Model class representing a restaurant
 */
public class Restaurant {
    private int id;
    private String restaurantName;
    private String restaurantPhone;
    private String picture;
    private String category;
    private String ownerUsername;
    private String address;
    private String businessHours;
    private int ownerId;

    public Restaurant(String restaurantName, String restaurantPhone, String picture, String ownerUsername) {
        this.restaurantName = restaurantName;
        this.restaurantPhone = restaurantPhone;
        this.picture = picture;
        this.ownerUsername = ownerUsername;

    }

    public Restaurant() {
    }

    public Restaurant(String name, String phone, String picture, String address, String hours, int ownerId) {
        this.restaurantName = name;
        this.restaurantPhone = phone;
        this.picture = picture;
        this.address = address;
        this.businessHours = hours;
        this.ownerId = ownerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantPhone() {
        return restaurantPhone;
    }

    public void setRestaurantPhone(String restaurantPhone) {
        this.restaurantPhone = restaurantPhone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCategory() {
        return category;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String newAddress) {
        this.address = newAddress;
    }
    public String getBusinessHours() {
        return businessHours;
    }

    public void setHours(String newHours) {
        this.businessHours = newHours;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public int getOwnerId() {
        return ownerId;
    }

}
