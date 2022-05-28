package com.example.chesstracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FriendsAdapter(val context: Context, val friends: ArrayList<String>) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvUsername: TextView
        val ivImage: ImageView

        init {
            tvUsername = itemView.findViewById(R.id.tv_friend_username)
            ivImage = itemView.findViewById(R.id.iv_friend_image)
        }

        fun bind(friend: String) {
            tvUsername.text = friend

            //Populate the ImageView
            //Glide.with(itemView.context).load(post.getImage()?.url).into(ivImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.friend_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendsAdapter.ViewHolder, position: Int) {
        val friendUsername = friends[position]
        holder.bind(friendUsername)
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    // Delete everything in the RV
    fun clear() {
        friends.clear()
        notifyDataSetChanged()
    }
}