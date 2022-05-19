package com.example.chesstracker

import org.json.JSONArray

data class Game (
    val gameUrl: String,
    val time: Int,
    val white: String,
    val Black: String
        ) {
    companion object {
        fun fromJsonArray(gameHistoryJsonArray: JSONArray): List<Game> {
            val games = mutableListOf<Game>()
            for (i in 0 until gameHistoryJsonArray.length()) {
                val gameJson = gameHistoryJsonArray.getJSONObject(i)
                games.add(
                    Game(
                        gameJson.getString("url"),
                        gameJson.getInt("end_time"),
                        gameJson.getJSONObject("white"),
                        gameJson.getJSONObject("black")
                    )
                )
            }
            return games
        }
    }
}
