package com.example.sudoku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val LOG_TAG = "LOG_TAG"
    private val gameField = mutableListOf<List<Int>>()
    private val ceils = mutableListOf<List<Button>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ceils.add(
            listOf(
                ceil_00,
                ceil_01,
                ceil_02,
                ceil_03,
                ceil_04,
                ceil_05,
                ceil_06,
                ceil_07,
                ceil_08,
            ),
        )

        fillGameField()
        debugPrintGameField()

//        createGameFieldViews()
        initCeils()
    }

    private fun fillGameField() {
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

    private fun debugPrintGameField() {
        var result = " \n"
        gameField.forEach {
            result += "$it\n"
        }
        Log.d(LOG_TAG, result)
    }

    private fun initCeils() {

        for (i in ceils.indices) {
            for (j in ceils[i].indices) {
                ceils[i][j].text = gameField[i][j].toString()
                ceils[i][j].tag = Pair(i, j)
                ceils[i][j].setOnClickListener {
                    ceilClicked(it)
                }
            }
        }
    }

    private fun ceilClicked(view: View) {
//        Toast.makeText(applicationContext, "${(view.tag as Pair<*, *>).second}", Toast.LENGTH_SHORT).show()
        view.setBackgroundColor(resources.getColor(R.color.black))
    }

    private fun createGameFieldViews() {

        for (i in 0..8) {
            val linLayoutHor = LinearLayout(this)
            var linLayoutHorPar = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            linLayoutHor.orientation = LinearLayout.HORIZONTAL
            linLayoutHor.layoutParams = linLayoutHorPar

            for (j in 0..8) {
                val button = Button(this)
                var params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
//                params.setMargins(10, 10, 0, 0)

                button.layoutParams = params
//                editText.inputType = InputType.TYPE_CLASS_NUMBER
//                editText.filters = arrayOf(InputFilter.LengthFilter(1))
                button.text = gameField[i][j].toString()

                linLayoutHor.addView(button)
            }

            linLayoutVer.addView(linLayoutHor)
        }
    }

}
