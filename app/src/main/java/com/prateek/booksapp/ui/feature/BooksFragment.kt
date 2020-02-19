package com.prateek.booksapp.ui.feature

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.google.android.material.snackbar.Snackbar
import com.prateek.booksapp.BookApplication
import com.prateek.booksapp.R
import com.prateek.booksapp.closeKeyboard
import com.prateek.booksapp.framework.model.Book
import kotlinx.android.synthetic.main.fragment_books.*
import javax.inject.Inject


class BooksFragment : Fragment(R.layout.fragment_books), BookAdapter.BooksClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var landingViewModel: LandingViewModel
    private lateinit var bookAdapter: BookAdapter
    private lateinit var snackBar: Snackbar
    private lateinit var customTabsIntent: CustomTabsIntent.Builder

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        BookApplication.application.appComponent.inject(this)

        landingViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(LandingViewModel::class.java)
        (activity as LandingActivity).updateTitle()

        initialiseUIElement()
        initialiseObservers()
        initialiseCustomTab()

        landingViewModel.fetchBooks()
    }

    private fun initialiseCustomTab() {
        customTabsIntent = CustomTabsIntent.Builder()
        customTabsIntent.setToolbarColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary))
        customTabsIntent.setShowTitle(true)
        customTabsIntent.setExitAnimations(
            requireActivity(),
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }

    private fun initialiseUIElement() {
        bookAdapter = BookAdapter(this, requireActivity())
        booksRecyclerView.apply {
            adapter = bookAdapter
            hasFixedSize()
        }

        booksRecyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && !booksRecyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                    reachedEndOfList()
                }
            }
        })

        searchButton.setOnClickListener {
            it.closeKeyboard()
            landingViewModel.onSearchButtonClicked(searchEditText.text.toString())
        }

        searchEditText.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                textView.closeKeyboard()
                landingViewModel.onSearchButtonClicked(textView.text.toString())
            }

            false
        }
    }

    private fun initialiseObservers() {
        landingViewModel.filteredBooksLiveData.observe(requireActivity(), Observer {
            if (::snackBar.isInitialized) {
                snackBar.apply {
                    setText(R.string.added_new_books)
                    duration = 500
                }.show()
            }

            it?.let { books ->
                loaderFrameLayout?.animate()?.alpha(0f)?.setDuration(500)?.start()
                if (books.isEmpty()) {
                    booksRecyclerView?.animate()?.alpha(0f)?.setDuration(500)?.start()
                    noBooksTextView?.animate()?.alpha(1f)?.setDuration(500)?.start()
                } else {
                    booksRecyclerView?.animate()?.alpha(1f)?.setDuration(500)?.start()
                    noBooksTextView?.animate()?.alpha(0f)?.setDuration(500)?.start()
                    bookAdapter.updateData(books)
                }
            }
        })

        landingViewModel.isNetworkConnected.observe(requireActivity(), Observer {
            if (it) {
                if (landingViewModel.filteredBooksLiveData.value?.isNullOrEmpty() == true) {
                    landingViewModel.fetchBooks()
                    loaderFrameLayout?.animate()?.alpha(1f)?.setDuration(500)?.start()
                }

                noNetworkImageView?.animate()?.alpha(0f)?.setDuration(500)?.start()
                searchCardView?.animate()?.alpha(1f)?.setDuration(500)?.start()
                searchButton?.animate()?.alpha(1f)?.setDuration(500)?.start()
                booksRecyclerView?.animate()?.alpha(1f)?.setDuration(500)?.start()
            } else {
                noNetworkImageView?.animate()?.alpha(1f)?.setDuration(500)?.start()
                loaderFrameLayout?.animate()?.alpha(0f)?.setDuration(500)?.start()
                searchCardView?.animate()?.alpha(0f)?.setDuration(500)?.start()
                searchButton?.animate()?.alpha(0f)?.setDuration(500)?.start()
                booksRecyclerView?.animate()?.alpha(0f)?.setDuration(500)?.start()
            }
        })
    }

    override fun onBookClicked(book: Book) {
        val bookURL = landingViewModel.onBookClicked(book)
        if (bookURL.isNullOrEmpty()) {
            Snackbar.make(container, R.string.unable_to_load_book, Snackbar.LENGTH_SHORT).show()
        } else {
            openBook(bookURL)
        }
    }

    private fun openBook(bookURL: String) {
        Log.d("URL", bookURL)
        customTabsIntent.build().launchUrl(requireActivity(), Uri.parse(bookURL))
    }

    fun reachedEndOfList() {
        landingViewModel.reachedEndOfList()
        snackBar =
            Snackbar.make(container, R.string.fetching_more_books, Snackbar.LENGTH_INDEFINITE)
        snackBar.show()
    }
}