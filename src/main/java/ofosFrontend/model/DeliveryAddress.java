package ofosFrontend.model;

/**
 * Model for the delivery address
 */
public class DeliveryAddress {
    private int deliveryAddressId;
    private String streetAddress;
    private String city;
    private String postalCode;
    private String info;
    private boolean defaultAddress;

    public int getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(int deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(boolean isDefault) {
        this.defaultAddress = isDefault;
    }
}
