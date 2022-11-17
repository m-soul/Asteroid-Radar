package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.database.DatabaseAsteroids

class ViewModelFactory(private val application: Application, private val db : AsteroidDataBase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(db,application) as T
        } else {
            throw IllegalAccessException("unknownViewModelClass")
        }
    }
}