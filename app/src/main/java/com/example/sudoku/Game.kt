package com.example.sudoku

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.random.nextInt

private const val LOG_TAG = "LOG_TAG"

class Game {

    private var gameField = mutableListOf<MutableList<Int>>()
    private lateinit var initialGameField: List<List<Int>>
    private val initialHidedCeils = mutableListOf<Pair<Int, Int>>()
    private var difficultyLevel: Int = 0

    fun start() {
        gameField.clear()
        initialHidedCeils.clear()


        gameField = fillGameField(gameField)
        gameField = mixGameField(gameField)
//        gameField = generateGameField(gameField)  // experimental
        initialGameField = gameField.map { it.toList() }
        gameField = hideSomeCeils(gameField, difficultyLevel)
    }

    fun getDifficultyLevel(): Int = difficultyLevel

    fun setDifficultyLevel(value: Int) {
        val maxValue = 81
        difficultyLevel = if (value > maxValue) {
            maxValue
        } else {
            value
        }
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

    // TODO: 24.08.2021
    //  экспериментальный вариант, пока работает не корректно.
    //  Не получается набрать матрицу уникальную просто перебором рандомного списка вариантов.
    //  Надо другой алгоритм гуглить...
    private fun generateGameField(
        gameField: MutableList<MutableList<Int>>
    ): MutableList<MutableList<Int>> {

        fun notInString(str: Int, value: Int): Boolean {
            for (col in gameField[str].indices) {
                if (gameField[str][col] == value) {
                    return false
                }
            }
            return true
        }

        fun notInColumn(col: Int, value: Int): Boolean {
            for (str in gameField.indices) {
                if (gameField[str][col] == value) {
                    return false
                }
            }
            return true
        }

        fun notInSquare(str: Int, col: Int, value: Int): Boolean {

            fun detectSquare(str: Int, col: Int): MutableMap<String, Int> {
                val result = mutableMapOf<String, Int>()

                when {
                    str <= 2 -> {
                        result["y1"] = 0
                        result["y2"] = 2
                    }
                    str in 3..5 -> {
                        result["y1"] = 3
                        result["y2"] = 5
                    }
                    str > 5 -> {
                        result["y1"] = 6
                        result["y2"] = 8
                    }
                }

                when {
                    col <= 2 -> {
                        result["x1"] = 0
                        result["x2"] = 2
                    }
                    col in 3..5 -> {
                        result["x1"] = 3
                        result["x2"] = 5
                    }
                    col > 5 -> {
                        result["x1"] = 6
                        result["x2"] = 8
                    }
                }                

                return result
            }

            val square = detectSquare(str, col)
            for (i in square["y1"]!!..square["y2"]!!) {
                for (j in square["x1"]!!..square["x2"]!!) {
                    if (gameField[i][j] == value) {
                        return false
                    }
                }
            }
            return true
        }

        repeat(9) { gameField.add(Array(9) { 0 }.toMutableList()) }

        var numbers = (1..9).toMutableList()
        repeat (9) {
            Log.d(LOG_TAG, numbers.toString())
//            val index = Random.nextInt(numbers.indices)
//            val number = numbers[index]
            val number = numbers.random()
            for (str in 0..8) {
                for (col in gameField[str].indices) {
//                    val col = Random.nextInt(gameField[str].indices)
                    if ((gameField[str][col] == 0)
                            && notInString(str, number)
                            && notInColumn(col, number)
                            && notInSquare(str, col, number)) {

                        gameField[str][col] = number
                        numbers.remove(number)
                        break
                    }
                }

            }

            debugPrintGameField(gameField)
        }



/*        var numbers = (1..9).toMutableList()
        val firstString = mutableListOf<Int>()
        repeat(9) { firstString.add(numbers.removeAt(Random.nextInt(numbers.indices))) }
        gameField[0] = firstString
        debugPrintGameField(gameField)

        val values = mutableListOf<Int>()
        for (str in 1..8) {
            numbers = (1..9).toMutableList()
            repeat(9) { values.add(numbers.removeAt(Random.nextInt(numbers.indices))) }
            for (col in 0..8) {
                forValues@ for(value in values) {
                    if (notInString(str, value)
                            && notInColumn(col, value)
                            && notInSquare(str, col, value)) {
                        gameField[str][col] = value
                        values.remove(value)
                        break@forValues
                    }
                }
            }
            debugPrintGameField(gameField)
        }*/

//        debugPrintGameField(gameField)
        return gameField
    }

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
        
        fun changeDigitsInStrings(
                gameFieldStrings: MutableList<MutableList<Int>>,
                times: Int
        ): MutableList<MutableList<Int>> {

            repeat(times) {
                val digits = (1..9).toMutableList()
                val firstDigit = digits.random()
                digits.remove(firstDigit)
                val secondDigit = digits.random()
//                Log.d(LOG_TAG, "$firstDigit, $secondDigit")

                for (str in gameFieldStrings) {
                    val firstDigitIndex = str.indexOf(firstDigit)
                    val secondDigitIndex = str.indexOf(secondDigit)
                    str[firstDigitIndex] = secondDigit
                    str[secondDigitIndex] = firstDigit
                }
            }

            return gameFieldStrings
        }

        var gameFieldBlocks: MutableList<MutableList<MutableList<Int>>>
        var mixedGameField = mutableListOf<MutableList<Int>>()
        val x = Random.nextInt(1, 3)

        debugPrintGameField(gameField)
//        mixedGameField = transpose(gameField)
/*        for (i in 0..x) {
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
        }*/

//        debugPrintGameField(mixedGameField)
//        mixedGameField = mixStrings(mixedGameField)
//        mixedGameField = transpose(mixedGameField)
        mixedGameField = changeDigitsInStrings(gameField, 10)
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