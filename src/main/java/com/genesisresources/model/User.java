package com.genesisresources.model;


import com.fasterxml.jackson.annotation.JsonInclude;

public class User {
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    private String surname;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String personID;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String uuid;


    public User()  {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', surname='" + surname + "', personID='" + personID + "', uuid='" + uuid + "'}";
    }
}