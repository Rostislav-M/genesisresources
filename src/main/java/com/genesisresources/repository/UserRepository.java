package com.genesisresources.repository;
import com.genesisresources.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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
       jdbcTemplate.update(sql, user.getName(), user.getSurname(),user.getPersonId(),user.getUuid());
   }

    public Integer isPersonIdUsedByUser(String personId){
        String sql = "SELECT count(*) FROM users WHERE person_id= ?";
        return jdbcTemplate.queryForObject(sql,Integer.class,personId);
    }

    public User getUserById(Long id){
        try{
        String sql = "SELECT id, name, surname FROM users WHERE id= ?";
        return jdbcTemplate.queryForObject(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet result, int rowNum) throws SQLException{
                User user = new User();
                user.setId(result.getLong("id"));
                user.setName(result.getString("name"));
                user.setSurname(result.getString("surname"));
                return user;
            }
        }, id);
        }catch (EmptyResultDataAccessException e){
           return null;
        }
    }

    public User getUserByIdDetailed(Long id){
    try{
        String sql = "SELECT * FROM users WHERE id= ?";
        return jdbcTemplate.queryForObject(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet result, int rowNum) throws SQLException{
                User user = new User();
                user.setId(result.getLong("id"));
                user.setName(result.getString("name"));
                user.setSurname(result.getString("surname"));
                user.setPersonId(result.getString("person_id"));
                user.setUuid(result.getString("uuid"));
                return user;
            }
        }, id);
    } catch (EmptyResultDataAccessException e){
       return null;
    }
}
    //List <{id: string, name: string, surname: string }>
    public List<User> getAllUsers(){
        String sql = "SELECT id, name, surname FROM users";
        return jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet result, int rowNum) throws SQLException{
                User user = new User();
                user.setId(result.getLong("id"));
                user.setName(result.getString("name"));
                user.setSurname(result.getString("surname"));
                return user;
            }
        });
    }

    public List<User> getAllUsersDetailed(){
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet result, int rowNum) throws SQLException{
                User user =  new User();
                user.setId(result.getLong("id"));
                user.setName(result.getString("name"));
                user.setSurname(result.getString("surname"));
                user.setPersonId(result.getString("person_id"));
                user.setUuid(result.getString("uuid"));
                return user;
            }
        });
    }

    public int updateUser(User user){
        String sql = "UPDATE users SET name= ?, surname= ? WHERE id= ?";
        return jdbcTemplate.update(sql,user.getName(),user.getSurname(),user.getId());
    }

    public int deleteUser(Long id){
        String sql = "DELETE FROM users WHERE id= ?";
        return jdbcTemplate.update(sql,id);
    }

}
