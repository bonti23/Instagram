package ubb.scs.map.laborator_6nou.controller;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ubb.scs.map.laborator_6nou.domain.Friendship;
import ubb.scs.map.laborator_6nou.domain.FriendshipRequest;
import ubb.scs.map.laborator_6nou.domain.User;
import ubb.scs.map.laborator_6nou.domain.event.FriendshipEntityChange;
import ubb.scs.map.laborator_6nou.service.FriendshipRequestService;
import ubb.scs.map.laborator_6nou.service.FriendshipService;
import ubb.scs.map.laborator_6nou.service.MessageService;
import ubb.scs.map.laborator_6nou.service.UserService;
import ubb.scs.map.laborator_6nou.utils.Observer;
import javafx.scene.control.ComboBox;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainMenuView implements Observer<FriendshipEntityChange> {
    @FXML
    public TableColumn<User, String> firstnameColumn;
    @FXML
    public TableView<User> tableView;
    @FXML
    public TableColumn<User, String> lastnameColumn;
    @FXML
    public Button sendRequestButton;
    @FXML
    public Button acceptRequestButton;
    @FXML
    public TableColumn<User, String> sinceColumn;
    @FXML
    public Button removeFriendButton;
    @FXML
    public Button removeUser;
    @FXML
    public Button accountSettingsButton;
    @FXML
    public Label userNameField;
    @FXML
    public ComboBox<Integer> userLimitComboBox;
    @FXML
    public Button nextButton;
    @FXML
    public Button previousButton;
    @FXML
    public Label pageInfoLabel;

    private ObservableList<User> usersList = FXCollections.observableArrayList();


    private Long IDUser;
    private UserService userService;
    private FriendshipService friendshipService;
    private FriendshipRequestService requestService;
    private Stage stage;
    private MessageService messageService;
    ObservableList<User> model = FXCollections.observableArrayList();

    private int pageNumber = 0;
    private int pageSize = 5;

    public void setService(Long IDUser, UserService userService, FriendshipService friendshipService, FriendshipRequestService requestService, MessageService messageService, Stage stage) {
        this.IDUser = IDUser;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.requestService = requestService;
        this.messageService = messageService;
        this.stage = stage;
        friendshipService.addObserver(this);
        initModel();
    }
    public void initialize() {
        userLimitComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        userLimitComboBox.setValue(5);
        userLimitComboBox.setOnAction(event -> updateTableViewByLimit());
        firstnameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastnameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        sinceColumn.setCellValueFactory(cellData -> {
            Long senderId = cellData.getValue().getID();
            Friendship sender = friendshipService.findOne(senderId, IDUser);
            return new SimpleStringProperty(sender != null ? sender.getDatesince().toString() : "Unknown");
        });

        lastnameColumn.setSortable(true);

        tableView.getSortOrder().add(lastnameColumn);

        tableView.setItems(model);
        nextButton.setOnAction(this::onNextPage);
        previousButton.setOnAction(this::onPreviousPage);
    }


    private void initModel() {
        int totalFriends = (int) friendshipService.countFriends(IDUser);

        if (pageNumber * pageSize >= totalFriends) {
            pageNumber = Math.max(0, (totalFriends - 1) / pageSize);
        }
        int limit = userLimitComboBox.getValue();
        Iterable<User> allUsers = friendshipService.getFriends(IDUser);

        List<User> sortedAndLimitedUsers = StreamSupport.stream(allUsers.spliterator(), false)
                .sorted((u1, u2) -> u1.getLastName().compareToIgnoreCase(u2.getLastName()))
                .skip(pageNumber*pageSize)
                .limit(limit)
                .collect(Collectors.toList());

        model.setAll(sortedAndLimitedUsers);
        setUser(IDUser);
        updatePageNavigation();

    }

    private void updatePageNavigation() {
        int totalFriends = (int) friendshipService.countFriends(IDUser);
        int totalPages = (int) Math.ceil((double) totalFriends / pageSize);
        pageInfoLabel.setText(totalFriends > 0
                ? "Page " + (pageNumber + 1) + " of " + totalPages
                : "No friends found");

        previousButton.setDisable(pageNumber <= 0 || totalFriends == 0);
        nextButton.setDisable(pageNumber >= totalPages - 1 || totalFriends == 0);
    }

    public void onNextPage(ActionEvent actionEvent) {
        if (pageNumber < (int) Math.ceil((double) friendshipService.countFriends(IDUser) / pageSize) - 1) {
            pageNumber++;
            initModel();
        }
    }

    public void onPreviousPage(ActionEvent actionEvent) {
        if (pageNumber > 0) {
            pageNumber--;
            initModel();
        }
    }

    public void updateTableViewByLimit() {
        pageSize = userLimitComboBox.getValue();
        int totalFriends = (int) friendshipService.countFriends(IDUser);
        pageNumber = Math.min(pageNumber, Math.max(0, (totalFriends - 1) / pageSize));
        initModel();
    }

    public void onItemsPerPageChanged(ActionEvent actionEvent) {
        pageSize = userLimitComboBox.getValue();
        int totalFriends = (int) friendshipService.countFriends(IDUser);
        pageNumber = Math.min(pageNumber, Math.max(0, (totalFriends - 1) / pageSize));
        initModel();
    }

    public void setUser(Long IDUser) {
        User user = userService.findOne(IDUser);
        String fullName=user.getFirstName()+" "+user.getLastName();
        userNameField.setText(fullName);
    }

    public void handleSendRequest(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ubb/scs/map/laborator_6nou/request-view.fxml"));

        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        Stage stage2 = new Stage();
        stage2.setScene(scene);
        stage2.setTitle("Instagram");

        RequestView requestView = loader.getController();
        requestView.setService(requestService,friendshipService, userService,IDUser);
        stage2.show();
    }

    public void handleAcceptRequest(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ubb/scs/map/laborator_6nou/accept-request.fxml"));

        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        Stage stage2 = new Stage();
        stage2.setScene(scene);
        stage2.setTitle("Instagram");

        AcceptRequest requestView = loader.getController();
        requestView.setService(requestService,userService,friendshipService,IDUser);
        stage2.show();

    }

    @Override
    public void update(FriendshipEntityChange friendshipEntityChange) {
        initModel();
    }

    public void handleRemoveFriend(ActionEvent actionEvent) {
        User request = (User) tableView.getSelectionModel().getSelectedItem();
        System.out.println(request.getID());
        Friendship friendship = friendshipService.findOne(request.getID(), IDUser);
        System.out.println(friendship.getID());
        FriendshipRequest friendshipRequest=requestService.findByIDs(IDUser,request.getID());
        System.out.println(friendshipRequest.getID());
        requestService.delete(friendshipRequest.getID());
        friendshipService.delete(friendship.getID());

    }

    public void handleRemoveUser(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ubb/scs/map/laborator_6nou/confirmation-view.fxml"));
            AnchorPane root = loader.load();


            Stage stage2 = new Stage();
            stage2.setScene(new Scene(root));
            stage2.setTitle("Confirmation");

            Confirmation confirmationController = loader.getController();
            confirmationController.setService(userService, IDUser, stage2);

            stage2.show();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading the Account Settings view.");
        }
    }


    public void handleAccountSetting(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ubb/scs/map/laborator_6nou/account-settings-view.fxml"));

            AnchorPane root = loader.load();

            Scene scene = new Scene(root);
            Stage settingsStage = new Stage();
            settingsStage.setScene(scene);
            settingsStage.setTitle("Account Settings");

            AccountSettings settingsController = loader.getController();
            settingsController.setService(userService, IDUser);

            settingsStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading the Account Settings view.");
        }
    }
    public void handleChatButtonClick(ActionEvent actionEvent) throws IOException {
        User selectedUser = (User) tableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            Long IdTo = selectedUser.getID();
            String receiverName = selectedUser.getFirstName() + " " + selectedUser.getLastName();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ubb/scs/map/laborator_6nou/chat-view.fxml"));

            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            Stage chatStage = new Stage();
            chatStage.setScene(scene);
            chatStage.setTitle("Chat");

            ChatView chatView = loader.getController();
            chatView.setService(messageService, userService, IdTo, IDUser, receiverName);

            chatStage.show();
        } else {
            System.out.println("No user selected.");
        }
    }


}