package com.example.kotlinsubmission2.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)

    @Query("DELETE FROM favorite_table")
    suspend fun deleteAllFavorite()

    @Query("SELECT * FROM favorite_table ORDER BY id DESC")
    fun getAllFavorites(): LiveData<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_table WHERE login = :login)")
     fun checkFavorite(login: String) : LiveData<Boolean>
}