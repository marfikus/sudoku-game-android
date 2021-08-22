package com.example.sudoku

import androidx.lifecycle.ViewModel

class MainViewModel(private val game: Game) : ViewModel() {

    private var selectedCeilIndex: Pair<*, *>? = null

    fun init() {
        game.start()
    }

    fun getFieldValue(i: Int, j: Int): Int = game.getValue(i, j)

    fun updateFieldValue(i: Int, j: Int, value: Int) {
        game.updateValue(i, j, value)
    }

    fun existSelectedCeil() = selectedCeilIndex != null

    fun getSelectedCeilIndex(): Pair<Int, Int> {
        return Pair((selectedCeilIndex?.first) as Int, (selectedCeilIndex?.second) as Int,)
    }

    fun selectCeil(index: Pair<*, *>) {
        selectedCeilIndex = index
    }

    fun unselectCeil() {
        selectedCeilIndex = null
    }

    fun debugPrintGameField() {
        game.debugPrintGameField()
        game.debugPrintGameField("initial")
    }

}