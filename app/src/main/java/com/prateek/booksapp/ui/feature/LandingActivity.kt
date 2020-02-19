package com.prateek.booksapp.ui.feature

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.prateek.booksapp.BookApplication
import com.prateek.booksapp.R
import kotlinx.android.synthetic.main.activity_landing.*
import javax.inject.Inject

class LandingActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var landingViewModel: LandingViewModel
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkRequest: NetworkRequest
    private lateinit var networkAvailabilityCallback: ConnectivityManager.NetworkCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        BookApplication.application.appComponent.inject(this)
        landingViewModel =
            ViewModelProvider(this, viewModelFactory).get(LandingViewModel::class.java)

        val navController = findNavController(R.id.navHostFragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        initialiseNetworkState()
    }

    /***
     * This method notifies view model [LandingViewModel] when
     * internet connection is available or lost
     * based on which books screen [BooksFragment] updates the UI
     */
    private fun initialiseNetworkState() {
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()

        networkAvailabilityCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                landingViewModel.updateNetworkConnectivity(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                landingViewModel.updateNetworkConnectivity(false)
            }
        }
    }

    /***
     * Updates the title of Books Fragment [BooksFragment] with selected category
     */
    fun updateTitle() {
        toolbar.title = landingViewModel.selectedCategory
    }


    override fun onStart() {
        super.onStart()
        connectivityManager.registerNetworkCallback(networkRequest, networkAvailabilityCallback)
    }

    override fun onStop() {
        super.onStop()
        connectivityManager.unregisterNetworkCallback(networkAvailabilityCallback)
    }
}