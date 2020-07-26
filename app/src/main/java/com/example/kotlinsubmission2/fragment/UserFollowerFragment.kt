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
import kotlinx.android.synthetic.main.fragment_user_follower.*

/**
 * A simple [Fragment] subclass.
 */
class UserFollowerFragment : Fragment() {
    private lateinit var  userViewModel : UserViewModel
    private lateinit var adapter : UserAdapter
    private var login : String? = null
    private  val saveLogin = "save_login"

    fun setFollower(login: String?){
        this.login = login
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(login != null){
            outState.putString(saveLogin, login)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(savedInstanceState != null){
            setFollower(savedInstanceState.getString(saveLogin))
        }
        adapter = UserAdapter(UserAdapter.FOLLOWER_ADAPTER)

        return inflater.inflate(R.layout.fragment_user_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_follower.adapter = adapter
        rv_follower.layoutManager = LinearLayoutManager(context)
        rv_follower.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        rv_follower.setHasFixedSize(true)
        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
        getFollowerData(login)
    }

    private fun getFollowerData(login: String?){
        userViewModel.getUserFollower(login).observe(viewLifecycleOwner, Observer { follower ->
           if(follower != null){
               bindFollowerData(follower)
               ItemClickSupport.addTo(rv_follower).setOnItemClickListener(object : ItemClickSupport.OnItemClickListener{
                   override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View) {
                       val intent = Intent(activity, UserDetailActivity::class.java)
                       intent.putExtra(UserDetailActivity.EXTRA_NAME, follower[position].login)
                       activity?.startActivity(intent)
                   }
               })
           }
        } )
    }

    private fun bindFollowerData(list : List<UserFollowModel>){
            adapter.setUserFollower(list)
            adapter.notifyDataSetChanged()
    }
}
