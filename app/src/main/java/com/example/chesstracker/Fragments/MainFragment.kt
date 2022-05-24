package com.example.chesstracker.Fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.chesstracker.*
import com.parse.ParseUser
import io.github.farshidroohi.ChartEntity
import io.github.farshidroohi.LineChart
import okhttp3.Headers
import org.json.JSONException
import java.lang.Integer.max

class MainFragment : Fragment() {
    val numberOfGamesToShow = 5

    lateinit var lineChart: LineChart

    private val gameHistory = mutableListOf<Game>()
    private lateinit var rvGameHistory: RecyclerView

    //Get current user
    val username = ParseUser.getCurrentUser().username
    private val PLAYER_GAME_HISTORY_URL = "https://api.chess.com/pub/player/$username/games/2022/05"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Log.i(TAG, "$username")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // testing for detail activity page
//        val i = Intent(requireContext(), DetailUserActivity::class.java)
//        startActivity(i)

        // access UI layout object
        lineChart = view.findViewById<LineChart>(R.id.lineChart)

    /// gameHistory
        val gameHistoryAdapter = GameHistoryAdapter(requireContext(), gameHistory)
        rvGameHistory = view.findViewById(R.id.rv_gameHistory)
        rvGameHistory.adapter = gameHistoryAdapter
        rvGameHistory.layoutManager = LinearLayoutManager(requireContext())

        rvGameHistory.addItemDecoration( // Adding divider line between view
            DividerItemDecoration(
                rvGameHistory.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    ///

    /// access chess.com api
        val client = AsyncHttpClient()
        client.get(PLAYER_GAME_HISTORY_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess Json: $json")
                try {
                    val gameHistoryJsonArray = json.jsonObject.getJSONArray("games")
                    gameHistory.addAll(Game.fromJsonArray(gameHistoryJsonArray))
                    gameHistoryAdapter.notifyDataSetChanged()
                    Log.i(TAG, "gameHistory list: $gameHistory")

                    // ratings chart
                    var ratingsList = ArrayList<Float>()
                    var legendVars = ArrayList<String>()
                    val startIndex = max(0, gameHistoryJsonArray.length()-numberOfGamesToShow)
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
                        legendVars.add(rating.toInt().toString())
                    }
                    var playerRatingData = ratingsList.toFloatArray()
                    var firstChartEntity = ChartEntity(Color.WHITE, playerRatingData)

                    var list = ArrayList<ChartEntity>().apply {
                        add(firstChartEntity)
                    }

                    lineChart.setList(list)
                    lineChart.setLegend(legendVars.toList())
                }catch (e: JSONException){
                    Log.e(TAG, "Encountered exception $e")
                }
            }

        })
    ///
    }

    companion object {
        const val TAG = "MainFragment"
    }

}