package com.example.kotlinsubmission2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kotlinsubmission2.adapter.UserPagerAdapter
import com.example.kotlinsubmission2.database.UserEntity
import com.example.kotlinsubmission2.helper.Helper
import com.example.kotlinsubmission2.model.UserDetailModel
import com.example.kotlinsubmission2.viewmodel.UserFavoriteViewModel
import com.example.kotlinsubmission2.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetailActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: UserPagerAdapter
    private lateinit var userfavoriteViewModel: UserFavoriteViewModel

    companion object {
        const val EXTRA_NAME = "extra_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserViewModel::class.java)

        userfavoriteViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                UserFavoriteViewModel::class.java
            )

        setActionBar(supportActionBar)

        val login = intent.getStringExtra(EXTRA_NAME)
        adapter = UserPagerAdapter(supportFragmentManager)

        if (login != null) {
            userViewModel.getUserDetail(login).observe(this@UserDetailActivity, Observer { detail ->
                setDetail(detail, login)
            })
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.menu_favorite ->{
                val intent = Intent(this@UserDetailActivity, UserFavorite::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_setting ->{
                val intent = Intent(this@UserDetailActivity, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setActionBar(supportActionBar: ActionBar?){
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun setDetail(detail: UserDetailModel?, login: String?) {
        val empty = resources.getString(R.string.empty)
        user_company.append(if (detail?.company != null) detail.company else empty)

        user_location.append(if (detail?.location != null) detail.location else empty)
        user_login.text = if (detail?.login != null) detail.login else empty
        user_name.text = if (detail?.name != null) resources.getString(
            R.string.user_name,
            detail.name
        ) else empty
        user_repository.append(
            if (detail?.publicRepos.toString()
                    .isNotBlank()
            ) detail?.publicRepos.toString() else empty
        )

        Glide.with(this).load(detail?.avatarUrl)
            .error(R.drawable.ic_launcher_background)
            .apply(RequestOptions.circleCropTransform())
            .into(user_image)

        adapter.setData(
            context = this,
            followerCount = detail?.followers,
            followingCount = detail?.following,
            login = login
        )
        adapter.notifyDataSetChanged()
        setTabLayoutAdapter()

        detail?.let {
            checkUserFavorite(detail.login, detail)
        }

    }

    private fun setTabLayoutAdapter() {
        user_view_pager.adapter = adapter
        user_tab_layout.setupWithViewPager(user_view_pager)
    }

    private fun checkUserFavorite(login: String, userDetailModel: UserDetailModel) {
            userfavoriteViewModel.checkFavorite(login)?.observe(this, Observer { isFavorite ->
                Log.d("FAVORITE", isFavorite.toString())

                if (isFavorite != null) {
                    if (isFavorite == true) {
                        setBtnFavoriteImage(R.drawable.favorite)
                        btn_favorite.setOnClickListener {
                            showAlertDialog(userDetailModel)
                        }

                    } else {
                        setBtnFavoriteImage(R.drawable.not_favorite)
                        btn_favorite.setOnClickListener {
                            insertToFavorite(userDetailModel)
                        }
                    }
                }


            })
    }

    private fun insertToFavorite(userDetailModel: UserDetailModel) {
        val userEntity = convertToUserEntity(userDetailModel)
        userfavoriteViewModel.insert(userEntity)
        setBtnFavoriteImage(R.drawable.favorite)
        Helper.showSnackBar(btn_favorite, R.string.insert_success)

        btn_favorite.setOnClickListener {
            showAlertDialog(userDetailModel)
        }
    }

    private fun showAlertDialog(userDetailModel: UserDetailModel) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        val userEntity = convertToUserEntity(userDetailModel)
        alertDialogBuilder.apply {
            setTitle(R.string.remove_favorite_title)
            setMessage(R.string.remove_favorite_message)
            setCancelable(false)
            setPositiveButton(R.string.yes) { _, _ ->
                userfavoriteViewModel.delete(userEntity)
                setBtnFavoriteImage(R.drawable.not_favorite)
                Helper.showSnackBar(btn_favorite, R.string.remove_success)

                btn_favorite.setOnClickListener {
                    insertToFavorite(userDetailModel)
                }

            }
            setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
            }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun convertToUserEntity(userDetailModel: UserDetailModel): UserEntity {
        val userEntity = UserEntity()
        userEntity.apply {
            id = userDetailModel.id
            login = userDetailModel.login
            avatarUrl = userDetailModel.avatarUrl
        }
        return userEntity
    }

    private fun setBtnFavoriteImage(image: Int) {
        btn_favorite.setImageDrawable(
            resources.getDrawable(
                image,
                applicationContext.theme
            )
        )
    }




}


