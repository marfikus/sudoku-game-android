package com.example.sudoku

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
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
    private val numberButtons = mutableListOf<Button>()
    private var selectedCeilIndex: Pair<*, *>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ceils.addAll(
            listOf(
                listOf(ceil_00, ceil_01, ceil_02, ceil_03, ceil_04, ceil_05, ceil_06, ceil_07, ceil_08),
                listOf(ceil_10, ceil_11, ceil_12, ceil_13, ceil_14, ceil_15, ceil_16, ceil_17, ceil_18),                
                listOf(ceil_20, ceil_21, ceil_22, ceil_23, ceil_24, ceil_25, ceil_26, ceil_27, ceil_28),                
                listOf(ceil_30, ceil_31, ceil_32, ceil_33, ceil_34, ceil_35, ceil_36, ceil_37, ceil_38),                
                listOf(ceil_40, ceil_41, ceil_42, ceil_43, ceil_44, ceil_45, ceil_46, ceil_47, ceil_48),                
                listOf(ceil_50, ceil_51, ceil_52, ceil_53, ceil_54, ceil_55, ceil_56, ceil_57, ceil_58),                
                listOf(ceil_60, ceil_61, ceil_62, ceil_63, ceil_64, ceil_65, ceil_66, ceil_67, ceil_68),                
                listOf(ceil_70, ceil_71, ceil_72, ceil_73, ceil_74, ceil_75, ceil_76, ceil_77, ceil_78),                
                listOf(ceil_80, ceil_81, ceil_82, ceil_83, ceil_84, ceil_85, ceil_86, ceil_87, ceil_88)                
            )
        )

        numberButtons.addAll(
            listOf(button_clear, button_1, button_2, button_3, button_4, button_5, button_6, button_7, button_8, button_9)
        )

        fillGameField()
        debugPrintGameField()

//        createGameFieldViews()
        initCeils()
        initNumberButtons()

        button_cancel.setOnClickListener {
            buttons_panel.visibility = View.INVISIBLE

            if (selectedCeilIndex != null) {
                val i = selectedCeilIndex?.first as Int
                val j = selectedCeilIndex?.second as Int
                ceils[i][j].setBackgroundColor(resources.getColor(R.color.purple_200))
                selectedCeilIndex = null
            }
        }
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

    private fun initNumberButtons() {
        for (i in numberButtons.indices) {
            numberButtons[i].tag = i
            numberButtons[i].setOnClickListener {
                numberButtonClicked(it)
            }
        }
    }

    private fun numberButtonClicked(view: View) {
//        Toast.makeText(applicationContext, "${view.tag}", Toast.LENGTH_SHORT).show()

        if (selectedCeilIndex != null) {
            val i = selectedCeilIndex?.first as Int
            val j = selectedCeilIndex?.second as Int

            val tag = view.tag.toString()
            // TODO: 21.08.21 заменить в массиве
//            gameField[i]
            if (tag == "0") {
                ceils[i][j].text = ""
            } else {
                ceils[i][j].text = tag
            }

            ceils[i][j].setBackgroundColor(resources.getColor(R.color.purple_200))
            selectedCeilIndex = null
        }
        buttons_panel.visibility = View.INVISIBLE
    }

    private fun ceilClicked(view: View) {
//        Toast.makeText(applicationContext, "${(view.tag as Pair<*, *>).second}", Toast.LENGTH_SHORT).show()
        view.setBackgroundColor(resources.getColor(R.color.black))
        // TODO: 21.08.21 определить дефолтный цвет кнопки, чтобы потом его вернуть
        buttons_panel.visibility = View.VISIBLE
        selectedCeilIndex = (view.tag as Pair<*, *>)
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

            ceils_field.addView(linLayoutHor)
        }
    }

}
