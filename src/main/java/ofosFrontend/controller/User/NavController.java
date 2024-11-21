package ofosFrontend.controller.User;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;
import java.util.Locale;



/**
 * Controller for the navigation bar
 * Handles search and dropdown menu
 */
public class NavController extends BasicController {
    @FXML
    public StackPane navBarRoot;
    @FXML
    private Text mainMenuLink;
    @FXML
    private TextField searchBar;
    @FXML
    private ImageView dropDownMenuBtn;
    @FXML
    private Label usernameLabel;
    @FXML
    private ImageView openCart;
    @FXML
    AnchorPane redDot;
    @FXML
    private ComboBox<String> languageSelector;
    public NavController() {

    }

    @FXML
    private void initialize() {
        navBarRoot.getProperties().put("controller", this);
        initializeUIComponents();
        setupEventHandlers();
        searchBar.addEventHandler(KeyEvent.KEY_RELEASED, event -> handleSearch());
        setupLanguageSelector();
    }

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
    private void handleSearch() {
        String query = searchBar.getText().toLowerCase();
        System.out.println("Query: " + query);


        if (mainController != null) {
            mainController.filterRestaurants(query);
        }
    }
    public void setUsernameLabel() {
        SessionManager sessionManager = SessionManager.getInstance();
        System.out.println(sessionManager.getUsername());
        usernameLabel.setText(sessionManager.getUsername());

    }

    public void initializeUIComponents() {
        setUsernameLabel();
        redDot.setMouseTransparent(true);
    }
    public void handleDropDownClick() {
        mainController.toggleSideMenu();
    }

    public void setupEventHandlers() {
        assert mainMenuLink != null;
        mainMenuLink.setOnMouseClicked(event -> mainController.loadDefaultContent());
        assert dropDownMenuBtn != null;
        dropDownMenuBtn.setOnMouseClicked(event -> handleDropDownClick());
        openCart.setOnMouseClicked(event -> handleCartClick());
    }

    @FXML
    private void handleCartClick() {
        mainController.toggleShoppingCart();
    }

    public void hideRedDot() {
        redDot.setVisible(false);
    }
    public void showRedDot() {

        redDot.setVisible(true);
    }




}
