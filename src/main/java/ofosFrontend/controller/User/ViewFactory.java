package ofosFrontend.controller.User;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import ofosFrontend.model.Restaurant;
import ofosFrontend.session.LocalizationManager;

import java.io.IOException;
// not really creating a view, just loading the fxml file and setupping the views...
public class ViewFactory {
    public static final String MAIN = "/ofosFrontend/User/mainUI.fxml";
    public static final String CHECKOUT = "/ofosFrontend/User/checkout.fxml";
    public static final String SETTINGS = "/ofosFrontend/User/newUserSettingsUI.fxml";
    public static final String RESTAURANT = "/ofosFrontend/User/restaurantMenuUI.fxml";
    public static final String ORDERHISTORY = "/ofosFrontend/OrderHistoryUI.fxml";
    private final UserMainController mainController;
    private String currentView = MAIN;

    public ViewFactory(UserMainController mainController) {
        this.mainController = mainController;
    }
    public Parent createCheckoutView(int rid) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(CHECKOUT));
            Parent root = loader.load();
            CheckoutController checkoutController = loader.getController();
            checkoutController.setMainController(mainController);
            checkoutController.setRid(rid);
            checkoutController.updateView();
            currentView = CHECKOUT;
            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Parent createSettingsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(SETTINGS));
            Parent root = loader.load();
            currentView = SETTINGS;
            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public ScrollPane createRestaurantView(Restaurant restaurant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(RESTAURANT));
            ScrollPane newCenterContent = loader.load();
            RMenuController controller = loader.getController();
            controller.setRestaurant(restaurant);
            controller.createCards();
            currentView = RESTAURANT;
            return newCenterContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Node createDefaultContent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN));
            loader.setResources(LocalizationManager.getBundle());
            Node mainContent = loader.load();
            MMenuController mmController = loader.getController();

            if (mmController != null) {
                mmController.setMainController(mainController);
            } else {
                System.out.println("mmController is null");
            }
            currentView = MAIN;
            return mainContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
