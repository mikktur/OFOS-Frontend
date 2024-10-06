package ofosFrontend.controller.User;


import javafx.scene.Node;

public class BasicController {
    UserMainController mainController;

    public void setMainController(UserMainController mainController) {
        this.mainController = mainController;
    }

    public void setCenterContent(Node content) {
        mainController.setCenterContent(content);
    }

    public void resetToDefaultCartView() {
        mainController.resetToDefaultCartView();
    }





}
