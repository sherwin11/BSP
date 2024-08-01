package com.bsp.bspregistration;

public class ChangepassCollection {
    private String token;
    private String username;
    private String newPassword;
    private String id;

    public ChangepassCollection(String token, String username, String newPassword, String id) {
        this.token = token;
        this.username = username;
        this.newPassword = newPassword;
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
