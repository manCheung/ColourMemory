package com.man.colourmemory.view

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.man.colourmemory.R
import kotlinx.android.synthetic.main.name_dialog.*

class NameDialog(score: Int, rank: Int) : DialogFragment(){

//    private var mPlayerName: String? = null

    /**
     * Dialog button click listener
     */
    private var mDialogClickListener: DialogClickListener? = null
    val currentScore = score
    val currentRank = rank

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder =
            AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.name_dialog, null)
        builder.setView(view)

        val userName = view.findViewById<EditText>(R.id.edit_text_name)
        val submitButton = view.findViewById<Button>(R.id.submit_button)
        val winInfo = view.findViewById<TextView>(R.id.tv_win_info)

        winInfo.text = getString(R.string.win_info, currentScore, currentRank)

        submitButton.setOnClickListener {
            mDialogClickListener?.onClick(userName.text.toString())
        }

        return builder.create()
    }

    fun setOnDialogClickListener(dialogClickListener: DialogClickListener?) {
        mDialogClickListener = dialogClickListener
    }

}

interface DialogClickListener {

    fun onClick(userName: String)

}
