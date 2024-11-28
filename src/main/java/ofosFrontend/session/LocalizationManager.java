package ofosFrontend.session;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Manages the localization of the application.
 */
public class LocalizationManager {

    private static final String BASE_NAME = "MessagesBundle";
    private static Locale locale = new Locale("en", "US");
    private static ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, locale);

    // Map with key as language code and value as display name
    private static final Map<String, String> languageMap = new HashMap<>();

    private static final StringProperty selectedLanguage = new SimpleStringProperty();

    static {
        languageMap.put("en", "English");
        languageMap.put("fi", "Finnish");
        languageMap.put("ja", "Japanese");
        languageMap.put("ru", "Russian");

        selectedLanguage.set(languageMap.getOrDefault(locale.getLanguage(), "English"));
    }

    /**
     * Gets the resource bundle for the current locale.
     * @return the resource bundle.
     */
    public static ResourceBundle getBundle() {
        return bundle;
    }

    /**
     * Gets the current locale.
     * @return the locale.
     */
    public static Locale getLocale() {
        return locale;
    }

    /**
     * Gets the current language code (e.g., "en", "fi", "ja", "ru").
     *
     * @return the language code.
     */
    public static String getLanguageCode() {
        return locale.getLanguage();
    }

    /**
     * Sets the locale for the application.
     * @param newLocale the new locale.
     */
    public static void setLocale(Locale newLocale) {
        locale = newLocale;
        bundle = ResourceBundle.getBundle(BASE_NAME, locale);

        selectedLanguage.set(languageMap.getOrDefault(locale.getLanguage(), "English"));
    }

    /**
     * Sets the selected language for the application.
     * @param language the language to set.
     */
    public static void setSelectedLanguage(String language) {
        for (Map.Entry<String, String> entry : languageMap.entrySet()) {
            if (entry.getValue().equals(language)) {
                setLocale(new Locale(entry.getKey()));
                break;
            }
        }
    }

    /**
     * Gets the selected language property.
     * @return the selected language property.
     */
    public static StringProperty selectedLanguageProperty() {
        return selectedLanguage;
    }
}
