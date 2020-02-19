package com.prateek.booksapp.di

import com.prateek.booksapp.BookApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: BookApplication) {

    @Provides
    @Singleton
    fun provideContext(): BookApplication = app
}