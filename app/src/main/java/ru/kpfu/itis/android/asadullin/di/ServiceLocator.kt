package ru.kpfu.itis.android.asadullin.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.kpfu.itis.android.asadullin.data.db.ApplicationDatabase

object ServiceLocator {
    private const val DATABASE_NAME = "MovieSearch.db"

    private var databaseInstance : ApplicationDatabase? = null
    fun createDatabase(context: Context) {
        databaseInstance = Room.databaseBuilder(context, ApplicationDatabase::class.java, DATABASE_NAME)
//            .fallbackToDestructiveMigration()
//            .addMigrations()
            .build()
    }
    fun getDatabaseInstance() : ApplicationDatabase {
        return databaseInstance ?: throw RuntimeException("Database is not initialized yet")
    }
}