package ofosFrontend.controller.Owner;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class AdminNavController  extends AdminBasicController {
    @FXML
    HBox adminNav;
    @FXML
    ImageView adminLogout;
    @FXML
    public void initialize() {
        adminNav.getProperties().put("controller", this);
    }
    @FXML
    public void ALogout() {
        mainController.logout();
    }
}
