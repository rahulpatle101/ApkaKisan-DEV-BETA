package com.apkakisan.myapplication;

import java.util.Date;

public class UserHelperClass {

    String fullName, location, createdDate, modifiedDate, phoneNumber, pinCode;

    public UserHelperClass(String fullName, String location, String phoneNumber, String pinCode, String createdDate, String modifiedDate) {
        this.fullName = fullName;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.pinCode = pinCode;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public UserHelperClass() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }


}
