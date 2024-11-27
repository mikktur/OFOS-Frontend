package ofosFrontend.controller.Owner;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import ofosFrontend.session.GenericHelper;
import ofosFrontend.session.LocalizationManager;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller for the owner side navigation bar
 */
public class AdminNavController  extends AdminBasicController {
    public javafx.scene.text.Text ownerText;
    @FXML
    HBox adminNav;
    @FXML
    ImageView adminLogout;
    @FXML
    ImageView adminHome;
    @FXML
    private ComboBox<String> languageSelector;

    /**
     * Initializes the navigation bar
     */
    @FXML
    public void initialize() {
        adminNav.getProperties().put("controller", this);
        setupLanguageSelector();
    }

    /**
     * Loads the home view
     */
    @FXML
    public void goToHome() {
        mainController.loadDefaultContent();
    }

    /**
     * Logs out the user
     */
    @FXML
    public void ALogout() {
        mainController.logout();
    }

    /**
     * Switches the language of the application
     * @param language the language to switch to
     */
    private void switchLanguage(String language) {
        GenericHelper.switchLanguage(language);
        updateLocalizedText();

        mainController.reloadPage();
    }


    /**
     * Sets up the language selector
     */
    private void setupLanguageSelector() {
        languageSelector.setValue(LocalizationManager.selectedLanguageProperty().get());

        languageSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                switchLanguage(newVal);
            }
        });

        languageSelector.valueProperty().bindBidirectional(LocalizationManager.selectedLanguageProperty());
    }

    /**
     * Updates the localized text
     */
    private void updateLocalizedText() {
        ResourceBundle bundle = LocalizationManager.getBundle();
        String localizedOwnerText = bundle.getString("Owner");
        ownerText.setText(localizedOwnerText);
    }
}
