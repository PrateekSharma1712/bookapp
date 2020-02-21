package com.prateek.booksapp.ui.feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prateek.booksapp.framework.model.Book
import com.prateek.booksapp.framework.network.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

class LandingViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {

    private var pageCount = 1
    private var isNextPagePresent: String? = null
    private var fetchingBooks = false
    private var searchString: String = ""
    private val _books = mutableListOf<Book?>()
    val isNetworkConnected = MutableLiveData<Boolean>()

    private val _filteredBooksLiveData = MutableLiveData<List<Book?>>()
    val filteredBooksLiveData: LiveData<List<Book?>>
        get() = _filteredBooksLiveData

    private lateinit var _selectedCategory: String
    var selectedCategory: String
        get() = _selectedCategory
        set(value) {
            _selectedCategory = value
        }


    /***
     * Called to retrieve books from repository and update the live data of books
     * filtering books based on category and search text typed in edit field
     */
    fun fetchBooks() {
        if (!fetchingBooks) {
            fetchingBooks = true
            viewModelScope.launch(Dispatchers.IO) {
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

    /***
     * @param searchString [String] is search string typed in the edit field
     * This method filters books based on title and authors as typed
     * in the search field
     */
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

    /***
     * This methods helps in calling next page of books when
     * end of recycler view is reached
     */
    fun reachedEndOfList() {
        if (isNextPagePresent != null) {
            pageCount++
            fetchBooks()
        }
    }

    /***
     * @param categoryName [String]
     * This method resets books list, search field text, page count and
     * also clears the live data storing books list, so that UI also has nothing to show
     */
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

    /***
     * @param book [Book]
     * This method returns a priority based book url in the following order
     * HTML <- PDF <- TEXT
     */
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