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

public class AdminMenuController extends AdminBasicController {

    @FXML
    private VBox productListVBox;
    @FXML
    private ImageView adminLogout;
    @FXML
    private Text restaurantNameText;

    private final ProductService productService = new ProductService();
    private int restaurantID;

    @FXML
    public void initialize() {
        System.out.println("Initializing :)");
    }

    public void setRestaurantID(int restaurantID, String restaurantName) {
        this.restaurantID = restaurantID;
        updateRestaurantName(restaurantName);
        loadProducts();
    }

    private void updateRestaurantName(String restaurantName) {
        restaurantNameText.setText(restaurantName);
    }

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

    private HBox createProductEntry(Product product, ResourceBundle bundle) {
        HBox productBox = new HBox();
        productBox.setSpacing(10.0);
        productBox.setStyle("-fx-padding: 5px; -fx-background-color: #e8f4fb; -fx-border-color: #000;");

        Text productNameText = new Text(bundle.getString("ProductNameText") + ": " + product.getProductName());
        Text productDescriptionText = new Text(bundle.getString("ProductDescriptionText") + ": " + product.getProductDesc());
        Text productPriceText = new Text(bundle.getString("ProductPriceText") + ": " + String.format("%.2f", product.getProductPrice()));
        Text productCategoryText = new Text(bundle.getString("ProductCategoryText") + ": " + product.getCategory());
        Text productStatusText = new Text(bundle.getString("ProductStatusText") + ": " + (product.isActive() ? "Yes" : "No"));

        productBox.getChildren().addAll(productNameText, productDescriptionText, productPriceText, productCategoryText, productStatusText);

        Button editButton = new Button(bundle.getString("EditButton"));
        editButton.setOnAction(event -> openEditDialog(product));
        productBox.getChildren().add(editButton);

        Button deleteButton = new Button(bundle.getString("DeleteButton"));
        deleteButton.setOnAction(event -> confirmAndDeleteProduct(product, bundle));
        productBox.getChildren().add(deleteButton);

        return productBox;
    }

    @FXML
    private void addItem() {
        ResourceBundle bundle = LocalizationManager.getBundle();
        Dialog<Product> dialog = createProductDialog(bundle, null);

        Optional<Product> result = dialog.showAndWait();

        result.ifPresent(product -> {
            try {
                productService.addProductToRestaurant(product, restaurantID);
                loadProducts();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void openEditDialog(Product product) {
        ResourceBundle bundle = LocalizationManager.getBundle();
        Dialog<Product> dialog = createProductDialog(bundle, product);

        Optional<Product> result = dialog.showAndWait();

        result.ifPresent(updatedProduct -> {
            try {
                productService.updateProduct(updatedProduct);
                loadProducts();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Dialog<Product> createProductDialog(ResourceBundle bundle, Product product) {
        boolean isEditMode = product != null;

        String dialogTitle = isEditMode ? bundle.getString("EditItemDialog") : bundle.getString("AddItemDialogTitle");
        String dialogHeader = isEditMode ? bundle.getString("EditItemHeader") : bundle.getString("AddItemDialogHeader");
        String confirmButton = isEditMode ? bundle.getString("UpdateButton") : bundle.getString("AddProductButton");
        String cancelButton = bundle.getString("CancelProduct");
        String dialogName = bundle.getString("DialogName");
        String dialogDescription = bundle.getString("DialogDescription");
        String dialogPrice = bundle.getString("DialogPrice");
        String dialogCategory = bundle.getString("DialogCategory");
        String dialogPicture = bundle.getString("DialogPicture");
        String dialogActive = bundle.getString("DialogActive");

        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);
        dialog.setHeaderText(dialogHeader);

        ButtonType confirmButtonType = new ButtonType(confirmButton, ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, cancelButtonType);

        // Create dialog content
        GridPane productDetailsGrid = new GridPane();
        productDetailsGrid.setHgap(10);
        productDetailsGrid.setVgap(10);

        TextField priceField = new TextField(isEditMode ? String.valueOf(product.getProductPrice()) : "");
        TextField categoryField = new TextField(isEditMode ? product.getCategory() : "");
        TextField pictureField = new TextField(isEditMode ? product.getPicture() : "");
        CheckBox activeCheckBox = new CheckBox(dialogActive);
        if (isEditMode) activeCheckBox.setSelected(product.isActive());

        productDetailsGrid.add(new Label(dialogPrice), 0, 0);
        productDetailsGrid.add(priceField, 1, 0);
        productDetailsGrid.add(new Label(dialogCategory), 0, 1);
        productDetailsGrid.add(categoryField, 1, 1);
        productDetailsGrid.add(new Label(dialogPicture), 0, 2);
        productDetailsGrid.add(pictureField, 1, 2);
        productDetailsGrid.add(new Label(dialogActive), 0, 3);
        productDetailsGrid.add(activeCheckBox, 1, 3);

        // Create tabs for translations
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                createTranslationTab("English", isEditMode ? product.getTranslationForLanguage("English") : null, bundle),
                createTranslationTab("Finnish", isEditMode ? product.getTranslationForLanguage("Finnish") : null, bundle),
                createTranslationTab("Japanese", isEditMode ? product.getTranslationForLanguage("Japanese") : null, bundle),
                createTranslationTab("Russian", isEditMode ? product.getTranslationForLanguage("Russian") : null, bundle)
        );

        VBox dialogContent = new VBox(10, tabPane, productDetailsGrid);
        dialogContent.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(dialogContent);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                double price = Double.parseDouble(priceField.getText());
                String category = categoryField.getText();
                String picture = pictureField.getText();
                boolean active = activeCheckBox.isSelected();

                List<Translation> translations = extractTranslationsFromTabs(tabPane);

                if (isEditMode) {
                    product.setProductPrice(price);
                    product.setCategory(category);
                    product.setPicture(picture);
                    product.setActive(active);
                    product.setTranslations(translations);
                    return product;
                } else {
                    return new Product(null, price, null, null, picture, category, active, translations);
                }
            }
            return null;
        });

        return dialog;
    }


    private Tab createTranslationTab(String languageKey, Translation existingTranslation, ResourceBundle bundle) {
        VBox tabContent = new VBox(10);
        tabContent.setPadding(new Insets(10));

        String nameLabel = bundle.getString(languageKey + "Name");
        String descriptionLabel = bundle.getString(languageKey + "Description");

        TextField nameField = new TextField(existingTranslation != null ? existingTranslation.getName() : "");
        TextField descriptionField = new TextField(existingTranslation != null ? existingTranslation.getDescription() : "");

        tabContent.getChildren().addAll(
                new Label(nameLabel), nameField,
                new Label(descriptionLabel), descriptionField
        );

        Tab tab = new Tab(languageKey);
        tab.setContent(tabContent);
        return tab;
    }


    private List<Translation> extractTranslationsFromTabs(TabPane tabPane) {
        List<Translation> translations = new ArrayList<>();

        for (Tab tab : tabPane.getTabs()) {
            VBox content = (VBox) tab.getContent();
            TextField nameField = (TextField) content.getChildren().get(1);
            TextField descriptionField = (TextField) content.getChildren().get(3);

            translations.add(new Translation(tab.getText(), nameField.getText(), descriptionField.getText()));
        }

        return translations;
    }

    private void confirmAndDeleteProduct(Product product, ResourceBundle bundle) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("DeleteDialogTitle"));
        alert.setHeaderText(bundle.getString("DeleteDialogText"));
        alert.setContentText(product.getProductName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                productService.deleteProduct(product, restaurantID);
                loadProducts();
            } catch (IOException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle(bundle.getString("DeleteTitle"));
                errorAlert.setContentText(bundle.getString("DeleteFailed"));
                errorAlert.showAndWait();
            }
        }
    }
}
