package com.example.maze

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferencesUtils {
    private val gson = Gson()

    fun saveMaze(prefs: SharedPreferences, name: String, maze: Array<IntArray>) {
        val json = gson.toJson(maze)
        prefs.edit().putString("maze_$name", json).apply()
    }

    fun loadMaze(prefs: SharedPreferences, name: String): Array<IntArray>? {
        val json = prefs.getString("maze_$name", null) ?: return null
        val type = object : TypeToken<Array<IntArray>>() {}.type
        return gson.fromJson(json, type)
    }

    fun getMazeNames(prefs: SharedPreferences): List<String> {
        return prefs.all.keys.filter { it.startsWith("maze_") }.map { it.removePrefix("maze_") }
    }
}