package com.dicoding.githubuserapp.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.githubuserapp.helper.MappingHelper
import com.dicoding.githubuserapp.objectparcelable.User

internal class StackRemoteViewsFactory(private val context: Context):
    RemoteViewsService.RemoteViewsFactory {

    private var mWidgetItems = ArrayList<Bitmap>()
    private var data = arrayListOf<User>()

    override fun onCreate() {

        val cursor = context.contentResolver.query(CONTENT_URI,
            null, null, null, null)
        data = MappingHelper.mapCursorToArrayList(cursor)
    }

    override fun onDataSetChanged() {
        for (item in data) {
            val dataAvatar = Glide.with(context)
                .asBitmap().load(data[0].avatar)
                .submit()
                .get()
            mWidgetItems.add(dataAvatar)
        }
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])

        val extras = bundleOf(ImageFavoriteWidget.EXTRA_ITEM to position)
        val fillIntent = Intent()
        fillIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}