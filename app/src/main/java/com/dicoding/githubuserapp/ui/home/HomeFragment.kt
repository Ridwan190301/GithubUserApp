package com.dicoding.githubuserapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.databinding.FragmentHomeBinding
import com.dicoding.githubuserapp.detailuser.activity.DetailUserActivity
import com.dicoding.githubuserapp.objectparcelable.User
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var adapter: HomeUserAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding as FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(HomeViewModel::class.java)

        showLoading(true)
        homeViewModel.setSearchUser("sidiq")
        showListHomeViewModel()

        homeViewModel.getSearchUsers().observe(viewLifecycleOwner, {
                userItems -> if (userItems != null ) {
                    adapter.setData(userItems)
                    showLoading(false)
            }
        })

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = resources.getString(R.string.username)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    showLoading(false)
                } else {
                    homeViewModel.listData.clear()
                    homeViewModel.setSearchUser(query)
                    showLoading(true)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showListHomeViewModel() {
        adapter = HomeUserAdapter()
        adapter.notifyDataSetChanged()

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickCallback(object : HomeUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(data: User) {
        val moveData = Intent(activity, DetailUserActivity::class.java)
        moveData.putExtra(DetailUserActivity.EXTRA_DATA, data)
        startActivity(moveData)
    }

    private fun showLoading(condition: Boolean) {
        if (condition) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}