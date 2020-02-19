package com.prateek.booksapp

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/***
 * Used to close the keyboard once the search button is clicked
 */
fun View?.closeKeyboard() {
    val inputMethodManager: InputMethodManager? =
        this?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.hideSoftInputFromWindow(this?.windowToken, 0)
}