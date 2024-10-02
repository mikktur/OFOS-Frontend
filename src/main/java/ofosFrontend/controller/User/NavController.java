package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ofosFrontend.session.SessionManager;

public class NavController extends BasicController {
    @FXML
    public StackPane navBarRoot;
    @FXML
    private Text mainMenuLink;
    private MainController mainController;
    @FXML
    private ImageView dropDownMenuBtn;
    @FXML
    private Label usernameLabel;
    @FXML
    private ImageView openCart;
    @FXML
    VBox shoppingCartContent;

    public NavController() {

    }

    @FXML
    private void initialize() {
        navBarRoot.getProperties().put("controller", this);
        initializeUIComponents();
        setupEventHandlers();
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


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
