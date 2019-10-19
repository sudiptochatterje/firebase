package com.globussoft.sudipfirebase;

import com.google.firebase.database.IgnoreExtraProperties;
@IgnoreExtraProperties
public class User {

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String firstName;
    public String lastName;
    public String email;
    public User() {
    }

    public User(String name, String lastName,String email) {
        this.firstName = name;
        this.lastName=lastName;
        this.email = email;
    }
}