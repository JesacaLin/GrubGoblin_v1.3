package com.JesacaLin.GrubGoblin_v13.controllers;

import com.JesacaLin.GrubGoblin_v13.daos.UserDAO;
import com.JesacaLin.GrubGoblin_v13.models.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app_user")
public class UserController {
    private UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("")
    public List<User> listOfUsers() {
        return userDAO.getAllUsers();
    }
    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userDAO.getUserByUsername(username);
    }
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userDAO.createUser(user);
    }
    @PutMapping("/{username}")
    public User updateUser(@PathVariable String username, @RequestBody User user) {
        return userDAO.updateUser(username, user);
    }
    @DeleteMapping("/{username}")
    public int deleteUser(@PathVariable String username) {
        return userDAO.deleteUser(username);
    }

    @GetMapping("/{username}/role")
    public List<String> getRolesForUser(@PathVariable String username) {
        return userDAO.getRolesForUser(username);
    }
    @PostMapping("/{username}/role")
    public void addRoleToUser(@PathVariable String username, @RequestBody String role) {
        userDAO.addRoleToUser(username, role);
    }
    @DeleteMapping("/{username}/role/{role}")
    public void removeRoleFromUser(@PathVariable String username, @PathVariable String role) {
        userDAO.removeRoleFromUser(username, role);
    }
}
