package ofosFrontend.controller.Owner;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ofosFrontend.model.Product;
import ofosFrontend.model.Translation;
import ofosFrontend.service.ProductService;
import ofosFrontend.session.LocalizationManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the owner menu view
 */
public class AdminMenuController extends AdminBasicController {

    @FXML
    private VBox productListVBox;
    @FXML
    private ImageView adminLogout;
    @FXML
    private Text restaurantNameText;

    private ProductService productService = new ProductService();
    private int restaurantID;
    private Product product = new Product();

    @FXML
    public void initialize() {
        System.out.println("Initializing :)");
    }

    /**
     * Set the restaurant ID and load the products
     * @param restaurantID The restaurant ID
     * @param restaurantName The restaurant name
     */
    public void setRestaurantID(int restaurantID, String restaurantName) {
        this.restaurantID = restaurantID;
        updateRestaurantName(restaurantName);
        loadProducts();
    }

    /**
     * Update the restaurant name
     * @param restaurantName The new restaurant name
     */
    private void updateRestaurantName(String restaurantName) {
        restaurantNameText.setText(restaurantName);
    }

    /**
     * Load the products for the restaurant
     * and add them to the product list
     */
    private void loadProducts() {
        try {
            productListVBox.getChildren().clear();
            ResourceBundle bundle = LocalizationManager.getBundle();

            List<Product> products = productService.getProductsByRID(restaurantID);

            for (Product product : products) {
                HBox productBox = createProductEntry(product, bundle);
                productListVBox.getChildren().add(productBox);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a product entry for the product list
     * @param product The product to create the entry for
     * @param bundle The resource bundle
     * @return The product entry
     */
    private HBox createProductEntry(Product product, ResourceBundle bundle) {
        HBox productBox = new HBox();
        productBox.setSpacing(10.0);
        productBox.setStyle("-fx-padding: 5px; -fx-background-color: #e8f4fb; -fx-border-color: #000;");

        String nameLabelText = bundle.getString("ProductNameText") + ": " + product.getProductName();
        String descriptionLabelText = bundle.getString("ProductDescriptionText") + ": " + product.getProductDesc();
        String priceLabelText = bundle.getString("ProductPriceText") + ": " + String.format("%.2f", product.getProductPrice());
        String categoryLabelText = bundle.getString("ProductCategoryText") + ": " + product.getCategory();
        String statusLabelText = bundle.getString("ProductStatusText") + ": " + (product.isActive() ? "Yes" : "No");
        String editButtonText = bundle.getString("EditButton");
        String deleteButtonText = bundle.getString("DeleteButton");
        String deleteDialogTitle = bundle.getString("DeleteDialogTitle");
        String deleteDialogText = bundle.getString("DeleteDialogText");

        Text productNameText = new Text(nameLabelText);
        Text productDescriptionText = new Text(descriptionLabelText);
        Text productPriceText = new Text(priceLabelText);
        Text productCategoryText = new Text(categoryLabelText);
        Text productStatusText = new Text(statusLabelText);

        productBox.getChildren().addAll(productNameText, productDescriptionText, productPriceText, productCategoryText, productStatusText);

        Button editButton = new Button(editButtonText);
        editButton.setOnAction(event -> openEditDialog(product, restaurantID));
        productBox.getChildren().add(editButton);

        Button deleteButton = new Button(deleteButtonText);
        deleteButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(deleteDialogTitle);
            alert.setHeaderText(deleteDialogText);
            alert.setContentText(product.getProductName());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteProduct(product);
            }
        });
        productBox.getChildren().add(deleteButton);

        return productBox;
    }

    /**
     * Add a new product to the restaurant
     */
    @FXML
    private void addItem() {
        ResourceBundle bundle = LocalizationManager.getBundle();
        String dialogTitle = bundle.getString("AddItemDialogTitle");
        String dialogHeader = bundle.getString("AddItemDialogHeader");
        String productButton = bundle.getString("AddProductButton");
        String cancelButton = bundle.getString("CancelProduct");

        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);
        dialog.setHeaderText(dialogHeader);

        ButtonType addButtonType = new ButtonType(productButton, ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Translation tabs (English, Finnish, Japanese, Russian)
        TabPane tabPane = new TabPane();
        Tab englishTab = createTranslationTab("English", product.getTranslations());
        Tab finnishTab = createTranslationTab("Finnish", product.getTranslations());
        Tab japaneseTab = createTranslationTab("Japanese", product.getTranslations());
        Tab russianTab = createTranslationTab("Russian", product.getTranslations());
        tabPane.getTabs().addAll(englishTab, finnishTab, japaneseTab, russianTab);

        // Fields for price, category, picture, etc.
        TextField priceField = new TextField();
        TextField categoryField = new TextField();
        TextField pictureField = new TextField();

        CheckBox activeCheckbox = new CheckBox();
        grid.add(new Label(bundle.getString("DialogPrice")), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(new Label(bundle.getString("DialogCategory")), 0, 2);
        grid.add(categoryField, 1, 2);
        grid.add(new Label(bundle.getString("DialogPicture")), 0, 3);
        grid.add(pictureField, 1, 3);
        grid.add(new Label(bundle.getString("DialogActive")), 0, 4);
        grid.add(activeCheckbox, 1, 4);

        grid.add(tabPane, 0, 0, 2, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                List<Translation> translations = new ArrayList<>();
                translations.add(getTranslationFromTab(englishTab));
                translations.add(getTranslationFromTab(finnishTab));
                translations.add(getTranslationFromTab(japaneseTab));
                translations.add(getTranslationFromTab(russianTab));

                return new Product(
                        translations.get(0).getName(),  // Default to English name
                        Double.parseDouble(priceField.getText()),
                        translations.get(0).getDescription(), // Default to English description
                        null, // ProductID will be set later
                        pictureField.getText(),
                        categoryField.getText(),
                        activeCheckbox.isSelected(),
                        translations
                );
            }
            return null;
        });
        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(product -> {
            restaurantNameText.setId(String.valueOf(restaurantID));
            try {
                productService.addProductToRestaurant(product, restaurantID);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            loadProducts();
        });

    }

    private Tab createTranslationTab(String language, List<Translation> translations) {
        Tab tab = new Tab(language);
        GridPane tabGrid = new GridPane();
        tabGrid.setPadding(new Insets(10, 10, 10, 10));
        tabGrid.setHgap(10);
        tabGrid.setVgap(10);

        TextField nameField = new TextField();
        TextArea descField = new TextArea();
        descField.setWrapText(true);

        tabGrid.add(new Label(language + " Name"), 0, 0);
        tabGrid.add(nameField, 1, 0);
        tabGrid.add(new Label(language + " Description"), 0, 1);
        tabGrid.add(descField, 1, 1);

        tab.setContent(tabGrid);
        return tab;
    }

    private Translation getTranslationFromTab(Tab tab) {
        GridPane tabGrid = (GridPane) tab.getContent();
        TextField nameField = (TextField) tabGrid.getChildren().get(1);
        TextArea descField = (TextArea) tabGrid.getChildren().get(3);

        return new Translation(tab.getText(), nameField.getText(), descField.getText());
    }

    /**
     * Open the edit dialog for a product
     * @param product The product to edit
     */
    private void openEditDialog(Product product, int rid) {
        ResourceBundle bundle = LocalizationManager.getBundle();
        String dialogTitle = bundle.getString("EditItemDialog");
        String dialogHeader = bundle.getString("EditItemHeader");
        String saveButtonText = bundle.getString("UpdateButton");
        String cancelButtonText = bundle.getString("CancelEdit");

        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);
        dialog.setHeaderText(dialogHeader);

        ButtonType saveButtonType = new ButtonType(saveButtonText, ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType(cancelButtonText, ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Translation tabs (English, Finnish, Japanese, Russian)
        TabPane tabPane = new TabPane();
        Tab englishTab = createTranslationTab("English", product.getTranslations());
        Tab finnishTab = createTranslationTab("Finnish", product.getTranslations());
        Tab japaneseTab = createTranslationTab("Japanese", product.getTranslations());
        Tab russianTab = createTranslationTab("Russian", product.getTranslations());
        tabPane.getTabs().addAll(englishTab, finnishTab, japaneseTab, russianTab);

        // Fields for price, category, picture, etc.
        TextField priceField = new TextField(String.valueOf(product.getProductPrice()));
        TextField categoryField = new TextField(product.getCategory());
        TextField pictureField = new TextField(product.getPicture());

        CheckBox activeCheckbox = new CheckBox();
        activeCheckbox.setSelected(product.isActive());

        grid.add(new Label(bundle.getString("DialogPrice")), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(new Label(bundle.getString("DialogCategory")), 0, 2);
        grid.add(categoryField, 1, 2);
        grid.add(new Label(bundle.getString("DialogPicture")), 0, 3);
        grid.add(pictureField, 1, 3);
        grid.add(new Label(bundle.getString("DialogActive")), 0, 4);
        grid.add(activeCheckbox, 1, 4);

        grid.add(tabPane, 0, 0, 2, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                List<Translation> updatedTranslations = new ArrayList<>();
                updatedTranslations.add(getTranslationFromTab(englishTab));
                updatedTranslations.add(getTranslationFromTab(finnishTab));
                updatedTranslations.add(getTranslationFromTab(japaneseTab));
                updatedTranslations.add(getTranslationFromTab(russianTab));

                product.setProductName(updatedTranslations.get(0).getName());  // Default to English name
                product.setProductDesc(updatedTranslations.get(0).getDescription());  // Default to English description
                product.setProductPrice(Double.parseDouble(priceField.getText()));
                product.setCategory(categoryField.getText());
                product.setPicture(pictureField.getText());
                product.setActive(activeCheckbox.isSelected());
                product.setTranslations(updatedTranslations);

                return product;
            }
            return null;
        });

        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(updatedProduct -> {
            try {
                productService.updateProduct(updatedProduct, rid);  // Update product in service
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            loadProducts();  // Reload product list
        });
    }


    /**
     * Delete a product from the restaurant
     * @param product The product to delete
     */
    private void deleteProduct(Product product) {
        ResourceBundle bundle = LocalizationManager.getBundle();
        String deleteDialogTitle = bundle.getString("DeleteTitle");
        String deleteFail = bundle.getString("DeleteFail");
        try {
            productService.deleteProduct(product, restaurantID);
            loadProducts();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(deleteDialogTitle);
            alert.setHeaderText(deleteFail);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


}
