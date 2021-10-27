package com.lettherebelight;

public class User {
    public String fullName,email, password, companyName;

    public User(){};

    public User(String fullName,  String email, String password, String companyName) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.companyName = companyName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
