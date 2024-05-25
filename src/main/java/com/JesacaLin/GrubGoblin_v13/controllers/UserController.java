package com.JesacaLin.GrubGoblin_v13.controllers;

import com.JesacaLin.GrubGoblin_v13.daos.UserDAO;
import com.JesacaLin.GrubGoblin_v13.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/app_user")
public class UserController {
    private UserDAO userDAO;


    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("")
    public List<User> listOfUsers() {
        return userDAO.getAllUsers();
    }

    @PreAuthorize("hasAuthority('contributor') or hasAuthority('admin')")
    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable String username) {
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }

    @PreAuthorize("hasAuthority('contributor') or hasAuthority('admin')")
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userDAO.createUser(user);
    }

    @PreAuthorize("hasAuthority('contributor') or hasAuthority('admin')")
    @PutMapping("/{username}")
    public User updateUser(@PathVariable String username, @RequestBody User user) {
        return userDAO.updateUser(username, user);
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{username}")
    public int deleteUser(@PathVariable String username) {
        return userDAO.deleteUser(username);
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/{username}/role")
    public List<String> getRolesForUser(@PathVariable String username) {
        return userDAO.getRolesForUser(username);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/{username}/role")
    public void addRoleToUser(@PathVariable String username, @RequestBody String role) {
        userDAO.addRoleToUser(username, role);
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{username}/role/{role}")
    public void removeRoleFromUser(@PathVariable String username, @PathVariable String role) {
        userDAO.removeRoleFromUser(username, role);
    }
}
