package ofosFrontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ofosFrontend.controller.User.UserMainController;

import java.io.IOException;

public class AdminMenuController {
    @FXML
    private Text restaurantName;
    @FXML
    private TilePane menuTilePane;
    @FXML
    private ImageView adminLogout;
    private UserMainController userMainController;
    @FXML
    private void addItem() {
        // Logic to add a new food item
        System.out.println("Adding new item");
    }
    @FXML


    public void editItem(ActionEvent event) {
    }

    public void setMainController(UserMainController userMainController) {
        this.userMainController = userMainController;

    }
}