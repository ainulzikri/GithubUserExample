package com.example.kotlinsubmission2

import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinsubmission2.adapter.UserFavoriteAdapter
import com.example.kotlinsubmission2.helper.Helper
import com.example.kotlinsubmission2.helper.ItemClickSupport
import com.example.kotlinsubmission2.viewmodel.UserFavoriteViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_user_favorite.*

class UserFavorite : AppCompatActivity() {

    private lateinit var userFavoriteViewModel: UserFavoriteViewModel
    private lateinit var favoriteAdapter: UserFavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_favorite)
        setActionBar(supportActionBar)
        userFavoriteViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                UserFavoriteViewModel::class.java
            )

        favoriteAdapter = UserFavoriteAdapter()
        setFavoriteRv()
    }

    private fun setFavoriteRv() {
        rv_favorite.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(this@UserFavorite)
            addItemDecoration(
                DividerItemDecoration(
                    this@UserFavorite,
                    LinearLayoutManager.VERTICAL
                )
            )
            setHasFixedSize(true)
        }

        userFavoriteViewModel.getAllFavorite()?.observe(this, Observer { list ->
            if (list.isNotEmpty()) {
                favoriteAdapter.submitList(list)
                favorite_empty.visibility = View.GONE
                rv_favorite.visibility = View.VISIBLE

                ItemClickSupport.addTo(rv_favorite).setOnItemClickListener(object : ItemClickSupport.OnItemClickListener{
                    override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View) {
                        val intent = Intent(this@UserFavorite, UserDetailActivity::class.java)
                        intent.putExtra(UserDetailActivity.EXTRA_NAME, list[position].login)
                        startActivity(intent)
                    }

                })
            } else {
                rv_favorite.visibility = View.GONE
                favorite_empty.setText(R.string.no_favorite)
                favorite_empty.visibility = View.VISIBLE
            }
        })

        setDeleteUserFavorite()

        if (favoriteAdapter.itemCount == 0) {
            rv_favorite.visibility = View.GONE
            favorite_empty.setText(R.string.no_favorite)
            favorite_empty.visibility = View.VISIBLE
        } else {
            rv_favorite.visibility = View.VISIBLE
            favorite_empty.visibility = View.GONE
        }
    }

    private fun setDeleteUserFavorite() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            var background = ColorDrawable(
                ContextCompat.getColor(
                    this@UserFavorite,
                    android.R.color.holo_red_dark
                )
            )
            val deleteIcon = ContextCompat.getDrawable(this@UserFavorite, R.drawable.ic_delete)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val alertDialogBuilder = AlertDialog.Builder(this@UserFavorite)
                alertDialogBuilder.apply {
                    setTitle(R.string.remove_favorite_title)
                    setMessage(R.string.remove_favorite_message)
                    setCancelable(false)
                    setPositiveButton(R.string.yes) { _, _ ->
                        val position = viewHolder.adapterPosition
                        val userEntity = favoriteAdapter.getUserPosition(position)
                        userFavoriteViewModel.delete(userEntity)
                        if (favoriteAdapter.itemCount == 1) {
                            favoriteAdapter.submitList(null)
                        }

                        Snackbar.make(rv_favorite, R.string.remove_success, Snackbar.LENGTH_SHORT)
                            .setAction(
                                R.string.undo
                            ) {
                                userFavoriteViewModel.insert(userEntity)
                            }.show()
                    }
                    setNegativeButton(R.string.no) { dialog, _ ->
                        favoriteAdapter.notifyItemChanged(viewHolder.adapterPosition)
                        dialog.cancel()
                    }
                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top
                val intrinsicHeight = deleteIcon!!.intrinsicHeight
                val intrinsicWidth = deleteIcon.intrinsicWidth
                val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
                val deleteIconLeft = itemView.left + deleteIconMargin
                val deleteIconRight = itemView.left + deleteIconMargin + intrinsicWidth
                val deleteIconBottom = deleteIconTop + intrinsicHeight

                //dX > 0 => swipe right and dx < 0 => swipe left
                //change itemview.left in deleteiconright/left to right and minus to plus if you want to swipe right

                if (dX > 0) {
                    background.setBounds(
                        itemView.left,
                        itemView.top,
                        itemView.left+ dX.toInt() ,
                        itemView.bottom
                    )
                    deleteIcon.setBounds(
                        deleteIconLeft,
                        deleteIconTop,
                        deleteIconRight,
                        deleteIconBottom
                    )
                }else {
                    background.setBounds(0, 0, 0, 0)
                }

                background.draw(c)
                deleteIcon.draw(c)

            }
        }).attachToRecyclerView(rv_favorite)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favorite_delete_all -> {
                if (favoriteAdapter.itemCount != 0) {
                    val alertDialogBuilder = AlertDialog.Builder(this@UserFavorite)
                    alertDialogBuilder.apply {
                        setTitle(R.string.remove_all_favorite_title)
                        setMessage(R.string.remove_all_favorite_message)
                        setCancelable(false)
                        setPositiveButton(R.string.yes) { _, _ ->
                            favoriteAdapter.submitList(null)
                            userFavoriteViewModel.deleteAllFavorite()
                            Helper.showSnackBar(rv_favorite, R.string.remove_all_success)
                        }
                        setNegativeButton(R.string.no) { dialog, _ ->
                            dialog.cancel()
                        }
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                } else {
                    Helper.showSnackBar(rv_favorite, R.string.favorite_already_empty)
                }
                return true
            }

            R.id.menu_setting ->{
                val intent = Intent(this@UserFavorite, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }

            android.R.id.home -> {
                onBackPressed()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setActionBar(supportActionBar: ActionBar?) {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
            setTitle(R.string.favorite_icon)
        }
    }
}




