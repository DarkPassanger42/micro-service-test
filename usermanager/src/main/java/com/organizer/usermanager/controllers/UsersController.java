package com.organizer.usermanager.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.MalformedJsonException;
import com.organizer.usermanager.model.UserDTO;
import com.organizer.usermanager.model.UserModel;
import com.organizer.usermanager.model.UserModelMapper;
import com.organizer.usermanager.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class UsersController {

    private UserService userService;
    private UserModelMapper userModelMapper;

    public UsersController(UserService userService, UserModelMapper userModelMapper){
        this.userModelMapper = userModelMapper;
        this.userService = userService;
    }


    @GetMapping("/test")
    @ResponseBody
    public String testMethod(){
        return "works";
    }

    @GetMapping("/users")
    public ResponseEntity getAllUsers(){
        List<UserModel> allUsers = userService.getAllUsers();
        List<UserDTO> users = allUsers.stream().map(user -> userModelMapper.toUserDTO(user))
                .collect(Collectors.toList());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity createUser(@RequestBody String request) throws MalformedJsonException {

        JsonObject jsonObject = new Gson().fromJson(request, JsonObject.class);

        List<String> keys = jsonObject.entrySet()
                .stream()
                .map(i -> i.getKey())
                .collect(Collectors.toCollection(ArrayList::new));

        //make sure we're getting what we expect
        if (keys.size() != 2){
            throw new MalformedJsonException("incorrect Json");
        }

        String email;
        String password;
        try{
            email = jsonObject.get("email").getAsString();
            password = jsonObject.get("password").getAsString();
        }
        catch (Exception ex){
            throw new MalformedJsonException("incorrect Json");
        }

        UserDTO newUser = userService.createUser(email, password);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userid}")
    public ResponseEntity getUserById(@PathVariable String userid){
        Optional<UserModel> userOpt = userService.findUserById(userid);

        if (userOpt.isPresent()){
            return new ResponseEntity<>(userModelMapper.toUserDTO(userOpt.get()), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/email/{email}")
    public ResponseEntity getUserByEmail(@PathVariable String email){
        List<UserModel> userByEmail = userService.findUserByEmail(email);

        if (userByEmail.size() == 0){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        List<UserDTO> users = userByEmail.stream().map(user -> userModelMapper.toUserDTO(user))
                .collect(Collectors.toList());

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
