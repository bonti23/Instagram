package ubb.scs.map.laborator_6nou.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ubb.scs.map.laborator_6nou.domain.User;
import ubb.scs.map.laborator_6nou.domain.validation.UserValidation;
import ubb.scs.map.laborator_6nou.domain.validation.ValidationException;
import ubb.scs.map.laborator_6nou.service.UserService;

import java.io.IOException;
import java.util.Optional;

public class RegisterView {

    private UserService service;
    private Stage stage;
    private UserValidation userValidation;
    @FXML
    public TextField firstnameText;
    @FXML
    public TextField lastnameText;
    @FXML
    public Button registerButton;
    @FXML
    public PasswordField passwordText;
    @FXML
    public TextField emailText;
    @FXML
    private Label errorMessageLabel;

    public void setService(UserService service, Stage stage) {
        this.service = service;
        this.stage = stage;
        //this.userValidation = new UserValidation();
    }

    public void handleRegisterButton(ActionEvent actionEvent) {
        String firstname = firstnameText.getText();
        String lastname = lastnameText.getText();
        String password = passwordText.getText();
        String email = emailText.getText();
        System.out.println(firstname + " " + lastname + " " + password+ " " + email);
        try{
            this.service.save(firstname, lastname, email,password);
            firstnameText.clear();
            lastnameText.clear();
            passwordText.clear();
            emailText.clear();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error!");
            stage.close();
        }
    }
}
/*
package ubb.scs.map.laborator_6nou.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ubb.scs.map.laborator_6nou.domain.User;
import ubb.scs.map.laborator_6nou.service.UserService;
import ubb.scs.map.laborator_6nou.domain.validation.UserValidation;
import ubb.scs.map.laborator_6nou.domain.validation.ValidationException;

import java.util.Optional;

public class RegisterView {

    private UserService service;
    private Stage stage;
    private UserValidation userValidation;
    @FXML
    public TextField firstnameText;
    @FXML
    public TextField lastnameText;
    @FXML
    public Button registerButton;
    @FXML
    public PasswordField passwordText;
    @FXML
    public TextField emailText;
    @FXML
    private Label errorMessageLabel;

    public void setService(UserService service, Stage stage) {
        this.service = service;
        this.stage = stage;
        this.userValidation = new UserValidation();
    }

    public void handleRegisterButton(ActionEvent actionEvent) {
        String firstname = firstnameText.getText();
        String lastname = lastnameText.getText();
        String password = passwordText.getText();
        String email = emailText.getText();
        System.out.println(firstname + " " + lastname + " " + password+ " " + email);
        try{
            userValidation.validatePassword(firstname);
            userValidation.validateEmail(lastname);
            userValidation.validatePassword(password);
            userValidation.validateEmail(email);
            this.service.save(firstname, lastname, email,password);
            firstnameText.clear();
            lastnameText.clear();
            passwordText.clear();
            emailText.clear();
        }catch (ValidationException e) {
            errorMessageLabel.setText("Error: " + e.getMessage());
        }
        stage.close();
    }
}

 */