package com.dicoding.githubuserapp.ui.favorite

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.databinding.FragmentFavoriteBinding
import com.dicoding.githubuserapp.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.githubuserapp.detailuser.activity.DetailUserActivity
import com.dicoding.githubuserapp.helper.MappingHelper
import com.dicoding.githubuserapp.objectparcelable.User
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

@DelicateCoroutinesApi
class FavoriteFragment : Fragment() {

    private lateinit var adapter: FavoriteAdapter
    private var _binding: FragmentFavoriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoriteAdapter()

        binding?.favoriteRecyclerView?.layoutManager = LinearLayoutManager(context)
        binding?.favoriteRecyclerView?.setHasFixedSize(true)
        binding?.favoriteRecyclerView?.adapter = adapter

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

        context?.contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding?.progressBar?.visibility = View.VISIBLE
            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = context?.contentResolver?.query(CONTENT_URI,
                    null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding?.progressBar?.visibility = View.INVISIBLE
            val favorite = deferredFavorite.await()
            if (favorite.size > 0) {
                adapter.listFavorite = favorite
            } else {
                adapter.listFavorite = ArrayList()
                view?.let {
                    Snackbar.make(it, "Tidak ada data favorit saat ini", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun showSelectedUser(data: User) {
        Log.d("Data", data.toString())
        val intent = Intent(context, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_FAVORITE, data)
        startActivity(intent)
    }
}