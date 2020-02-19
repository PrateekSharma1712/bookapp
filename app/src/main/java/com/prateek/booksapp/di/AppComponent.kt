package com.prateek.booksapp.di

import com.prateek.booksapp.ui.feature.LandingViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, BookApiModule::class])
interface AppComponent {

    fun inject(viewModel: LandingViewModel)
}