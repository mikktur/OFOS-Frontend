package modelTests;

import ofosFrontend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("testUser", "testPass");
    }

    @Test
    public void testDefaultConstructor() {
        User defaultUser = new User();
        assertEquals("", defaultUser.getUsername(), "Username should be an empty string for the default constructor.");
        assertEquals("", defaultUser.getPassword(), "Password should be an empty string for the default constructor.");
    }

    @Test
    public void testParameterizedConstructor() {
        assertEquals("testUser", user.getUsername(), "Username should be 'testUser'.");
        assertEquals("testPass", user.getPassword(), "Password should be 'testPass'.");
    }

    @Test
    public void testGetAndSetUsername() {
        user.setUsername("newUser");
        assertEquals("newUser", user.getUsername(), "Username should be 'newUser'.");
    }

    @Test
    public void testGetAndSetPassword() {
        user.setPassword("newPass");
        assertEquals("newPass", user.getPassword(), "Password should be 'newPass'.");
    }
}
