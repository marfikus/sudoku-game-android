package com.example.sudoku

import android.util.Log
import kotlin.random.Random
import kotlin.random.nextInt

private const val LOG_TAG = "LOG_TAG"

class Game {

    private var gameField = mutableListOf<MutableList<Int>>()
    private lateinit var initialGameField: List<List<Int>>
    private val initialHidedCeils = mutableListOf<Pair<Int, Int>>()

    fun start() {
        gameField.clear()
        initialHidedCeils.clear()

        gameField = fillGameField(gameField)
        gameField = mixGameField(gameField)
        initialGameField = gameField.map { it.toList() }

        gameField = hideSomeCeils(gameField, 2)
    }

    fun getValue(i: Int, j: Int): Int = gameField[i][j]

    fun getInitialHidedCeilsCount() = initialHidedCeils.size

    fun updateValue(i: Int, j: Int, value: Int) {
        gameField[i][j] = value
    }

    fun debugPrintGameField(field: MutableList<MutableList<Int>> = gameField) {

        var result = " \n"
        field.forEach {
            result += "$it\n"
        }
        Log.d(LOG_TAG, result)
    }

    fun isHided(i: Int, j: Int) = Pair(i, j) in initialHidedCeils

    private fun fillGameField(
        gameField: MutableList<MutableList<Int>>
    ): MutableList<MutableList<Int>> {

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
        return gameField
    }

    private fun mixGameField(
        gameField: MutableList<MutableList<Int>>
    ): MutableList<MutableList<Int>> {

        fun createBlocksView(
            gameFieldStrings: MutableList<MutableList<Int>>,
            mode: String
        ): MutableList<MutableList<MutableList<Int>>> {

            val gameFieldBlocks = mutableListOf<MutableList<MutableList<Int>>>()

            for (n in 0..8 step 3) {
                val block = mutableListOf<MutableList<Int>>()
                for (m in 0..2) {
                    when (mode) {
                        "strings" -> block.add(gameFieldStrings[m + n])
                        "columns" -> {
                            val column = mutableListOf<Int>()
                            for (i in gameFieldStrings.indices) {
                                column.add(gameFieldStrings[i][m + n])
                            }
                            block.add(column)
                        }
                        else -> throw IllegalArgumentException("undefined mode '$mode'")
                    }
                }
                gameFieldBlocks.add(block)
            }
            return gameFieldBlocks
        }

        fun mixBlocks(
            gameFieldBlocks: MutableList<MutableList<MutableList<Int>>>
        ): MutableList<MutableList<MutableList<Int>>> {

            val count = gameFieldBlocks.size
            for (i in 0 until count) {
                var n = Random.nextInt(0, count - 1)
                val block = gameFieldBlocks.removeAt(n)
                n = Random.nextInt(0, count - 1)
                gameFieldBlocks.add(n, block)
            }
            return gameFieldBlocks
        }

        fun mixStringsInBlocks(
            gameFieldBlocks: MutableList<MutableList<MutableList<Int>>>
        ): MutableList<MutableList<MutableList<Int>>> {

            val count = gameFieldBlocks.size
            for (i in 0 until count) {
                for (j in 0 until count) {
                    var n = Random.nextInt(0, count - 1)
                    val string = gameFieldBlocks[i].removeAt(n)
                    n = Random.nextInt(0, count - 1)
                    gameFieldBlocks[i].add(n, string)
                }
            }
            return gameFieldBlocks
        }

        fun createStringsView(
            gameFieldBlocks: MutableList<MutableList<MutableList<Int>>>
        ): MutableList<MutableList<Int>> {

            val gameFieldStrings = mutableListOf<MutableList<Int>>()
            val count = gameFieldBlocks.size

            for (i in 0 until count) {
                for (j in 0 until count) {
                    gameFieldStrings.add(gameFieldBlocks[i][j])
                }
            }
            return gameFieldStrings
        }

        fun transpose(
            gameFieldStrings: MutableList<MutableList<Int>>
        ): MutableList<MutableList<Int>> {

            val transposedGameField = mutableListOf<MutableList<Int>>()

            for (j in gameFieldStrings.indices) {
                val column = mutableListOf<Int>()
                for (i in gameFieldStrings.indices) {
                    column.add(gameFieldStrings[i][j])
                }
                transposedGameField.add(column)
            }

            return transposedGameField
        }

        fun mixStrings(
            gameFieldStrings: MutableList<MutableList<Int>>
        ): MutableList<MutableList<Int>> {

            val mixedGameField = mutableListOf<MutableList<Int>>()
            val variants = gameFieldStrings.indices.toMutableList()

            for (j in gameFieldStrings.indices) {
                val n = variants.removeAt(Random.nextInt(variants.indices))
                mixedGameField.add(gameFieldStrings[n])
            }

            return mixedGameField
        }

        var gameFieldBlocks: MutableList<MutableList<MutableList<Int>>>
        var mixedGameField = mutableListOf<MutableList<Int>>()
        val x = Random.nextInt(1, 3)

        debugPrintGameField(gameField)
//        mixedGameField = transpose(gameField)
        for (i in 0..x) {
            gameFieldBlocks = createBlocksView(gameField, "columns")
            gameFieldBlocks = mixBlocks(gameFieldBlocks)
            gameFieldBlocks = mixStringsInBlocks(gameFieldBlocks)
            mixedGameField = createStringsView(gameFieldBlocks)
//            mixedGameField = transpose(mixedGameField)

            gameFieldBlocks = createBlocksView(mixedGameField, "strings")
            gameFieldBlocks = mixBlocks(gameFieldBlocks)
            gameFieldBlocks = mixStringsInBlocks(gameFieldBlocks)
            mixedGameField = createStringsView(gameFieldBlocks)
//            mixedGameField = transpose(mixedGameField)
        }

//        debugPrintGameField(mixedGameField)
//        mixedGameField = mixStrings(mixedGameField)
//        mixedGameField = transpose(mixedGameField)
        debugPrintGameField(mixedGameField)
        return mixedGameField
    }

    private fun hideSomeCeils(
        gameField: MutableList<MutableList<Int>>,
        count: Int
    ): MutableList<MutableList<Int>> {

        var hided = 0
        while (hided != count) {
            val i = Random.nextInt(gameField.indices)
            val j = Random.nextInt(gameField[i].indices)
            if (gameField[i][j] == 0) continue
            gameField[i][j] = 0
            hided++
            initialHidedCeils.add(Pair(i, j))
        }
        return gameField
    }

    fun gameFieldSolved() = gameField == initialGameField
}