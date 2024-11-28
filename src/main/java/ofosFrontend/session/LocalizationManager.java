package ofosFrontend.session;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.*;

/**
 * Manages the localization of the application.
 */
public class LocalizationManager {

    private static final String BASE_NAME = "MessagesBundle";
    private static Locale locale = Locale.ENGLISH;
    private static ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, locale);


    private static final Map<String, String> languageMap = Map.of(
            "en", "English",
            "fi", "Finnish",
            "ja", "Japanese",
            "ru", "Russian"
    );

    private static final StringProperty selectedLanguage = new SimpleStringProperty();

    static {
        selectedLanguage.set(languageMap.getOrDefault(locale.getLanguage(), "English"));
    }

    private LocalizationManager() {
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
        try {
            locale = newLocale;
            bundle = ResourceBundle.getBundle(BASE_NAME, locale);
        } catch (MissingResourceException e) {
            locale = Locale.ENGLISH;
            bundle = ResourceBundle.getBundle(BASE_NAME, locale);
        }
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
