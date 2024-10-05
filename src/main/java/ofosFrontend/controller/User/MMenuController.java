package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ofosFrontend.model.Restaurant;
import ofosFrontend.model.RestaurantList;
import ofosFrontend.model.ShoppingCart;
import ofosFrontend.service.RestaurantService;

import java.io.IOException;
import java.util.Objects;


public class MMenuController extends BasicController{
    @FXML
    private FlowPane restaurantFlowPane;

    @FXML
    private ScrollPane mainScroll;

    @FXML
    private Text returnToMenu;
    private final RestaurantService restaurantService = new RestaurantService();

    @FXML
    public void initialize() {
        RestaurantList restaurantList = new RestaurantList();
        try {
            restaurantList.setRestaurants(restaurantService.getAllRestaurants());
            for (Restaurant restaurant : restaurantList.getRestaurantList()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/restaurant_card.fxml"));
                VBox card = loader.load();

                ImageView imageView = (ImageView) card.lookup("#restaurantImage");
                Label descriptionLabel = (Label) card.lookup("#restaurantDesc");

                imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/hamburga.jpg"))));
                descriptionLabel.setText(restaurant.getRestaurantName() + "\n" + restaurant.getRestaurantPhone());

                card.setOnMouseClicked(event -> {
                    try {
                        goToRestaurant(restaurant);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });


                restaurantFlowPane.getChildren().add(card);

                mainScroll.widthProperty().addListener((obs, oldVal, newVal) -> {
                    restaurantFlowPane.setPrefWrapLength(newVal.doubleValue());
                    restaurantFlowPane.requestLayout();
                });
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToRestaurant(Restaurant restaurant) throws IOException {
        setupRestaurantView(restaurant);
    }






    private void setupRestaurantView(Restaurant restaurant) throws IOException {
        mainController.getShoppingCartController().initializeCartForRestaurant(restaurant.getId(),restaurant);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/restaurantMenuUI.fxml"));
        ScrollPane newCenterContent = loader.load();
        RMenuController controller = loader.getController();
        controller.setRestaurant(restaurant);
        controller.createCards();

        mainController.setCenterContent(newCenterContent);


    }

}
