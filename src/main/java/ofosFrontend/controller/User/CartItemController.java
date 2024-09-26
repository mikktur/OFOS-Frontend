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


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/cartItem.fxml"));
        VBox cartItem = loader.load();





        itemName.setText(item.getProduct().getProductName());
        itemPrice.setText(String.valueOf(item.getProduct().getProductPrice()));
        itemQuantity.textProperty().bind(item.quantityProperty().asString());

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