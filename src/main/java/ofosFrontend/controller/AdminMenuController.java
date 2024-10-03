package ofosFrontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ofosFrontend.model.Product;

import java.io.IOException;
import java.util.List;

public class AdminMenuController {
    @FXML
    private Text restaurantName;
    @FXML
    private TilePane menuTilePane;
    @FXML
    private ImageView adminLogout;

    @FXML
    private void addItem() {
        // Logic to add a new food item
        System.out.println("Adding new item");
    }
    @FXML
    public void logOut(MouseEvent mouseEvent) throws IOException {
        System.out.println("Logging out...");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/loginUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) adminLogout.getScene().getWindow();

        Scene backToLoginScene = new Scene(root, 650, 400);
        currentStage.setTitle("OFOS Login");
        currentStage.setScene(backToLoginScene);
        currentStage.show();
    }

    public void editItem(ActionEvent event) {
    }
}