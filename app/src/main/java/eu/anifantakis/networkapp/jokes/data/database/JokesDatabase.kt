package eu.anifantakis.networkapp.jokes.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import eu.anifantakis.networkapp.jokes.model.JokeEntity

// Define migration from version 1 to 2 (adding isFavorite column)
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add isFavorite column with default value of 0 (false)
        database.execSQL("ALTER TABLE joke ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0")
    }
}

@Database(entities = [JokeEntity::class], version = 2, exportSchema = false)
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
                )
                    .addMigrations(MIGRATION_1_2) // Add migration
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}