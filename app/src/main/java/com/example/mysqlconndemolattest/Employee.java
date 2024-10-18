package com.example.mysqlconndemolattest;

public class Employee {

    int id;
    String fullname;
    String address;
    String contact;

    public Employee() {
    }

    public Employee(int id, String fullname, String address, String contact) {

    this.id=id;
    this.fullname=fullname;
    this.address=address;
    this.contact=contact;

    }

    public int getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
