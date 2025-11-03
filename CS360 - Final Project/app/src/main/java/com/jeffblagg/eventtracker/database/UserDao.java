/*
 * UserDao.java
 *
 * Author: Jeff Blagg
 * Class: CS-360 - Mobile Architecture and Programming
 * Professor: Jerome DiMarzio
 * Date: October 2025
 */

package com.jeffblagg.eventtracker.database;

import com.jeffblagg.eventtracker.entities.User;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * Data Access Object (DAO) interface used by Room to handle
 * database operations on the {@link User} table.
 */
@Dao
public interface UserDao {
    /**
     * Inserts a new {@link User} into the database.
     *
     * @param user The user to insert.
     * @return The id of the inserted user.
     */
    @Insert
    long insert(User user);

    /**
     * Fetches a specific user by username.
     *
     * @param username The id of the user to fetch.
     * @return The user with the specified username.
     * Returns {@code null} if no user is found.
     */
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUser(String username);
}
