package ofosFrontend.session;

import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.function.Consumer;

public final class GenericHelper {
    private static final Logger logger = LogManager.getLogger(GenericHelper.class);
    private GenericHelper() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * Executes a task and calls the onSuccess consumer if the task succeeds.
     * @param task The task to execute.
     * @param onSuccess The consumer to call if the task succeeds.
     * @param onFailure The runnable to call if the task fails.
     * @param <T> The type of the task result.
     */
    public static <T> void executeTask(Task<T> task, Consumer<T> onSuccess, Runnable onFailure) {
        task.setOnSucceeded(event -> {
            T result = task.getValue();
            if (result != null) {
                onSuccess.accept(result);
            } else {
                onFailure.run();
            }
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            if (exception != null) {
                logger.error("Task failed", exception);
            }
            onFailure.run();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }



    /**
     * Switches the application language.
     * Updates the LocalizationManager with the new locale.
     *
     * @param language The language to switch to.
     * @return The new Locale based on the selected language.
     */
    public static Locale switchLanguage(String language) {
        Locale newLocale;

        switch (language) {
            case "Finnish":
                newLocale = Locale.forLanguageTag("fi-FI");
                break;
            case "Japanese":
                newLocale = Locale.JAPAN;
                break;
            case "Russian":
                newLocale = new Locale("ru", "RU"); // Russian has no constant
                break;
            default:
                newLocale = Locale.US;
                break;
        }

        LocalizationManager.setLocale(newLocale);
        return newLocale;
    }

}
