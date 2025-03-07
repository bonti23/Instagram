package ubb.scs.map.laborator_6nou.repository.paging;

import ubb.scs.map.laborator_6nou.domain.User;
import ubb.scs.map.laborator_6nou.domain.validation.Validation;
import ubb.scs.map.laborator_6nou.repository.UserDBRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class UserDBPagingRepository extends UserDBRepository implements PagingRepository<Long, User>{
    public UserDBPagingRepository(String url, String username, String password, Validation<User> validator) {
        super(url, username, password, validator);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        List<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"User\" ORDER BY \"id_user\" OFFSET ? LIMIT ?");
        ){
            statement.setInt(1, pageable.getPageSize()*pageable.getPageNumber());
            statement.setInt(2, pageable.getPageSize());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long ID1 = resultSet.getLong("id_user");
                String firstName1 = resultSet.getString("firstname");
                String lastName1 = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                User user1 = new User(firstName1, lastName1, email, password);
                user1.setID(ID1);
                users.add(user1);
            }
            return new PageImplementation<User>(pageable, users.stream());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
