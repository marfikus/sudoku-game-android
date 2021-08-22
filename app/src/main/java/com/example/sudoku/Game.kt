package com.example.sudoku

import android.util.Log

private const val LOG_TAG = "LOG_TAG"

class Game {

    private val gameField = mutableListOf<MutableList<Int>>()

    fun fillGameField() {
        for (blk in 1..3) {
            for (str in blk..9 step 3) {
                val list = mutableListOf<Int>()
                for (i in 0..8) {
                    val n = str + i
                    if (n > 9) list.add(n - 9) else list.add(n)
                }
                gameField.add(list)
            }
        }
    }

    fun getValue(i: Int, j: Int): Int = gameField[i][j]

    fun updateValue(i: Int, j: Int, value: Int) {
        gameField[i][j] = value
    }

    fun debugPrintGameField() {
        var result = " \n"
        gameField.forEach {
            result += "$it\n"
        }
        Log.d(LOG_TAG, result)
    }
}