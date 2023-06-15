package com.example.naviku_versi_karisma.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Code::class], version = 1)
abstract class CodeRoomDatabase : RoomDatabase() {
    abstract fun codeDao(): CodeDao

    companion object {
        @Volatile
        private var INSTANCE: CodeRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): CodeRoomDatabase {
            if (INSTANCE == null) {
                synchronized(CodeRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, CodeRoomDatabase::class.java, "code_database")
                        .build()
                }
            }
            return INSTANCE as CodeRoomDatabase
        }
    }
}