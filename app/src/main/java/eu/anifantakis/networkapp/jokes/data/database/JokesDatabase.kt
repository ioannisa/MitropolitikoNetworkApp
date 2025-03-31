package eu.anifantakis.networkapp.jokes.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import eu.anifantakis.networkapp.jokes.model.JokeEntity

@Database(entities = [JokeEntity::class], version = 1, exportSchema = false)
abstract class JokesDatabase : RoomDatabase() {
    abstract fun jokesDao(): JokesDao

    companion object {
        @Volatile
        private var INSTANCE: JokesDatabase? = null

        fun getDatabase(context: Context): JokesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JokesDatabase::class.java,
                    "jokes_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}