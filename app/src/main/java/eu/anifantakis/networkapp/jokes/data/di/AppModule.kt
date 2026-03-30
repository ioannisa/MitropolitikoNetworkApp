package eu.anifantakis.networkapp.jokes.data.di

import android.content.Context
import androidx.room.Room
import eu.anifantakis.networkapp.jokes.data.JokesRepository
import eu.anifantakis.networkapp.jokes.data.database.JokesDatabase
import eu.anifantakis.networkapp.jokes.data.network.KtorClient
import io.ktor.client.HttpClient

object AppModule {

    private lateinit var appContext: Context

    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    // SINGLETONS: We keep these as lazy so we don't open multiple database
    // connections or create multiple heavy Ktor clients.
    val ktorClient: HttpClient by lazy {
        KtorClient.httpClient
    }

    val jokesDatabase: JokesDatabase by lazy {
        Room.databaseBuilder(
            appContext,
            JokesDatabase::class.java,
            "jokes_database"
        ).build()
    }

    // FACTORY: Using get() means a new instance is created on every call.
    val jokesRepository: JokesRepository
        get() = JokesRepository(
            httpClient = ktorClient,
            database = jokesDatabase.jokesDao()
        )
}