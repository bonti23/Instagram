package ubb.scs.map.laborator_6nou.service;

import ubb.scs.map.laborator_6nou.domain.User;
import ubb.scs.map.laborator_6nou.domain.event.ChangeEventType;
import ubb.scs.map.laborator_6nou.domain.event.UserEntityChange;
import ubb.scs.map.laborator_6nou.repository.UserDBRepository;
import ubb.scs.map.laborator_6nou.repository.paging.Page;
import ubb.scs.map.laborator_6nou.repository.paging.Pageable;
import ubb.scs.map.laborator_6nou.repository.paging.PageableImplementation;
import ubb.scs.map.laborator_6nou.repository.paging.UserDBPagingRepository;
import ubb.scs.map.laborator_6nou.utils.Observable;
import ubb.scs.map.laborator_6nou.utils.Observer;
import ubb.scs.map.laborator_6nou.domain.validation.UserValidation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService implements Service<User>, Observable<UserEntityChange> {
    //private UserDBRepository repository;
    private UserDBPagingRepository repository;

    private List<Observer<UserEntityChange>> observers=new ArrayList<>();

    private int pageNumber = 0;
    private int pageSize = 10;

    public UserService(UserDBPagingRepository repository) {
        this.repository = repository;
        //this.friendshipRepository = friendshipRepository;
    }

    @Override
    public User delete(Long ID) {
        User user = repository.findOne(ID).orElseThrow(() -> new IllegalArgumentException("User not found"));
        repository.delete(ID).orElseThrow(null);
        UserEntityChange event=new UserEntityChange(ChangeEventType.DELETE, user);
        notifyObservers(event);
        return user;
    }

    public User save(String firstName, String lastName,String email,String password) {
        User newUser = new User(firstName, lastName, email, password);
        User a=repository.save(newUser).orElse(null);
        UserEntityChange event=new UserEntityChange(ChangeEventType.ADD,newUser);
        notifyObservers(event);
        return a;
    }

    public User update(Long ID, String firstName, String lastName,String email,String password) {
        User toBeUpdated = new User(firstName, lastName, email, password);
        toBeUpdated.setID(ID);
        User a = repository.update(toBeUpdated).orElse(null);
        UserEntityChange event = new UserEntityChange(ChangeEventType.UPDATE, a);
        notifyObservers(event);
        return a;
    }
    @Override
    public Iterable<User> findAll(){

        return repository.findAll();
    }

    public User findOne(Long ID) {
        return repository.findOne(ID).orElseThrow(()-> new IllegalArgumentException("User not found"));
    }


    public Long findUserByNames(String firstName, String lastName) {
        Iterable<User> users = repository.findAll();
        for (User user : users) {
            if (user.getFirstName().equals(firstName) && user.getLastName().equals(lastName)) {
                return user.getID();
            }
        }
        return null;
    }
    public Page<User> findFriends() {
        Pageable pageable = new PageableImplementation(pageNumber, pageSize);
        return repository.findAll(pageable);
    }
    @Override
    public void addObserver(Observer<UserEntityChange> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<UserEntityChange> e) {
        //observers.remove(e);
    }

    @Override
    public void notifyObservers(UserEntityChange t) {

        observers.stream().forEach(x->x.update(t));
    }
    public boolean updatePassword(Long userId, String newPassword) {
        boolean updated = repository.updatePassword(userId, newPassword);

        if (updated) {
            System.out.println("Password updated successfully!");
        } else {
            System.out.println("Password update failed.");
        }

        return updated;
    }
    public User findByEmail(String email, String password) {
        return repository.findByEmail(email, password);
    }
}