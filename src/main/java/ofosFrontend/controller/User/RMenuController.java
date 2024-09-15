package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ofosFrontend.model.Product;
import ofosFrontend.model.Restaurant;
import ofosFrontend.service.ProductService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class RMenuController {
    private Restaurant restaurant;
    private FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/restaurantMenuUI.fxml"));
    private ProductService productService = new ProductService();
    @FXML
    private FlowPane menuContainer;

    public RMenuController(Restaurant restaurant) {
        this.restaurant = restaurant;

    }
    @FXML
    public void initialize() {
        createCards();
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

                ImageView imageView = (ImageView) card.lookup("#itemImage");
                Label descriptionLabel = (Label) cText.lookup("#itemDesc");
                Label priceLabel = (Label) cText.lookup("#itemPrice");
                Label nameLabel = (Label) cText.lookup("#itemName");
                nameLabel.setText(product.getProductName());
                descriptionLabel.setText(product.getProductDesc());
                priceLabel.setText(String.valueOf(product.getProductPrice()));
                imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/hamburga.jpg"))));//+ product.getPicture()))
                menuContainer.getChildren().add(card);


            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Product product : products) {
            System.out.println("Product: " + product.getProductName());
        }
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

}