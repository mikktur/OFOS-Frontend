package ofosFrontend.controller.Owner;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import ofosFrontend.model.Restaurant;
import ofosFrontend.session.LocalizationManager;

import java.io.IOException;

public class AdminViewFactory {
    private final static String ADMINHOME = "/ofosFrontend/Owner/adminMainUI.fxml";
    private final static String ADMINRESTAURANT = "/ofosFrontend/Owner/adminFoodMenuUI.fxml";
    private final AdminController mainController;
    private String currentView;
    private Restaurant resta;
    public AdminViewFactory(AdminController mainController) {
        this.mainController = mainController;
    }

    public Parent createAdminHomeView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ADMINHOME));
            loader.setResources(LocalizationManager.getBundle());
            Parent root = loader.load();
            AdminMainMenuController adminMainController = loader.getController();
            adminMainController.setMainController(mainController);
            currentView = ADMINHOME;
            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Parent createAdminRestaurantView(Restaurant restaurant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ADMINRESTAURANT));
            loader.setResources(LocalizationManager.getBundle());
            Parent root = loader.load();
            AdminMenuController adminMenuController = loader.getController();
            adminMenuController.setRestaurantID(restaurant.getId(), restaurant.getRestaurantName());
            adminMenuController.setMainController(mainController);
            currentView = ADMINRESTAURANT;
            resta = restaurant;
            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void reloadPage(){
        switch (currentView){
            case ADMINHOME:
                mainController.loadDefaultContent();
                break;
            case ADMINRESTAURANT:
                mainController.loadRestaurantContent(resta);
                break;
        }

    }


}
