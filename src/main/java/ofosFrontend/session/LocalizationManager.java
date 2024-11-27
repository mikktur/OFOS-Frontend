package ofosFrontend.session;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.*;

public class LocalizationManager {

    private static final String BASE_NAME = "MessagesBundle";
    private static Locale locale = Locale.ENGLISH;
    private static ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, locale);

    // Map with key as language code and value as display name
    private static final Map<String, String> languageMap = Map.of(
            "en", "English",
            "fi", "Finnish",
            "ja", "Japanese",
            "ru", "Russian"
    );

    private static final StringProperty selectedLanguage = new SimpleStringProperty();

    private LocalizationManager() {
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

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

    public static void setLocale(Locale newLocale) {
        try {
            locale = newLocale;
            bundle = ResourceBundle.getBundle(BASE_NAME, locale);
            selectedLanguage.set(languageMap.getOrDefault(locale.getLanguage(), "English"));
        } catch (MissingResourceException e) {
            locale = Locale.ENGLISH;
            bundle = ResourceBundle.getBundle(BASE_NAME, locale);
            selectedLanguage.set("English");
        }
    }


    public static StringProperty selectedLanguageProperty() {
        return selectedLanguage;
    }
}
