package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query("select * from asteroids_table where closeApproachDate >= date('now', 'localtime') order by closeApproachDate asc ")
    fun getAsteroids(): LiveData<List<DatabaseAsteroids>>
    @Query("select * from asteroids_table where closeApproachDate = date('now', 'localtime') order by closeApproachDate asc")
    fun getTodayAsteroids(): LiveData<List<DatabaseAsteroids>>
    @Query("select * from asteroids_table order by closeApproachDate asc")
    fun getSavedAsteroids(): LiveData<List<DatabaseAsteroids>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asteroids: List<DatabaseAsteroids>)
}
@Database(entities = [DatabaseAsteroids::class], version = 1,  exportSchema = false)
abstract class AsteroidDataBase : RoomDatabase() {

    abstract val asteroidDao: AsteroidDao

    companion object {

        @Volatile
        private var INSTANCE: AsteroidDataBase? = null

        fun getInstance(context: Context): AsteroidDataBase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDataBase::class.java,
                        "asteroids_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}