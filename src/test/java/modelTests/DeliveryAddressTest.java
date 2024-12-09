package modelTests;

import ofosFrontend.model.DeliveryAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryAddressTest {

    private DeliveryAddress deliveryAddress;

    @BeforeEach
    public void setUp() {
        deliveryAddress = new DeliveryAddress();
    }

    @Test
    void testSetAndGetDeliveryAddressId() {
        deliveryAddress.setDeliveryAddressId(123);
        assertEquals(123, deliveryAddress.getDeliveryAddressId(), "DeliveryAddressId should be 123.");
    }

    @Test
    void testSetAndGetStreetAddress() {
        deliveryAddress.setStreetAddress("456 Elm St");
        assertEquals("456 Elm St", deliveryAddress.getStreetAddress(), "StreetAddress should be '456 Elm St'.");
    }

    @Test
    void testSetAndGetCity() {
        deliveryAddress.setCity("Los Angeles");
        assertEquals("Los Angeles", deliveryAddress.getCity(), "City should be 'Los Angeles'.");
    }

    @Test
    void testSetAndGetPostalCode() {
        deliveryAddress.setPostalCode("90001");
        assertEquals("90001", deliveryAddress.getPostalCode(), "PostalCode should be '90001'.");
    }

    @Test
    void testSetAndGetInfo() {
        deliveryAddress.setInfo("Leave at the door.");
        assertEquals("Leave at the door.", deliveryAddress.getInfo(), "Info should be 'Leave at the door'.");
    }

    @Test
    void testSetAndGetDefaultAddress() {
        deliveryAddress.setDefaultAddress(true);
        assertTrue(deliveryAddress.isDefaultAddress(), "DefaultAddress should be true.");

        deliveryAddress.setDefaultAddress(false);
        assertFalse(deliveryAddress.isDefaultAddress(), "DefaultAddress should be false.");
    }
}
