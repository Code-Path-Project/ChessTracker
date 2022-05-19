package com.example.chesstracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.lang.Long.parseLong
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

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

    private fun getDateTime(s: String): String? {
        try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = Date(parseLong(s) * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tv_url = itemView.findViewById<TextView>(R.id.tv_url)
        private val tv_blackUsername = itemView.findViewById<TextView>(R.id.tv_blackUsername)
        private val tv_blackResult = itemView.findViewById<TextView>(R.id.tv_blackResult)
        private val tv_whiteUsername = itemView.findViewById<TextView>(R.id.tv_whiteUsername)
        private val tv_whiteResult = itemView.findViewById<TextView>(R.id.tv_whiteResult)
        private val tv_date = itemView.findViewById<TextView>(R.id.tv_date)


        fun bind(game: Game){
            tv_url.text = game.gameUrl
            tv_blackUsername.text = game.blackUsername
            tv_blackResult.text = game.blackResult
            tv_whiteUsername.text = game.whiteUsername
            tv_whiteResult.text = game.whiteResult
            tv_date.text = getDateTime(game.time.toString())
        }
    }
}
