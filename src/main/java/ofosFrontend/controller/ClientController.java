package ofosFrontend.controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ofosFrontend.model.Restaurant;
import ofosFrontend.model.RestaurantList;
import ofosFrontend.service.RestaurantService;

import java.io.IOException;
import java.util.Objects;


public class ClientController {

    @FXML
    private ImageView mcButton;
    @FXML
    private FlowPane restaurantFlowPane;
    @FXML
    private Button mVButton;
    @FXML
    private Button mCButton;
    @FXML
    private Button bmButton;
    @FXML
    private Text adminTest;
    @FXML
    private ImageView dropDownMenu;
    @FXML
    private Text returnToMenu;
    private final RestaurantService restaurantService = new RestaurantService();

    @FXML
    private void goToRestaurant() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/restaurantMenuUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) mcButton.getScene().getWindow();

        Scene restaurantScene = new Scene(root, 650, 400);

        currentStage.setTitle("OFOS Restaurant");

        currentStage.setScene(restaurantScene);

        currentStage.show();
    }

    public void initMenu() {
        RestaurantList restaurantList = new RestaurantList();
        try {
            restaurantList.setRestaurants(restaurantService.getAllRestaurants());
            for (Restaurant restaurant : restaurantList.getRestaurantList()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/restaurant_card.fxml"));
                System.out.println(loader);
                VBox card = loader.load();

                // Access UI elements directly if no controller is used
                ImageView imageView = (ImageView) card.lookup("#restaurantImage");
                Label descriptionLabel = (Label) card.lookup("#restaurantDesc");

                // Set data

                imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/hamburga.jpg"))));//+ restaurant.getPicture()));


                descriptionLabel.setText(restaurant.getRestaurantName() + "\n" + restaurant.getRestaurantPhone());

                // Add card to FlowPane
                restaurantFlowPane.getChildren().add(card);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void goAdmin(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/adminMainUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) adminTest.getScene().getWindow();

        Scene adminScene = new Scene(root, 650, 400);

        currentStage.setTitle("OFOS Admin");

        currentStage.setScene(adminScene);

        currentStage.show();
    }

    public void goToSettings(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/userSettingsUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) dropDownMenu.getScene().getWindow();

        Scene settingsScene = new Scene(root, 650, 400);

        currentStage.setTitle("OFOS Settings");

        currentStage.setScene(settingsScene);

        currentStage.show();
    }

    public void backToMenu(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/mainUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) returnToMenu.getScene().getWindow();

        Scene mainScene = new Scene(root, 650, 400);

        currentStage.setTitle("OFOS Restaurant");

        currentStage.setScene(mainScene);

        currentStage.show();
    }

    public void applySettings(ActionEvent event) {
        System.out.println("Settings applied");
    }

    public void changePassword(ActionEvent event) {
        System.out.println("Changing password hasn't been implemented yet");
    }
}
