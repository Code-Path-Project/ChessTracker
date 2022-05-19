package com.example.chesstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val PLAYER_GAME_HISTORY_URL = "https://api.chess.com/pub/player/erik/games"
const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private val gameHistory = mutableListOf<Game>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gameHistoryAdapter = GameHistoryAdapter(this, gameHistory)

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
                    Log.i(TAG, "gameHistory list: $gameHistory")
                }catch (e: JSONException){
                    Log.e(TAG, "Encountered exception $e")
                }
            }

        })
    }
}