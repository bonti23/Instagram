package ubb.scs.map.laborator_6nou.service;

import ubb.scs.map.laborator_6nou.domain.Message;
import ubb.scs.map.laborator_6nou.domain.event.ChangeEventType;
import ubb.scs.map.laborator_6nou.domain.event.MessageEntityChange;
import ubb.scs.map.laborator_6nou.repository.MessageDBRepository;
import ubb.scs.map.laborator_6nou.utils.Observable;
import ubb.scs.map.laborator_6nou.utils.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MessageService implements Service<Message>, Observable<MessageEntityChange> {
    private MessageDBRepository repository;
    private List<Observer<MessageEntityChange>> observers = new ArrayList<>();

    public MessageService(MessageDBRepository repository) {
        this.repository = repository;
    }

    @Override
    public Message delete(Long ID) {
        Message m = repository.delete(ID).orElse(null);
        MessageEntityChange messageEntityChange = new MessageEntityChange(ChangeEventType.DELETE, m);
        notifyObservers(messageEntityChange);
        return m;
    }

    @Override
    public Iterable<Message> findAll() {
        return repository.findAll();
    }

    public Message save(Long to, Long from, String text) {
        Message m = new Message(to, from, text);
        Message a = repository.save(m).orElse(null);
        MessageEntityChange messageEntityChange = new MessageEntityChange(ChangeEventType.ADD, a);
        notifyObservers(messageEntityChange);
        return m;
    }

    public Message findOne(Long ID) {
        return repository.findOne(ID).orElse(null);
    }

    public Message update(Long id, Long to, Long from, String text, LocalDateTime time,Long reply) {
        Message m = new Message(to, from, text);
        m.setReply(reply);
        m.setID(id);
        m.setTime(time);

        Message a = repository.update(m).orElse(null);
        MessageEntityChange messageEntityChange = new MessageEntityChange(ChangeEventType.UPDATE, a);
        notifyObservers(messageEntityChange);
        return m;
    }

    public List<Message> getConversation(Long to, Long from) {
        ArrayList<Message> list = new ArrayList<>();
        for (Message message : repository.findAll()) {
            if (message.getTo().equals(to) && message.getFrom().equals(from)) {
                list.add(message);
            } else if (message.getTo().equals(from) && message.getFrom().equals(to)) {
                list.add(message);
            }
        }
        return list.stream().sorted(Comparator.comparing(Message::getTime))
                .collect(Collectors.toList());
    }

    public void removeByID(Long ID) {
        Iterable<Message> messages = repository.findAll();
        for (Message message : messages) {
            if (message.getFrom().equals(ID) || message.getTo().equals(ID)) {
                repository.delete(message.getID());
            }
        }
    }
    public Message findByMessage(String message){
        Iterable<Message> all=repository.findAll();
        for(Message m:all){
            if(m.getMessage().equals(message)){
                return m;
            }
        }
        return null;
    }
    public Message findRepliedText(Long id){
        Iterable<Message> all=repository.findAll();
        for(Message m:all) {
            if (m.getReply() != null) {
                if (m.getReply().equals(id)) {
                    return m;
                }
            }
        }
        return null;
    }

    @Override
    public void addObserver(Observer<MessageEntityChange> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<MessageEntityChange> e) {
        //observers.remove(e);
    }

    @Override
    public void notifyObservers(MessageEntityChange t) {
        observers.stream().forEach(x -> x.update(t));
    }

}