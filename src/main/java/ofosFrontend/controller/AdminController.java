package ofosFrontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {
    @FXML
    private Button backToItemsMenu;
    @FXML
    private Button foodItemEdit;
    @FXML
    public Text backToAdminMain;
    @FXML
    private Button editMenu;  // Corresponds to "editMenu" Button in FXML
    @FXML
    private ImageView adminLogout;  // Corresponds to "adminLogout" ImageView in FXML

    // The following methods correspond to the buttons/actions in the FXML file

    @FXML
    public void saveAddress(ActionEvent event) {
        System.out.println("Address saved");
    }

    @FXML
    public void savePhone(ActionEvent event) {
        System.out.println("Phone number saved");
    }

    @FXML
    public void saveHours(ActionEvent event) {
        System.out.println("Business hours saved");
    }

    @FXML
    public void goToAdminMenu(ActionEvent event) throws IOException {
        // Navigate to the admin food menu UI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/adminFoodMenuUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) editMenu.getScene().getWindow();  // Use the current stage

        Scene adminMenuScene = new Scene(root, 650, 400);
        currentStage.setTitle("OFOS Admin");
        currentStage.setScene(adminMenuScene);
        currentStage.show();
    }

    @FXML
    public void logOut(MouseEvent mouseEvent) throws IOException {
        // Navigate to the login UI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/loginUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) adminLogout.getScene().getWindow();  // Use the current stage

        Scene backToLoginScene = new Scene(root, 650, 400);
        currentStage.setTitle("OFOS Login");
        currentStage.setScene(backToLoginScene);
        currentStage.show();
    }

    @FXML
    public void editItem(ActionEvent event) throws IOException {
        // Navigate to the edit food item UI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/editFoodUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) foodItemEdit.getScene().getWindow();  // Use the current stage

        Scene editFoodScene = new Scene(root, 650, 400);
        currentStage.setTitle("OFOS Admin");
        currentStage.setScene(editFoodScene);
        currentStage.show();
    }

    @FXML
    public void addItem(ActionEvent event) {
        System.out.println("Adding items not implemented yet");
    }

    @FXML
    public void foodChanges(ActionEvent event) {
        System.out.println("Saving food changes not implemented yet");
    }

    public void returnToAMain(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/adminMainUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) backToAdminMain.getScene().getWindow();  // Use the current stage

        Scene adminScene = new Scene(root, 650, 400);
        currentStage.setTitle("OFOS Admin");
        currentStage.setScene(adminScene);
        currentStage.show();
    }
    public void backToItems(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/adminFoodMenuUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) backToItemsMenu.getScene().getWindow();  // Use the current stage

        Scene backToItemsScene = new Scene(root, 650, 400);
        currentStage.setTitle("OFOS Admin");
        currentStage.setScene(backToItemsScene);
        currentStage.show();
    }
}
