package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ofosFrontend.model.CartItem;
import ofosFrontend.model.Product;
import ofosFrontend.model.Restaurant;
import ofosFrontend.model.ShoppingCart;
import ofosFrontend.service.ProductService;
import ofosFrontend.session.SessionManager;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class RMenuController {
    private Restaurant restaurant;
    private FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/restaurantMenuUI.fxml"));
    private ProductService productService = new ProductService();
    @FXML
    private FlowPane menuContainer;
    @FXML
    private ScrollPane menuScroll;
    public RMenuController(Restaurant restaurant) {
        this.restaurant = restaurant;

    }

    public RMenuController() {
    }


    public void createCards() {
        List<Product> products = null;
        System.out.println("Restaurant: " + this.restaurant.getRestaurantName());
        try {
            products  = productService.getProductsByRID(this.restaurant.getId());

            for(Product product : products) {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/ofosFrontend/menuItem.fxml"));
                VBox card = cardLoader.load();
                VBox cText = (VBox) card.lookup("#itemInfo");
                Button addToCartButton = (Button) card.lookup("#addToCart");
                ImageView imageView = (ImageView) card.lookup("#itemImage");
                Label descriptionLabel = (Label) cText.lookup("#itemDesc");
                Label priceLabel = (Label) cText.lookup("#itemPrice");
                Label nameLabel = (Label) cText.lookup("#itemName");
                nameLabel.setText(product.getProductName());
                descriptionLabel.setText(product.getProductDesc());
                priceLabel.setText(String.valueOf(product.getProductPrice()));
                addToCartButton.setOnMouseClicked(event -> {
                    addProductToCart(product);

                });
                imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/hamburga.jpg"))));//+ product.getPicture()))
                menuContainer.getChildren().add(card);


            }
            menuScroll.widthProperty().addListener((obs, oldVal, newVal) -> {
                menuContainer.setPrefWrapLength(newVal.doubleValue());
                menuContainer.requestLayout();
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getRestaurantId() {
        return restaurant.getId();
    }
    private void addProductToCart(Product product) {
        SessionManager sessionManager = SessionManager.getInstance();
        ShoppingCart cart = sessionManager.getCart(restaurant.getId());

        // If no cart exists, create a new one
        if (cart == null) {
            cart = new ShoppingCart(restaurant);
            sessionManager.addCart(restaurant.getId(), cart);
        }
        cart.addItem(product, 1);
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

}