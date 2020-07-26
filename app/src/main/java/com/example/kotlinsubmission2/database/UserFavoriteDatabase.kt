package com.example.kotlinsubmission2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kotlinsubmission2.helper.Helper

@Database(entities = [UserEntity::class], version = 2)
abstract class UserFavoriteDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: UserFavoriteDatabase? = null

        fun getDatabase(context: Context): UserFavoriteDatabase? {
            if (instance == null) {
                synchronized(UserFavoriteDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserFavoriteDatabase::class.java,
                        Helper.DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance
        }
    }
}