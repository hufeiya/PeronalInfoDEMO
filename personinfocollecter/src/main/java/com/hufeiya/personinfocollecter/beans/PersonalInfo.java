package com.hufeiya.personinfocollecter.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hufeiya on 16/6/4.
 */
public class PersonalInfo implements Parcelable {
    private String name;
    private String phone;
    private String address;

    public PersonalInfo() {

    }

    public PersonalInfo(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    protected PersonalInfo(Parcel in) {
        name = in.readString();
        phone = in.readString();
        address = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PersonalInfo> CREATOR = new Creator<PersonalInfo>() {
        @Override
        public PersonalInfo createFromParcel(Parcel in) {
            return new PersonalInfo(in);
        }

        @Override
        public PersonalInfo[] newArray(int size) {
            return new PersonalInfo[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(address);
    }

    @Override
    public String toString() {
        return name + ' ' + phone + ' ' + address;
    }
}
