package com.genesisresources.service;

import com.genesisresources.model.User;
import com.genesisresources.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;



@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private static final String FILE_PATH="src/main/resources/dataPersonId.txt";


    public List<String> loadFromFileDataPersonId() throws IOException {
        List<String> personIdList= new ArrayList<>();
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader(FILE_PATH)))){
            while(scanner.hasNextLine()){
                String line= scanner.nextLine();
                personIdList.add(line);
            }

        }return personIdList;
    }
    public String createUser(User user) throws IOException {
        if (user.getName() == null && user.getPersonID() == null){
            throw new IllegalArgumentException("User must contain at least a name and personID");
        }
        /*
        if ((user.getName() == null || user.getName().trim().isEmpty()) &&
                (user.getPersonID() == null || user.getPersonID().trim().isEmpty())) {
            throw new IllegalArgumentException("User must contain at least a name and personID");
        }

         */
        /*if (user.getName() == null && user.getPersonID() == null) {
            throw new IllegalArgumentException("User object cannot be empty");
        }*/
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (user.getPersonID() == null || user.getPersonID().trim().isEmpty()) {
            throw new IllegalArgumentException("PersonID cannot be null or empty");
        }
        if (user.getUuid()!= null) {
            throw new IllegalArgumentException("UUID should not be provided in the request");
        }
        if((user.getId()!= null)){
            throw new IllegalArgumentException("Id should not be provided in the request");
        }
        if(userRepository.isPersonIdUsedByUser(user.getPersonID())){
            return "idUsed";
        }else{
            for(String personID : loadFromFileDataPersonId()){
                if(user.getPersonID().equals(personID)){
                    user.setPersonID(personID);
                    user.setUuid(UUID.randomUUID().toString());
                    userRepository.createUser(user);
                    return "userCreated";
                }

            }
            return "notValidId";
        }

    }


    public User getUserById(Long id){
        return userRepository.getUserById(id);
    }
    public User getUserByIdDetailed(Long id){
        return userRepository.getUserByIdDetailed(id);
    }

    public List<User> getAllUsers(){
        return userRepository.getAllUsers();
    }

    public List<User> getAllUsersDetailed(){
        return userRepository.getAllUsersDetailed();
    }
    public boolean updateUser(User user){
        return userRepository.updateUser(user);
    }

    public boolean deleteUser(Long id){
        return userRepository.deleteUser(id);
    }



}
