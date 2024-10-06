package ofosFrontend.controller.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import ofosFrontend.model.Restaurant;
import ofosFrontend.model.RestaurantList;
import ofosFrontend.session.SessionManager;

import java.util.List;
import java.util.stream.Collectors;

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

    public NavController() {

    }

    @FXML
    private void initialize() {
        navBarRoot.getProperties().put("controller", this);
        initializeUIComponents();
        setupEventHandlers();
        searchBar.addEventHandler(KeyEvent.KEY_RELEASED, event -> handleSearch());
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




}
