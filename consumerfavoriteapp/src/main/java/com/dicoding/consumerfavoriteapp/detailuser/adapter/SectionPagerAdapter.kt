package com.dicoding.consumerfavoriteapp.detailuser.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.consumerfavoriteapp.detailuser.followers.FollowersFragment
import com.dicoding.consumerfavoriteapp.detailuser.following.FollowingFragment

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