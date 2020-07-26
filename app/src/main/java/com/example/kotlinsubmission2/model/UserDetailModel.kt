package com.example.kotlinsubmission2.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDetailModel(
    @Expose
    @SerializedName("login")
    var login: String,

    @Expose
    @SerializedName("id")
    var id: Int,

    @Expose
    @SerializedName("avatar_url")
    var avatarUrl: String,

    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("company")
    val company: String,

    @Expose
    @SerializedName("location")
    val location: String,

    @Expose
    @SerializedName("public_repos")
    val publicRepos: Int,

    @Expose
    @SerializedName("followers")
    val followers: Int,

    @Expose
    @SerializedName("following")
    val following: Int
) : Parcelable