package ofosFrontend.controller.Owner;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import ofosFrontend.session.GenericHelper;
import ofosFrontend.session.LocalizationManager;
import java.util.ResourceBundle;

public class AdminNavController  extends AdminBasicController {
    @FXML
    private Text ownerText;
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
    public void AdminLogout() {
        mainController.logout();
    }
    @FXML
    private ComboBox<String> languageSelector;


    private void switchLanguage(String language) {
        GenericHelper.switchLanguage(language);
        updateLocalizedText();

        mainController.reloadPage();
    }



    private void setupLanguageSelector() {
        languageSelector.setValue(LocalizationManager.selectedLanguageProperty().get());
        languageSelector.setOnAction(event -> {
            String selectedLanguage = languageSelector.getValue();
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
