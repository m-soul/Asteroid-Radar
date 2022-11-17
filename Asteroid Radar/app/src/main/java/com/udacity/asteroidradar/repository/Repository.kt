package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException


class Repository(val dataBase: AsteroidDataBase) {
lateinit var asteroids : LiveData<List<Asteroid>>
    fun getAllAsteroid()
    {
        val asteroids = Transformations.map(dataBase.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

        this.asteroids = asteroids
    }
    fun getTodayAsteroid()
    {
         val asteroids = Transformations.map(dataBase.asteroidDao.getTodayAsteroids())
        {
            it.asDomainModel()
        }
        this.asteroids = asteroids
    }
    fun getSavedAsteroid()
    {
        val asteroids = Transformations.map(dataBase.asteroidDao.getSavedAsteroids())
        {
            it.asDomainModel()
        }
        this.asteroids = asteroids
    }
    suspend fun getNewAsteroids()
    {
        withContext(Dispatchers.IO)
        {
            try {
                val asteroidsResult = Network.api.getAsteroids(Constants.API_KEY)
                val asteroidFormatted = parseAsteroidsJsonResult(JSONObject(asteroidsResult)).asDatabaseModel()

                dataBase.asteroidDao.insert(asteroidFormatted)
            }catch (e: HttpException)
            {
                Log.i("Api","couldn't get asteroids")

            }

        }
    }

}