package ofosFrontend.controller.Owner;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import ofosFrontend.session.LocalizationManager;

import java.util.Locale;

public class AdminNavController  extends AdminBasicController {
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
        if (language.equals("Finnish")) {
            newLocale = new Locale("fi", "FI");
        } else {
            newLocale = new Locale("en", "US");
        }

        // Updates the locale in LocalizationManager and reloads the main UI
        LocalizationManager.setLocale(newLocale);
        mainController.reloadPage();
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
}
