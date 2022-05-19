package com.example.chesstracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.sql.Timestamp

class GameHistoryAdapter(private val context: Context, private val gameHistory: List<Game>)
    : RecyclerView.Adapter<GameHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_game, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = gameHistory[position]
        return holder.bind(game)
    }

    override fun getItemCount() = gameHistory.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tv_url = itemView.findViewById<TextView>(R.id.tv_url)
        private val tv_black = itemView.findViewById<TextView>(R.id.tv_black)
        private val tv_white = itemView.findViewById<TextView>(R.id.tv_white)
        private val tv_time = itemView.findViewById<TextView>(R.id.tv_time)

        fun bind(game: Game){
            tv_url.text = game.gameUrl
            tv_black.text = game.Black
            tv_white.text = game.white
            tv_time.text = game.startTime.toString()
        }
    }
}
