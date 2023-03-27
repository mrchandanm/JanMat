package com.prabitra.janmat;

public class UserDetails {
    String name;
    String phonenumber;

    public UserDetails(String name, String phonenumber) {
        this.name = name;
        this.phonenumber = phonenumber;
    }

    public UserDetails() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
