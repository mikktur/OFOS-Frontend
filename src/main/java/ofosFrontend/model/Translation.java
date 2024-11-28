package ofosFrontend.model;

public class Translation {
    private String languageCode;
    private String text;

    public Translation(String languageCode, String text) {
        this.languageCode = languageCode;
        this.text = text;
    }

    public Translation() {
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Translation{" +
                "languageCode='" + languageCode + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
