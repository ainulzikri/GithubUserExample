package com.example.kotlinsubmission2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinsubmission2.model.UserDetailModel
import com.example.kotlinsubmission2.model.UserFollowModel
import com.example.kotlinsubmission2.model.UserSearchModel
import com.example.kotlinsubmission2.repository.UserRepository

class UserViewModel : ViewModel() {
    private val userRepository: UserRepository = UserRepository.getInstance()

    fun getUserSearch(query : String?) : LiveData<UserSearchModel>{
        return userRepository.getUserSearch(query)
    }

    fun getUserDetail(userName : String?) : LiveData<UserDetailModel>{
        return userRepository.getUserDetail(userName)
    }

    fun getUserFollowing(userName: String?) : LiveData<List<UserFollowModel>>{
        return userRepository.getUserFollowing(userName)
    }

    fun getUserFollower(userName: String?) : LiveData<List<UserFollowModel>>{
        return userRepository.getUserFollower(userName)
    }

}