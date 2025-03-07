package ubb.scs.map.laborator_6nou.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ubb.scs.map.laborator_6nou.domain.User;
import ubb.scs.map.laborator_6nou.domain.validation.UserValidation;
import ubb.scs.map.laborator_6nou.domain.validation.ValidationException;
import ubb.scs.map.laborator_6nou.service.UserService;

public class AccountSettings {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorMessageLabel;  // Error message label

    private UserService userService;
    private Long userId;
    private UserValidation userValidation;

    public void setService(UserService userService, Long userId) {
        this.userService = userService;
        this.userId = userId;
        this.userValidation = new UserValidation();

        User user = userService.findOne(userId);
        if (user != null) {
            firstNameField.setText(user.getFirstName());
            lastNameField.setText(user.getLastName());
            emailField.setText(user.getEmail());
        }
    }

    public void handleSave(ActionEvent actionEvent) {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        try {
            userValidation.validatePassword(newPassword);
            if (!newPassword.equals(confirmPassword)) {
                errorMessageLabel.setText("Passwords do not match!");
                return;
            }
            userService.updatePassword(userId, newPassword);
            System.out.println("Password updated successfully!");
            ((javafx.stage.Stage) newPasswordField.getScene().getWindow()).close();
        } catch (ValidationException e) {
            errorMessageLabel.setText("Error: " + e.getMessage());
        }
    }

    public void handleCancel(ActionEvent actionEvent) {
        ((javafx.stage.Stage) firstNameField.getScene().getWindow()).close();
    }
}