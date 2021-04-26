package com.example.myapplication3;
public class UserHelperClass {
    String name, username, password,profession,email,phone;
    public UserHelperClass() {
    }
    public UserHelperClass(String name, String username,String password,String email,String profession,String phone) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.profession=profession;
        this.email=email;
        this.phone=phone;

    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {return password; }
    public void setPassword(String password) { this.password = password;  }
    public String getEmail() {
        return email;
    }
    public void setEmail(String password) { this.email = email;  }
    public String getProfession() {
        return profession;
    }
    public void setProfession(String password) {
        this.profession = profession;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}