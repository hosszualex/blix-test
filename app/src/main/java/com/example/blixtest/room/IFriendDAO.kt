package com.example.blixtest.room

import androidx.room.*
import modals.Friend

@Dao
interface IFriendDAO {
    @Query("SELECT * FROM friends_table ORDER BY id ASC")
    suspend fun getFriendsAlphabetically(): List<Friend>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(order: List<Friend>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: Friend)

    @Query("DELETE FROM friends_table")
    suspend fun deleteAll()
}