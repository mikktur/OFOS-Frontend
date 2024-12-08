package ofosFrontend.controller.User;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import ofosFrontend.session.GenericHelper;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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
    private static final Logger logger = LogManager.getLogger(NavController.class);
    public NavController() {
        // required by FXML loader

    }

    /**
     * Initializes the navigation bar
     * Sets the controller in the properties of the root element
     * Initializes the UI components
     * Sets up event handlers
     * Sets up the language selector
     */
    @FXML
    private void initialize() {
        navBarRoot.getProperties().put("controller", this);
        initializeUIComponents();
        setupEventHandlers();
        searchBar.addEventHandler(KeyEvent.KEY_RELEASED, event -> handleSearch());
        setupLanguageSelector();
    }

    /**
     * Switches the language of the application
     * @param language The language to switch to
     */
    private void switchLanguage(String language) {
        GenericHelper.switchLanguage(language);
        mainController.reloadPage();
    }

    /**
     * Sets up the language selector
     */
    private void setupLanguageSelector() {
        languageSelector.setValue(LocalizationManager.selectedLanguageProperty().get());
        languageSelector.setOnAction(event -> {
            String selectedLanguage = languageSelector.getValue();
            if (selectedLanguage != null) {
                switchLanguage(selectedLanguage);
            }
        });
    }

    /**
     * Handles the search event
     */
    private void handleSearch() {
        String query = searchBar.getText().toLowerCase();
        logger.info("Query: {}", query);


        if (mainController != null) {
            mainController.filterRestaurants(query);
        }
    }

    /**
     * Sets the username label to the current user's username
     */
    public void setUsernameLabel() {
        SessionManager sessionManager = SessionManager.getInstance();
        logger.info(sessionManager.getUsername());
        usernameLabel.setText(sessionManager.getUsername());

    }

    /**
     * Initializes the UI components
     * Sets the username label
     * Makes the red dot transparent
     */
    public void initializeUIComponents() {
        setUsernameLabel();
        redDot.setMouseTransparent(true);
    }

    /**
     * Handles the dropdown menu click event
     */
    public void handleDropDownClick() {
        mainController.toggleSideMenu();
    }

    /**
     * Sets up the event handlers for the navigation bar
     */
    public void setupEventHandlers() {
        assert mainMenuLink != null;
        mainMenuLink.setOnMouseClicked(event -> mainController.loadDefaultContent());
        assert dropDownMenuBtn != null;
        dropDownMenuBtn.setOnMouseClicked(event -> handleDropDownClick());
        openCart.setOnMouseClicked(event -> handleCartClick());

        usernameLabel.setOnMouseClicked(event -> {
            mainController.loadSettingsView();
        });
    }

    /**
     * Handles the cart click event
     */
    @FXML
    private void handleCartClick() {
        mainController.toggleShoppingCart();
    }

    /**
     * Hides the red dot
     */
    public void hideRedDot() {
        redDot.setVisible(false);
    }

    /**
     * Shows the red dot
     */
    public void showRedDot() {

        redDot.setVisible(true);
    }




}
