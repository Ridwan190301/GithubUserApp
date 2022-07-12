package com.dicoding.consumerfavoriteapp.detailuser.followers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.consumerfavoriteapp.databinding.FragmentFollowersBinding
import com.dicoding.githubuserapp.detailuser.followers.FollowersAdapter

class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var adapter: FollowersAdapter

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
    }

    private fun showLoading(condition: Boolean) {
        if (condition) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}