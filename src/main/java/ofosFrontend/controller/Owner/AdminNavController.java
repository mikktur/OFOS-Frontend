package ofosFrontend.controller.Owner;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import ofosFrontend.session.LocalizationManager;

import java.util.Locale;
import java.util.ResourceBundle;

public class AdminNavController  extends AdminBasicController {
    public javafx.scene.text.Text ownerText;
    @FXML
    HBox adminNav;
    @FXML
    ImageView adminLogout;
    @FXML
    ImageView adminHome;
    @FXML
    private ComboBox<String> adminLanguageSelector;
    @FXML
    public void initialize() {
        adminNav.getProperties().put("controller", this);
        setupLanguageSelector();
    }
    @FXML
    public void goToHome() {
        mainController.loadDefaultContent();
    }
    @FXML
    public void ALogout() {

        mainController.logout();
    }


    @FXML
    private void switchLanguage(String language) {
        Locale newLocale;

        switch (language) {
            case "Finnish":
                newLocale = new Locale("fi", "FI");
                break;
            case "Japanese":
                newLocale = new Locale("ja", "JP");
                break;
            case "Russian":
                newLocale = new Locale("ru", "RU");
                break;
            default:
                newLocale = new Locale("en", "US");
                break;
        }
        LocalizationManager.setLocale(newLocale);
        updateLocalizedText();
        mainController.reloadPage();
    }


    private void setupLanguageSelector() {
        adminLanguageSelector.setValue(LocalizationManager.selectedLanguageProperty().get());
        adminLanguageSelector.setOnAction(event -> {
            String selectedLanguage = adminLanguageSelector.getValue();
            if (selectedLanguage != null) {
                switchLanguage(selectedLanguage);
            }
        });
    }

    private void updateLocalizedText() {
        ResourceBundle bundle = LocalizationManager.getBundle();
        String localizedOwnerText = bundle.getString("Owner");
        ownerText.setText(localizedOwnerText);
    }
}
