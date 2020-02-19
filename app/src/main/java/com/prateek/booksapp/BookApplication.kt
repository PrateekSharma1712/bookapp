package com.prateek.booksapp

import android.app.Application
import com.prateek.booksapp.di.AppComponent
import com.prateek.booksapp.di.AppModule
import com.prateek.booksapp.di.BookApiModule
import com.prateek.booksapp.di.DaggerAppComponent

class BookApplication : Application() {

    lateinit var appComponent: AppComponent

    private fun initAppComponent(app: BookApplication): AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .bookApiModule(BookApiModule())
            .build()

    }

    companion object {
        @get:Synchronized
        lateinit var application: BookApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        appComponent = initAppComponent(this)
    }
}