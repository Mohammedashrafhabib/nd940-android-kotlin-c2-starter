package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.database.AsteroidDatabaseDAO

class DetailViewModelFactory(
    private val dataSource: AsteroidDatabaseDAO,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewmodel::class.java)) {
            return DetailViewmodel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}