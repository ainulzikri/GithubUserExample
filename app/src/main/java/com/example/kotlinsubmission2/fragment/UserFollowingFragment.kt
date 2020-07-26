package com.example.kotlinsubmission2.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinsubmission2.R
import com.example.kotlinsubmission2.UserDetailActivity
import com.example.kotlinsubmission2.adapter.UserAdapter
import com.example.kotlinsubmission2.helper.ItemClickSupport
import com.example.kotlinsubmission2.model.UserFollowModel
import com.example.kotlinsubmission2.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_user_following.*

/**
 * A simple [Fragment] subclass.
 */
class UserFollowingFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: UserAdapter
    private var login: String? = null
    private val saveLogin = "save_login"

    fun setFollowing(login: String?) {
        this.login = login
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(saveLogin, login)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (savedInstanceState != null) {
            setFollowing(savedInstanceState.getString(saveLogin))
        }
        return inflater.inflate(R.layout.fragment_user_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserViewModel::class.java)
        adapter = UserAdapter(UserAdapter.FOLLOWING_ADAPTER)

        rv_following.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
            it.setHasFixedSize(true)
            it.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        getFollowingData(login)
    }

    private fun getFollowingData(login: String?) {
        userViewModel.getUserFollowing(login).observe(viewLifecycleOwner, Observer { following ->
            if (following != null) {
                bindFollowingData(following)

                ItemClickSupport.addTo(rv_following)
                    .setOnItemClickListener(object : ItemClickSupport.OnItemClickListener {
                        override fun onItemClicked(
                            recyclerView: RecyclerView,
                            position: Int,
                            v: View
                        ) {
                            val intent = Intent(activity, UserDetailActivity::class.java)
                            intent.putExtra(
                                UserDetailActivity.EXTRA_NAME,
                                following[position].login
                            )
                            activity?.startActivity(intent)
                        }
                    })
            }
        })
    }

    private fun bindFollowingData(list: List<UserFollowModel>) {
        adapter.setUserFollowing(list)
        adapter.notifyDataSetChanged()
    }
}
