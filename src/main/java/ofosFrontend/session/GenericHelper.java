package ofosFrontend.session;

import javafx.concurrent.Task;

import java.util.function.Consumer;

public class GenericHelper {
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
                exception.printStackTrace();
            }
            onFailure.run();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

}
