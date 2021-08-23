package com.example.sudoku

import androidx.lifecycle.ViewModel

class MainViewModel(private val game: Game) : ViewModel() {

    private var selectedCeilIndex: Pair<*, *>? = null
    private var emptyCeilsCount = 0
    private var difficultyLevel = 20

    fun init() {
        game.setDifficultyLevel(difficultyLevel)
        game.start()
        emptyCeilsCount = game.getInitialHidedCeilsCount()
        selectedCeilIndex = null
    }

    fun getDifficultyLevel(): Int = difficultyLevel

    fun setDifficultyLevel(value: Int) {
        difficultyLevel = value
        game.setDifficultyLevel(value)
    }
    
    fun getFieldValue(i: Int, j: Int): Int = game.getValue(i, j)

    fun updateFieldValue(i: Int, j: Int, newValue: Int) {
        val oldValue = game.getValue(i, j)
        if (newValue != oldValue) {
            if (newValue == 0) {
                emptyCeilsCount++
            } else {
                emptyCeilsCount--
            }
            game.updateValue(i, j, newValue)
        }

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
    }

    fun isHidedCeil(i: Int, j: Int) = game.isHided(i, j)

    fun existEmptyCeils() = emptyCeilsCount > 0

    fun gameFieldSolved() = game.gameFieldSolved()

}