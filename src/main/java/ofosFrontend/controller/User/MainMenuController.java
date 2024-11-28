package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;

import javafx.scene.input.MouseEvent;
import ofosFrontend.model.Restaurant;
import ofosFrontend.model.RestaurantList;
import ofosFrontend.service.RestaurantService;
import ofosFrontend.session.LocalizationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;


import static ofosFrontend.session.Validations.showError;

/**
 * Controller for the main menu view
 * Contains methods to load restaurants by category and to navigate to the restaurant view
 */
public class MainMenuController extends BasicController {
    ResourceBundle bundle = LocalizationManager.getBundle();
    @FXML
    private FlowPane restaurantFlowPane;
    private final Logger logger = LogManager.getLogger(MainMenuController.class);


    private final RestaurantService restaurantService = new RestaurantService();
    private final RestaurantList restaurantList = new RestaurantList();
    private static final String URL = "http://10.120.32.94:8000/images/restaurant/";

    // Reference to the main controller


    @FXML
    public void initialize() {
        // DONT ADD ANYTHING HERE THAT USES MAINCONTROLLER IT WONT WORK SINCE ITS NULL. CALL THEM IN THE LOADDEFAULTCONTENT FUNCTION.
        loadRestaurantsByCategory("All");
    }

    /**
     * Loads the default content of the main menu view
     */
    @FXML
    private void goToRestaurant(Restaurant restaurant) {
        setupRestaurantView(restaurant);
    }

    /**
     * Handles the click event on a category
     * @param event the mouse event
     * Gets the id of the clicked category and loads the restaurants of that category
     */
    @FXML
    private void handleCategoryClick(MouseEvent event) {
        if (event.getSource() instanceof AnchorPane pane) {
            String categoryId = pane.getId();

            switch (categoryId) {
                case "burger_category" -> loadRestaurantsByCategory("Burger");
                case "pizza_category" -> loadRestaurantsByCategory("Pizza");
                case "steak_category" -> loadRestaurantsByCategory("Steak");
                default -> showError(bundle.getString("Invalid_category_selected"));
            }
        } else {
            showError(bundle.getString("Invalid_event_source"));
        }
    }


    /**
     * Loads the restaurants by category.
     *
     * @param categoryName the category name
     */
    private void loadRestaurantsByCategory(String categoryName) {
        try {
            setupRestaurantFlowPane();

            List<Restaurant> restaurants = fetchRestaurantsByCategory(categoryName);

            if (restaurants.isEmpty()) {
                displayNoResultsMessage();
            } else {
                populateRestaurantCards(restaurants);
            }

        } catch (IOException e) {
            handleLoadingError(e);
        }
    }

    /**
     * Sets up the restaurant FlowPane by clearing it and resetting properties.
     */
    private void setupRestaurantFlowPane() {
        restaurantFlowPane.getChildren().clear();
        restaurantFlowPane.setPrefWrapLength(0);
        restaurantFlowPane.setAlignment(Pos.CENTER);
    }

    /**
     * Fetches restaurants based on the given category name.
     *
     * @param categoryName the category name
     * @return the list of restaurants in the specified category
     * @throws IOException if an error occurs while fetching data
     */
    private List<Restaurant> fetchRestaurantsByCategory(String categoryName) throws IOException {
        if ("All".equals(categoryName)) {
            return restaurantService.getAllRestaurants();
        } else {
            return restaurantService.getRestaurantsByCategory(categoryName);
        }
    }

    /**
     * Displays a message when no restaurants are found in the category.
     */
    private void displayNoResultsMessage() {
        Label noResultsLabel = new Label(bundle.getString("No_restaurants_found"));
        restaurantFlowPane.getChildren().add(noResultsLabel);
    }


    /**
     * Populates the FlowPane with restaurant cards.
     *
     * @param restaurants the list of restaurants
     */
    private void populateRestaurantCards(List<Restaurant> restaurants) {
        for (Restaurant restaurant : restaurants) {
            addRestaurantCard(restaurant);
        }
    }

    /**
     * Handles errors that occur while loading restaurants.
     *
     * @param e the exception that occurred
     */
    private void handleLoadingError(IOException e) {
        logger.error("Error loading restaurants: {}", e.getMessage());
        Label errorLabel = new Label(bundle.getString("Restaurant_loading_error"));
        restaurantFlowPane.getChildren().add(errorLabel);
    }

    /**
     * Loads the restaurant view.
     * @param restaurant the restaurant to load
     */
    private void setupRestaurantView(Restaurant restaurant) {
        if (mainController == null) {
            logger.error("Error: mainController is null in MMenuController.");
            return;
        }
        //makes sure that the cart that is used is for the correct restaurant.
        mainController.loadRestaurantView(restaurant);
    }


    public MainMenuController getController() {
        return this;
    }

    /**
     * Filters and displays restaurants based on the query.
     *
     * @param query The search query to filter restaurants by.
     */
    public void filterRestaurants(String query) {
        List<Restaurant> filteredRestaurants = query.isEmpty()
                ? restaurantList.getRestaurantList()
                : restaurantList.getRestaurantList().stream()
                .filter(restaurant -> restaurant.getRestaurantName().toLowerCase().contains(query.toLowerCase()))
                .toList();

        updateRestaurantFlowPane(filteredRestaurants);
    }

    /**
     * Updates the FlowPane with the given list of restaurants.
     *
     * @param restaurants The list of restaurants to display.
     */
    private void updateRestaurantFlowPane(List<Restaurant> restaurants) {
        restaurantFlowPane.getChildren().clear();
        for (Restaurant restaurant : restaurants) {
            addRestaurantCard(restaurant);
        }
    }


    /**
     * Creates and adds a restaurant card to the FlowPane.
     *
     * @param restaurant The restaurant to display.
     */
    private void addRestaurantCard(Restaurant restaurant) {
        VBox card = loadRestaurantCardFXML();
        if (card != null) {
            bindRestaurantDataToCard(card, restaurant);
            setCardClickEvent(card, restaurant);
            restaurantFlowPane.getChildren().add(card);
        }
    }

    /**
     * Loads the restaurant card FXML.
     *
     * @return The VBox containing the restaurant card, or null if an error occurs.
     */
    private VBox loadRestaurantCardFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/restaurant_card.fxml"));
            return loader.load();
        } catch (IOException e) {
            logger.error("Failed to load restaurant card FXML", e);
            return null;
        }
    }

    /**
     * Binds the restaurant data to the card's UI components.
     *
     * @param card       The VBox representing the restaurant card.
     * @param restaurant The restaurant data to display.
     */
    private void bindRestaurantDataToCard(VBox card, Restaurant restaurant) {
        ImageView imageView = (ImageView) card.lookup("#restaurantImage");
        Label descriptionLabel = (Label) card.lookup("#restaurantDesc");

        if (imageView != null) {
            Image image = new Image(URL + restaurant.getPicture(), true);
            imageView.setImage(image);
        }

        if (descriptionLabel != null) {
            descriptionLabel.setText(restaurant.getRestaurantName() + "\n" + restaurant.getRestaurantPhone());
        }
    }

    /**
     * Sets the click event for the restaurant card.
     *
     * @param card       The VBox representing the restaurant card.
     * @param restaurant The restaurant associated with the card.
     */
    private void setCardClickEvent(VBox card, Restaurant restaurant) {
        card.setOnMouseClicked(event -> goToRestaurant(restaurant));
    }

}

