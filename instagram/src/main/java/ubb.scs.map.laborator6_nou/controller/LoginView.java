package ubb.scs.map.laborator_6nou.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ubb.scs.map.laborator_6nou.domain.User;
import ubb.scs.map.laborator_6nou.domain.validation.MessageValidation;
import ubb.scs.map.laborator_6nou.repository.MessageDBRepository;
import ubb.scs.map.laborator_6nou.service.FriendshipRequestService;
import ubb.scs.map.laborator_6nou.service.FriendshipService;
import ubb.scs.map.laborator_6nou.service.MessageService;
import ubb.scs.map.laborator_6nou.service.UserService;

import java.io.IOException;

public class LoginView {
    @FXML
    public TextField emailText;
    @FXML
    public Button loginButton;
    @FXML
    public Button registerButton;
    @FXML
    public PasswordField passwordText;
    @FXML
    public Label errorMessage;
    @FXML
    public ImageView yahooLogo;

    private UserService userService;
    private FriendshipService friendshipService;
    private FriendshipRequestService friendshipRequestService;
    private MessageService messageService;
    private MainMenuView mainMenuView;


    public void handleRegisterButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ubb/scs/map/laborator_6nou/register-view.fxml"));

        AnchorPane root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Register");

        RegisterView registerView = loader.getController();
        registerView.setService(userService, stage);

        stage.show();
    }

    public void setService(UserService userService, FriendshipService friendshipService, FriendshipRequestService friendshipRequestService, MessageService messageService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.friendshipRequestService = friendshipRequestService;
        this.messageService = messageService;
    }

    public void handleLoginButton(ActionEvent actionEvent) throws IOException {
        String email = emailText.getText();
        String password = passwordText.getText();

        User loggedIn = userService.findByEmail(email, password);

        if (loggedIn != null && loggedIn.getID() != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ubb/scs/map/laborator_6nou/main-menu-view.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Main Menu");

            MainMenuView mainMenuView = loader.getController();
            mainMenuView.setService(loggedIn.getID(), userService, friendshipService, friendshipRequestService, messageService, stage);

            errorMessage.setVisible(false);
            stage.show();
        } else {
            emailText.clear();
            passwordText.clear();
            errorMessage.setText("Invalid email or password");
            errorMessage.setVisible(true);
        }
    }


}