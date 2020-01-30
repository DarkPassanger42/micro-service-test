package com.organizer.usermanager.model;

public class UserDTO {

    private String email;
    private String id;

    public UserDTO() {
    }

    public UserDTO(String email, String password, String id) {
        this.email = email;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
