package com.example.sudoku

import android.content.Context


private const val APP_SETTINGS = "SUDOKU_GAME_SETTINGS"
private const val DIFFICULTY_LEVEL = "DIFFICULTY_LEVEL"
private const val DIFFICULTY_LEVEL_DEFAULT = 20

class AppSettings(context: Context) {

    private val settings = context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)

    fun getDifficultyLevel(): Int = settings.getInt(DIFFICULTY_LEVEL, DIFFICULTY_LEVEL_DEFAULT)

    fun updateDifficultyLevel(value: Int) {
        val editor = settings.edit()
        editor.putInt(DIFFICULTY_LEVEL, value)
        editor.apply()
    }
}