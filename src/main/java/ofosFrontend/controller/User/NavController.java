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
    private Text mainMenuLink;
    @FXML
    private ImageView dropDownMenuBtn;
    @FXML
    private StackPane navBar;
    @FXML
    private VBox dropDown;
    @FXML
    AnchorPane dropDownContent;
    @FXML
    private Label usernameLabel;
    public NavController() {

    }

    @FXML
    private void initialize() {

        initializeUIComponents();
        setupEventHandlers();

    }

    public void handleDropDownClick() {
        System.out.println("Drop down clicked");

        try {
            Parent parent = navBar.getParent();
            BorderPane borderPane = (BorderPane) parent;

            if (dropDownContent == null) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/dropDownUi.fxml"));
                dropDownContent = loader.load();
            }


            if (dropDownContent.isVisible()) {

                dropDownContent.setVisible(false);
                dropDownContent.setManaged(false);
                borderPane.setLeft(null);
            } else {

                dropDownContent.setVisible(true);
                dropDownContent.setManaged(true);
                borderPane.setLeft(dropDownContent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadDropDownContent() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/dropDownUi.fxml"));
        try {
            dropDownContent = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setUsernameLabel() {
        SessionManager sessionManager = SessionManager.getInstance();
        System.out.println(sessionManager.getUsername());
        usernameLabel.setText(sessionManager.getUsername());

    }

    public void initializeUIComponents() {
        loadDropDownContent();
        setUsernameLabel();
    }

    public void setupEventHandlers() {
        assert mainMenuLink != null;
        mainMenuLink.setOnMouseClicked(event -> super.goToMain());
        assert dropDownMenuBtn != null;
        dropDownMenuBtn.setOnMouseClicked(event -> handleDropDownClick());
    }
}
