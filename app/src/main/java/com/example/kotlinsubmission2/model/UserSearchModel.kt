package com.example.kotlinsubmission2.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class UserSearchModel(
    @Expose
    @SerializedName("total_count")
    val totalCount : Int,

    @Expose
    @SerializedName("items")
    val itemsEntity: @RawValue List<ItemsEntity>

) : Parcelable {
    data class ItemsEntity(
        @Expose
        @SerializedName("login")
        val login: String,
        @Expose
        @SerializedName("id")
        val id: Int,
        @Expose
        @SerializedName("avatar_url")
        val avatarUrl: String
    )
}