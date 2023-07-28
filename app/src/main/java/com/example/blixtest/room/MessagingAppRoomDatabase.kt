package com.example.blixtest.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import modals.Friend
import modals.Message

@Database(entities = [Friend::class, Message::class], version = 4, exportSchema = false)
abstract class MessagingAppRoomDatabase : RoomDatabase() {
    abstract fun friendDAO(): IFriendDAO
    abstract fun messageDAO(): IMessagesDAO

    companion object {
        @Volatile
        private var INSTANCE: MessagingAppRoomDatabase? = null

        fun getDatabase(context: Context): MessagingAppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MessagingAppRoomDatabase::class.java,
                    "blix_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}