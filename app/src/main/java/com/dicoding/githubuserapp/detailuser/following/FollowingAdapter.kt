package com.dicoding.githubuserapp.detailuser.following

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.databinding.ListMainUserBinding
import com.dicoding.githubuserapp.objectparcelable.User
import java.lang.StringBuilder

class FollowingAdapter:
    RecyclerView.Adapter<FollowingAdapter.FollowingHolder>() {

    private val mData = ArrayList<User>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setFollowingData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            FollowingHolder {

        val mView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_main_user, parent, false)
        return FollowingHolder(mView)
    }

    override fun onBindViewHolder(holder: FollowingHolder,
                                  position: Int) {
        holder.bind(mData[position])

        holder.itemView.setOnClickListener { onItemClickCallback
            .onItemClicked(mData[holder.absoluteAdapterPosition]) }
    }

    override fun getItemCount(): Int = mData.size

    inner class FollowingHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
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