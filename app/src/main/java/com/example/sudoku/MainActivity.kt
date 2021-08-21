package com.example.sudoku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val LOG_TAG = "LOG_TAG"
    private val gameField = mutableListOf<List<Int>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fillGameField()
        debugPrintGameField()

        createGameFieldViews()
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
                val editText = EditText(this)
                var params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
//                params.setMargins(10, 10, 0, 0)

                editText.layoutParams = params
                editText.inputType = InputType.TYPE_CLASS_NUMBER
                editText.filters = arrayOf(InputFilter.LengthFilter(1))
                editText.setText(gameField[i][j].toString())

                linLayoutHor.addView(editText)
            }

            linLayoutVer.addView(linLayoutHor)
        }
    }
}
