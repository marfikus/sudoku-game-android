package com.example.sudoku

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
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
            diffDialogInput?.setText(currentDifficulty.toString())

            builder.setView(view)
                    .setTitle(getString(R.string.diff_dialog_title))
                    .setPositiveButton(R.string.ok) { dialog, _ ->
                        (activity as MainActivity).changeDifficulty(diffDialogInput?.text.toString().toInt())
                        dialog.cancel()
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel()}
            builder.create()

        } ?: throw IllegalStateException("activity cannot be null")
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