package com.example.blixtest.room

import androidx.room.*
import modals.Friend
import modals.Message

@Dao
interface IMessagesDAO {
    @Query("SELECT * FROM messages_table WHERE parent_id = :parentId ORDER BY message_order ASC")
    suspend fun getMessagesChronologically(parentId: Int): List<Message>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: Message)

    @Query("DELETE FROM messages_table")
    suspend fun deleteAll()
}