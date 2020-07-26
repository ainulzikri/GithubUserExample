package com.example.kotlinsubmission2.network


import com.example.kotlinsubmission2.helper.Helper
import com.example.kotlinsubmission2.model.UserDetailModel
import com.example.kotlinsubmission2.model.UserFollowModel
import com.example.kotlinsubmission2.model.UserSearchModel
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token ${Helper.PERSONAL_TOKEN}")
    fun getSearchUser(@Query("q") query:String?) : Call<UserSearchModel>

    @GET("users/{username}")
    @Headers("Authorization: token ${Helper.PERSONAL_TOKEN}")
    fun getDetailUser(@Path("username") username: String?) : Call<UserDetailModel>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${Helper.PERSONAL_TOKEN}")
    fun getFollower(@Path("username") username: String?) : Call<List<UserFollowModel>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${Helper.PERSONAL_TOKEN}")
    fun getFollowing(@Path("username") username: String?) : Call<List<UserFollowModel>>
}