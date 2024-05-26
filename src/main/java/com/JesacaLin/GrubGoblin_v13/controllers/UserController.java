package com.JesacaLin.GrubGoblin_v13.controllers;

import com.JesacaLin.GrubGoblin_v13.daos.UserDAO;
import com.JesacaLin.GrubGoblin_v13.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

/**
 * Controller for managing User entities.
 */

@RestController
@RequestMapping("/app_user")
public class UserController {
    private UserDAO userDAO;

    /**
     * Constructs a new UserController with the given UserDAO.
     *
     * @param userDAO the UserDAO to use for user management
     */
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Retrieves all Users.
     *
     * @return a list of all Users
     */
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("")
    public List<User> listOfUsers() {
        return userDAO.getAllUsers();
    }

    /**
     * Retrieves a User by its username.
     *
     * @param username the username of the User to retrieve
     * @return the User with the given username
     */
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable String username) {
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }

    /**
     * Creates a new User.
     *
     * @param user the User to create
     * @return the created User
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userDAO.createUser(user);
    }

   /**
     * Updates a User.
     *
     * @param username the username of the User to update
     * @param user the User data to update
     * @return the updated User if the authorized user is same user.
     */
    @PreAuthorize("hasAuthority('contributor') or hasAuthority('admin')")
    @PutMapping("/{username}")
    public User updateUser(@PathVariable String username, @RequestBody User user, Principal principal) {
        User existingUser = userDAO.getUserByUsername(username);
        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (!existingUser.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to update this user");
        }
        return userDAO.updateUser(username, user);
    }

    /**
     * Deletes a User by its username.
     *
     * @param username the username of the User to delete
     * @return the number of rows affected by the delete
     */
    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{username}")
    public int deleteUser(@PathVariable String username) {
        return userDAO.deleteUser(username);
    }

    /**
     * Retrieves all roles for a given User.
     *
     * @param username the username of the User to retrieve roles for
     * @return a list of roles for the given User
     */
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/{username}/role")
    public List<String> getRolesForUser(@PathVariable String username) {
        return userDAO.getRolesForUser(username);
    }

    /**
     * Adds a role to a User.
     *
     * @param username the username of the User to add a role to
     * @param role the role to add
     */
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/{username}/role")
    public void addRoleToUser(@PathVariable String username, @RequestBody String role) {
        userDAO.addRoleToUser(username, role);
    }

    /**
     * Removes a role from a User.
     *
     * @param username the username of the User to remove a role from
     * @param role the role to remove
     */
    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{username}/role/{role}")
    public void removeRoleFromUser(@PathVariable String username, @PathVariable String role) {
        userDAO.removeRoleFromUser(username, role);
    }
}
