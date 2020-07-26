package com.example.kotlinsubmission2.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.kotlinsubmission2.R
import com.example.kotlinsubmission2.fragment.UserFollowerFragment
import com.example.kotlinsubmission2.fragment.UserFollowingFragment

class UserPagerAdapter(
    fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private lateinit var context : Context
    private var login : String? = null
    private var followerCount : Int? = 0
    private var followingCount : Int? = 0

    fun setData(
        context: Context,
        login: String?,
        followerCount: Int?,
        followingCount: Int?
    ) {
        this.context = context
        this.login = login
        this.followerCount = followerCount
        this.followingCount = followingCount
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position) {
            0 -> {
                fragment = UserFollowerFragment()
                fragment.setFollower(login)
            }
            1 -> {
                fragment = UserFollowingFragment()
                fragment.setFollowing(login)
            }
        }
        return fragment as Fragment
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {

        val follower = context.resources.getString(R.string.follower)
        val following = context.resources.getString(R.string.following)
        var title: String? = null

        when (position) {
            0 -> title = "$follower($followerCount)"
            1 -> title = "$following($followingCount)"
        }
        return title

    }
}