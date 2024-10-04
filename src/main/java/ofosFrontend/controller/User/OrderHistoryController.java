package ofosFrontend.controller.User;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;

public class OrderHistoryController {
    @FXML
    public GridPane dynamicGridPane;

    @FXML
    public void initialize() {
        int rows = 5;

        for (int row = 0; row < rows; row++) {
            Label dateLabel = new Label(LocalDate.now().minusDays(row).toString());
            GridPane.setHalignment(dateLabel, HPos.CENTER);
            GridPane.setValignment(dateLabel, VPos.CENTER);
            dynamicGridPane.add(dateLabel, 0, row);

            Label restaurantLabel = new Label("Restaurant " + row);
            GridPane.setHalignment(restaurantLabel, HPos.CENTER);
            GridPane.setValignment(restaurantLabel, VPos.CENTER);
            dynamicGridPane.add(restaurantLabel, 1, row);

            Label productsLabel = new Label("Product " + row);
            GridPane.setHalignment(productsLabel, HPos.CENTER);
            GridPane.setValignment(productsLabel, VPos.CENTER);
            dynamicGridPane.add(productsLabel, 2, row);

            Label quantityLabel = new Label("Quantity " + row);
            GridPane.setHalignment(quantityLabel, HPos.CENTER);
            GridPane.setValignment(quantityLabel, VPos.CENTER);
            dynamicGridPane.add(quantityLabel, 3, row);

        }
    }

}
