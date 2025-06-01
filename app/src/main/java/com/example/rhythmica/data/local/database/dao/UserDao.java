package com.example.rhythmica.data.local.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.lifecycle.LiveData;

import com.example.rhythmica.data.local.database.entities.User;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM users WHERE is_logged_in = 1 LIMIT 1")
    LiveData<User> getCurrentUser();

    @Query("SELECT * FROM users WHERE is_logged_in = 1 LIMIT 1")
    User getCurrentUserSync();

    @Query("UPDATE users SET is_logged_in = 0")
    void logoutAllUsers();

    @Query("DELETE FROM users")
    void deleteAllUsers();
}
