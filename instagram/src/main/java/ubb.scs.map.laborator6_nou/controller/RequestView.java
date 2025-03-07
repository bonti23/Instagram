package ubb.scs.map.laborator_6nou.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ubb.scs.map.laborator_6nou.domain.User;
import ubb.scs.map.laborator_6nou.repository.FriendshipRequestDBRepository;
import ubb.scs.map.laborator_6nou.service.FriendshipRequestService;
import ubb.scs.map.laborator_6nou.service.FriendshipService;
import ubb.scs.map.laborator_6nou.service.UserService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RequestView {
    @FXML
    public TextField searchBar;

    @FXML
    public Button searchButton;

    @FXML
    public TableView<User> searchResults;

    @FXML
    public TableColumn<User, String> firstNameColumn;

    @FXML
    public TableColumn<User, String> lastNameColumn;

    @FXML
    public Button sendRequestButton;

    @FXML
    public Label statusLabel;

    private FriendshipRequestService friendshipRequestService;
    private UserService userService;
    private Long ID;
    private ObservableList<User> model = FXCollections.observableArrayList();
    private FriendshipService friendshipService;

    public void setService(FriendshipRequestService friendshipRequestService, FriendshipService friendshipService, UserService userService,Long ID){
        this.friendshipRequestService = friendshipRequestService;
        this.friendshipService = friendshipService;
        this.userService = userService;
        this.ID = ID;
        initializeColumns();
    }
    private void initializeColumns() {
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        searchResults.setItems(model);
    }

    public void handleSearchButton(ActionEvent actionEvent) {
        String searchQuery = searchBar.getText().toLowerCase();
        if (!searchQuery.isEmpty()) {
            List<User> filteredUsers = StreamSupport.stream(userService.findAll().spliterator(), false)
                    .filter(user -> (user.getFirstName() + " " + user.getLastName()).toLowerCase().contains(searchQuery))
                    .collect(Collectors.toList());
            model.setAll(filteredUsers);
            model.setAll(filteredUsers);
        } else {
            model.clear();
        }
    }

    public void handleSendRequest(ActionEvent actionEvent) {
        User selectedUser = searchResults.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Long ID2 = selectedUser.getID();

            if (ID.equals(ID2)) {
                statusLabel.setText("You cannot send a friend request to yourself!");
                statusLabel.setVisible(true);
                return;
            }

            boolean isAlreadyFriend = friendshipService.getFriends(ID).stream()
                    .anyMatch(friend -> friend.getID().equals(ID2));

            if (isAlreadyFriend) {
                statusLabel.setText("You are already friends!");
                statusLabel.setVisible(true);
            } else {
                friendshipRequestService.sendRequest(ID, ID2);
                statusLabel.setText("Friend request sent!");
                statusLabel.setVisible(true);
                model.clear();
                searchBar.clear();
            }
        } else {
            statusLabel.setText("No user selected.");
            statusLabel.setVisible(true);
        }
    }

    /*public void handleSendButton(ActionEvent actionEvent) {
        String firstname = firstnameText.getText();
        String lastname = lastnameText.getText();
        //System.out.println(firstname + " " + lastname);
        Long ID2=userService.findUserByNames(firstname,lastname);
        if(ID2!=null){
            friendshipRequestService.sendRequest(ID,ID2);
            failMessage.setVisible(false);
            firstnameText.clear();
            lastnameText.clear();
        }
        else{
            failMessage.setText("The request cannot be send.");
            failMessage.setVisible(true);
        }
    }*/
}