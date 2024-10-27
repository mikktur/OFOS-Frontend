package ofosFrontend.session;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class LocalizationManager {

    private static final String BASE_NAME = "MessagesBundle";
    private static Locale locale = new Locale("en", "US"); // Default locale
    private static ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, locale);

    // Map with key as language code and value as display name
    private static final Map<String, String> languageMap = new HashMap<>();


    private static final StringProperty selectedLanguage = new SimpleStringProperty();

    static {
        languageMap.put("en", "English");
        languageMap.put("fi", "Finnish");

        selectedLanguage.set(languageMap.getOrDefault(locale.getLanguage(), "English"));
    }


    public static ResourceBundle getBundle() {
        return bundle;
    }


    public static void setLocale(Locale newLocale) {
        locale = newLocale;
        bundle = ResourceBundle.getBundle(BASE_NAME, locale);


        selectedLanguage.set(languageMap.getOrDefault(locale.getLanguage(), "English"));
    }


    public static void setSelectedLanguage(String language) {
        for (Map.Entry<String, String> entry : languageMap.entrySet()) {
            if (entry.getValue().equals(language)) {
                setLocale(new Locale(entry.getKey()));
                break;
            }
        }
    }


    public static StringProperty selectedLanguageProperty() {
        return selectedLanguage;
    }

}
