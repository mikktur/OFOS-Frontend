package ofosFrontend.controller.Owner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ofosFrontend.model.Product;
import ofosFrontend.model.Restaurant;
import ofosFrontend.service.ProductService;
import ofosFrontend.session.LocalizationManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class AdminMenuController  extends AdminBasicController {

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

        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);
        dialog.setHeaderText(dialogHeader);

        ButtonType addButtonType = new ButtonType(productButton, ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        //tästä eteenpäin lokalisaatio ei tehty
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");

        TextField pictureField = new TextField();
        pictureField.setPromptText("Picture URL");

        CheckBox activeCheckBox = new CheckBox("Active");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Category:"), 0, 3);
        grid.add(categoryField, 1, 3);
        grid.add(new Label("Picture URL:"), 0, 4);
        grid.add(pictureField, 1, 4);
        grid.add(new Label("Active:"), 0, 5);
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
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Edit Product");
        dialog.setHeaderText("Edit the product details");

        ButtonType updateButtonType = new ButtonType("Update Product", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField(product.getProductName());
        TextField descriptionField = new TextField(product.getProductDesc());
        TextField priceField = new TextField(String.valueOf(product.getProductPrice()));
        TextField categoryField = new TextField(product.getCategory());
        TextField pictureField = new TextField(product.getPicture());
        CheckBox activeCheckBox = new CheckBox("Active");
        activeCheckBox.setSelected(product.isActive());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Category:"), 0, 3);
        grid.add(categoryField, 1, 3);
        grid.add(new Label("Picture URL:"), 0, 4);
        grid.add(pictureField, 1, 4);
        grid.add(new Label("Active:"), 0, 5);
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
        try {
            productService.deleteProduct(product);
            loadProducts();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to delete the product.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

}
