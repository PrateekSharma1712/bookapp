package com.prateek.booksapp.ui.feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prateek.booksapp.framework.model.Book
import com.prateek.booksapp.framework.network.BookRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

class LandingViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {

    private lateinit var _selectedCategory: String
    var selectedCategory: String
        get() = _selectedCategory
        set(value) {
            _selectedCategory = value
        }

    private var pageCount = 1
    private var isNextPagePresent: String? = null
    private var fetchingBooks = false
    private var searchString: String = ""
    private val _books = mutableListOf<Book?>()
    val isNetworkConnected = MutableLiveData<Boolean>()

    private val _filteredBooksLiveData = MutableLiveData<List<Book?>>()
    val filteredBooksLiveData: LiveData<List<Book?>>
        get() = _filteredBooksLiveData

    fun fetchBooks() {
        if (!fetchingBooks) {
            fetchingBooks = true
            viewModelScope.launch {
                delay(500)
                try {
                    val (books, nextPage) = repository.fetchBooks(pageCount)
                    isNextPagePresent = nextPage
                    val booksByCategory = books.filter {
                        it?.subjects?.any { subject ->
                            subject?.contains(_selectedCategory, true) ?: true
                        } ?: true
                    }
                    _books.addAll(booksByCategory)
                    filterBooksByAuthorAndTitle(searchString)
                    fetchingBooks = false
                } catch (unknownHostException: UnknownHostException) {
                    fetchingBooks = false
                    isNetworkConnected.postValue(false)
                }
            }
        }
    }

    private fun filterBooksByAuthorAndTitle(searchString: String) {
        val filteredBooks = _books.filter {
            it?.authors?.any { author ->
                author?.name?.contains(searchString, true) ?: false
            } ?: false || it?.title?.contains(searchString, true) ?: false
        }

        _filteredBooksLiveData.postValue(filteredBooks)
    }

    fun onSearchButtonClicked(searchString: String) {
        this.searchString = searchString
        filterBooksByAuthorAndTitle(searchString)
    }

    fun reachedEndOfList() {
        if (isNextPagePresent != null) {
            pageCount++
            fetchBooks()
        }
    }

    fun onCategorySelected(categoryName: String) {
        searchString = ""
        _books.clear()
        pageCount = 1
        selectedCategory = categoryName
        _filteredBooksLiveData.postValue(_books)
    }

    fun updateNetworkConnectivity(isConnected: Boolean) {
        isNetworkConnected.postValue(isConnected)
    }

    fun onBookClicked(book: Book): String? {
        return when {
            book.formats?.pdf?.isNotEmpty() == true && !book.formats.pdf.endsWith(".zip") -> {
                book.formats.pdf
            }
            book.formats?.html?.isNotEmpty() == true -> book.formats.html
            book.formats?.htmlCharsetUTF8?.isNotEmpty() == true -> book.formats.htmlCharsetUTF8
            book.formats?.htmlCharsetISO?.isNotEmpty() == true -> book.formats.htmlCharsetISO
            book.formats?.htmlCharsetUSAscii?.isNotEmpty() == true -> book.formats.htmlCharsetUSAscii
            book.formats?.text?.isNotEmpty() == true -> book.formats.text
            book.formats?.textCharsetUTF8?.isNotEmpty() == true -> book.formats.textCharsetUTF8
            book.formats?.textCharsetUSAscii?.isNotEmpty() == true -> book.formats.textCharsetUSAscii
            book.formats?.textCharsetISO?.isNotEmpty() == true -> book.formats.textCharsetISO
            else -> null
        }
    }
}