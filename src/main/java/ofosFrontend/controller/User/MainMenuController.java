package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import ofosFrontend.model.Restaurant;
import ofosFrontend.model.RestaurantList;
import ofosFrontend.service.RestaurantService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class MainMenuController extends BasicController {
    @FXML
    private FlowPane restaurantFlowPane;
    @FXML
    private ScrollPane mainScroll;
    @FXML
    private AnchorPane pizza_category;
    @FXML
    private AnchorPane burger_category;
    @FXML
    private AnchorPane steak_category;



    private final RestaurantService restaurantService = new RestaurantService();
    private final RestaurantList restaurantList = new RestaurantList();
    private final String URL = "http://10.120.32.94:8000/images/restaurant/";

    // Reference to the main controller


    @FXML
    public void initialize() {
        // DONT ADD ANYTHING HERE THAT USES MAINCONTROLLER IT WONT WORK SINCE ITS NULL. CALL THEM IN THE LOADDEFAULTCONTENT FUNCTION.
        loadRestaurantsByCategory("All");
    }


    @FXML
    private void goToRestaurant(Restaurant restaurant) throws IOException {
        setupRestaurantView(restaurant);
    }

    @FXML
    private void handlePizzaCategoryClick(MouseEvent event) {
        loadRestaurantsByCategory("Pizza");
    }

    @FXML
    private void handleBurgerCategoryClick(MouseEvent event) {
        loadRestaurantsByCategory("Burger");
    }

    @FXML
    private void handleSteakCategoryClick(MouseEvent event) {
        loadRestaurantsByCategory("Steak");
    }



    private void loadRestaurantsByCategory(String categoryName) {
        try {
            restaurantFlowPane.getChildren().clear();
            restaurantFlowPane.setPrefWrapLength(0);
            restaurantFlowPane.setAlignment(Pos.CENTER);

            List<Restaurant> restaurants;

            if (categoryName.equals("All")) {
                restaurants = restaurantService.getAllRestaurants();
            } else {
                restaurants = restaurantService.getRestaurantsByCategory(categoryName);
            }

            if (restaurants.isEmpty()) {
                Label noResultsLabel = new Label("No restaurants found in this category.");
                restaurantFlowPane.getChildren().add(noResultsLabel);
            } else {
                for (Restaurant restaurant : restaurants) {
                    addRestaurantCard(restaurant);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            Label errorLabel = new Label("An error occurred while loading restaurants.");
            restaurantFlowPane.getChildren().add(errorLabel);
        }
    }





    private void setupRestaurantView(Restaurant restaurant) {
        if (mainController == null) {
            System.out.println("Error: mainController is null in MMenuController.");
            return;
        }
        //makes sure that the cart that is used is for the correct restaurant.
        mainController.loadRestaurantView(restaurant);
    }


    public MainMenuController getController() {
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
                .toList();

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

            imageView.setImage(new Image(URL + restaurant.getPicture()));
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

