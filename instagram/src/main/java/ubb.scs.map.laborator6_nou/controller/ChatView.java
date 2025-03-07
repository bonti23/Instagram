package ubb.scs.map.laborator_6nou.controller;

import javafx.fxml.FXML;
import ubb.scs.map.laborator_6nou.domain.Message;
import ubb.scs.map.laborator_6nou.domain.event.MessageEntityChange;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ubb.scs.map.laborator_6nou.service.MessageService;
import ubb.scs.map.laborator_6nou.service.UserService;
import ubb.scs.map.laborator_6nou.utils.Observer;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChatView implements Observer<MessageEntityChange> {
    @FXML
    public ListView<String> listView;
    @FXML
    public TextField messageText;
    @FXML
    public Button sendButton;
    @FXML
    public Label nameLabel;
    @FXML
    public Button replyButton;
    @FXML
    private Label feedbackLabel;

    private MessageService messageService;
    private UserService userService;
    private Long IdTo;
    private Long IdFrom;
    String receiver;
    ObservableList<String> model = FXCollections.observableArrayList();

    public void setService(MessageService messageService, UserService userService, Long IdTo, Long IdFrom, String receiverName) {
        this.messageService = messageService;
        this.userService = userService;
        this.IdTo = IdTo;
        this.IdFrom = IdFrom;
        this.receiver = receiverName;
        messageService.addObserver(this);
        initModel();
    }

    public void handleSend(ActionEvent actionEvent) {
        String message = messageText.getText();
        System.out.println(message);
        messageService.save(IdTo,IdFrom,message);
        messageText.clear();

    }
    public void initialize() {
        listView.setItems(model);
    }

    private void initModel() {
        Iterable<Message> messages = messageService.getConversation(IdTo,IdFrom);
        ArrayList<String> all=new ArrayList<>();
        for(Message m : messages) {
            String name = userService.findOne(m.getFrom()).getFirstName() + " " + userService.findOne(m.getFrom()).getLastName();
            if(m.getFrom().equals(IdFrom)) {
                name="You";
            }
            if (messageService.findRepliedText(m.getID())!=null) {
                Message replyText=messageService.findRepliedText(m.getID());
                String line = name + " replied to " + replyText.getMessage() + ": " + m.getMessage();
                all.add(line);
            } else {

                String line = name + ": " + m.getMessage();
                all.add(line);
            }
        }

        model.setAll(all);
        nameLabel.setText(receiver);
    }

    @Override
    public void update(MessageEntityChange messageEntityChange) {
        initModel();
    }

    public void handleReply(ActionEvent actionEvent) {
        String row=listView.getSelectionModel().getSelectedItem();
        System.out.println(row);
        String rawMessage= Arrays.stream(row.split(": ")).toList().getLast();

        Message initialMessage=messageService.findByMessage(rawMessage);

        String text = messageText.getText();
        messageService.save(IdTo,IdFrom,text);

        Message replyMessage=messageService.findByMessage(text);


        System.out.println("ID:"+replyMessage.getID());

        initialMessage.setReply(replyMessage.getID());
        System.out.println(initialMessage.getID()+" "+initialMessage.getTo()+" "+initialMessage.getFrom()+" "+initialMessage.getMessage()+" "+initialMessage.getTime() +" "+ initialMessage.getReply());

        messageService.update(initialMessage.getID(), initialMessage.getTo(), initialMessage.getFrom(),initialMessage.getMessage(),initialMessage.getTime(), initialMessage.getReply());

        messageText.clear();

    }
    public void handleDelete(ActionEvent actionEvent) {
        String row = listView.getSelectionModel().getSelectedItem();
        System.out.println(row);

        if (row != null) {
            String rawMessage = Arrays.stream(row.split(": ")).toList().getLast();

            Message messageToDelete = messageService.findByMessage(rawMessage);

            if (messageToDelete != null) {
                if (messageToDelete.getFrom().equals(IdFrom)) {
                    messageService.delete(messageToDelete.getID());
                    feedbackLabel.setText("Message deleted.");
                } else {
                    feedbackLabel.setText("System: You can only delete your own messages.");
                }
            }
        }
    }
}