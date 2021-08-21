package com.example.sudoku

import androidx.lifecycle.ViewModel

class MainViewModel(private val game: Game) : ViewModel() {

    var selectedCeilIndex: Pair<*, *>? = null

    fun init() {
        game.fillGameField()
    }

    fun getFieldValue(i: Int, j: Int): Int = game.getValue(i, j)

    fun updateFieldValue(i: Int, j: Int, value: Int) {
        game.updateValue(i, j, value)
    }

    fun printGameField() {
        game.debugPrintGameField()
    }

}