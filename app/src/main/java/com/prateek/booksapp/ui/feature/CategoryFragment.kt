package com.prateek.booksapp.ui.feature

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.prateek.booksapp.BookApplication
import com.prateek.booksapp.R
import kotlinx.android.synthetic.main.fragment_category.*
import javax.inject.Inject

class CategoryFragment : Fragment(R.layout.fragment_category), CategoryAdapter.CategoryClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var landingViewModel: LandingViewModel
    private lateinit var categoryArray: Array<String>
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        BookApplication.application.appComponent.inject(this)

        landingViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(LandingViewModel::class.java)

        fetchCategories()
        showCategories()
    }

    /***
     * Loads category from strings array [R.array.categories]
     */
    private fun fetchCategories() {
        categoryArray = resources.getStringArray(R.array.categories)
    }

    /***
     * This methods shows the categories in the categories recycler view
     */
    private fun showCategories() {
        categoryAdapter = CategoryAdapter(this)
        categoriesRecyclerView.apply {
            adapter = categoryAdapter
            hasFixedSize()
        }

        categoryAdapter.submitList(categoryArray.toList())
    }

    /***
     * Navigates to books screen when a category is selected
     */
    override fun onCategoryClicked(categoryName: String) {
        landingViewModel.onCategorySelected(categoryName)
        findNavController().navigate(CategoryFragmentDirections.actionCategoryClicked())
    }

}