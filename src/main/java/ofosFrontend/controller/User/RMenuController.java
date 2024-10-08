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
import ofosFrontend.session.SessionManager;

import java.io.IOException;
import java.util.List;

public class RMenuController extends BasicController{
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

    private final String URL = "http://10.120.32.94:8000/images/";
    private final String RURL = "http://10.120.32.94:8000/images/restaurant/";

    public RMenuController() {
    }


    public void createCards() {
        List<Product> products = null;
        setRestaurantInfo();

        System.out.println("Restaurant: " + this.restaurant.getRestaurantName());
        try {
            products  = productService.getProductsByRID(this.restaurant.getId());

            for(Product product : products) {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/menuItem.fxml"));
                VBox card = cardLoader.load();
                VBox cText = (VBox) card.lookup("#itemInfo");
                Button addToCartButton = (Button) card.lookup("#addToCart");
                ImageView imageView = (ImageView) card.lookup("#itemImage");
                Label descriptionLabel = (Label) cText.lookup("#itemDesc");
                Label priceLabel = (Label) cText.lookup("#itemPrice");
                Label nameLabel = (Label) cText.lookup("#itemName");
                nameLabel.setText(product.getProductName());
                descriptionLabel.setText(product.getProductDesc());
                priceLabel.setText((product.getProductPrice() + " €"));
                addToCartButton.setOnMouseClicked(event -> {
                    addProductToCart(product);
                });
                imageView.setImage(new Image(URL + product.getPicture()));
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

    public void setRestaurantInfo() {
        System.out.println("image url: " + RURL + restaurant.getPicture());
        restaurantImage.setImage(new Image(RURL + restaurant.getPicture()));
        restaurantName.setText(restaurant.getRestaurantName());
        restaurantAddress.setText(restaurant.getAddress());
        restaurantPhone.setText(restaurant.getRestaurantPhone());
        restaurantHours.setText(restaurant.getBusinessHours());
    }

}