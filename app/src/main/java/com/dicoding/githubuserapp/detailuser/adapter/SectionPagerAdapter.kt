package com.dicoding.githubuserapp.detailuser.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubuserapp.detailuser.followers.FollowersFragment
import com.dicoding.githubuserapp.detailuser.following.FollowingFragment
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class SectionPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    var data = arrayListOf<String>()

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowingFragment.newInstance(data)
            1 -> fragment = FollowersFragment.newInstance(data)
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}