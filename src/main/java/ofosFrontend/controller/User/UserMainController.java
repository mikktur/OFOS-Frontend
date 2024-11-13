package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ofosFrontend.model.Restaurant;
import ofosFrontend.session.CartManager;
import ofosFrontend.session.LocalizationManager;

import java.io.IOException;

public class UserMainController {

    @FXML
    private StackPane centerPane;

    @FXML
    private BorderPane root;
    @FXML
    private VBox cart;
    @FXML
    ShoppingCartController shoppingCartController;
    @FXML
    NavController navController;
    @FXML
    private StackPane navBar;
    @FXML
    private AnchorPane dropDownRoot;
    @FXML
    DropDownMenuController dropDownMenuController;
    private ViewFactory viewFactory;
    MainMenuController mmController;
    private Restaurant currentRestaurant;
    private final CartManager cartManager = new CartManager();

    @FXML
    public void initialize() {
        viewFactory = new ViewFactory(this);
        setControllers();

        System.out.println("Main controller initialized");

        root.setLeft(null);
        root.setRight(null);
        loadDefaultContent();
    }


    public void setCenterContent(Node content) {
        if (centerPane == null) {
            System.out.println("centerPane is null!");
            return;
        }

        centerPane.getChildren().clear();
        centerPane.getChildren().add(content);
        StackPane.setAlignment(content, Pos.CENTER);
    }
    public void loadCheckoutView(int rid) {
        Parent checkoutView = viewFactory.createCheckoutView(rid);

        if (checkoutView != null) {
            setCenterContent(checkoutView);
        }
    }

    public void loadSettingsView() {
        Parent settingsView = viewFactory.createSettingsView();
        if (settingsView != null) {
            resetToDefaultCartView();
            setCenterContent(settingsView);
        }
    }

    public void loadAdminDashboardView() {
        Parent adminDashboardView = viewFactory.createAdminDashboardView();
        if (adminDashboardView != null) {
            resetToDefaultCartView();
            setCenterContent(adminDashboardView);
        }
    }


    public void loadRestaurantView(Restaurant restaurant) {
        ScrollPane restaurantView = viewFactory.createRestaurantView(restaurant);
        getShoppingCartController().initializeCartForRestaurant(restaurant.getId(), restaurant);

        if (restaurantView != null) {
            currentRestaurant = restaurant;
            System.out.println(currentRestaurant);
            setCenterContent(restaurantView);
        }
    }
    public void setCurrentRestaurant(Restaurant restaurant) {
        currentRestaurant = restaurant;
    }
    public void loadHistoryView() {
        Parent historyView = viewFactory.createOrderHistoryView();

        if (historyView != null) {
            setCenterContent(historyView);
        }
    }
    public void loadDefaultContent() {
        Node defaultContent = viewFactory.createDefaultContent();

        if (defaultContent != null) {
            setCenterContent(defaultContent);
            currentRestaurant=null;
        }
    }
    public void setMmController(MainMenuController mainMenuController){
        mmController = mainMenuController;
    }

    public void reloadDropDown() {
        try {
            boolean isVisible = dropDownRoot.isVisible();
            if(isVisible){
                toggleSideMenu();
            }
            root.setLeft(null);
            FXMLLoader dropDownLoader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/dropDownUI.fxml"));
            dropDownLoader.setResources(LocalizationManager.getBundle());
            dropDownRoot = dropDownLoader.load();
            dropDownMenuController = dropDownLoader.getController();
            dropDownMenuController.setMainController(this);

            if (isVisible) {
                root.setLeft(dropDownRoot);
                toggleSideMenu();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void reloadCart() {
        try {
            boolean isVisible = cart.isVisible();

            root.setRight(null);
            if(isVisible){
                toggleShoppingCart();
            }
            FXMLLoader cartLoader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/shoppingCart.fxml"));
            cartLoader.setResources(LocalizationManager.getBundle());
            cart = cartLoader.load();
            shoppingCartController = cartLoader.getController();
            shoppingCartController.setMainController(this);
            if(currentRestaurant!=null){
                shoppingCartController.setRid(currentRestaurant.getId());
                shoppingCartController.initializeCartForRestaurant(currentRestaurant.getId(), currentRestaurant);
                shoppingCartController.loadCartItems();

            }

            if(isVisible){
                root.setRight(cart);
                toggleShoppingCart();
            }
            shoppingCartController.updateCart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // could be made better. cba.
    public void setControllers() {
        shoppingCartController = (ShoppingCartController) cart.getProperties().get("controller");
        if (shoppingCartController != null) {
            shoppingCartController.setMainController(this);
        }

        navController = (NavController) navBar.getProperties().get("controller");
        if (navController != null) {
            navController.setMainController(this);
        }

        dropDownMenuController = (DropDownMenuController) dropDownRoot.getProperties().get("controller");
        if (dropDownMenuController != null) {
            dropDownMenuController.setMainController(this);
        }
    }

    public void toggleSideMenu() {
        if (dropDownRoot != null) {
            boolean isVisible = dropDownRoot.isVisible();
            dropDownRoot.setVisible(!isVisible);
            root.setLeft(isVisible ? null : dropDownRoot);
        }
    }

    public void toggleShoppingCart() {
        if (cart != null) {
            shoppingCartController.updateCart();
            boolean isVisible = cart.isVisible();
            cart.setVisible(!isVisible);
            root.setRight(isVisible ? null : cart);
        }
    }

    public void hideRedDot() {
        if (navController != null) {
            navController.hideRedDot();
        }
    }

    public void showRedDot() {
        if (navController != null) {
            navController.showRedDot();
        }
    }

    public void filterRestaurants(String query) {
        System.out.println(mmController);
        if (mmController != null) {
            mmController.filterRestaurants(query);
        }
    }

    public ShoppingCartController getShoppingCartController() {
        return shoppingCartController;
    }

    public void resetToDefaultCartView() {
        if (shoppingCartController == null) {
            return;
        }
        shoppingCartController.resetCartView();
    }
    public void reloadPage(){

        viewFactory.reloadPage();
        reloadCart();
        reloadDropDown();

    }
    public Restaurant getCurrentRestaurant() {
        return currentRestaurant;
    }
}


