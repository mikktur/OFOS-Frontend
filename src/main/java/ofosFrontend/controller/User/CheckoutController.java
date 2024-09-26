package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ofosFrontend.model.CartItem;
import ofosFrontend.model.ShoppingCart;
import ofosFrontend.session.SessionManager;

public class CheckoutController {
    @FXML
    VBox summaryContainer;
    @FXML
    Label subTotal;

    public CheckoutController() {
    }
    @FXML
    public void initialize() {
        renderSummary();
    }

    public void renderSummary() {
        SessionManager session = SessionManager.getInstance();
        ShoppingCart cart = session.getCart();
        for (CartItem item : cart.getItems()) {
            HBox container = new HBox();
            container.setSpacing(5);
            Label name = new Label();
            Label price = new Label();
            Label quantity = new Label();
            name.setText(item.getProduct().getProductName());
            price.setText(String.valueOf(item.getTotalPrice()));
            quantity.setText(String.valueOf(item.getQuantity()));
            container.getChildren().addAll(name, price, quantity);
            summaryContainer.getChildren().add(container);
        }
        subTotal.setText(String.valueOf(cart.getTotalPrice()));
    }
}
