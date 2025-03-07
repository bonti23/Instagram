package ubb.scs.map.laborator_6nou.repository;

import ubb.scs.map.laborator_6nou.domain.Message;
import ubb.scs.map.laborator_6nou.domain.validation.Validation;
import ubb.scs.map.laborator_6nou.domain.validation.ValidationException;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MessageDBRepository extends AbstractDBRepository<Long, Message> {
    public MessageDBRepository(String url, String username, String password, Validation<Message> validator) {
        super(url, username, password, validator);
    }

    @Override
    public Optional<Message> findOne(Long id) {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Message\" WHERE id_message = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long idMessage = resultSet.getLong("id_message");
                Long toUser = resultSet.getLong("to_user");
                Long fromUser = resultSet.getLong("from_user");
                String message = resultSet.getString("message");
                Timestamp time = resultSet.getTimestamp("time");
                Long reply = resultSet.getLong("reply");

                Message msg = new Message(toUser, fromUser, message);
                msg.setID(idMessage);
                msg.setTime(time.toLocalDateTime());
                msg.setReply(reply == 0 ? null : reply);

                return Optional.of(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Message\"");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long idMessage = resultSet.getLong("id_message");
                Long toUser = resultSet.getLong("to_user");
                Long fromUser = resultSet.getLong("from_user");
                String message = resultSet.getString("message");
                Timestamp time = resultSet.getTimestamp("time");
                Long reply = resultSet.getLong("reply");

                Message msg = new Message(toUser, fromUser, message);
                msg.setID(idMessage);
                msg.setTime(time.toLocalDateTime());
                msg.setReply(reply == 0 ? null : reply);

                messages.add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Optional<Message> save(Message entity) {
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO \"Message\" (to_user, from_user, message, time, reply) VALUES (?, ?, ?, ?, ?)"
            );
            getValidator().validate(entity);
            statement.setLong(1, entity.getTo());
            statement.setLong(2, entity.getFrom());
            statement.setString(3, entity.getMessage());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getTime()));
            statement.setObject(5, entity.getReply(), Types.BIGINT);

            rez = statement.executeUpdate();
        } catch (SQLException | ValidationException e) {
            e.printStackTrace();
        }
        if (rez > 0) {
            return Optional.empty();
        } else {
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Message> delete(Long id) {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            Optional<Message> message = findOne(id);
            if (message.isEmpty()) {
                return Optional.empty();
            }
            PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Message\" WHERE id_message = ?");
            statement.setLong(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Message> update(Message entity) {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            Optional<Message> message = findOne(entity.getID());
            if (message.isEmpty()) {
                return Optional.of(entity);
            }
            getValidator().validate(entity);
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE \"Message\" SET to_user = ?, from_user = ?, message = ?, time = ?, reply = ? WHERE id_message = ?"
            );
            statement.setLong(1, entity.getTo());
            statement.setLong(2, entity.getFrom());
            statement.setString(3, entity.getMessage());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getTime()));
            statement.setObject(5, entity.getReply(), Types.BIGINT);
            statement.setLong(6, entity.getID());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException | ValidationException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
