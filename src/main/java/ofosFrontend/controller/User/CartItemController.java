package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ofosFrontend.model.CartItem;

import java.io.IOException;

public class CartItemController {
    @FXML
    VBox cartItemContainer;
    @FXML
    private Button addBtn;
    @FXML
    private Button subBtn;
    @FXML
    private ImageView deleteBtn;
    @FXML
    private Label itemName;
    @FXML
    private Label itemPrice;
    @FXML
    private Label itemQuantity;

    public CartItemController() {

    }

    @FXML
    public void Initialize() {

    }

    public void loadCartItem(CartItem item) throws IOException {

        // Load the FXML and get the controller for the loaded FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/cartItem.fxml"));
        VBox cartItem = loader.load();

        // Get the controller associated with the loaded FXML


        // Now use the controller to set values
        itemName.setText(item.getProduct().getProductName());
        itemPrice.setText(String.valueOf(item.getProduct().getProductPrice()));
        itemQuantity.setText(String.valueOf(item.getQuantity()));

        addBtn.setOnMouseClicked(event -> {
            item.addQuantity();
        });

        subBtn.setOnMouseClicked(event -> {
            item.subQuantity();
        });

        deleteBtn.setOnMouseClicked(event -> {
            cartItemContainer.getChildren().remove(cartItem);
        });
    }
}