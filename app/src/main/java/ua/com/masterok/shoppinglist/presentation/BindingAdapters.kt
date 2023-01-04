package ua.com.masterok.shoppinglist.presentation

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import ua.com.masterok.shoppinglist.R

@BindingAdapter("errorInputName")
fun bindingErrorInputName(textInputLayout: TextInputLayout, isError: Boolean) {
    val msg = if (isError) {
        textInputLayout.context.getString(R.string.error_name)
    } else {
        null
    }
    textInputLayout.error = msg
}


@BindingAdapter("errorInputCount")
fun bindingErrorInputCount(textInputLayout: TextInputLayout, isError: Boolean) {
    val msg = if (isError) {
        textInputLayout.context.getString(R.string.error_count)
    } else {
        null
    }
    textInputLayout.error = msg
}