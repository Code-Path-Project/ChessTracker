package com.example.chesstracker

import android.content.Context
import android.content.Intent
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
import com.example.chesstracker.Fragments.FriendsFragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.parse.ParseUser
//import io.github.farshidroohi.ChartEntity
//import io.github.farshidroohi.LineChart
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailUserActivity : AppCompatActivity() {

    private val gameHistory = mutableListOf<Game>()
    private lateinit var rvGameHistory: RecyclerView

    var count = 0
    val num = 5

    lateinit var datasets: ArrayList<LineDataSet>

    //Get current user
    val user = ParseUser.getCurrentUser()
    val username = ParseUser.getCurrentUser().username
    // modified according to date in function onViewCreated
    private var PLAYER_GAME_HISTORY_URL = "https://api.chess.com/pub/player/$username/games/2022/05"

    lateinit var tvUsername: TextView
    lateinit var lineChart: LineChart

    lateinit var rv: RecyclerView
    lateinit var btn_goMain: Button
    lateinit var btnAddFriend: Button
    lateinit var tvPlayerRating: TextView
    lateinit var tvYourRating: TextView
    lateinit var gameHistoryAdapter: GameHistoryAdapter

    val numberOfGamesToShow = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        tvUsername = findViewById(R.id.tv_name)
        lineChart = findViewById(R.id.lineChart)
        rv = findViewById(R.id.rv)
        btnAddFriend = findViewById(R.id.btnAddFriend)
        tvPlayerRating = findViewById(R.id.tvPlayerRating)
        tvYourRating = findViewById(R.id.tvYourRating)

        tvUsername.text = otherUser

        // populate game history
        gameHistoryAdapter = GameHistoryAdapter(this, gameHistory)
        rvGameHistory = findViewById(R.id.rv)
        rvGameHistory.adapter = gameHistoryAdapter
        rvGameHistory.layoutManager = LinearLayoutManager(this)
        rvGameHistory.addItemDecoration(
            DividerItemDecoration(
                rvGameHistory.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )



        // get current date
        val dateFormatMonth = SimpleDateFormat("MM")
        val date = Date()
        val month = dateFormatMonth.format(date).toInt()
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        Log.i(MainFragment.TAG, "year: $year, month: $month")

        // access chess.com api
        datasets = ArrayList<LineDataSet>()
        // accesss api for the two players
        clientAccess(username, year, month)
        clientAccess(otherUser, year, month)

        // addFriend button
        btnAddFriend.setOnClickListener {
            val allFriends = ParseUser.getCurrentUser().getString("friends_list")
            val allFriendsList = allFriends?.split(",")
            Log.i("current Friendlist", allFriendsList.toString())
            if (!allFriendsList.isNullOrEmpty() && allFriendsList.contains(otherUser)) {
                Toast.makeText(this,
                    "Player: $otherUser is already in your friend list", Toast.LENGTH_SHORT).show()
            }else{
                var newFriends: String
                if (allFriends.isNullOrBlank()) { newFriends = otherUser}
                else{ newFriends = allFriends + "," + otherUser }
                    user.put("friends_list", newFriends)
                user.saveInBackground()

                Toast.makeText(this,
                    "Player: $otherUser added to your friends list", Toast.LENGTH_SHORT).show()
            }
        }
        btn_goMain = findViewById(R.id.btn_goMain)
        btn_goMain.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }


    fun clientAccess(user: String, year:Int, month:Int) {
        var entries = ArrayList<Entry>()
        var Userusername = user
        if (month < 10) {
            PLAYER_GAME_HISTORY_URL =
                "https://api.chess.com/pub/player/$Userusername/games/${year.toString()}/0${month.toString()}/"
        } else {
            PLAYER_GAME_HISTORY_URL =
                "https://api.chess.com/pub/player/$Userusername/games/${year.toString()}/${month.toString()}/"
        }


        var client = AsyncHttpClient()
        Log.i(MainFragment.TAG, "$PLAYER_GAME_HISTORY_URL")
        client.get(PLAYER_GAME_HISTORY_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {
                Log.e(MainFragment.TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                try {
                    // for recycler view of games. get current month
                    val gameHistoryJsonArray = json.jsonObject.getJSONArray("games")
                    gameHistory.addAll(Game.fromJsonArray(gameHistoryJsonArray))
                    gameHistoryAdapter.notifyDataSetChanged()
                    Log.i(MainFragment.TAG, "gameHistory list: $gameHistory")

                    // ratings chart
                    var ratingsList = ArrayList<Float>()
                    val startIndex =
                        Integer.max(0, gameHistoryJsonArray.length() - numberOfGamesToShow)

                    var counter = 0f
                    if (gameHistoryJsonArray.length() < numberOfGamesToShow) {
                        counter = (numberOfGamesToShow - gameHistoryJsonArray.length()).toFloat()
                    }
                    var lastRating = 0f

                    for (i in startIndex until gameHistoryJsonArray.length()) {
                        var gameJson = gameHistoryJsonArray.getJSONObject(gameHistoryJsonArray.length()-i-1)
                        var white = gameJson.getJSONObject("white")
                        var black = gameJson.getJSONObject("black")
                        var player = black
                        if (white.getString("username") == username) {
                            // player played white
                            player = white
                        }
                        var rating = player.getString("rating").toFloat()
                        if (i == startIndex) {
                            lastRating = rating
                        }
                        ratingsList.add(rating)
                    }

                    var counterSave = counter
                    for (i in 0 until counter.toInt()) {
                        entries.add(Entry(i.toFloat(), lastRating))
                    }
                    for (i in 0 until ratingsList.size) {
                        entries.add(Entry((i+counter).toFloat(), ratingsList.get(i)))
                    }

                    lateinit var text: String
                    if (ratingsList.isEmpty()) {text = "No current rating for player: $Userusername" } // Null handling
                    else{text = "$Userusername current rating: " + ratingsList.get(ratingsList.size-1).toInt()}
                    if (username == Userusername) { tvYourRating.text = text }
                    else {tvPlayerRating.text = text }

                    displayDataSet(entries, Userusername)

                } catch (e: JSONException) {
                    Log.e(MainFragment.TAG, "Encountered exception $e")
                }
            }
        })
    }

    fun displayDataSet(entries:ArrayList<Entry>, user: String) {
        Log.i(TAG, "entries display: $entries")
        var textt = "Player: $user's Rating"

        val dataSet = LineDataSet(entries, textt)

        dataSet.setColor(Color.RED)
        datasets.add(dataSet)
        Log.i(TAG, "datasets: $datasets")
        val lineData = LineData(datasets as List<ILineDataSet>?)
        lineChart.setData(lineData)
        lineChart.invalidate()
        lineChart.notifyDataSetChanged()
        Log.i(TAG, "SHOULD HAVE DISPLAYED DATA")
    }

    companion object {
        const val TAG = "DetailAcitivty"
        lateinit var otherUser: String
        fun setUser(otherUsername: String) { otherUser = otherUsername }
    }
}