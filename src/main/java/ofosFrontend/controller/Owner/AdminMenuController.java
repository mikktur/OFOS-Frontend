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
import ofosFrontend.service.ProductService;
import ofosFrontend.session.LocalizationManager;

import java.io.IOException;
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

    private ProductService productService = new ProductService();
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

        String nameLabelText = bundle.getString("ProductNameText") + ": " + product.getProductName();
        String descriptionLabelText = bundle.getString("ProductDescriptionText") + ": " + product.getProductDesc();
        String priceLabelText = bundle.getString("ProductPriceText") + ": $" + String.format("%.2f", product.getProductPrice());
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
        editButton.setOnAction(event -> openEditDialog(product));
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

    @FXML
    private void addItem() {
        ResourceBundle bundle = LocalizationManager.getBundle();
        String dialogTitle = bundle.getString("AddItemDialogTitle");
        String dialogHeader = bundle.getString("AddItemDialogHeader");
        String productButton = bundle.getString("AddProductButton");
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

        ButtonType addButtonType = new ButtonType(productButton, ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE); // Localized cancel button
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField nameField = new TextField();
        nameField.setPromptText(dialogName);

        TextField descriptionField = new TextField();
        descriptionField.setPromptText(dialogDescription);

        TextField priceField = new TextField();
        priceField.setPromptText(dialogPrice);

        TextField categoryField = new TextField();
        categoryField.setPromptText(dialogCategory);

        TextField pictureField = new TextField();
        pictureField.setPromptText(dialogPicture);

        CheckBox activeCheckBox = new CheckBox(dialogActive);

        grid.add(new Label(dialogName), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label(dialogDescription), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label(dialogPrice), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label(dialogCategory), 0, 3);
        grid.add(categoryField, 1, 3);
        grid.add(new Label(dialogPicture), 0, 4);
        grid.add(pictureField, 1, 4);
        grid.add(new Label(dialogActive), 0, 5);
        grid.add(activeCheckBox, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String name = nameField.getText();
                String description = descriptionField.getText();
                double price = Double.parseDouble(priceField.getText());
                String category = categoryField.getText();
                String picture = pictureField.getText();
                boolean active = activeCheckBox.isSelected();

                return new Product(name, price, description, null, picture, category, active);
            }
            return null;
        });

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
        String dialogTitle = bundle.getString("EditItemDialog");
        String dialogHeader = bundle.getString("EditItemHeader");
        String updateButton = bundle.getString("UpdateButton");
        String cancelButton = bundle.getString("CancelEdit");
        String editActive = bundle.getString("EditActive");
        String editName = bundle.getString("EditName");
        String editDescription = bundle.getString("EditDescription");
        String editPrice = bundle.getString("EditPrice");
        String editCategory = bundle.getString("EditCategory");
        String editPicture = bundle.getString("EditPicture");

        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);
        dialog.setHeaderText(dialogHeader);

        ButtonType updateButtonType = new ButtonType(updateButton, ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE); // Localized cancel button
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField(product.getProductName());
        TextField descriptionField = new TextField(product.getProductDesc());
        TextField priceField = new TextField(String.valueOf(product.getProductPrice()));
        TextField categoryField = new TextField(product.getCategory());
        TextField pictureField = new TextField(product.getPicture());
        CheckBox activeCheckBox = new CheckBox(editActive);
        activeCheckBox.setSelected(product.isActive());

        grid.add(new Label(editName), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label(editDescription), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label(editPrice), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label(editCategory), 0, 3);
        grid.add(categoryField, 1, 3);
        grid.add(new Label(editPicture), 0, 4);
        grid.add(pictureField, 1, 4);
        grid.add(new Label(editActive), 0, 5);
        grid.add(activeCheckBox, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                product.setProductName(nameField.getText());
                product.setProductDesc(descriptionField.getText());
                product.setProductPrice(Double.parseDouble(priceField.getText()));
                product.setCategory(categoryField.getText());
                product.setPicture(pictureField.getText());
                product.setActive(activeCheckBox.isSelected());

                return product;
            }
            return null;
        });

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
