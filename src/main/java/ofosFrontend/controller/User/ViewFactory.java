package ofosFrontend.controller.User;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import ofosFrontend.model.Restaurant;
import ofosFrontend.session.LocalizationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating views
 */
public class ViewFactory {
    public static final String MAIN = "/ofosFrontend/User/mainUI.fxml";
    public static final String CHECKOUT = "/ofosFrontend/User/checkout.fxml";
    public static final String SETTINGS = "/ofosFrontend/User/newUserSettingsUI.fxml";
    public static final String RESTAURANT = "/ofosFrontend/User/restaurantMenuUI.fxml";
    public static final String ORDERHISTORY = "/ofosFrontend/OrderHistoryUI.fxml";
    public static final String ADMINDASHBOARD = "/ofosFrontend/User/adminDashboardUI.fxml";

    private final UserMainController mainController;
    private final Map<String, Runnable> reloadActions = new HashMap<>();
    private String currentView;
    private final Logger logger = LogManager.getLogger(ViewFactory.class);
    public ViewFactory(UserMainController mainController) {
        this.mainController = mainController;
        initializeReloadActions();
    }

    /**
     * Loads an FXML file, sets resources, and returns the root node.
     *
     * @param fxmlPath Path to the FXML file.
     * @param controllerSetup A callback to configure the controller (optional).
     * @return The root node of the loaded FXML.
     */
    private <T> Parent loadView(String fxmlPath, ControllerConsumer<T> controllerSetup) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setResources(LocalizationManager.getBundle());
            Parent root = loader.load();
            if (controllerSetup != null) {
                T controller = loader.getController();
                controllerSetup.accept(controller);
            }
            currentView = fxmlPath;
            return root;
        } catch (IOException e) {
            logger.error("Error loading checkout view");
            return null;
        }
    }

    /**
     * Creates the checkout view.
     *
     * @param rid The restaurant ID.
     * @return The parent node.
     */
    public Parent createCheckoutView(int rid) {
        return loadView(CHECKOUT, controller -> {
            CheckoutController checkoutController = (CheckoutController) controller;
            checkoutController.setMainController(mainController);
            checkoutController.setRestaurant(rid);
            mainController.setRid(rid);
            checkoutController.updateView();
        });
    }

    /**
     * Creates the settings view.
     *
     * @return The parent node.
     */
    public Parent createSettingsView() {
        return loadView(SETTINGS, null);
    }

    /**
     * Creates the order history view.
     *
     * @return The parent node.
     */
    public Parent createOrderHistoryView() {
        return loadView(ORDERHISTORY, null);
    }

    /**
     * Creates the restaurant view.
     *
     * @param restaurant The restaurant to display.
     * @return The scroll pane.
     */
    public ScrollPane createRestaurantView(Restaurant restaurant) {
        return (ScrollPane) loadView(RESTAURANT, controller -> {
            RestaurantMenuController menuController = (RestaurantMenuController) controller;
            menuController.setRestaurant(restaurant);
            menuController.createCards();
        });
    }

    /**
     * Creates the default content view.
     *
     * @return The node.
     */
    public Node createDefaultContent() {
        return loadView(MAIN, controller -> {
            MainMenuController mmController = (MainMenuController) controller;
            mainController.setMmController(mmController);
            mainController.resetToDefaultCartView();
            if (mmController != null) {
                mmController.setMainController(mainController);
            } else {
                logger.error("mmController is null");
            }
            mainController.reloadNavBar();
            mainController.reloadDropDown();
        });
    }

    /**
     * Creates the admin dashboard view.
     *
     * @return The parent node.
     */
    public Parent createAdminDashboardView() {
        return loadView(ADMINDASHBOARD, null);
    }

    /**
     * Reloads the current page.
     */
    public void reloadPage() {
        Runnable reloadAction = reloadActions.get(currentView);
        if (reloadAction != null) {
            reloadAction.run();
        } else {
            mainController.loadDefaultContent();
        }
    }

    /**
     * Initializes reload actions for different views.
     */
    private void initializeReloadActions() {
        reloadActions.put(CHECKOUT, () -> mainController.loadCheckoutView(mainController.getRid()));
        reloadActions.put(SETTINGS, mainController::loadSettingsView);
        reloadActions.put(RESTAURANT, () -> mainController.loadRestaurantView(mainController.getCurrentRestaurant()));
        reloadActions.put(ORDERHISTORY, mainController::loadHistoryView);
        reloadActions.put(ADMINDASHBOARD, mainController::loadAdminDashboardView);
        reloadActions.put(MAIN, mainController::loadDefaultContent);
    }

    /**
     * A functional interface for accepting a controller.
     *
     * @param <T> The type of the controller.
     */
    @FunctionalInterface
    private interface ControllerConsumer<T> {
        void accept(T controller);
    }


}
