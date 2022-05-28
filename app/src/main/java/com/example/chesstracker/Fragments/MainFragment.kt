package com.example.chesstracker.Fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.chesstracker.*
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.parse.ParseUser
import okhttp3.Headers
import org.eazegraph.lib.charts.ValueLineChart
import org.json.JSONException
import java.lang.Integer.max
import java.security.KeyStore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainFragment : Fragment() {
    val numberOfGamesToShow = 5

    lateinit var client: AsyncHttpClient
    lateinit var lineChart: LineChart
    lateinit var datasets: ArrayList<LineDataSet>
    lateinit var entries: ArrayList<Entry>
    var count = 0
    var num = 5

    private val gameHistory = mutableListOf<Game>()
    private lateinit var rvGameHistory: RecyclerView
    lateinit var gameHistoryAdapter: GameHistoryAdapter

    private lateinit var searchBar: SearchView

    //Get current user
    val username = ParseUser.getCurrentUser().username
    // modified according to date in function onViewCreated
    private var PLAYER_GAME_HISTORY_URL = "https://api.chess.com/pub/player/$username/games/2022/05"

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
        lineChart = view.findViewById(R.id.lineChart)

        //searchBar
        searchBar = view.findViewById(R.id.sv_searchBar)
        searchBar.setSubmitButtonEnabled(true)
        searchBar.setIconifiedByDefault(false)

        fun checkChessAPI(username: String){

        }

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(typedName: String): Boolean {

                //verify if username is valid
                val client = AsyncHttpClient()
                client.get("https://api.chess.com/pub/player/$typedName", object: JsonHttpResponseHandler() {
                    override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
                    ) {//If the api call fails / the username does not exist in chess.com
                        Toast.makeText(requireContext(), "User: $typedName does not exist", Toast.LENGTH_SHORT).show()
                    }
                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) { //Player found
                        DetailUserActivity.setUser(typedName)
                        val intent = Intent(requireContext(), DetailUserActivity::class.java)
                        startActivity(intent)
                    }
                })
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
        //End searchBar

    /// gameHistory
        gameHistoryAdapter = GameHistoryAdapter(requireContext(), gameHistory)
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
        // check current date
        val dateFormatMonth = SimpleDateFormat("MM")
        val date = Date()
        val month = dateFormatMonth.format(date).toInt()
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        Log.i(TAG, "year: $year, month: $month")

        // variables for data sets
        datasets = ArrayList<LineDataSet>()
        entries = ArrayList<Entry>()
        clientAccess(year, month)
        ///
    }

    fun clientAccess(year: Int, month: Int) {
        if (month < 10) {
            PLAYER_GAME_HISTORY_URL =
                "https://api.chess.com/pub/player/$username/games/${year.toString()}/0${month.toString()}/"
        } else {
            PLAYER_GAME_HISTORY_URL =
                "https://api.chess.com/pub/player/$username/games/${year.toString()}/${month.toString()}/"
        }

        client = AsyncHttpClient()
        Log.i(TAG, "$PLAYER_GAME_HISTORY_URL")
        client.get(PLAYER_GAME_HISTORY_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                try {
                    // for recycler view of games. get current month
                    val gameHistoryJsonArray = json.jsonObject.getJSONArray("games")
                    gameHistory.addAll(Game.fromJsonArray(gameHistoryJsonArray))
                    gameHistoryAdapter.notifyDataSetChanged()
                    Log.i(TAG, "gameHistory list: $gameHistory")

                    // ratings chart
                    var ratingsList = ArrayList<Float>()
                    val startIndex =
                        Integer.max(0, gameHistoryJsonArray.length() - numberOfGamesToShow)
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
                        ratingsList.add(rating)
                        entries.add(Entry(i.toFloat(), rating))
                    }
                    displayDataSet()

                } catch (e: JSONException) {
                    Log.e(TAG, "Encountered exception $e")
                }
            }
        })
    }

    fun displayDataSet() {
        Log.i(TAG, "entries display: $entries")
        val dataSet = LineDataSet(entries, "Your Rating")

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
        const val TAG = "MainFragment"
    }

}