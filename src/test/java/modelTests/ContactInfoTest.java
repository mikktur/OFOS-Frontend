package modelTests;

import ofosFrontend.model.ContactInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactInfoTest {

    private ContactInfo contactInfo;

    @BeforeEach
    public void setUp() {
        contactInfo = new ContactInfo();
    }

    @Test
    void testSetAndGetUserId() {
        contactInfo.setUserId(101);
        assertEquals(101, contactInfo.getUserId(), "UserId should be 101.");
    }

    @Test
    void testSetAndGetPhoneNumber() {
        contactInfo.setPhoneNumber("123-456-7890");
        assertEquals("123-456-7890", contactInfo.getPhoneNumber(), "PhoneNumber should be '123-456-7890'.");
    }

    @Test
    void testSetAndGetAddress() {
        contactInfo.setAddress("123 Main St");
        assertEquals("123 Main St", contactInfo.getAddress(), "Address should be '123 Main St'.");
    }

    @Test
    void testSetAndGetCity() {
        contactInfo.setCity("New York");
        assertEquals("New York", contactInfo.getCity(), "City should be 'New York'.");
    }

    @Test
    void testSetAndGetFirstName() {
        contactInfo.setFirstName("John");
        assertEquals("John", contactInfo.getFirstName(), "FirstName should be 'John'.");
    }

    @Test
    void testSetAndGetLastName() {
        contactInfo.setLastName("Doe");
        assertEquals("Doe", contactInfo.getLastName(), "LastName should be 'Doe'.");
    }

    @Test
    void testSetAndGetEmail() {
        contactInfo.setEmail("john.doe@example.com");
        assertEquals("john.doe@example.com", contactInfo.getEmail(), "Email should be 'john.doe@example.com'.");
    }

    @Test
    void testSetAndGetPostalCode() {
        contactInfo.setPostalCode("10001");
        assertEquals("10001", contactInfo.getPostalCode(), "PostalCode should be '10001'.");
    }
}
