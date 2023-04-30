package com.yosuahaloho.storiku.presentation.custom

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Typeface
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.EndIconMode
import com.yosuahaloho.storiku.R
import com.yosuahaloho.storiku.utils.dpToPx

/**
 * Created by Yosua on 18/04/2023
 */
class EditTextPasswordCustomView : TextInputEditText {

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

        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        doOnTextChanged { text, _, _, _ ->
            val parentLayout = parent.parent as TextInputLayout
            if (text.toString().length < 8) {
                parentLayout.error = "Input must be at least 8 characters"
                parentLayout.errorIconDrawable = null
            } else {
                parentLayout.isErrorEnabled = false
            }
        }
    }


}