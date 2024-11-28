package ofosFrontend.controller.User;

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
import ofosFrontend.model.Product;
import ofosFrontend.model.Restaurant;
import ofosFrontend.model.ShoppingCart;
import ofosFrontend.service.ProductService;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

/**
 * Controller for the restaurant menu view
 * This class is responsible for creating the product cards for the restaurant
 * and adding them to the menu container
 */
public class RestaurantMenuController extends BasicController {
    private Restaurant restaurant;
    private ProductService productService = new ProductService();
    @FXML
    private FlowPane menuContainer;
    @FXML
    private ScrollPane menuScroll;
    @FXML
    private Text restaurantName;
    @FXML
    private Text restaurantAddress;
    @FXML
    private Text restaurantPhone;
    @FXML
    private Text restaurantHours;
    @FXML
    private ImageView restaurantImage;
    private static final Logger logger = LogManager.getLogger(RestaurantMenuController.class);
    private static final String URL = "http://10.120.32.94:8000/images/";
    private static final String RURL = "http://10.120.32.94:8000/images/restaurant/";

    public RestaurantMenuController() {
        // required by FXML loader
    }

    /**
     * Create the product cards for the restaurant
     * and add them to the menu container
     */
    public void createCards() {
        List<Product> products = null;
        setRestaurantInfo();

        try {
            products = productService.getProductsByRID(this.restaurant.getId());
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(LocalizationManager.getLocale());
            for (Product product : products) {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/menuItem.fxml"));
                cardLoader.setResources(LocalizationManager.getBundle());
                VBox card = cardLoader.load();
                VBox cText = (VBox) card.lookup("#itemInfo");
                Button addToCartButton = (Button) card.lookup("#addToCart");
                ImageView imageView = (ImageView) card.lookup("#itemImage");
                Label descriptionLabel = (Label) cText.lookup("#itemDesc");
                Label priceLabel = (Label) cText.lookup("#itemPrice");
                Label nameLabel = (Label) cText.lookup("#itemName");
                nameLabel.setText(product.getProductName());
                descriptionLabel.setText(product.getProductDesc());
                priceLabel.setText(currencyFormatter.format(product.getProductPrice()));
                addToCartButton.setOnMouseClicked(event -> addProductToCart(product));
                Image image = new Image(URL + product.getPicture(),true);
                imageView.setImage(image);
                menuContainer.getChildren().add(card);


            }
            menuScroll.widthProperty().addListener((obs, oldVal, newVal) -> {
                menuContainer.setPrefWrapLength(newVal.doubleValue());
                menuContainer.requestLayout();
            });


        } catch (IOException e) {
            logger.error("Failed to load product cards", e);
        }
    }

    /**
     * Add a product to the shopping cart
     *
     * @param product the product to add
     */
    private void addProductToCart(Product product) {
        SessionManager sessionManager = SessionManager.getInstance();
        ShoppingCart cart = sessionManager.getCart(restaurant.getId());

        if (cart == null) {
            cart = new ShoppingCart(restaurant);
            sessionManager.addCart(restaurant.getId(), cart);
        }
        cart.addItem(product, 1);
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    /**
     * Set the restaurant information
     * This method sets the restaurant image, name, address, phone, and hours
     */
    public void setRestaurantInfo() {
        restaurantImage.setImage(new Image(RURL + restaurant.getPicture()));
        restaurantName.setText(restaurant.getRestaurantName());
        restaurantAddress.setText(restaurant.getAddress());
        restaurantPhone.setText(restaurant.getRestaurantPhone());
        restaurantHours.setText(restaurant.getBusinessHours());
    }

}