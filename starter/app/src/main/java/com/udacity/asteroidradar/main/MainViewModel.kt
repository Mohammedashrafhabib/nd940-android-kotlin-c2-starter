@file:Suppress("SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection")

package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.Repository
import com.udacity.asteroidradar.api.NasaApiService
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidDatabaseDAO
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
enum class Status {LOADING, ERROR, DONE }
enum class DataFilter { WEEK, TODAY, SAVED }

@SuppressLint("NewApi")
class MainViewModel(val application: Application) : ViewModel() {

    private val _PictureStatus = MutableLiveData<Status>()
    val PictureStatus: LiveData<Status>
        get() = _PictureStatus
    private val _astroidDataFilter = MutableLiveData<DataFilter>()
    val astroidDataFilter : LiveData<DataFilter>
        get() = _astroidDataFilter
    private val database = AsteroidDatabase.getInstance(application)
    private val repository = Repository(database)
    val astroidStatus: LiveData<Status>
        get() = repository.astroidStatus
    val asteroidsWeek: LiveData<List<Asteroid>>
        get()= repository.astroidWeek
    val asteroidsToday: LiveData<List<Asteroid>>
        get()= repository.astroidToday
    val asteroidsSaved: LiveData<List<Asteroid>>
        get()= repository.astroidSaved



    private val _pictureOfDay=MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay
    init {
        _astroidDataFilter.value=DataFilter.WEEK
        getPictureOfDay()
        viewModelScope.launch {
        repository.refreshAsteroid()
        }

    }


    fun setDataFilter(filter: DataFilter){
        _astroidDataFilter.value=filter


    }

    fun getPictureOfDay(){
        viewModelScope.launch {
            _PictureStatus.value=Status.LOADING
            try {
            var res= NasaApiService.NasaApi.retrofitService.getImage()
                if (res.mediaType=="image")
            _pictureOfDay.value= res
                _PictureStatus.value=Status.DONE
            }
            catch (e:Exception){
                _PictureStatus.value=Status.ERROR
                _pictureOfDay.value=null
            }
        }
    }
    private val _navigateToAstroidDetails = MutableLiveData<Asteroid>()
    val navigateToAstroidDetails:LiveData<Asteroid>
        get() = _navigateToAstroidDetails




    fun onAstroidDetailsNavigated() {
        _navigateToAstroidDetails.value = null

    }
    fun onAstroidClicked(it: Asteroid) {
        _navigateToAstroidDetails.value = it

    }

    fun refresh() {
        viewModelScope.launch {

        repository.refreshAsteroid()
        }
    }
}