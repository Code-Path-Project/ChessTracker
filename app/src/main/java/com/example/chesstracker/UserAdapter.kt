package com.example.chesstracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(val context: Context, val users: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvUsername: TextView
        //val ivImage: ImageView
        //val tvRating : TextView

        init {
            tvUsername = itemView.findViewById(R.id.tv_friend_username)
            //ivImage = itemView.findViewById(R.id.iv_friend_image)
            //tvRating = itemView.findViewById(R.id.tv_friend_rating)
        }

        fun bind(user: User) {
            //tvRating.text = post.getDescription()
            tvUsername.text = user.getUser()?.username

            //Populate the ImageView
            //Glide.with(itemView.context).load(post.getImage()?.url).into(ivImage)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.friend_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val user = users.get(position)
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    // Delete everything in the RV
    fun clear() {
        users.clear()
        notifyDataSetChanged()
    }
}