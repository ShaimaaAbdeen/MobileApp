package com.example.mrsshimaa.mobileapp;

/**
 * Created by Mrs.shimaa on 5/8/2018.
 */

public class DoctorsGroupTherapyDetails {

    public  String Name;
    public  String ID;
    public  String Email;
    public  String Password;
    public  String Phonenumber;
    public  String Position;
    public  String Patient;

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEmail() {
        return Email;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPassword() {
        return Password;
    }

    public void setPhonenumber(String phonenumber) {
        Phonenumber = phonenumber;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getPosition() {
        return Position;
    }

    public void setPatient(String patient) {
        Patient = patient;
    }

    public String getPatient() {
        return Patient;
    }
}
