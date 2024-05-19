package com.JesacaLin.GrubGoblin_v13.daos;

import com.JesacaLin.GrubGoblin_v13.models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserDAO {
    private JdbcTemplate jdbcTemplate;

    public UserDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM app_user", this::mapRowToUser);
    }
    public User getUserByUsername(String username) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM app_user WHERE username = ?",this::mapRowToUser, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public User createUser(User user) {
        jdbcTemplate.update("INSERT INTO app_user (username, password, email) VALUES (?, ?, ?)", user.getUsername(), user.getPassword(), user.getEmail());
        return getUserByUsername(user.getUsername());
    }

    public User updateUser(String username, User user) {
        jdbcTemplate.update("UPDATE app_user SET password = ? WHERE username = ?", user.getPassword(), user.getEmail(), username);
        return getUserByUsername(username);
    }

    public int deleteUser(String username) {
        return jdbcTemplate.update("DELETE FROM app_user WHERE username = ?", username);
    }

    public List<String> getRolesForUser(String username) {
        return jdbcTemplate.queryForList("SELECT user_role FROM role WHERE username = ?", String.class, username);
    }

    public List<String> addRoleToUser(String username, String role) {
        try {
            jdbcTemplate.update("INSERT INTO role (username, user_role) VALUES (?, ?)", username, role);
        } catch (Exception e) {
            System.out.println(username + " and " + role + " already exists!");
        }
        return getRolesForUser(username);
    }
    public void removeRoleFromUser(String username, String role) {
        try {
            jdbcTemplate.update("DELETE FROM role WHERE username = ? and user_role = ?", username, role);
        } catch (Exception e) {
            System.out.println(username + " and " + role + " could not be deleted!");
        }
    }
    private User mapRowToUser(ResultSet row, int rowNumber) throws SQLException {
        User user = new User();
        user.setUsername(row.getString("username"));
        user.setPassword(row.getString("password"));
        user.setEmail(row.getString("email"));
        return user;
    }
}
