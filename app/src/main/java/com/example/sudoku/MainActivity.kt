package com.example.sudoku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

private const val DIFFICULTY_DIALOG = "DIFFICULTY_DIALOG"

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private val ceils = mutableListOf<List<Button>>()
    private val numberButtons = mutableListOf<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = (application as App).mainViewModel

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
        initCeils()

        numberButtons.addAll(
            listOf(button_clear, button_1, button_2, button_3, button_4, button_5, button_6, button_7, button_8, button_9)
        )
        initNumberButtons()

        button_cancel.setOnClickListener {
            if (!mainViewModel.existSelectedCeil()) return@setOnClickListener

            hideButtonsPanel()
            val index = mainViewModel.getSelectedCeilIndex()
            unselectCeil(index.first, index.second)
            mainViewModel.unselectCeil()
        }
        button_cancel.background.setTint(resources.getColor(R.color.selected_ceil))
        button_cancel.textSize = 16f
        button_clear.textSize = 16f

        if (mainViewModel.existSelectedCeil()) {
            val index = mainViewModel.getSelectedCeilIndex()
            selectCeil(index.first, index.second)
            showButtonsPanel()
        }

        checkGameField()

//        mainViewModel.debugPrintGameField()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.new_game -> newGame()
            R.id.change_difficulty -> {
                ChangeDifficultyDialogFragment
                        .newInstance(mainViewModel.getDifficultyLevel())
                        .show(supportFragmentManager, DIFFICULTY_DIALOG)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun changeDifficultyAndRestart(newValue: Int) {
        mainViewModel.setDifficultyLevel(newValue)
        newGame()
    }

    private fun initCeils() {
        for (i in ceils.indices) {
            for (j in ceils[i].indices) {
                val value = mainViewModel.getFieldValue(i, j).toString()
                if (value == "0") {
                    ceils[i][j].text = ""
                } else {
                    ceils[i][j].text = value
                }
                ceils[i][j].isEnabled = mainViewModel.isHidedCeil(i, j)
                ceils[i][j].tag = Pair(i, j)
                ceils[i][j].setOnClickListener {
                    ceilClicked(it)
                }
                unselectCeil(i, j)
                ceils[i][j].setTextColor(resources.getColor(R.color.white))
                ceils[i][j].textSize = 24f
            }
        }
    }

    private fun ceilClicked(view: View) {
        if (mainViewModel.existSelectedCeil()) {
            button_cancel.performClick()
        }

        selectCeil(view)
        mainViewModel.selectCeil(view.tag as Pair<*, *>)
        showButtonsPanel()
    }

    private fun initNumberButtons() {
        for (i in numberButtons.indices) {
            numberButtons[i].tag = i
            numberButtons[i].setOnClickListener {
                numberButtonClicked(it)
            }
            numberButtons[i].background.setTint(resources.getColor(R.color.selected_ceil))
            numberButtons[i].textSize = 24f
        }
    }

    private fun numberButtonClicked(view: View) {
        if (!mainViewModel.existSelectedCeil()) return

        val i = mainViewModel.getSelectedCeilIndex().first
        val j = mainViewModel.getSelectedCeilIndex().second

        val tag = view.tag.toString()
        mainViewModel.updateFieldValue(i, j, tag.toInt())
        if (tag == "0") {
            ceils[i][j].text = ""
        } else {
            ceils[i][j].text = tag
        }

        unselectCeil(i, j)
        mainViewModel.unselectCeil()

        hideButtonsPanel()
        mainViewModel.debugPrintGameField()

        checkGameField()
    }

    private fun checkGameField() {
        hideSolvedBanner()
        if (!mainViewModel.existEmptyCeils()) {
            if (mainViewModel.gameFieldSolved()) {
//                Toast.makeText(applicationContext, "Solved!", Toast.LENGTH_SHORT).show()
                showSolvedBanner()
//                newGame()
            }
        }
    }

    private fun showSolvedBanner() {
        solved_banner.visibility = View.VISIBLE
    }

    private fun hideSolvedBanner() {
        solved_banner.visibility = View.INVISIBLE
    }

    private fun showButtonsPanel() {
        buttons_panel.visibility = View.VISIBLE
    }

    private fun hideButtonsPanel() {
        buttons_panel.visibility = View.INVISIBLE
    }

    private fun selectCeil(i: Int, j: Int) {
        ceils[i][j].background.setTint(resources.getColor(R.color.selected_ceil))
    }

    private fun selectCeil(view: View) {
        view.background.setTint(resources.getColor(R.color.selected_ceil))
    }

    private fun unselectCeil(i: Int, j: Int) {
        if (mainViewModel.isHidedCeil(i, j)) {
            ceils[i][j].background.setTint(resources.getColor(R.color.unselected_hided_ceil))
        } else {
            ceils[i][j].background.setTint(resources.getColor(R.color.unselected_ceil))
        }
    }

    private fun newGame() {
        hideSolvedBanner()
        hideButtonsPanel()
        mainViewModel.init()
        initCeils()
        checkGameField()
    }

}
