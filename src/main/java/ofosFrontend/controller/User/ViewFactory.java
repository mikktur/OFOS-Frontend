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
    public static final String ADMINDASHBOARD = "/ofosFrontend/User/adminDashboardUI.fxml";
    private final UserMainController mainController;
    public String currentView;

    public ViewFactory(UserMainController mainController) {
        this.mainController = mainController;
    }
    public Parent createCheckoutView(int rid) {
        try {
            System.out.println(currentView);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(CHECKOUT));
            loader.setResources(LocalizationManager.getBundle());
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
            loader.setResources(LocalizationManager.getBundle());
            Parent root = loader.load();
            currentView = SETTINGS;
            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Parent createOrderHistoryView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ORDERHISTORY));
            loader.setResources(LocalizationManager.getBundle());
            Parent root = loader.load();
            currentView = ORDERHISTORY;
            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public ScrollPane createRestaurantView(Restaurant restaurant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(RESTAURANT));
            loader.setResources(LocalizationManager.getBundle());
            ScrollPane newCenterContent = loader.load();
            RestaurantMenuController controller = loader.getController();
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
            MainMenuController mmController = loader.getController();
            mainController.setMmController(mmController);
            if (mmController != null) {
                mmController.setMainController(mainController);
            } else {
                System.out.println("mmController is null");
            }
            mainController.resetToDefaultCartView();
            mainController.reloadDropDown();
            currentView = MAIN;
            return mainContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Parent createAdminDashboardView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ADMINDASHBOARD));
            loader.setResources(LocalizationManager.getBundle());
            Parent root = loader.load();
            currentView = ADMINDASHBOARD;
            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void reloadPage(){
        switch (currentView){
            case CHECKOUT:
                mainController.loadCheckoutView(mainController.getCurrentRestaurant().getId());
                break;
            case SETTINGS:
                mainController.loadSettingsView();
                break;
            case RESTAURANT:
                mainController.loadRestaurantView(mainController.getCurrentRestaurant());
                break;
            case ORDERHISTORY:
                mainController.loadHistoryView();
                break;
            case ADMINDASHBOARD:
                mainController.loadAdminDashboardView();
                break;
            default:
                mainController.loadDefaultContent();
        }

    }

}
