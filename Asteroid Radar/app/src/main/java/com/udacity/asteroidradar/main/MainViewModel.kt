package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.ImageOfTheDay
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parseImageOfTheDay
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.repository.Repository
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel(db : AsteroidDataBase, application: Application) : AndroidViewModel(application) {
    var m_imageOfTheDay = MutableLiveData<ImageOfTheDay>()
    private var repository : Repository
    var asteroids : LiveData<List<Asteroid>>
    var asteroidsChanged = MutableLiveData<Boolean>()
    private var m_navigateToAsteroidDetail = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetail: LiveData<Asteroid>
        get() = m_navigateToAsteroidDetail

    init {
        repository = Repository(db)
        repository.getAllAsteroid()
        asteroids = repository.asteroids
        asteroidsChanged.value = false
        m_navigateToAsteroidDetail.value = null
        getImageOfTheDay()
    }

    private fun getImageOfTheDay() {
        viewModelScope.launch {
            try {
                val imageResult = Network.api.getImageOfTheDay(Constants.API_KEY)
                m_imageOfTheDay.value = parseImageOfTheDay(JSONObject(imageResult))
            } catch (e: Exception) {
                Log.i("ImageOfTheDay", "$e")
            }


        }
    }
fun asteroidChangedDone()
{
    asteroidsChanged.value = false
}
    fun onAsteroidClicked(asteroid: Asteroid) {
        m_navigateToAsteroidDetail.value = asteroid
    }

    fun onNavigateDone() {
        m_navigateToAsteroidDetail.value = null
    }

    fun filterTodayAsteroids()
    {
        repository.getTodayAsteroid()
        asteroids = repository.asteroids
        asteroidsChanged.value = true
    }
    fun filterSavedAsteroids()
    {
        repository.getSavedAsteroid()
        asteroids = repository.asteroids
        asteroidsChanged.value = true

    }
    fun filterWeekAsteroid()
    {
        repository.getAllAsteroid()
        asteroids = repository.asteroids
        asteroidsChanged.value = true
    }
}