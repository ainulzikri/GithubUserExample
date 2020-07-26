package com.example.kotlinsubmission2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kotlinsubmission2.R
import com.example.kotlinsubmission2.database.UserEntity
import kotlinx.android.synthetic.main.rv_home_item.view.*

class UserFavoriteAdapter :
    ListAdapter<UserEntity, UserFavoriteAdapter.FavoriteViewHolder>(DiffCallback()) {

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindFavorite(position: Int) {
            with(itemView) {
                val userEntity = getItem(position)
                userEntity.apply{
                    user_name.text = userEntity.login
                    Glide.with(context)
                        .load(userEntity.avatarUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(user_image)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem.avatarUrl == newItem.avatarUrl &&
                    oldItem.login == newItem.login
        }

    }

    fun getUserPosition(position: Int) : UserEntity{
        return getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rv_home_item, parent, false)
        return FavoriteViewHolder(v)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
       holder.bindFavorite(position)
    }

}

