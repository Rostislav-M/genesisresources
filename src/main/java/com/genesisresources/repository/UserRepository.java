package com.genesisresources.repository;
import com.genesisresources.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;


    public UserRepository(JdbcTemplate jdbcTemplate) {
       this.jdbcTemplate = jdbcTemplate;
    }

    public void createUser (User user) {
       String sql = "INSERT INTO users (name, surname, person_id, uuid) " +
            "VALUES(?,?,?,?)";
        jdbcTemplate.update(sql, user.getName(), user.getSurname(),user.getPersonID(),user.getUuid());
    }
    public boolean isPersonIdUsedByUser(String personID){
        String sql = "SELECT count(*) FROM users WHERE person_id= ?";
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,personID);
        return (count !=null && count > 0);
    }
    /*
    public User getUserByPersonId(String personID){
        String sql = "select * from users where person_id ='" +personID + "'";
        return jdbcTemplate.queryForObject(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet result, int rowNum) throws SQLException{
                User user = new User();
                user.setId(result.getLong("id"));
                user.setName(result.getString("name"));
                user.setSurname(result.getString("surname"));
                user.setPersonID(result.getString("person_id"));
                user.setUuid(result.getString("uuid"));
                return user;
            }
        });
    }

     */
    // upravit na {id: string, name: string, surname: string }
    public User getUserById(Long id){
        try{
        String sql = "SELECT id, name, surname FROM users WHERE id="+id;
        return jdbcTemplate.queryForObject(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet result, int rowNum) throws SQLException{
                User user = new User();
                user.setId(result.getLong("id"));
                user.setName(result.getString("name"));
                user.setSurname(result.getString("surname"));
                return user;
            }
        });
        }catch (EmptyResultDataAccessException e){
           return null;
        }

    }

    //{id: string, name: string, surname: string, personID: string , uuid: string  }
    public User getUserByIdDetailed(Long id){
        try{
        String sql = "SELECT * FROM users WHERE id="+id;
        return jdbcTemplate.queryForObject(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet result, int rowNum) throws SQLException{
                User user = new User();
                user.setId(result.getLong("id"));
                user.setName(result.getString("name"));
                user.setSurname(result.getString("surname"));
                user.setPersonID(result.getString("person_id"));
                user.setUuid(result.getString("uuid"));
                return user;
            }
        });
        }catch (EmptyResultDataAccessException e){
            return null;
        }

    }

    //List <{id: string, name: string, surname: string }>
    public List<User> getAllUsers(){
        String sql = "SELECT id, name, surname FROM users";
        List<User> users= jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet result, int rowNum) throws SQLException{
                User user = new User();
                user.setId(result.getLong("id"));
                user.setName(result.getString("name"));
                user.setSurname(result.getString("surname"));
                return user;
            }
        });
        if(users.isEmpty()){
            return Collections.emptyList();
        }
        return users;
    }
    public List<User> getAllUsersDetailed(){
        String sql = "SELECT * FROM users";
        List<User> users= jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet result, int rowNum) throws SQLException{
                User user =  new User();
                user.setId(result.getLong("id"));
                user.setName(result.getString("name"));
                user.setSurname(result.getString("surname"));
                user.setPersonID(result.getString("person_id"));
                user.setUuid(result.getString("uuid"));
                return user;
            }
        });
        if(users.isEmpty()){
            return Collections.emptyList();
        }
        return users;
    }

    public boolean updateUser(User user){
        String sql = "UPDATE users SET name= ?, surname= ? WHERE id= ?";
        int updatedRowsCount = jdbcTemplate.update(sql,user.getName(),user.getSurname(),user.getId());
        return (updatedRowsCount > 0);
    }

    public boolean deleteUser(Long id){
        String sql = "DELETE FROM users WHERE id= ?";
        int deletedRowsCount= jdbcTemplate.update(sql,id);
        return (deletedRowsCount > 0);
    }





}
