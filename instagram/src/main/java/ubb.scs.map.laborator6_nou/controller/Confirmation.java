package ubb.scs.map.laborator_6nou.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import ubb.scs.map.laborator_6nou.service.UserService;
import javafx.scene.control.Label;


public class Confirmation {
    private UserService userService;
    private Long IDUser;
    private Stage stage;

    @FXML
    private Label confirmationLabel;

    public void setService(UserService userService, Long IDUser, Stage stage) {
        this.userService = userService;
        this.IDUser = IDUser;
        this.stage = stage;
    }

    @FXML
    public void handleYesButton(ActionEvent actionEvent) {
        userService.delete(IDUser);
        System.out.println("User deleted successfully!");

        stage.close();
    }

    @FXML
    public void handleNoButton(ActionEvent actionEvent) {
        stage.close();
    }
}
