package com.example.groceryshopaish;

public class User {
    String uid;
    String email,pass,name,address,mobile;

    public User(){
    }

    public User(String uid, String email,String pass, String name, String address, String mobile)
    {
        this.uid=uid;
        this.email=email;
        this.pass=pass;
        this.name=name;
        this.address=address;
        this.mobile=mobile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String toString()
    {
        return "Name : "+name+" \nAddress : "+address+"\nContact No. : "+mobile;
    }
}
