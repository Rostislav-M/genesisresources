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
    private static final String FILE_PATH="src/main/resources/dataPersonId.txt";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private List<String> loadFromFileDataPersonId() throws IOException {
        List<String> personIdList= new ArrayList<>();
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader(FILE_PATH)))){
            while(scanner.hasNextLine()){
                String line= scanner.nextLine();
                personIdList.add(line);
            }
        }
        return personIdList;
    }

    public String createUser(User user) throws IOException {
        List<String> personIdList = loadFromFileDataPersonId();

        if (user.getName() == null || user.getName().trim().isEmpty() ||
            user.getPersonId() == null || user.getPersonId().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid user object! " + "\n" +
                    "Attributes 'name' and 'personId' must not be null or empty, 'surname' is optional");
        }

        if (user.getUuid() != null) {
            throw new IllegalArgumentException("uuid should not be provided in the request");
        }

        if (user.getId()!= null) {
            throw new IllegalArgumentException("id should not be provided in the request");
        }

        if (isPersonIdUsedByUser(user.getPersonId())) {
            return "idUsed";
        } else {
            for(String personId : personIdList){
                if (user.getPersonId().equals(personId)) {
                    user.setPersonId(personId);
                    user.setUuid(generateUniqueUuid());
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
        if (user.getId() == null || user.getId() <=0 || user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid user object! " + "\n" +
                    "Attributes 'id' must be a positive number and cannot be null or empty," + "\n" +
                    "'name' must not be null or empty, 'surname' is optional");
        }

        if (user.getUuid() != null) {
            throw new IllegalArgumentException("uuid should not be provided in the request");
        }

        if (user.getPersonId() != null){
            throw new IllegalArgumentException("personId should not be provided in the request");
        }

        int updatedRowsCount = userRepository.updateUser(user);
        return (updatedRowsCount > 0);
    }

    public boolean deleteUser(Long id){
        int deletedRowsCount = userRepository.deleteUser(id);
        return (deletedRowsCount > 0);
    }

    private boolean isPersonIdUsedByUser(String personId) {
        Integer count = userRepository.isPersonIdUsedByUser(personId);
        return (count != null && count > 0);
    }

    private boolean isUuidUsedByUser(String uuid) {
        Integer count = userRepository.isUuidUsedByUser(uuid);
        return (count != null && count > 0);
    }

    private String generateUniqueUuid() {
        String uniqueUuid = UUID.randomUUID().toString();
        while (isUuidUsedByUser(uniqueUuid)) {
            uniqueUuid = UUID.randomUUID().toString();
        }
        return uniqueUuid;
    }

}
