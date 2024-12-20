package com.genesisresources.controller;

import com.genesisresources.model.User;
import com.genesisresources.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;


@RestController

public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("api/v1/users")
    public ResponseEntity<String> createUser(@RequestBody User user) throws IOException {
        String createdUserMessage = userService.createUser(user);

        switch (createdUserMessage) {
                case "notValidId":
                    return new ResponseEntity<>("The provided personId is invalid. Make sure that it is valid and authorized", HttpStatus.BAD_REQUEST);
                case "idUsed":
                    return new ResponseEntity<>("The provided personId is used by user in database", HttpStatus.BAD_REQUEST);
                case "userCreated":
                   return new ResponseEntity<>("User has been created", HttpStatus.CREATED);
                default:
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @GetMapping("api/v1/users/{ID}")
    public ResponseEntity<?> getUserById(@PathVariable (value = "ID") Long id, @RequestParam (required = false) String detail){
        if (detail == null) {
            User foundUser= userService.getUserById(id);
            if (foundUser == null){
                 return new ResponseEntity<>("User under id: " + id + " does not exist",HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(foundUser,HttpStatus.OK);
        }

        if (detail.equals("true")) {
            User foundDetailedUser = userService.getUserByIdDetailed(id);
            if (foundDetailedUser == null) {
                 return new ResponseEntity<>("User under id: " + id + " does not exist",HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(foundDetailedUser,HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("api/v1/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam (required = false) String detail){
        if (detail == null) {
            List<User> foundUserList = userService.getAllUsers();
            if (foundUserList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(foundUserList,HttpStatus.OK);
        }

        if (detail.equals("true")) {
            List<User> foundUserListDetailed = userService.getAllUsersDetailed();
            if (foundUserListDetailed.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(foundUserListDetailed,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("api/v1/users/{ID}")
    public ResponseEntity<?> deleteUser(@PathVariable (value = "ID") Long id){
        boolean isUserDeleted = userService.deleteUser(id);
        if (isUserDeleted) {
            return new ResponseEntity<>("User with id : " + id + " has been successfully deleted",HttpStatus.OK);
        }
        return new ResponseEntity<>("User with id " + id + " was not found and could not be deleted.", HttpStatus.NOT_FOUND);
    }

    @PutMapping("api/v1/users")
    public ResponseEntity<?> updateUser(@RequestBody User user){
        boolean isUserUpdated =  userService.updateUser(user);
        if (isUserUpdated) {
        return new ResponseEntity<>("User with id: " + user.getId() +  " has been successfully updated", HttpStatus.OK);
        }
        return new ResponseEntity<>("User with id: " + user.getId() + " was not found and could not be updated.", HttpStatus.NOT_FOUND);
    }

}