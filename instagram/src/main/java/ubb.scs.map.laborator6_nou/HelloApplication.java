package ubb.scs.map.laborator_6nou;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ubb.scs.map.laborator_6nou.controller.LoginView;
import ubb.scs.map.laborator_6nou.domain.validation.FriendshipValidation;
import ubb.scs.map.laborator_6nou.domain.validation.MessageValidation;
import ubb.scs.map.laborator_6nou.domain.validation.RequestValidation;
import ubb.scs.map.laborator_6nou.domain.validation.UserValidation;
import ubb.scs.map.laborator_6nou.repository.FriendshipDBRepository;
import ubb.scs.map.laborator_6nou.repository.FriendshipRequestDBRepository;
import ubb.scs.map.laborator_6nou.repository.MessageDBRepository;
import ubb.scs.map.laborator_6nou.repository.UserDBRepository;
import ubb.scs.map.laborator_6nou.repository.paging.UserDBPagingRepository;
import ubb.scs.map.laborator_6nou.service.FriendshipRequestService;
import ubb.scs.map.laborator_6nou.service.FriendshipService;
import ubb.scs.map.laborator_6nou.service.MessageService;
import ubb.scs.map.laborator_6nou.service.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    UserDBRepository userRepository;
    FriendshipDBRepository friendshipRepository;
    UserService userService;
    FriendshipService friendshipService;
    FriendshipRequestDBRepository friendshipRequestDBRepository;
    FriendshipRequestService friendshipRequestService;
    MessageService messageService;


    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        String username = "alexandrabontidean";
        String password = "alexandramiha";
        String url = "jdbc:postgresql://localhost:5432/map";
        userRepository = new UserDBPagingRepository(url, username, password, new UserValidation());
        friendshipRepository = new FriendshipDBRepository(url, username, password, new FriendshipValidation());
        userService = new UserService((UserDBPagingRepository) userRepository);
        friendshipService = new FriendshipService(friendshipRepository, (UserDBPagingRepository) userRepository);
        friendshipRequestDBRepository = new FriendshipRequestDBRepository(url, username, password, new RequestValidation());
        friendshipRequestService = new FriendshipRequestService(friendshipRequestDBRepository);

        MessageDBRepository messageDBRepository = new MessageDBRepository(url, username, password, new MessageValidation());
        messageService = new MessageService(messageDBRepository);


        initView(primaryStage);
        primaryStage.setWidth(600);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));
        primaryStage.setTitle("Instagram");
        //Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("org/example/socialnetworkfx/socialnetworkfx/images/yahoo.png")));
        //primaryStage.getIcons().add(icon);
        LoginView loginController = fxmlLoader.getController();
        loginController.setService(userService,friendshipService,friendshipRequestService, messageService);

    }


    public static void main(String[] args) {
        launch();
    }


}
