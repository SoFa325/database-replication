package com.company;

public class Record {
    int Id;
    String FirstName;
    String LastName;
    String Phone;
    String Email;
    public Record(int id, String fname, String lname, String phone, String email) {
        this.FirstName = fname;
        this.LastName = lname;
        this.Phone = phone;
        this.Email = email;
        this.Id = id;
    }
}
