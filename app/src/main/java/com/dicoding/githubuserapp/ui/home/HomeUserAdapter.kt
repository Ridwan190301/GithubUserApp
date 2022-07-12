package com.dicoding.githubuserapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.objectparcelable.User
import com.dicoding.githubuserapp.databinding.ListMainUserBinding
import java.lang.StringBuilder

class HomeUserAdapter: RecyclerView.Adapter<HomeUserAdapter.MainUserHolder>() {
    private val mData = ArrayList<User>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainUserHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.list_main_user, parent, false)
        return MainUserHolder(mView)
    }

    override fun onBindViewHolder(holder: MainUserHolder, position: Int) {
        holder.bind(mData[position])

        holder.itemView.setOnClickListener { onItemClickCallback
            .onItemClicked(mData[holder.absoluteAdapterPosition]) }
    }

    override fun getItemCount(): Int = mData.size

    inner class MainUserHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
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