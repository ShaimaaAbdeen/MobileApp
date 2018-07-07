package com.example.mrsshimaa.mobileapp;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Mrs.shimaa on 3/13/2018.
 */

public class HospitalIntensiveDetails implements  Comparable {
    public String Name;
    public String Address;
    public String ContactNumber;
    public String Email;
    public String Location;
    public int Rooms;
    public String RoomType;
    public  int Number;
    public String distance="";
    public String time="";
    public ArrayList<String> RoomTypes;
    public ArrayList<Integer>Roomnumbers;
    public ImageView distimg;
    public ImageView timeimg;




    public  HospitalIntensiveDetails()
    {
        setDistance("");
        setTime("");
    }

    public void setName(String name) {
        Name = name;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setRooms(int rooms) {
        Rooms = rooms;
    }

    public void setRoomType(String roomType) {
        RoomType = roomType;
    }

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public String getEmail() {
        return Email;
    }

    public String getLocation() {
        return Location;
    }

    public int getRooms() {
        return Rooms;
    }

    public String getRoomType() {
        return RoomType;
    }
    public void  setRoomTypes(ArrayList<String>RT)
    {
        RoomTypes=RT;
    }
    public void setRoomnumbers(ArrayList<Integer>RN)
    {
        Roomnumbers=RN;
    }
    public  ArrayList<String>getRoomTypes()
    {
        return  RoomTypes;

    }
    public ArrayList<Integer>getRoomnumbers()
    {
        return Roomnumbers;
    }
    public  void  setNumber(int number)
    {
        Number=number;
    }
    public  int getNumber()
    {
        return  Number;
    }

    public void setDistance(String dist)
    {
        distance=dist;
    }

    public  String getDistance(){
        return  distance;
    }

    public void setTime(String t)
    {
        time =t;
    }

    public  String getTime(){
        return  time;
    }

    public void setDistimg(ImageView img)
    {
        distimg=img;
    }

    public  ImageView getDistimg()
    {
        return  distimg;
    }

    public  void  setTimeimg(ImageView img)
    {
        timeimg=img;
    }
    public  ImageView getTimeimg()
    {
        return  timeimg;
    }




    @Override
    public int compareTo(@NonNull Object o) {
        if(((HospitalIntensiveDetails)o).getDistance().toString().compareTo("")==1) {
            int comparedist = Integer.parseInt(((HospitalIntensiveDetails) o).getDistance());
            return Integer.parseInt(this.distance) - comparedist;
        }
        else
            return 0;
    }
}




