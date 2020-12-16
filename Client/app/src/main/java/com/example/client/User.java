package com.example.client;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import Model.Kupovina;

public class User implements Parcelable {
    private String username;
    private String password;
    private Kupovina kupovina;

    protected User(Parcel in) {
        username = in.readString();
        password = in.readString();
        kupovina = in.readParcelable(Kupovina.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Kupovina getKupovina() {
        return kupovina;
    }

    public void setKupovina(Kupovina kupovina) {
        this.kupovina = kupovina;
    }

    public User(String username, String password, Kupovina kupovina) {
        this.username = username;
        this.password = password;
        this.kupovina = kupovina;
    }

    public User(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeParcelable(kupovina, flags);
    }
}
