package ofosFrontend.controller.User;

public enum ViewPath {
    MAIN("/ofosFrontend/User/mainUI.fxml"),
    CHECKOUT("/ofosFrontend/User/checkout.fxml"),
    SETTINGS("/ofosFrontend/User/newUserSettingsUI.fxml"),
    RESTAURANT("/ofosFrontend/User/restaurantMenuUI.fxml"),
    ORDERHISTORY("/ofosFrontend/OrderHistoryUI.fxml"),
    ADMINDASHBOARD("/ofosFrontend/User/adminDashboardUI.fxml");

    private final String path;

    ViewPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
