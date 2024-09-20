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

    public VBox loadCartItem(CartItem item) throws IOException {
        System.out.println("Load cart function! " + item.getProduct().getProductName());

        // Load the FXML and get the controller for the loaded FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/cartItem.fxml"));
        VBox cartItem = loader.load();

        // Get the controller associated with the loaded FXML
        CartItemController controller = loader.getController();

        // Now use the controller to set values
        controller.itemName.setText(item.getProduct().getProductName());
        controller.itemPrice.setText(String.valueOf(item.getProduct().getProductPrice()));
        System.out.println("Quantity: " + item.getQuantity());
        controller.itemQuantity.setText(String.valueOf(item.getQuantity()));

        controller.addBtn.setOnMouseClicked(event -> {
            item.addQuantity();
        });

        controller.subBtn.setOnMouseClicked(event -> {
            item.subQuantity();
        });

        controller.deleteBtn.setOnMouseClicked(event -> {
            cartItemContainer.getChildren().remove(cartItem);
        });

        return cartItem;
    }
}