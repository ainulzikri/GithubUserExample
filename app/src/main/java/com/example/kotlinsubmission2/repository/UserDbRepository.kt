package com.example.kotlinsubmission2.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.kotlinsubmission2.database.UserDao
import com.example.kotlinsubmission2.database.UserEntity
import com.example.kotlinsubmission2.database.UserFavoriteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class UserDbRepository(application: Application) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var userDao : UserDao? = null
    private var allFavorite :LiveData<List<UserEntity>>? = null

    init {
        val database = UserFavoriteDatabase.getDatabase(application)
        userDao = database?.userDao()
        allFavorite = userDao?.getAllFavorites()
    }

    fun insert(userEntity: UserEntity){
        launch { insertTask(userEntity) }
    }

    fun delete(userEntity: UserEntity){
        launch { deleteTask(userEntity) }
    }

    fun deleteAll(){
        launch { deleteAllFavoriteTask() }
    }

    fun getAllFavorites() : LiveData<List<UserEntity>>? {
        return allFavorite
    }

    fun checkFavorite(login : String) : LiveData<Boolean>? {
        return userDao?.checkFavorite(login)
    }

    private suspend fun insertTask(userEntity: UserEntity){
        withContext(Dispatchers.IO){
            userDao?.insert(userEntity)
        }
    }

    private suspend fun deleteTask(userEntity: UserEntity){
        withContext(Dispatchers.IO){
            userDao?.delete(userEntity)
        }
    }

    private suspend fun deleteAllFavoriteTask(){
        withContext(Dispatchers.IO){
            userDao?.deleteAllFavorite()
        }
    }

}