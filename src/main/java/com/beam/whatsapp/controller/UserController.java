package com.beam.whatsapp.controller;

import com.beam.whatsapp.Service.UserService;
import com.beam.whatsapp.model.GenericResponse;
import com.beam.whatsapp.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.UUID;

import static com.beam.whatsapp.Service.UserService.SESSION_KEY;

@RequiredArgsConstructor
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/{name}", produces = "application/json")
    public GenericResponse getUserByName(@PathVariable("name") String name) {
        GenericResponse<User> response = new GenericResponse<>();
        User user = userService.getUserByName(name);
        if (user != null) {
            response.setCode(200).setMessage("Success");
        } else {
            response.setCode(404).setMessage("User not found");
        }
        response.setData(user);

        return response;
    }

    @GetMapping(produces = "application/json")
    public User getUser(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY);
        User newUser = User.builder().username(user.getUsername()).imageUrl(user.getImageUrl()).id(user.getId()).
                lastSeen(user.getLastSeen()).build();
        return newUser;
    }


    @PostMapping(consumes = "application/json")
    public GenericResponse saveUser(@RequestBody User user) {
        GenericResponse<User> response = new GenericResponse<>();
        user.setId(UUID.randomUUID().toString());
        User saveUser = userService.saveUser(user);
        if (saveUser != null) {
            response.setCode(200).setMessage("Success");
        } else {
            response.setCode(404).setMessage("User not saved");
        }
        response.setData(saveUser);
        return response;
    }

    @DeleteMapping(value = "/{name}")
    public GenericResponse deleteUser(@PathVariable("name") String name) {
        GenericResponse<User> response = new GenericResponse<>();
        User user = userService.deleteUser(name);
        if (user != null) {
            response.setCode(200).setMessage("Success");
        } else {
            response.setCode(404).setMessage("User not found");
        }
        response.setData(user);
        return response;
    }

    @GetMapping(produces = "application/json", value = "/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

}
