package com.udacity.asteroidradar

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase
import retrofit2.HttpException

    @SuppressLint("NewApi")
class Worker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = Repository(database)
        return try {
            repository.refreshAsteroid()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}