package com.dicoding.githubuserapp.detailuser.followers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.databinding.ListMainUserBinding
import com.dicoding.githubuserapp.objectparcelable.User
import java.lang.StringBuilder

class FollowersAdapter: RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {
    private val mFollowersData = ArrayList<User>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setFollowersData(items: ArrayList<User>) {
        mFollowersData.clear()
        mFollowersData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder {
        val mView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_main_user, parent, false)
        return FollowersViewHolder(mView)
    }

    override fun onBindViewHolder(holder: FollowersViewHolder,
                                  position: Int) {
        holder.bind(mFollowersData[position])

        holder.itemView.setOnClickListener { onItemClickCallback
            .onItemClicked(mFollowersData[holder.absoluteAdapterPosition]) }
    }

    override fun getItemCount(): Int = mFollowersData.size

    inner class FollowersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ListMainUserBinding.bind(itemView)
        fun bind(user: User) {
            with(binding){
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .into(imgUser)

                tvName.text = user.name

                //Usernama
                val username = user.username
                tvUsername.text = StringBuilder("(")
                    .append(username)
                    .append(")")
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}