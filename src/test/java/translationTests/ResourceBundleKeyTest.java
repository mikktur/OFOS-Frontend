package translationTests;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.fail;

public class ResourceBundleKeyTest {

    private static final String BASE_FILE = "MessagesBundle_en_US.properties";
    private static final String[] TARGET_FILES = {
            "MessagesBundle_fi_FI.properties",
            "MessagesBundle_ja_JP.properties",
            "MessagesBundle_ru_RU.properties"
    };

    @Test
    public void testResourceBundleKeys() {
        try {
            // Load the base properties file
            Properties baseProperties = loadProperties(BASE_FILE);
            Set<String> baseKeys = baseProperties.stringPropertyNames();

            // Compare with each target properties file
            for (String targetFile : TARGET_FILES) {
                Properties targetProperties = loadProperties(targetFile);
                Set<String> targetKeys = targetProperties.stringPropertyNames();

                // Find missing keys in the target file
                Set<String> missingKeys = new HashSet<>(baseKeys);
                missingKeys.removeAll(targetKeys);

                // Report missing keys if any
                if (!missingKeys.isEmpty()) {
                    fail("Missing keys in " + targetFile + ": " + missingKeys);
                }
            }
        } catch (IOException e) {
            fail("Failed to load properties files: " + e.getMessage());
        }
    }

    private Properties loadProperties(String fileName) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + fileName);
            }
            properties.load(inputStream);
        }
        return properties;
    }
}
