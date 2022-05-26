package com.example.chesstracker

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chesstracker.Fragments.MainFragment
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.parse.ParseUser
import io.github.farshidroohi.ChartEntity
import io.github.farshidroohi.LineChart
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONException

class DetailUserActivity : AppCompatActivity() {

    private val gameHistory = mutableListOf<Game>()
    private lateinit var rvGameHistory: RecyclerView

    //Get current user
    val username = ParseUser.getCurrentUser().username
    private val PLAYER_GAME_HISTORY_URL = "https://api.chess.com/pub/player/$username/games/2022/05"
    // other user
    val otherUser = "erik"
    private val OTHER_PLAYER_GAME_HISTORY_URL = "https://api.chess.com/pub/player/$otherUser/games/2022/05"

    lateinit var tvUsername: TextView
    lateinit var lineChart: LineChart
    lateinit var rv: RecyclerView
    lateinit var btnAddFriend: Button
    lateinit var tvPlayerRating: TextView
    lateinit var tvYourRating: TextView

    lateinit var list: ArrayList<ChartEntity>

    val numberOfGamesToShow = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        tvUsername = findViewById(R.id.tv_name)
        lineChart = findViewById(R.id.lineChart)
        rv = findViewById(R.id.rv)
        btnAddFriend = findViewById(R.id.btnAddFriend)
        tvPlayerRating = findViewById(R.id.tvPlayerRating)
        tvYourRating = findViewById(R.id.tvYourRating)

        // container for graph lines
        list = ArrayList<ChartEntity>()

        tvUsername.text = username

        // populate game history
        val gameHistoryAdapter = GameHistoryAdapter(this, gameHistory)
        rvGameHistory = findViewById(R.id.rv)
        rvGameHistory.adapter = gameHistoryAdapter
        rvGameHistory.layoutManager = LinearLayoutManager(this)
        rvGameHistory.addItemDecoration(
            DividerItemDecoration(
                rvGameHistory.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        // access chess.com api
        val client = AsyncHttpClient()
        client.get(PLAYER_GAME_HISTORY_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {
                Log.e("DetailScreen", "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(MainFragment.TAG, "onSuccess Json: $json")
                try {
                    val gameHistoryJsonArray = json.jsonObject.getJSONArray("games")

                    // ratings chart
                    loadRatings(getRatings(gameHistoryJsonArray), 1)

                }catch (e: JSONException){
                    Log.e("DetailScreen", "Encountered exception $e")
                }
            }
        })

        val client2 = AsyncHttpClient()
        client2.get(OTHER_PLAYER_GAME_HISTORY_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {
                Log.e("DetailScreen", "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(MainFragment.TAG, "onSuccess Json: $json")
                try {
                    val gameHistoryJsonArray = json.jsonObject.getJSONArray("games")
                    gameHistory.addAll(Game.fromJsonArray(gameHistoryJsonArray))
                    gameHistoryAdapter.notifyDataSetChanged()
                    Log.i("DetailScreen", "gameHistory list: $gameHistory")
                    // ratings chart
                    loadRatings(getRatings(gameHistoryJsonArray), 0)
                }catch (e: JSONException){
                    Log.e("DetailScreen", "Encountered exception $e")
                }
            }
        })

        btnAddFriend.setOnClickListener {
            Toast.makeText(this, "add friend button pressed", Toast.LENGTH_SHORT).show()
        }

    }

    fun getRatings(gameHistoryJsonArray: JSONArray): ArrayList<Float> {
        var ratingsList = ArrayList<Float>()
        val startIndex =
            Integer.max(0, gameHistoryJsonArray.length() - numberOfGamesToShow)
        for (i in gameHistoryJsonArray.length()-1 downTo startIndex) {
            var gameJson = gameHistoryJsonArray.getJSONObject(i)
            var white = gameJson.getJSONObject("white")
            var black = gameJson.getJSONObject("black")
            var player = black
            if (white.getString("username") == username) {
                // player played white
                player = white
            }
            var rating = player.getString("rating").toFloat()
            ratingsList.add(rating)
        }

        return ratingsList
    }

    fun loadRatings(playersRatingList: ArrayList<Float>, num: Int) {
        var playerRatingData = playersRatingList.toFloatArray()
        var color = Color.WHITE
        if (num == 0) {
            // other players ratings
            color = Color.YELLOW
            val text = "Player's current rating: " + playersRatingList.get(playerRatingData.size-1).toInt()
            tvPlayerRating.text = text
        } else {
            // user's player rating
            val text = "Your current rating: " + playersRatingList.get(playerRatingData.size-1).toInt()
            tvYourRating.text = text
        }
        var ChartEntity = ChartEntity(color, playerRatingData)
        list.add(ChartEntity)

        lineChart.setList(list)
    }
}