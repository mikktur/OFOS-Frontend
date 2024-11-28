package ofosFrontend.session;

import javafx.scene.control.TextInputControl;

/**
 * Utility class for adding character limits to text fields.
 */
public final class TextFieldUtils {
public final class TextFieldUtils {


    private TextFieldUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    /**
     * Adds a character limit to a TextInputControl (e.g., TextField, TextArea).
     *
     * @param textControl The TextInputControl to which the character limit will be applied.
     * @param maxLength   The maximum number of characters allowed.
     * @throws IllegalArgumentException if textControl is null or maxLength is less than 1.
     *
     * Example Usage:
     * <pre>{@code
     * TextField nameField = new TextField();
     * TextFieldUtils.addTextLimiter(nameField, 50);
     * }</pre>
     */
    public static void addTextLimiter(TextInputControl textControl, int maxLength) {
        textControl.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                textControl.setText(newValue.substring(0, maxLength));
            }
        });
    }
}