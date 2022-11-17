package com.udacity.asteroidradar.work

import android.app.Application
import android.content.Context
import androidx.work.*
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val db = AsteroidDataBase.getInstance(applicationContext)
        val repo = Repository(db)
        return try {
            repo.getNewAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}

class AsteroidsApplication : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
        applicationScope.launch { setupWork() }
    }

    private fun setupWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true).build()
        val repeatingRequest =
            PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS).setConstraints(
                constraints
            )
                .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}