package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ofosFrontend.model.CartItem;
import ofosFrontend.model.ShoppingCart;
import ofosFrontend.session.SessionManager;

public class CheckoutController extends BasicController {
    @FXML
    VBox summaryContainer;
    @FXML
    Label subTotal;
    private int rid;
    public CheckoutController() {
        this.rid = 0;
    }
    @FXML
    public void initialize() {

    }

    public void renderSummary() {
        SessionManager session = SessionManager.getInstance();
        ShoppingCart cart = session.getCart(this.rid);
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

    public void setRid(int rid) {
        this.rid = rid;
        renderSummary();
    }
}
