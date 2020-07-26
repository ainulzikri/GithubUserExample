package com.example.kotlinsubmission2

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinsubmission2.adapter.UserAdapter
import com.example.kotlinsubmission2.alarm.AlarmNotification
import com.example.kotlinsubmission2.helper.ItemClickSupport
import com.example.kotlinsubmission2.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkAlarm()
        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserViewModel::class.java)

        adapter = UserAdapter(UserAdapter.HOME_ADAPTER)

        rv_home.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this)
            it.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
            it.setHasFixedSize(true)
        }

        search_empty.also {
            it.text = resources.getString(R.string.search_hint)
            it.visibility = View.VISIBLE
        }

        btn_see_favorite.setOnClickListener(this)
        btn_setting.setOnClickListener(this)
        searchData()
    }

    private fun checkAlarm(){
        val alarmNotification = AlarmNotification()
        val notificationKey = resources.getString(R.string.notification_key)
        val sharedPreferences =PreferenceManager.getDefaultSharedPreferences(this)
        val isAlarm = sharedPreferences.getBoolean(notificationKey, false)

        if(isAlarm){
            alarmNotification.setAlarm(this)
        }else{
            alarmNotification.cancelAlarm(this)
        }
    }

    private fun searchData() {
        val searchManager: SearchManager? =
            this.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        user_search.also {
            it.setSearchableInfo(searchManager?.getSearchableInfo(this.componentName))
            it.isFocusable = false
            it.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        search_empty.visibility = View.GONE
                        user_progress_bar.visibility = View.VISIBLE
                        disMissKeyboard()
                        getSearchData(userViewModel, query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (TextUtils.isEmpty(newText)) {
                        disMissKeyboard()
                        adapter.removeUserList()
                        adapter.notifyDataSetChanged()
                        search_count.visibility = View.GONE
                        card_view_rv.visibility = View.GONE
                        search_empty.text = resources.getString(R.string.search_hint)
                        search_empty.visibility = View.VISIBLE
                    }
                    return false
                }
            })

            it.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    disMissKeyboard()
                }
            }
        }
    }

    private fun disMissKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(user_search.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun getSearchData(userViewModel: UserViewModel, query: String) {
        userViewModel.getUserSearch(query)
            .observe(this@MainActivity, Observer { userSearch ->
                if (userSearch != null) {
                    val searchCount =
                        "Found <b> ${userSearch.totalCount}</b> result for <b>$query</b>"
                    search_count.text =
                        HtmlCompat.fromHtml(
                            searchCount,
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )

                    adapter.setUserList(userSearch.itemsEntity)
                    adapter.notifyDataSetChanged()

                    ItemClickSupport.addTo(rv_home)
                        .setOnItemClickListener(object : ItemClickSupport.OnItemClickListener {
                            override fun onItemClicked(
                                recyclerView: RecyclerView,
                                position: Int,
                                v: View
                            ) {
                                val intent =
                                    Intent(this@MainActivity, UserDetailActivity::class.java)
                                intent.putExtra(
                                    UserDetailActivity.EXTRA_NAME,
                                    userSearch.itemsEntity[position].login
                                )
                                startActivity(intent)

                            }

                        })

                    user_progress_bar.visibility = View.GONE
                    card_view_rv.visibility = View.VISIBLE
                    search_count.visibility = View.VISIBLE
                }

                if (userSearch.totalCount == 0) {
                    search_count.visibility = View.GONE
                    user_progress_bar.visibility = View.GONE
                    card_view_rv.visibility = View.GONE
                    search_empty.visibility = View.VISIBLE

                    val searchEmpty = "No Result Found for <b>$query</b>"
                    search_empty.text =
                        HtmlCompat.fromHtml(
                            searchEmpty,
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                }
            })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_see_favorite -> {
                val intent = Intent(this, UserFavorite::class.java)
                startActivity(intent)
            }
            R.id.btn_setting ->{
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
    }

}

