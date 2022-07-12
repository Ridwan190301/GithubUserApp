package com.dicoding.consumerfavoriteapp

import android.database.Cursor
import java.util.*

object MappingHelper {

    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<User> {
        val favoriteList = ArrayList<User>()

        favoriteCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.NAME))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOCATION))
                val repository = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.REPOSITORY))
                val followers = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWERS))
                val following = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWING))
                favoriteList.add(User(id, name, username, avatar, company,
                    location, repository, followers, following))
            }
        }
        return favoriteList
    }

    fun mapCursorToObject(favoriteCursor: Cursor?): User {
        var user = User()
        favoriteCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
            val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.NAME))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
            val company = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COMPANY))
            val location = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOCATION))
            val repository = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.REPOSITORY))
            val followers = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWERS))
            val following = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWING))
            user = User(id, name, username, avatar, company, location, repository, followers, following)
        }
        return user
    }
}