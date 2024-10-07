package ofosFrontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant {
    private int Id;
    private String restaurantName;
    private String restaurantPhone;
    private String picture;
//    private String category;
//    private String ownerUsername;
    private String address;
    private String businessHours;

    public Restaurant(String restaurantName, String restaurantPhone, String picture) {
        this.restaurantName = restaurantName;
        this.restaurantPhone = restaurantPhone;
        this.picture = picture;

    }

    public Restaurant() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
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

//    public String getCategory() {
//        return category;
//    }
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
}
