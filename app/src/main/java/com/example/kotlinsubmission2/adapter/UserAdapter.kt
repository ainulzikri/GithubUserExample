package com.example.kotlinsubmission2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kotlinsubmission2.R
import com.example.kotlinsubmission2.model.UserFollowModel
import com.example.kotlinsubmission2.model.UserSearchModel
import kotlinx.android.synthetic.main.rv_home_item.view.*

class UserAdapter(private val key: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val HOME_ADAPTER = "home_adapter"
        const val FOLLOWER_ADAPTER = "follower_adapter"
        const val FOLLOWING_ADAPTER = "following_adapter"
    }

    private var listSearch: List<UserSearchModel.ItemsEntity>? = null
    private var listFollower: List<UserFollowModel>? = null
    private var listFollowing: List<UserFollowModel>? = null

    inner class UserViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bindSearch(position: Int, listSearch: List<UserSearchModel.ItemsEntity>?) {
            with(itemView) {
                listSearch?.let {
                    user_name.text = it[position].login
                    Glide.with(context).load(it[position].avatarUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(user_image)
                }
            }
        }
    }

    inner class FollowerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bindFollower(position: Int, listFollower: List<UserFollowModel>?) {
            with(itemView) {
                listFollower?.let{
                    user_name.text = it[position].login
                    Glide.with(context).load(it[position].avatarUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(user_image)
                }
            }
        }
    }

    inner class FollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindFollowing(position: Int, listFollowing: List<UserFollowModel>?) {
            with(itemView) {
                listFollowing?.let {
                    user_name.text = it[position].login
                    Glide.with(context).load(it[position].avatarUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(user_image)
                }
            }
        }
    }

    fun setUserList(listSearch: List<UserSearchModel.ItemsEntity>) {
        this.listSearch = listSearch
    }

    fun setUserFollower(listFollower: List<UserFollowModel>) {
        this.listFollower = listFollower
    }

    fun setUserFollowing(listFollowing: List<UserFollowModel>) {
        this.listFollowing = listFollowing
    }

    fun removeUserList() {
        this.listSearch = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rv_home_item, parent, false)

        return when (key) {
            HOME_ADAPTER -> UserViewHolder(v)
            FOLLOWER_ADAPTER -> FollowerViewHolder(v)
            else -> FollowingViewHolder(v)
        }
    }

    override fun getItemCount(): Int {
        return when (key) {
            HOME_ADAPTER -> listSearch?.size ?: 0
            FOLLOWER_ADAPTER -> listFollower?.size ?: 0
            FOLLOWING_ADAPTER -> listFollowing?.size ?: 0
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (key) {
            HOME_ADAPTER -> {
                val userHolder = holder as UserViewHolder
                userHolder.bindSearch(position, listSearch)
            }
            FOLLOWER_ADAPTER -> {
                val followerHolder = holder as FollowerViewHolder
                followerHolder.bindFollower(position, listFollower)
            }
            FOLLOWING_ADAPTER -> {
                val followingHolder = holder as FollowingViewHolder
                followingHolder.bindFollowing(position, listFollowing)
            }
            else -> return
        }
    }

}