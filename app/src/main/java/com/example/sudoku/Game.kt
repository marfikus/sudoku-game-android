package com.example.sudoku

import android.util.Log
import kotlin.random.Random
import kotlin.random.nextInt

private const val LOG_TAG = "LOG_TAG"

class Game {

    private val gameField = mutableListOf<MutableList<Int>>()
    private lateinit var initialGameField: List<List<Int>>
    private val initialHidedCeils = mutableListOf<Pair<Int, Int>>()

    fun start() {
        fillGameField()
        initialGameField = gameField.map { it.toList() }
        Log.d(LOG_TAG, gameFieldSolved().toString())
        mixGameField()
        hideSomeCeils(2)
        Log.d(LOG_TAG, gameFieldSolved().toString())
    }

    fun getValue(i: Int, j: Int): Int = gameField[i][j]

    fun getInitialHidedCeilsCount() = initialHidedCeils.size

    fun updateValue(i: Int, j: Int, value: Int) {
        gameField[i][j] = value
    }

    fun debugPrintGameField(which: String = "") {
        val field = if (which == "initial") initialGameField else gameField

        var result = " \n"
        field.forEach {
            result += "$it\n"
        }
        Log.d(LOG_TAG, result)
    }

    fun isHided(i: Int, j: Int) = Pair(i, j) in initialHidedCeils

    private fun fillGameField() {
        for (block in 1..3) {
            for (str in block..9 step 3) {
                val list = mutableListOf<Int>()
                for (i in 0..8) {
                    val n = str + i
                    if (n > 9) list.add(n - 9) else list.add(n)
                }
                gameField.add(list)
            }
        }
    }

    private fun mixGameField() {

    }

    private fun hideSomeCeils(n: Int) {
        var hided = 0
        while (hided != n) {
            val i = Random.nextInt(gameField.indices)
            val j = Random.nextInt(gameField[i].indices)
            if (gameField[i][j] == 0) continue
            gameField[i][j] = 0
            hided++
            initialHidedCeils.add(Pair(i, j))
        }
    }

    fun gameFieldSolved() = gameField == initialGameField
}