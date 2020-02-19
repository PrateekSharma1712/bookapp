package com.prateek.booksapp.di

import com.prateek.booksapp.ui.feature.BooksFragment
import com.prateek.booksapp.ui.feature.CategoryFragment
import com.prateek.booksapp.ui.feature.LandingActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, BookApiModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(categoryFragment: CategoryFragment)
    fun inject(booksFragment: BooksFragment)
    fun inject(activity: LandingActivity)
}