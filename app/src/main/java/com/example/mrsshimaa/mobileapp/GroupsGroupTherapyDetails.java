package com.example.mrsshimaa.mobileapp;

/**
 * Created by Mrs.shimaa on 5/8/2018.
 */

public class GroupsGroupTherapyDetails {
    String Doctorid;
    String Groupname;
    String Username;
    String ID;
    String DoctorFirstname;
    String DoctorLastname;

    public void setDoctorid(String doctorid) {
        Doctorid = doctorid;
    }

    public String getDoctorid() {
        return Doctorid;
    }

    public void setGroupname(String groupname) {
        Groupname = groupname;
    }

    public String getGroupname() {
        return Groupname;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUsername() {
        return Username;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setDoctorFirstname(String doctorFirstname) {
        DoctorFirstname = doctorFirstname;
    }

    public String getDoctorFirstname() {
        return DoctorFirstname;
    }

    public void setDoctorLastname(String doctorLastname) {
        DoctorLastname = doctorLastname;
    }

    public String getDoctorLastname() {
        return DoctorLastname;
    }
}
