package com.example.rhythmica.data.local.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "avatar_url")
    public String avatarUrl;

    @ColumnInfo(name = "auth_token")
    public String authToken;

    @ColumnInfo(name = "is_logged_in")
    public boolean isLoggedIn;

    @ColumnInfo(name = "last_sync")
    public long lastSync;

    public User() {}

    public User(int id, String username, String email, String authToken) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.authToken = authToken;
        this.isLoggedIn = true;
        this.lastSync = System.currentTimeMillis();
    }
}