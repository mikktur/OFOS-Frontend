package ofosFrontend.controller.User;


//pretty useless class, didn't work as i thought it would

/**
 * Basic controller class for the user interface.
 * Contains a method to navigate back to the main view.
 */
public class BasicController {
    UserMainController mainController;

    public void setMainController(UserMainController mainController) {

        this.mainController = mainController;
    }

    public void goToMain() {
        mainController.loadDefaultContent();
    }




}
