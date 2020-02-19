package com.prateek.booksapp.ui.feature

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.prateek.booksapp.R
import com.prateek.booksapp.framework.model.Book
import kotlinx.android.synthetic.main.line_item_book.view.*

class BookAdapter(
    private val booksClickListener: BooksClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var data = mutableListOf<Book?>()

    interface BooksClickListener {
        fun onBookClicked(book: Book)
    }

    val requestOptions: RequestOptions by lazy {
        RequestOptions()
            .placeholder(R.drawable.ic_book_placeholder)
            .centerCrop()
    }

    fun updateData(newData: List<Book?>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(book: Book?) {
            itemView.authorTextView.text =
                book?.authors?.joinToString(separator = ", ") { it?.name.orEmpty() }
            itemView.titleTextView.text = book?.title
            itemView.downloadsTextView.text =
                context.getString(R.string.downloads_count, book?.downloadCount?.toString())
            itemView.languagesTextView.text = context.getString(
                R.string.languages,
                book?.languages?.joinToString(separator = " | ")
            )

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(book?.formats?.imageURL)
                .into(itemView.bookImageView)

            itemView.setOnClickListener {
                book?.let {
                    booksClickListener.onBookClicked(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.line_item_book,
            parent,
            false
        )

        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}