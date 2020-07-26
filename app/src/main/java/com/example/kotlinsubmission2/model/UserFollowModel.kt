package com.example.kotlinsubmission2.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserFollowModel(
    @Expose
    @SerializedName("login")
    val login: String,
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("avatar_url")
    val avatarUrl: String
) : Parcelable

