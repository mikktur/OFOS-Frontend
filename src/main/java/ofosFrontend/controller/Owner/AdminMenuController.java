package ofosFrontend.controller.Owner;

import javafx.fxml.FXML;
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

            var products = productService.getProductsByRID(restaurantID);

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
        deleteButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(bundle.getString("DeleteDialogTitle"));
            alert.setHeaderText(bundle.getString("DeleteDialogText"));
            alert.setContentText(product.getProductName());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteProduct(product);
            }
        });
        productBox.getChildren().add(deleteButton);

        return productBox;
    }

    @FXML
    private void addItem() {
        showProductDialog(new Product(), false);
    }

    private void openEditDialog(Product product) {
        showProductDialog(product, true);
    }

    private void showProductDialog(Product product, boolean isEdit) {
        ResourceBundle bundle = LocalizationManager.getBundle();
        String dialogTitle = isEdit ? bundle.getString("EditItemDialog") : bundle.getString("AddItemDialogTitle");

        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);

        ButtonType confirmButtonType = new ButtonType(
                isEdit ? bundle.getString("UpdateButton") : bundle.getString("AddProductButton"),
                ButtonBar.ButtonData.OK_DONE
        );
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField(product.getProductName());
        TextField descriptionField = new TextField(product.getProductDesc());
        TextField priceField = new TextField(String.valueOf(product.getProductPrice()));
        TextField categoryField = new TextField(product.getCategory());
        TextField pictureField = new TextField(product.getPicture());
        CheckBox activeCheckBox = new CheckBox(bundle.getString("ProductStatusText"));
        activeCheckBox.setSelected(product.isActive());

        // Translation fields
        TextField finnishField = new TextField(getTranslationText(product, "fi"));
        TextField japaneseField = new TextField(getTranslationText(product, "ja"));
        TextField russianField = new TextField(getTranslationText(product, "ru"));

        grid.add(new Label(bundle.getString("ProductNameText")), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label(bundle.getString("ProductDescriptionText")), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label(bundle.getString("ProductPriceText")), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label(bundle.getString("ProductCategoryText")), 0, 3);
        grid.add(categoryField, 1, 3);
        grid.add(new Label(bundle.getString("ProductPictureText")), 0, 4);
        grid.add(pictureField, 1, 4);
        grid.add(activeCheckBox, 1, 5);

        // Add translation fields
        grid.add(new Label("Finnish Translation"), 0, 6);
        grid.add(finnishField, 1, 6);
        grid.add(new Label("Japanese Translation"), 0, 7);
        grid.add(japaneseField, 1, 7);
        grid.add(new Label("Russian Translation"), 0, 8);
        grid.add(russianField, 1, 8);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                product.setProductName(nameField.getText());
                product.setProductDesc(descriptionField.getText());
                product.setProductPrice(Double.parseDouble(priceField.getText()));
                product.setCategory(categoryField.getText());
                product.setPicture(pictureField.getText());
                product.setActive(activeCheckBox.isSelected());

                // Set translations as a list
                List<Translation> translations = new ArrayList<>();
                translations.add(new Translation("fi", finnishField.getText()));
                translations.add(new Translation("ja", japaneseField.getText()));
                translations.add(new Translation("ru", russianField.getText()));
                product.setTranslations(translations);

                return product;
            }
            return null;
        });

        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(updatedProduct -> {
            try {
                if (isEdit) {
                    productService.updateProduct(updatedProduct);
                } else {
                    productService.addProductToRestaurant(updatedProduct, restaurantID);
                }
                loadProducts();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String getTranslationText(Product product, String language) {
        return product.getTranslations().stream()
                .filter(t -> t.getLanguageCode().equals(language))
                .map(Translation::getText)
                .findFirst()
                .orElse("");
    }

    private void deleteProduct(Product product) {
        try {
            productService.deleteProduct(product, restaurantID);
            loadProducts();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
