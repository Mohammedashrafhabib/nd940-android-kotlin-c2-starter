package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDatabaseDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: Asteroid)
    @Update
    suspend fun update(asteroid:Asteroid)
    @Query("SELECT * from saved_asteroid_table WHERE closeApproachDate >= :date order by closeApproachDate ")
      fun getWeek(date :String): LiveData<List<Asteroid>>
    @Query("SELECT * from saved_asteroid_table WHERE closeApproachDate >= :date AND closeApproachDate <= :date")
      fun getToday(date :String):LiveData<List<Asteroid>>
    @Query("SELECT * from saved_asteroid_table where saved=1")
    fun getSaved():LiveData<List<Asteroid>>

    @Query("delete  from saved_asteroid_table where saved=0 ")
    suspend  fun delete()
}