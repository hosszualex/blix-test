package com.example.blixtest.room

import androidx.room.*
import modals.Friend
import modals.Message

@Dao
interface IMessagesDAO {
    @Query("SELECT * FROM messages_table ORDER BY id ASC")
    suspend fun getMessagesChronologically(): List<Message>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: Message)

    @Query("DELETE FROM messages_table")
    suspend fun deleteAll()
}