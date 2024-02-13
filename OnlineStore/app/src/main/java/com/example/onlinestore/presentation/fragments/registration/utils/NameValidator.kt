package com.example.onlinestore.presentation.fragments.registration.utils

import android.text.Editable
import android.text.TextWatcher

class NameValidator(
    private val onValidCallback: () -> Unit,
    private val onErrorCallback: () -> Unit,
) : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        val isValidName = isValidName(s.toString().trim())

        if (isValidName) onValidCallback()
        else onErrorCallback()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    private fun isValidName(str: String): Boolean {
        return str.chars()
            .mapToObj(Character.UnicodeBlock::of)
            .allMatch { ch -> ch.equals(Character.UnicodeBlock.CYRILLIC) }
    }
}