package com.dicoding.githubuserapp.detailuser.followers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.databinding.FragmentFollowersBinding
import com.dicoding.githubuserapp.detailuser.activity.DetailUserActivity
import com.dicoding.githubuserapp.objectparcelable.User
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding as FragmentFollowersBinding
    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var adapter: FollowersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentFollowersBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments?.getInt(EXTRA_FOLLOWERS) == 0) {
            showLoading(false)
        } else {
            followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(FollowersViewModel::class.java)

            showLoading(true)
            followersViewModel.setFollowersUser(arguments?.getString(FOLLOWERS_USERNAME).toString())
            showListHomeViewModel()
            followersViewModel.getUsers().observe(viewLifecycleOwner, {
                    userItems -> if (userItems != null) {
                        adapter.setFollowersData(userItems)
                        showLoading(false)
                    }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showListHomeViewModel() {
        adapter = FollowersAdapter()
        adapter.notifyDataSetChanged()

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickCallback(object : FollowersAdapter.OnItemClickCallback {
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

    companion object {
        const val FOLLOWERS_USERNAME = "extra_followers"
        const val EXTRA_FOLLOWERS = "data_followers"

        fun newInstance(dataUser: ArrayList<String>): FollowersFragment {
            val mFollowersFragment = FollowersFragment()
            val mBundle = Bundle()
            mBundle.putString(FOLLOWERS_USERNAME, dataUser[2])
            mBundle.putInt(EXTRA_FOLLOWERS, dataUser[0].toInt())

            mFollowersFragment.arguments = mBundle
            return mFollowersFragment
        }
    }
}