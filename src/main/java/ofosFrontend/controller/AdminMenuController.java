package ofosFrontend.controller;

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

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AdminMenuController {

    @FXML
    private VBox productListVBox;
    @FXML
    private ImageView adminLogout;
    @FXML
    private Text restaurantNameText;

    private ProductService productService = new ProductService();
    private int restaurantID = 1;

    @FXML
    public void initialize() {

    }
    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
        loadProducts();  // Now call loadProducts since restaurantID is set
    }
    private void loadProducts() {
        try {
            productListVBox.getChildren().clear();

            List<Product> products = productService.getProductsByRID(restaurantID);

            for (Product product : products) {
                if (product.isActive()) {
                    HBox productBox = createProductEntry(product);
                    productListVBox.getChildren().add(productBox);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HBox createProductEntry(Product product) {
        HBox productBox = new HBox();
        productBox.setSpacing(10.0);
        productBox.setStyle("-fx-padding: 5px; -fx-background-color: #e8f4fb; -fx-border-color: #000;");

        Text productNameText = new Text("Name: " + product.getProductName());
        Text productDescriptionText = new Text("Description: " + product.getProductDesc());
        Text productPriceText = new Text("Price: $" + String.format("%.2f", product.getProductPrice()));
        Text productCategoryText = new Text("Category: " + product.getCategory());
        Text productStatusText = new Text("Active: " + (product.isActive() ? "Yes" : "No"));

        productBox.getChildren().addAll(productNameText, productDescriptionText, productPriceText, productCategoryText, productStatusText);

        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> openEditDialog(product));
        productBox.getChildren().add(editButton);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Product");
            alert.setHeaderText("Are you sure you want to delete the product?");
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
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add New Product");
        dialog.setHeaderText("Enter the product details");

        ButtonType addButtonType = new ButtonType("Add Product", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

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

    @FXML
    public void logOut(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/loginUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) adminLogout.getScene().getWindow();
        Scene backToLoginScene = new Scene(root, 650, 400);
        currentStage.setTitle("OFOS Login");
        currentStage.setScene(backToLoginScene);
        currentStage.show();
    }
    public void setRId(int restaurantID) {
        this.restaurantID = restaurantID;
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
