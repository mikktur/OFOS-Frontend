package ofosFrontend.controller.User;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ofosFrontend.model.Restaurant;
import ofosFrontend.model.RestaurantList;
import ofosFrontend.model.ShoppingCart;
import ofosFrontend.service.RestaurantService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class MMenuController extends BasicController{
    @FXML
    private FlowPane restaurantFlowPane;
    @FXML
    private ScrollPane mainScroll;
    private final RestaurantService restaurantService = new RestaurantService();
    private RestaurantList restaurantList = new RestaurantList();


    @FXML
    public void initialize() {
        try {
            restaurantList.setRestaurants(restaurantService.getAllRestaurants());

            for (Restaurant restaurant : restaurantList.getRestaurantList()) {
                addRestaurantCard(restaurant);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainScroll.widthProperty().addListener((obs, oldVal, newVal) -> {
            restaurantFlowPane.setPrefWrapLength(newVal.doubleValue());
            restaurantFlowPane.requestLayout();
        });

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

        setCenterContent(newCenterContent);


    }
    public MMenuController getController() {
        return this;
    }
    public void filterRestaurants(String query) {

        restaurantFlowPane.getChildren().clear();


        if (query.isEmpty()) {
            for (Restaurant restaurant : restaurantList.getRestaurantList()) {
                addRestaurantCard(restaurant);
            }
            return;
        }


        List<Restaurant> matchedRestaurants = restaurantList.getRestaurantList().stream()
                .filter(restaurant -> restaurant.getRestaurantName().toLowerCase().contains(query))
                .collect(Collectors.toList());

        for (Restaurant restaurant : matchedRestaurants) {
            addRestaurantCard(restaurant);
        }
    }
    private void addRestaurantCard(Restaurant restaurant) {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
