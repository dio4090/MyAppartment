package com.sourcey.myappartment.model;

/**
 * Created by diogo on 28/11/2017.
 */

public class User {

    private int user_id;
    private String name;
    private String addess;
    private String email;
    private String password;
    private int mobile_number;
    private int login_updated_at;


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddess() {
        return addess;
    }

    public void setAddress(String addess) {
        this.addess = addess;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(int mobile_number) {
        this.mobile_number = mobile_number;
    }

    public int getLogin_updated_at() {
        return login_updated_at;
    }

    public void setLogin_updated_at(int login_updated_at) {
        this.login_updated_at = login_updated_at;
    }
}
