package com.example.moviemania.app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviemania.app.model.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}