package com.JesacaLin.GrubGoblin_v13.daos;

import com.JesacaLin.GrubGoblin_v13.models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO class for managing User entities in the database.
 */
@Component
public class UserDAO {
    private JdbcTemplate jdbcTemplate;
    private PasswordEncoder passwordEncoder;

    /**
     * Constructs a new UserDAO with the given DataSource and PasswordEncoder.
     *
     * @param dataSource the DataSource to use for database access
     * @param passwordEncoder the PasswordEncoder to use for password hashing
     */
    public UserDAO(DataSource dataSource, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.passwordEncoder = passwordEncoder;
    }

     /**
     * Retrieves all Users.
     *
     * @return a list of all Users
     */
    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM app_user ORDER BY username", this::mapRowToUser);
    }

    /**
     * Retrieves a User by its username.
     *
     * @param username the username of the User to retrieve
     * @return the User with the given username, or null if no such User exists
     */
    public User getUserByUsername(String username) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM app_user WHERE username = ?",this::mapRowToUser, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Creates a new User.
     *
     * @param user the User to create
     * @return the created User, or null if the User could not be created
     */
    public User createUser(User user) {
        try {
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            jdbcTemplate.update("INSERT INTO app_user (username, password, email) VALUES (?, ?, ?)", user.getUsername(), hashedPassword, user.getEmail());
            return getUserByUsername(user.getUsername());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Updates a User.
     *
     * @param username the username of the User to update
     * @param user the User data to update
     * @return the updated User
     */
    public User updateUser(String username, User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        jdbcTemplate.update("UPDATE app_user SET password = ?, email = ? WHERE username = ?", hashedPassword, user.getEmail(), username);
        return getUserByUsername(username);
    }

    /**
     * Deletes a User by its username.
     *
     * @param username the username of the User to delete
     * @return the number of rows affected by the delete
     */
    public int deleteUser(String username) {
        return jdbcTemplate.update("DELETE FROM app_user WHERE username = ?", username);
    }

    /**
     * Retrieves all roles for a specific User.
     *
     * @param username the username of the User to retrieve roles for
     * @return a list of all roles for the specified User
     */
    public List<String> getRolesForUser(String username) {
        return jdbcTemplate.queryForList("SELECT user_role FROM role WHERE username = ?", String.class, username);
    }

    /**
     * Adds a role to a User.
     *
     * @param username the username of the User to add the role to
     * @param role the role to add
     * @return a list of all roles for the specified User after the add operation
     */
    public List<String> addRoleToUser(String username, String role) {
        try {
            jdbcTemplate.update("INSERT INTO role (username, user_role) VALUES (?, ?)", username, role);
        } catch (Exception e) {
            System.out.println(username + " and " + role + " already exists!");
        }
        return getRolesForUser(username);
    }

    /**
     * Removes a role from a User.
     *
     * @param username the username of the User to remove the role from
     * @param role the role to remove
     */
    public void removeRoleFromUser(String username, String role) {
        try {
            jdbcTemplate.update("DELETE FROM role WHERE username = ? and user_role = ?", username, role);
        } catch (Exception e) {
            System.out.println(username + " and " + role + " could not be deleted!");
        }
    }

    /**
     * Maps a ResultSet to a User.
     *
     * @param row the ResultSet to map
     * @param rowNumber the number of the current row
     * @return the mapped User
     * @throws SQLException if a database access error occurs
     */
    private User mapRowToUser(ResultSet row, int rowNumber) throws SQLException {
        User user = new User();
        user.setUsername(row.getString("username"));
        user.setPassword(row.getString("password"));
        user.setEmail(row.getString("email"));
        return user;
    }
}
