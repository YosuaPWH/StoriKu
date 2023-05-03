package com.yosuahaloho.storiku.presentation.custom

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.yosuahaloho.storiku.R
import com.yosuahaloho.storiku.utils.dpToPx

/**
 * Created by Yosua on 29/04/2023
 */
class EditTextEmailCustomView : TextInputEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        setPadding(12.dpToPx(), 16.dpToPx(), 12.dpToPx(), 16.dpToPx())

        doOnTextChanged { text, _, _, _ ->
            val parentLayout = parent.parent as TextInputLayout
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()) {
                parentLayout.isErrorEnabled = false
            } else {
                parentLayout.error = resources.getString(R.string.error_valid_email)
            }
        }

    }

}