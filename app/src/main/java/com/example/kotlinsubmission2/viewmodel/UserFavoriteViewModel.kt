package com.example.kotlinsubmission2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.kotlinsubmission2.database.UserEntity
import com.example.kotlinsubmission2.repository.UserDbRepository

class UserFavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserDbRepository(application)
    private var allFavorite : LiveData<List<UserEntity>>? = null

    init {
        allFavorite = repository.getAllFavorites()
    }

    fun insert(userEntity: UserEntity){
        repository.insert(userEntity)
    }

    fun delete(userEntity: UserEntity){
        repository.delete(userEntity)
    }

    fun deleteAllFavorite(){
        repository.deleteAll()
    }

    fun getAllFavorite() : LiveData<List<UserEntity>>?{
        return allFavorite
    }

    fun checkFavorite(login: String) : LiveData<Boolean>?{
        return repository.checkFavorite(login)
    }
}