package com.dihanov

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.dihanov.util.KeyboardManager
import kotlinx.android.synthetic.main.search_bar_layout.view.*
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.coroutines.CoroutineContext


class SearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), CoroutineScope, KoinComponent {
    companion object {
        const val LAST_SEARCH_KEY = "LAST_SEARCH"
        const val SUPER_KEY = "SUPER"
        const val KEYBOARD_STATE = "KEYBOARD_STATE"
    }

    interface SearchQuerySubmitListener {
        fun onQuerySubmitted(query: String)
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    var isKeyboardVisible = true
        private set

    private val keyboardManager: KeyboardManager by inject()

    private var job: Job? = null

    private val searchLayout: ConstraintLayout =
        View.inflate(context, R.layout.search_bar_layout, this) as ConstraintLayout

    private val searchEditText = searchLayout.search_bar_text

    private var searchQuerySubmitListener: SearchQuerySubmitListener? = null
    private val watcher = object : TextWatcher {
        var searchFor = ""

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val searchText = s.toString().trim()
            if (searchText == searchFor) return

            searchFor = searchText

            job = launch {
                delay(500)  //debounce timeout
                if (searchText != searchFor || searchFor.length < 3) return@launch
                performSearch(searchFor)
            }
        }

        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    }

    init {
        searchEditText.post {
            if (isKeyboardVisible) {
                showKeyboardAndGetFocus()
            }
        }

        searchEditText.addTextChangedListener(watcher)

        //show the keyboard when any part of the layout is clicked
        searchLayout.setOnClickListener {
            showKeyboardAndGetFocus()
        }

        //set this explicitly so we know if the keyboard was shown via clicking on the edit text manually
        searchEditText.setOnFocusChangeListener { v, hasFocus ->
            isKeyboardVisible = hasFocus
        }
    }



    fun setSearchQuerySubmitListener(searchQuerySubmitListener: SearchQuerySubmitListener) {
        this.searchQuerySubmitListener = searchQuerySubmitListener
    }

    fun clear() {
        this.searchEditText.setText("")
    }

    fun getSearchKeyword() = searchEditText.text.toString()

    fun showKeyboardAndGetFocus() {
        searchEditText.requestFocus()
        keyboardManager.showSoftKeyboard(searchEditText)
        isKeyboardVisible = true
    }

    fun hideKeyboard() {
        keyboardManager.hideKeyboard(searchEditText)
        isKeyboardVisible = false
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        job?.cancel()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            val bundle = state
            super.onRestoreInstanceState(bundle.getParcelable(SUPER_KEY))

            //restore watcher state
            watcher.searchFor = bundle.getString(LAST_SEARCH_KEY, "")

            val keyboadVisible = bundle.getBoolean(KEYBOARD_STATE, true)

            if (keyboadVisible) {
                showKeyboardAndGetFocus()
            } else {
                hideKeyboard()
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(SUPER_KEY, super.onSaveInstanceState())

        //save watcher state
        bundle.putString(LAST_SEARCH_KEY, getSearchKeyword())

        //save keyboard state
        bundle.putBoolean(KEYBOARD_STATE, isKeyboardVisible)

        return bundle
    }

    private fun performSearch(searchQuery: String) {
        this.searchQuerySubmitListener?.onQuerySubmitted(searchQuery)
    }
}