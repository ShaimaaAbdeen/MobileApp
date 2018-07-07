package com.example.mrsshimaa.mobileapp;

/**
 * Created by Mrs.shimaa on 2/21/2018.
 */

public class HospitalBloodDetails {

    public String Name;
    public String Address;
    public String ContactNumber;
    public String Email;
    public String AvailableBloodTypes;
    public String NeededBloodTypes;


    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAddress() {
        return Address;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEmail() {
        return Email;
    }

    public void setAvailableBloodTypes(String availableBloodTypes) {
        AvailableBloodTypes = availableBloodTypes;
    }

    public String getAvailableBloodTypes() {
        return AvailableBloodTypes;
    }

    public void setNeededBloodTypes(String neededBloodTypes) {
        NeededBloodTypes = neededBloodTypes;
    }

    public String getNeededBloodTypes() {
        return NeededBloodTypes;
    }
}
