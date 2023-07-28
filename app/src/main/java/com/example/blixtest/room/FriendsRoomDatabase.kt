package com.example.blixtest.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import modals.Friend

@Database(entities = [Friend::class], version = 1, exportSchema = false)
abstract class FriendsRoomDatabase: RoomDatabase() {
    abstract fun friendDAO(): IFriendDAO

    companion object {
        @Volatile
        private var INSTANCE: FriendsRoomDatabase? = null

        fun getDatabase(context: Context): FriendsRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FriendsRoomDatabase::class.java,
                    "blix_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}