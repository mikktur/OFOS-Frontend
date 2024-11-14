package ofosFrontend.controller.Owner;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import ofosFrontend.session.LocalizationManager;
import org.w3c.dom.Text;

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
    private ComboBox<String> languageSelector;


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

        // Updates the locale in LocalizationManager and reloads the main UI
        LocalizationManager.setLocale(newLocale);

        // Ensure the navbar text is updated
        updateLocalizedText();

        mainController.reloadPage(); // This will reload the rest of the page
    }


    private void setupLanguageSelector() {
        languageSelector.setValue(LocalizationManager.selectedLanguageProperty().get());

        languageSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                switchLanguage(newVal);
            }
        });

        languageSelector.valueProperty().bindBidirectional(LocalizationManager.selectedLanguageProperty());
    }

    private void updateLocalizedText() {
        ResourceBundle bundle = LocalizationManager.getBundle();
        String localizedOwnerText = bundle.getString("Owner");
        ownerText.setText(localizedOwnerText);
    }
}
