package kz.cifron.smartcon.utils

import android.app.Application
import androidx.room.Room
import kz.cifron.smartcon.feature_result.data.ResultDatabase

class App : Application() {
    companion object{
        lateinit var database: ResultDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this,ResultDatabase::class.java,"result_database")
            .build()
    }
}