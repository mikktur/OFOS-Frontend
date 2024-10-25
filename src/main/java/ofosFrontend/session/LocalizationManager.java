package ofosFrontend.session;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {
    private static Locale locale = new Locale("en", "US"); // Default locale
    private static final String BASE_NAME = "MessagesBundle";
    private static ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, locale);

    // Get the current ResourceBundle
    public static ResourceBundle getBundle() {
        return bundle;
    }

    // Set a new locale and update the ResourceBundle
    public static void setLocale(Locale newLocale) {
        locale = newLocale;
        bundle = ResourceBundle.getBundle(BASE_NAME, locale);
    }

    public static Locale getLocale() {
        return locale;
    }
}
