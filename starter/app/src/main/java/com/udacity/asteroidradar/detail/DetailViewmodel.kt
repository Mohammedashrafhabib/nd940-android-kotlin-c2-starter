package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Repository
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidDatabaseDAO
import com.udacity.asteroidradar.main.DataFilter
import kotlinx.coroutines.launch

class DetailViewmodel(val dataSource: AsteroidDatabaseDAO, val application: Application): ViewModel() {

    private val _isAstroidSaved = MutableLiveData<Boolean>()
    val isAstroidSaved: LiveData<Boolean>
        get() = _isAstroidSaved
    private val database = AsteroidDatabase.getInstance(application)
    private val repository = Repository(database)
    fun isAstoridSaved(asteroid: Asteroid){


                _isAstroidSaved.value =asteroid.saved





    }
    fun saveAstorroid(item:Asteroid){
        viewModelScope.launch {
            repository.saveAsteroid(item)

        }
    }

    fun deleteAstorroid(item:Asteroid) {
        viewModelScope.launch {
            repository.deleteAsteroid(item)

        }
    }
}