package com.dicoding.githubuserapp.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.databinding.ListMainUserBinding
import com.dicoding.githubuserapp.objectparcelable.User
import java.lang.StringBuilder
import java.util.*

class FavoriteAdapter :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    var listFavorite = ArrayList<User>()
        set(listNotes) {
            this.listFavorite.clear()
            this.listFavorite.addAll(listNotes)

            notifyDataSetChanged()
        }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_main_user, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])

        holder.itemView.setOnClickListener { onItemClickCallback
            .onItemClicked(listFavorite[holder.absoluteAdapterPosition]) }
    }

    override fun getItemCount(): Int = this.listFavorite.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListMainUserBinding.bind(itemView)
        fun bind(user: User) {
            Glide.with(itemView.context)
                .load(user.avatar)
                .into(binding.imgUser)

            binding.tvName.text = user.name

            //Usernama
            val username = user.username
            binding.tvUsername.text = StringBuilder("(")
                .append(username)
                .append(")")
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}