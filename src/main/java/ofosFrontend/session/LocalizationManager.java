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
    private static Locale locale = new Locale("en", "US");
    private static ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, locale);

    private static final Map<String, String> languageMap = new HashMap<>();

    private static final StringProperty selectedLanguage = new SimpleStringProperty();

    static {
        languageMap.put("en", "English");
        languageMap.put("fi", "Finnish");
        languageMap.put("ja", "Japanese");
        languageMap.put("ru", "Russian");

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
