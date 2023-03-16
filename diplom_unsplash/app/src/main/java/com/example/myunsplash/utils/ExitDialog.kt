package com.example.myunsplash.utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.myunsplash.databinding.DialogExitBinding

class ExitDialog(
    private val logoutCallback: () -> Unit,
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding: DialogExitBinding = DialogExitBinding.inflate(layoutInflater).apply {
            dialogExitButtonPositive.setOnClickListener {
                logoutCallback()
            }
            dialogExitButtonCancel.setOnClickListener {
                dismiss()
            }
        }
        return AlertDialog
            .Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    companion object {
        const val TAG = "ExitDialogTag"
    }
}