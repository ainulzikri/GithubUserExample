package com.example.kotlinsubmission2.helper

import android.view.View
import com.google.android.material.snackbar.Snackbar

class Helper {
    companion object {
        const val BASIC_URL = " https://api.github.com/"
        const val PERSONAL_TOKEN: String = "ee7460d8620a22ea8ef07f8ce73426c3ec36174b"
        const val TABLE_NAME = "favorite_table"
        const val DATABASE_NAME = "user"

        fun showSnackBar(view: View, message: Int) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        }

    }


}