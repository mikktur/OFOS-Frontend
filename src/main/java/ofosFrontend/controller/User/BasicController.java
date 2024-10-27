package ofosFrontend.controller.User;


//pretty useless class, didn't work as i thought it would
public class BasicController {
    UserMainController mainController;

    public void setMainController(UserMainController mainController) {

        this.mainController = mainController;
    }

    public void goToMain() {
        mainController.loadDefaultContent();
    }




}
