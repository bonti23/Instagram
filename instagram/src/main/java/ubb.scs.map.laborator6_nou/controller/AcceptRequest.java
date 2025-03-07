package ubb.scs.map.laborator_6nou.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ubb.scs.map.laborator_6nou.domain.FriendshipRequest;
import ubb.scs.map.laborator_6nou.domain.User;
import ubb.scs.map.laborator_6nou.service.FriendshipRequestService;
import ubb.scs.map.laborator_6nou.service.FriendshipService;
import ubb.scs.map.laborator_6nou.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AcceptRequest {
    @FXML
    public TableView<FriendshipRequest> tableView;
    @FXML
    public TableColumn<FriendshipRequest, String> firstnameColumn;
    @FXML
    public TableColumn<FriendshipRequest, String> lastnameColumn;
    @FXML
    public TableColumn<FriendshipRequest, String> dateColumn;
    @FXML
    public TableColumn<FriendshipRequest, String> statusColumn;
    @FXML
    public Button acceptButton;
    @FXML
    public Button rejectButton;
    @FXML
    public Label showMessage;

    private Long ID;
    private FriendshipRequestService friendshipRequestService;
    private FriendshipService friendshipService;
    private UserService userService;
    ObservableList<FriendshipRequest> model = FXCollections.observableArrayList();

    public void setService(FriendshipRequestService friendshipRequestService, UserService userService,
                           FriendshipService friendshipService, Long ID) {
        this.friendshipRequestService = friendshipRequestService;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.ID = ID;
        initModel();
    }

    public void initialize() {
        firstnameColumn.setCellValueFactory(cellData -> {
            Long senderId = cellData.getValue().getId_user1();
            User sender = userService.findOne(senderId);
            return new SimpleStringProperty(sender != null ? sender.getFirstName() : "Unknown");
        });

        lastnameColumn.setCellValueFactory(cellData -> {
            Long senderId = cellData.getValue().getId_user1();
            User sender = userService.findOne(senderId);
            return new SimpleStringProperty(sender != null ? sender.getLastName() : "Unknown");
        });

        dateColumn.setCellValueFactory(cellData -> {
            LocalDateTime timeSend = cellData.getValue().getTimeSend();
            if (timeSend != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                return new SimpleStringProperty(timeSend.format(formatter));
            } else {
                return new SimpleStringProperty("Unknown");
            }
        });

        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        tableView.setItems(model);
    }

    private void initModel() {
        Iterable<FriendshipRequest> messages = friendshipRequestService.findByReceiver(ID);
        List<FriendshipRequest> requests = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(requests);
    }

    public void handleAccept(ActionEvent actionEvent) {
        FriendshipRequest request = tableView.getSelectionModel().getSelectedItem();
        if (request != null && "PENDING".equals(request.getStatus())) {
            friendshipRequestService.acceptRequest(request.getSender(), request.getReceiver());
            friendshipService.save(request.getSender(), request.getReceiver());
            showMessage.setText("You have a new friend.");
            showMessage.setVisible(true);
            initModel();
        }
    }

    public void handleDecline(ActionEvent actionEvent) {
        FriendshipRequest request = tableView.getSelectionModel().getSelectedItem();
        if (request != null && "PENDING".equals(request.getStatus())) {
            friendshipRequestService.declineRequest(request.getSender(), request.getReceiver());
            showMessage.setText("You declined the friendship.");
            showMessage.setVisible(true);
            initModel();
        }
    }
}
