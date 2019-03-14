package com.example.shodan.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "locations")
public class Location implements Serializable {
    @NonNull
    @PrimaryKey
    public String search_location;      //primary Key
}
