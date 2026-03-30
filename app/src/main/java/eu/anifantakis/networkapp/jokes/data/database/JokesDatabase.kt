package eu.anifantakis.networkapp.jokes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import eu.anifantakis.networkapp.jokes.model.JokeEntity

@Database(entities = [JokeEntity::class], version = 1, exportSchema = false)
abstract class JokesDatabase : RoomDatabase() {
    abstract fun jokesDao(): JokesDao
}