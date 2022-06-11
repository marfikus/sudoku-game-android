package com.example.sudoku

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

private const val ARG_DIFFICULTY = "ARG_DIFFICULTY"

class ChangeDifficultyDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val currentDifficulty = arguments?.getInt(ARG_DIFFICULTY)

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = activity?.layoutInflater
            val view = inflater?.inflate(R.layout.change_difficulty_dialog_fragment, null)
            val diffDialogInput = view?.findViewById<EditText>(R.id.diff_dialog_input)
            diffDialogInput?.apply {
                setText(currentDifficulty.toString())
                requestFocus()
                selectAll()
                showKeyboard()
            }

            builder.setView(view)
                    .setTitle(getString(R.string.diff_dialog_title))
                    .setPositiveButton(R.string.ok) { dialog, _ ->
                        val value = diffDialogInput?.text.toString().trim()
                        if (value.isNotEmpty()) {
                            (activity as MainActivity).changeDifficultyAndRestart(value.toInt())
                        }
                        dialog.cancel()
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.cancel()
                    }
            builder.create()

        } ?: throw IllegalStateException("activity cannot be null")
    }

    override fun onCancel(dialog: DialogInterface) {
        hideKeyboard()
        super.onCancel(dialog)
    }

    override fun onPause() {
        dialog?.cancel()
        super.onPause()
    }

    private fun showKeyboard() {
        val inputMethodManager = (context?.getSystemService(Context.INPUT_METHOD_SERVICE)) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun hideKeyboard() {
        val inputMethodManager = (context?.getSystemService(Context.INPUT_METHOD_SERVICE)) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    companion object {
        fun newInstance(currentDifficulty: Int): ChangeDifficultyDialogFragment {
            val args = Bundle().apply {
                putInt(ARG_DIFFICULTY, currentDifficulty)
            }
            return ChangeDifficultyDialogFragment().apply {
                arguments = args
            }
        }
    }
}