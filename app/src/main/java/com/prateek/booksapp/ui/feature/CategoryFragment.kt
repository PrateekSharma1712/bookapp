package com.prateek.booksapp.ui.feature

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.prateek.booksapp.R

class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var landingViewModel: LandingViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        landingViewModel = ViewModelProvider(activity!!).get(LandingViewModel::class.java)


    }

}