package com.udacity.asteroidradar

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.api.NasaApiService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.main.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

    @SuppressLint("NewApi")
class Repository(private val database: AsteroidDatabase) {
    private val startDate=SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).format(Calendar.getInstance().time)
        val  astroidWeek: LiveData<List<Asteroid>> =database.asteroidDatabaseDAO.getWeek(startDate)
        val  astroidToday: LiveData<List<Asteroid>> =database.asteroidDatabaseDAO.getToday(startDate)
        val  astroidSaved: LiveData<List<Asteroid>> =database.asteroidDatabaseDAO.getSaved()
        private val _astroidStatus = MutableLiveData<Status>()
        val astroidStatus: LiveData<Status>
            get() = _astroidStatus



    suspend fun saveAsteroid(item: Asteroid){
        var new=Asteroid(item.id,item.codename,item.closeApproachDate,item.absoluteMagnitude,item.estimatedDiameter,item.relativeVelocity
            ,item.distanceFromEarth,item.isPotentiallyHazardous,true)
        database.asteroidDatabaseDAO.update(new)
    }
    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun refreshAsteroid(){
        try {

            _astroidStatus.value=Status.LOADING
            withContext(Dispatchers.IO) {

            val res = NasaApiService.NasaApi.retrofitService.getAsteroids(startDate)
            val json = JSONObject(res.string().toString())
            val asteroids = parseAsteroidsJsonResult(json).toTypedArray()
                database.asteroidDatabaseDAO.delete()
            database.asteroidDatabaseDAO.insertAll(*asteroids)
            }

        }
        catch(e:Exception) {
            _astroidStatus.value=Status.ERROR
        }
    }
    suspend fun deleteAsteroid(item: Asteroid){
        var new=Asteroid(item.id,item.codename,item.closeApproachDate,item.absoluteMagnitude,item.estimatedDiameter,item.relativeVelocity
            ,item.distanceFromEarth,item.isPotentiallyHazardous,false)
        database.asteroidDatabaseDAO.update(new)
    }

}