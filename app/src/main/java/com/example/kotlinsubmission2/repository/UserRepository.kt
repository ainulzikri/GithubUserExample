package com.example.kotlinsubmission2.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.kotlinsubmission2.R
import com.example.kotlinsubmission2.model.UserDetailModel
import com.example.kotlinsubmission2.model.UserFollowModel
import com.example.kotlinsubmission2.model.UserSearchModel
import com.example.kotlinsubmission2.network.ApiService
import com.example.kotlinsubmission2.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private var apiService: ApiService) {

    companion object{
        val TAG = UserRepository::class.java.simpleName
        private var userRepository: UserRepository? = null

        fun getInstance() : UserRepository{
            if(userRepository == null){
                userRepository = UserRepository(RetrofitService.createService(ApiService::class.java))
            }
            return userRepository as UserRepository
        }
    }

    fun getUserSearch(query : String?) : MutableLiveData<UserSearchModel>{
       val list = MutableLiveData<UserSearchModel>()

        apiService.getSearchUser(query).enqueue(object : Callback<UserSearchModel> {
            override fun onFailure(call: Call<UserSearchModel>, t: Throwable) {
                Log.e(TAG, "${R.string.error1} : ${t.cause}")
                Log.e(TAG, "${R.string.error2} : ${t.localizedMessage}")
                Log.e(TAG, "${R.string.error3} : ${t.message}")
            }

            override fun onResponse(call: Call<UserSearchModel>, response: Response<UserSearchModel>) {
               if(response.isSuccessful){
                    if(response.body() != null){
                        list.postValue(response.body())
                    }
               }else{
                   Log.d(TAG, "${R.string.response_failure} : ${response.message()}" )
               }
            }

        })
        return list
    }

    fun getUserDetail(userName : String?) : MutableLiveData<UserDetailModel>{
        val list = MutableLiveData<UserDetailModel>()

        apiService.getDetailUser(userName).enqueue(object : Callback<UserDetailModel>{
            override fun onFailure(call: Call<UserDetailModel>, t: Throwable) {
                Log.e(TAG, "${R.string.error1} : ${t.cause}")
                Log.e(TAG, "${R.string.error2} : ${t.localizedMessage}")
                Log.e(TAG, "${R.string.error3} : ${t.message}")
            }

            override fun onResponse(call: Call<UserDetailModel>, response: Response<UserDetailModel>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        list.postValue(response.body())
                    }
                }else{
                    Log.d(TAG, "${R.string.response_failure} : ${response.message()}")
                }
            }

        })
        return list
    }

    fun getUserFollower(userName : String?) : MutableLiveData<List<UserFollowModel>>{
        val list = MutableLiveData<List<UserFollowModel>>()

        apiService.getFollower(userName).enqueue(object : Callback<List<UserFollowModel>>{
            override fun onFailure(call: Call<List<UserFollowModel>>, t: Throwable) {
                Log.e(TAG, "${R.string.error1} : ${t.cause}")
                Log.e(TAG, "${R.string.error2} : ${t.localizedMessage}")
                Log.e(TAG, "${R.string.error3} : ${t.message}")
            }

            override fun onResponse(call: Call<List<UserFollowModel>>, response: Response<List<UserFollowModel>>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        list.postValue(response.body())
                    }
                }else{
                    Log.d(TAG, "${R.string.response_failure} : ${response.message()}")
                }
            }

        })
        return list
    }

    fun getUserFollowing(userName : String?) : MutableLiveData<List<UserFollowModel>>{
        val list = MutableLiveData<List<UserFollowModel>>()

        apiService.getFollowing(userName).enqueue(object : Callback<List<UserFollowModel>>{
            override fun onFailure(call: Call<List<UserFollowModel>>, t: Throwable) {
                Log.e(TAG, "${R.string.error1} : ${t.cause}")
                Log.e(TAG, "${R.string.error2} : ${t.localizedMessage}")
                Log.e(TAG, "${R.string.error3} : ${t.message}")
            }

            override fun onResponse(
                call: Call<List<UserFollowModel>>,
                response: Response<List<UserFollowModel>>
            ) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        list.postValue(response.body())
                    }
                }else{
                    Log.d(TAG, "${R.string.response_failure} : ${response.message()}")
                }
            }

        })
        return list
    }

}