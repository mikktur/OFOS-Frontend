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
import ofosFrontend.service.ProductService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AdminMenuController {

    @FXML
    private VBox productListVBox;  // VBox in the FXML where products will be displayed
    @FXML
    private ImageView adminLogout;
    @FXML
    private Text restaurantNameText;
    private ProductService productService = new ProductService();  // Use ProductService to handle products
    private int restaurantId = 1;  // For simplicity, assuming restaurant ID is 1. Replace with dynamic value.



    @FXML
    public void initialize() {
        loadProducts();  // Load products when the UI initializes
    }
    public void updateRestaurantName(String restaurantName) {
        restaurantNameText.setText(restaurantName);  // Update the Text node with the restaurant name
    }

    // Load products dynamically into the VBox
    private void loadProducts() {
        try {
            productListVBox.getChildren().clear();  // Clear existing product entries in the VBox

            // Fetch products by restaurant ID using ProductService
            List<Product> products = productService.getProductsByRID(restaurantId);

            // Iterate through the list of products and add them to the VBox
            for (Product product : products) {
                HBox productBox = createProductEntry(product);
                productListVBox.getChildren().add(productBox);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to create an HBox for each product
    private HBox createProductEntry(Product product) {
        HBox productBox = new HBox();
        productBox.setSpacing(10.0);
        productBox.setStyle("-fx-padding: 5px; -fx-background-color: #e8f4fb; -fx-border-color: #000;");

        // Create Text nodes for product details
        Text productNameText = new Text("Name: " + product.getProductName());
        Text productDescriptionText = new Text("Description: " + product.getProductDesc());
        Text productPriceText = new Text("Price: $" + String.format("%.2f", product.getProductPrice()));
        Text productCategoryText = new Text("Category: " + product.getCategory());
        Text productStatusText = new Text("Active: " + (product.isActive() ? "Yes" : "No"));

        // Add the Text nodes to the HBox
        productBox.getChildren().addAll(productNameText, productDescriptionText, productPriceText, productCategoryText, productStatusText);

        // Add an "Edit" button for each product
        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> openEditDialog(product));  // Open edit dialog on click
        productBox.getChildren().add(editButton);

        return productBox;
    }

    // Logic for adding a new product
    @FXML
    private void addItem() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add New Product");
        dialog.setHeaderText("Enter the product details");

        // Create buttons for the dialog
        ButtonType addButtonType = new ButtonType("Add Product", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the fields for product name, description, price, category, and status
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

        // Handle the dialog result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                // Validate and create the product object
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

        // Show the dialog and get the result
        Optional<Product> result = dialog.showAndWait();

        result.ifPresent(product -> {
            // Add product to the database (You will need to implement this in ProductService)
            try {
                productService.addProductToRestaurant(product, restaurantId);  // Pass the product and restaurant ID
                loadProducts();  // Update the UI with the newly added product
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Open a dialog to edit a product
    private void openEditDialog(Product product) {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Edit Product");
        dialog.setHeaderText("Edit the product details");

        // Create buttons for the dialog
        ButtonType updateButtonType = new ButtonType("Update Product", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Pre-fill the fields with existing product data
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

        // Handle the dialog result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                // Update the product with the new values
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

        // Show the dialog and get the result
        Optional<Product> result = dialog.showAndWait();

        result.ifPresent(updatedProduct -> {
            try {
                productService.updateProduct(updatedProduct);  // Call service to update the product in the backend
                loadProducts();  // Refresh the product list
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
}
