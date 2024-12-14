package com.genesisresources.controller;
import com.genesisresources.model.User;
import com.genesisresources.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController

public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("api/v1/users")
    public ResponseEntity<String> createUser(@RequestBody User user){
        try {
            String createdUserMessage= userService.createUser(user);
            switch (createdUserMessage) {
                case "notValidId":
                    return new ResponseEntity<>("The provided personID is invalid. Make sure that it is valid and authorized", HttpStatus.BAD_REQUEST);
                case "idUsed":
                    return new ResponseEntity<>("The provided personID is used by user in database", HttpStatus.BAD_REQUEST);
                case "userCreated":
                    return new ResponseEntity<>("User has been created", HttpStatus.OK);
                default:
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("api/v1/users/{ID}")
    public ResponseEntity<?> getUserById (@PathVariable (value = "ID") Long id, @RequestParam (required = false) String detail){
        if (detail == null) {
            User foundUser= userService.getUserById(id);
            if(foundUser == null){
                return new ResponseEntity<>("User under id:" + id + "does not exist",HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(foundUser,HttpStatus.OK);
        }
        if(detail.equals("true")){
            User foundDetailedUser = userService.getUserByIdDetailed(id);
            if(foundDetailedUser == null){
                return new ResponseEntity<>("User under id:" + id + "does not exist",HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(foundDetailedUser,HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
   /* @GetMapping("api/v1/users/{ID}")
    public ResponseEntity<?> getUserByIdDetailed (@PathVariable (value = "ID") long id, @RequestParam String detail ){
        if(detail.equals("true")){
            return new ResponseEntity<>(userService.getUserByIdDetailed(id),HttpStatus.OK);
        }
       return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
*/
    @GetMapping("api/v1/users")
    public ResponseEntity<?> getAllUsers(@RequestParam (required = false) String detail){
        if (detail == null) {
            List<User> foundUserList = userService.getAllUsers();
            if(foundUserList.isEmpty()){
                return new ResponseEntity<>("There are no users in the database",HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(foundUserList,HttpStatus.OK);
        }
        if(detail.equals("true")){
            List<User> foundUserListDetailed = userService.getAllUsersDetailed();
            if(foundUserListDetailed.isEmpty()){
                return new ResponseEntity<>("There are no users in the database",HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(foundUserListDetailed,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

  /*  @GetMapping("api/v1/users")
    public ResponseEntity<?> getAllUsersDetailed(@RequestParam boolean detail){
        if(detail){
        List<User> userList = userService.getAllUsersDetailed();
        return new ResponseEntity<>(userList,HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
*/

    @DeleteMapping("api/v1/users/{ID}")
    public ResponseEntity<?> deleteUser(@PathVariable (value = "ID") Long id){
        boolean isUserDeleted = userService.deleteUser(id);
        if(isUserDeleted){
        return new ResponseEntity<>("User with ID: " +id + " has been successfully deleted",HttpStatus.OK);
        }
        return new ResponseEntity<>("User with ID " + id + " was not found and could not be deleted.", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("api/v1/users")
    public ResponseEntity<?> updateUser(@RequestBody User user){
        boolean isUserUpdated =  userService.updateUser(user);
        if(isUserUpdated){
        return new ResponseEntity<>("User with ID: " + user.getId() +  " has been updated", HttpStatus.OK);
        }
        return new ResponseEntity<>("User with ID " + user.getId() + " was not found and could not be updated.", HttpStatus.BAD_REQUEST);
    }


    /*
    public ResponseEntity<String> createUser(@RequestBody User user) {
       try {
            user.setPersonID(userService.loadFromFileDataPersonId().get(index++));

        } catch (IOException e) {
            return  new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        try (Connection con = DriverManager.getConnection
                ("jdbc:mysql://localhost:3306/genesisdb", "root", "")) {


            String sql = "INSERT INTO users (name, surname, person_id, uuid) VALUES ('" +
                    user.getName() + "', '" + user.getSurname() + "', '" +
                    user.getPersonID() + "', '" + user.getUuid() + "')";
            Statement statement = con.createStatement(); //Výraz Statement statement = con.createStatement();
            // vytvoří SQL statement (dotaz), který umožňuje provádět SQL operace (např. dotazy nebo aktualizace)
            // nad databází prostřednictvím připojení (Connection).
            statement.executeUpdate(sql);
            return new ResponseEntity<>("User created", HttpStatus.CREATED);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }*/
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }



}