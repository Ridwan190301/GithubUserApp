package com.dicoding.consumerfavoriteapp.detailuser.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.consumerfavoriteapp.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var adapter: FollowingAdapter

    companion object {
        const val EXTRA_FOLLOWING = "extra_following"
        const val DATA_FOLLOWING = "data_following"

        fun newInstance(dataUser: ArrayList<String>): FollowingFragment {
            val mFollowingFragment = FollowingFragment()
            val mBundle = Bundle()
            mBundle.putString(EXTRA_FOLLOWING, dataUser[2])
            mBundle.putInt(DATA_FOLLOWING, dataUser[1].toInt())

            mFollowingFragment.arguments = mBundle
            return mFollowingFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments?.getInt(DATA_FOLLOWING) == 0) {
            showLoading(false)
        } else {
            followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(FollowingViewModel::class.java)

            showLoading(true)

            followingViewModel.setFollowingUser(arguments?.getString(EXTRA_FOLLOWING).toString())
            showListHomeViewModel()

            followingViewModel.getUsers().observe(viewLifecycleOwner, Observer {
                    userItems -> if (userItems != null) {
                        adapter.setFollowingData(userItems)
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
        adapter = FollowingAdapter()
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