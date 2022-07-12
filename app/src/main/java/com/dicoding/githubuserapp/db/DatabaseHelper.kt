package com.dicoding.githubuserapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.githubuserapp.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "dbgithubapp"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.FavoriteColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.FavoriteColumns.NAME} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.USERNAME} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.AVATAR} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.COMPANY} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.LOCATION} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.REPOSITORY} INT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.FOLLOWERS} INT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.FOLLOWING} INT NOT NULL)"
    }
}