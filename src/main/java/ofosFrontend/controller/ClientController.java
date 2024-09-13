package ofosFrontend.controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;


public class ClientController {

    @FXML
    private ImageView mcButton;
    @FXML
    private Button mVButton;
    @FXML
    private Button mCButton;
    @FXML
    private Button bmButton;

    @FXML
    private void goToRestaurant() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/restaurantMenuUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) mcButton.getScene().getWindow();

        Scene registerScene = new Scene(root, 650, 400);

        currentStage.setTitle("OFOS Restaurant");

        currentStage.setScene(registerScene);

        currentStage.show();
    }

    public void openMainMenu(MouseEvent mouseEvent) {
        System.out.println("Main Menu button clicked");
    }

    public void openCart(MouseEvent mouseEvent) {
        System.out.println("Cart button clicked");
    }

    public void mcVeganToCart(ActionEvent event) {
        System.out.println("McVegan added to cart");
    }

    public void mcChickenTOCart(ActionEvent event) {
        System.out.println("McChicken added to cart");
    }

    public void bigMacToCart(ActionEvent event) {
        System.out.println("Big Mac added to cart");
    }
}
