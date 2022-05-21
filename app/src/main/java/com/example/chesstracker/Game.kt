package com.example.chesstracker

import org.json.JSONArray

data class Game (
    val gameUrl: String,
    val time: Int, //timestamp
    val whiteUsername: String, // May use Player class
    val whiteResult: String,
    val blackUsername: String, //May use Player class
    val blackResult: String
        ) {
    companion object {
        fun fromJsonArray(gameHistoryJsonArray: JSONArray): List<Game> {
            val games = mutableListOf<Game>()
            for (i in 0 until gameHistoryJsonArray.length()) {
                val gameJson = gameHistoryJsonArray.getJSONObject(i)
                val whitePlayer = gameJson.getJSONObject("white")
                val blackPlayer = gameJson.getJSONObject("black")
                games.add(
                    Game(
                        gameJson.getString("url"),
                        gameJson.getInt("end_time"),
                        whitePlayer.getString("username"),
                        whitePlayer.getString("result"),
                        blackPlayer.getString("username"),
                        blackPlayer.getString("result")
                    )
                )
            }
            return games
        }
    }
}
