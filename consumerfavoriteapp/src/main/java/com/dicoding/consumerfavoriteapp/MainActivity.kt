package com.dicoding.consumerfavoriteapp

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.consumerfavoriteapp.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.consumerfavoriteapp.databinding.ActivityMainBinding
import com.dicoding.consumerfavoriteapp.detailuser.activity.DetailUserActivity
import kotlinx.coroutines.*

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite"

        adapter = FavoriteAdapter()

        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.favoriteRecyclerView.setHasFixedSize(true)
        binding.favoriteRecyclerView.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler =  Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadNotesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI,
                    null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBar.visibility = View.INVISIBLE
            val favorite = deferredFavorite.await()
            Log.d("Favorite", favorite.toString())
            Log.d("Favorite", favorite.size.toString())
            if (favorite.size > 0) {
                adapter.listFavorite = favorite
            } else {
                adapter.listFavorite = ArrayList()

                Toast.makeText(this@MainActivity, "Tidak ada data favorite saat ini",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSelectedUser(data: User) {
        val intent = Intent(this, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_FAVORITE, data)
        startActivity(intent)
    }

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }
}