package com.example.sagliklibeslen.Model;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class GetGunlukModel implements Serializable {
    public int UsersId;
    public Timestamp Date;

    public GetGunlukModel(int usersId, Timestamp date) {
        UsersId = usersId;
        Date = date;
    }

    public GetGunlukModel() {
    }

    @Override
    public String toString() {
        return "GetGunlukModel{" +
                "UsersId=" + UsersId +
                ", Date=" + Date +
                '}';
    }

    public int getUsersId() {
        return UsersId;
    }

    public void setUsersId(int usersId) {
        UsersId = usersId;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(Timestamp date) {
        Date = date;
    }
}
