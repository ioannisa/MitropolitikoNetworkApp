package eu.anifantakis.networkapp.jokes.data.di

import android.content.Context
import eu.anifantakis.networkapp.jokes.data.JokesRepository
import eu.anifantakis.networkapp.jokes.data.database.JokesDatabase
import eu.anifantakis.networkapp.jokes.data.network.KtorClient
import io.ktor.client.HttpClient

/**
 * Simple dependency injector for the application.
 * Provides centralized access to database and repositories.
 */
object AppModule {
    lateinit var jokesDatabase: JokesDatabase
        private set

    lateinit var ktorClient: HttpClient
        private set

    lateinit var jokesRepository: JokesRepository
        private set

    fun initialize(context: Context) {
        ktorClient = KtorClient.httpClient

        jokesDatabase = JokesDatabase.getDatabase(context)
        jokesRepository = JokesRepository(
            httpClient = ktorClient,
            database = jokesDatabase.jokesDao()
        )
    }
}