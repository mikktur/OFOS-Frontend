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
    private final UserMainController mainController;
    private ViewPath currentView;

    public ViewFactory(UserMainController mainController) {
        this.mainController = mainController;
    }
    public Parent createCheckoutView(int rid) {
        try {
            System.out.println(currentView);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPath.CHECKOUT.getPath()));
            loader.setResources(LocalizationManager.getBundle());
            Parent root = loader.load();
            CheckoutController checkoutController = loader.getController();
            checkoutController.setMainController(mainController);
            checkoutController.setRid(rid);
            checkoutController.updateView();
            currentView = ViewPath.CHECKOUT;

            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Parent createSettingsView() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPath.SETTINGS.getPath()));
            loader.setResources(LocalizationManager.getBundle());
            Parent root = loader.load();
            currentView = ViewPath.SETTINGS;
            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Parent createOrderHistoryView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPath.ORDERHISTORY.getPath()));
            loader.setResources(LocalizationManager.getBundle());
            Parent root = loader.load();
            currentView = ViewPath.ORDERHISTORY;
            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public ScrollPane createRestaurantView(Restaurant restaurant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPath.RESTAURANT.getPath()));
            loader.setResources(LocalizationManager.getBundle());
            ScrollPane newCenterContent = loader.load();
            RestaurantMenuController controller = loader.getController();
            controller.setRestaurant(restaurant);
            controller.createCards();
            currentView = ViewPath.RESTAURANT;
            return newCenterContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Node createDefaultContent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPath.MAIN.getPath()));
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
            currentView = ViewPath.MAIN;
            return mainContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Parent createAdminDashboardView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewPath.ADMINDASHBOARD.getPath()));
            loader.setResources(LocalizationManager.getBundle());
            Parent root = loader.load();
            currentView = ViewPath.ADMINDASHBOARD;
            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void reloadPage(){
        switch (currentView) {
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
